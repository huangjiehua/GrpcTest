package com.buaa.lottery.executor;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.buaa.lottery.bean.Block;
import com.buaa.lottery.bean.Transaction;
import com.buaa.lottery.bean.State;
import com.buaa.lottery.datasource.LevelDbDataSource;
import com.buaa.lottery.executor.ExecutorServer;
import com.buaa.lottery.routeguide.Feature;
import com.buaa.lottery.routeguide.Point;
import com.buaa.lottery.transaction.Compute;
import com.buaa.lottery.trie.DmgTrieImpl;
import com.buaa.lottery.trie.TrieImpl;
import com.buaa.lottery.util.Utils;
import com.buaa.lottery.util.Values;
import com.buaa.lottery.executor.ExecutorGrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class ExecutorServer {
	private static final Logger logger = Logger.getLogger(ExecutorServer.class.getName());

	private Server server;

	public static LevelDbDataSource levelDb = new LevelDbDataSource("triedb");
	private static String state_root;
	private static int state_height;
	private static int state_tx; // count from 0
	private static Integer state_latest;

	/** Start serving requests. */
	public void start() throws IOException {
		/* The port on which the server should run */
		int port = 50051;
		server = ServerBuilder.forPort(port).addService(new ExecutorService()).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its
				// JVM shutdown hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				ExecutorServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
		if(state_root == "" && state_height == 0 && state_tx == 0) {
			TrieImpl trie = new TrieImpl(levelDb);
			TrieImpl newtrie = new TrieImpl(levelDb);
			Values val = new Values(newtrie.getRoot());
			// NewTable OrderInsert transaction
			JSONObject jo = new JSONObject();
			jo.put("id", "60000000000000010000000000000000");
			jo.put("name", "OrderInsert");
			jo.put("root", Utils.bytesToHexString(val.encode()));
			DmgTrieImpl.update32(trie, jo.getString("id"), jo.toString());

			// NewTable Charge transaction
			jo = new JSONObject();
			jo.put("id", "60000000000000020000000000000000");
			jo.put("name", "Charge");
			jo.put("root", Utils.bytesToHexString(val.encode()));
			DmgTrieImpl.update32(trie, jo.getString("id"), jo.toString());

			// NewTable WithdrawInsert transaction
			jo = new JSONObject();
			jo.put("id", "60000000000000030000000000000000");
			jo.put("name", "WithdrawInsert");
			jo.put("root", Utils.bytesToHexString(val.encode()));
			DmgTrieImpl.update32(trie, jo.getString("id"), jo.toString());

			trie.sync();
			// levelDb.close();
			state_root = Utils.bytesToHexString(trie.getRootHash());
			levelDb.put("state_root".getBytes(), Utils.hexStringToBytes(state_root));
			levelDb.put("state_height".getBytes(), String.valueOf(0).getBytes());
			levelDb.put("state_tx".getBytes(), String.valueOf(0).getBytes());
			levelDb.put("state_latest".getBytes(), String.valueOf(0).getBytes());
			levelDb.put("blocktrans:0".getBytes(), "".getBytes());
			levelDb.put("block:0 tran:0".getBytes(), jo.toJSONString().getBytes());

		}
		computestart();
	}

	/** Stop serving requests and shutdown resources. */
	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main method. This comment makes the linter happy.
	 */
	public static void main(String[] args) throws Exception {
		levelDb.init();
		byte[] str = levelDb.get("state_root".getBytes());
		if (str == null)
			state_root = "";
		else
			state_root = Utils.bytesToHexString(str);
		str = levelDb.get("state_height".getBytes());
		if (str == null)
			state_height = 0;
		else {
			state_height = Integer.valueOf(new String(str));
		}
		str = levelDb.get("state_tx".getBytes());
		if (str == null)
			state_tx = 0;
		else
			state_tx = Integer.valueOf(new String(str));
		str = levelDb.get("state_latest".getBytes());
		if (str == null)
			state_latest = 0;
		else
			state_latest = Integer.valueOf(new String(str));
		if (state_height == 0)
			levelDb.put("blocktrans:0".getBytes(), "".getBytes());
		ExecutorServer server = new ExecutorServer();
		server.start();
		server.blockUntilShutdown();
	}

	/**
	 * Our implementation of Executor service.
	 *
	 * <p>
	 * See executor.proto for details of the methods.
	 */
	private static class ExecutorService extends ExecutorGrpc.ExecutorImplBase {
		/**
		 */
		public void query(QueryRequest request, io.grpc.stub.StreamObserver<StateReply> responseObserver) {
			responseObserver.onNext(querystate(request));
			responseObserver.onCompleted();
		}

		private StateReply querystate(QueryRequest request) {
			return StateReply.newBuilder().setStateRoot(state_root).setStateHeight(state_height).setStateTx(state_tx)
					.build();
		}

		/**
		 */
		public void verify(VerifyRequest request, io.grpc.stub.StreamObserver<BooleanReply> responseObserver) {
			responseObserver.onNext(verifystate(request));
			responseObserver.onCompleted();
		}

		private BooleanReply verifystate(VerifyRequest request) {
			int _state_height = request.getStateHeight();
			int _state_tx = request.getStateTx();
			String _state_root = request.getStateRoot();
			String str = result(_state_height, _state_tx);
			byte[] ja = levelDb.get(str.getBytes());
			if (ja != null) {
				JSONObject jo = JSONObject.parseObject(new String(ja));
				String root = jo.getString("root");
				System.out.println(
						"height:" + String.valueOf(state_height) + " tx:" + String.valueOf(state_tx) + " root:" + root);
				if (root.equals(_state_root))
					return BooleanReply.newBuilder().setResult(true).build();
			}
			return BooleanReply.newBuilder().setResult(false).build();
		}

		/**
		 */
		public void newblock(BlockRequest request, io.grpc.stub.StreamObserver<BooleanReply> responseObserver) {
			responseObserver.onNext(recordeblock(request));
			responseObserver.onCompleted();
		}

		private BooleanReply recordeblock(BlockRequest request) {
			JSON json = JSON.parseObject(request.getBlock());
			Block block = JSONObject.toJavaObject(json, Block.class);
			int height = block.getHeight();
			byte[] key = storeName(height).getBytes();
			List<Transaction> txs = block.getTrans();
			if (txs != null) {
				String transactionlist = JSONObject.toJSONString(txs);
				// 将每个块中的交易存储起来，key为"blocktrans:i"
				levelDb.put(key, transactionlist.getBytes());
			} else
				levelDb.put(key, "".getBytes());
			state_latest = height;
			return BooleanReply.newBuilder().setResult(true).build();
		}

	}

	private static String storeName(int blocknumber) {
		return "blocktrans:" + String.valueOf(blocknumber);
	}

	private static String result(int blocknumber, int tx) {
		return "block:" + String.valueOf(blocknumber) + " tran:" + String.valueOf(tx);
	}


	public void computestart() {
		int height;
		byte[] key;
		byte[] value;
		String transactionlist;
		String re = null;
		String result_name;
		List<Transaction> txs = null;
		try {
			while (true) {
				while (state_height <= state_latest && state_latest > 0) {
					// System.out.println("----------------hello world");
					key = storeName(state_height).getBytes();
					value = levelDb.get(key);
					transactionlist = new String(value);
					if (!transactionlist.equals(""))
						txs = JSON.parseArray(transactionlist, Transaction.class);
					if (state_height > 0) {
						while (state_tx < txs.size() - 1) {
							re = exec(levelDb, txs.get(state_tx + 1));
							JSONObject jo = JSONObject.parseObject(re);
							state_root = jo.getString("root");
							state_tx = state_tx + 1;
							result_name = result(state_height, state_tx);
							levelDb.put(result_name.getBytes(), re.getBytes());
							logger.info(result_name + " result:" + re);
						}

					}
					state_height = state_height + 1;
					state_tx = -1;
					result_name = result(state_height, state_tx);
					levelDb.put(result_name.getBytes(), re.getBytes());
				}
				Thread.sleep(10000);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			levelDb.put("state_root".getBytes(), Utils.hexStringToBytes(state_root));
			levelDb.put("state_height".getBytes(), String.valueOf(state_height).getBytes());
			levelDb.put("state_tx".getBytes(), String.valueOf(state_tx).getBytes());
			levelDb.put("state_latest".getBytes(), String.valueOf(state_latest).getBytes());
			logger.info("state_root:" + state_root + " state_height:" + String.valueOf(state_height) + " state_tx:"
					+ String.valueOf(state_tx) + " state_latest:" + String.valueOf(state_latest));
			levelDb.close();
		}

	}

	private String exec(LevelDbDataSource levelDb, Transaction transaction) {
		byte[] root = levelDb.get(Utils.hexStringToBytes(state_root));
		Values val = Values.fromRlpEncoded(root);
		TrieImpl trie = new TrieImpl(levelDb, val.asObj());
		String result = Compute.execute(trie, transaction.getData());
		String new_root = Utils.bytesToHexString(trie.getRootHash());
		JSONObject jo = new JSONObject();
		jo.put("root", new_root);
		jo.put("result", result);
		trie.sync();
		return jo.toJSONString();
		// TODO Auto-generated method stub

	}

}