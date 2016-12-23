package com.buaa.lottery.executor;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.*;

import com.buaa.lottery.executor.ExecutorGrpc;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;
import com.buaa.lottery.bean.Block;
import com.buaa.lottery.bean.Blockheader;
import com.buaa.lottery.bean.Transaction;
import com.buaa.lottery.executor.ExecutorClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class ExecutorClient {
	private static final Logger logger = Logger.getLogger(ExecutorClient.class.getName());

	private final ManagedChannel channel;
	private final ExecutorGrpc.ExecutorBlockingStub blockingStub;
	private TestHelper testHelper;

	/** Construct client connecting to Executor server at {@code host:port}. */
	public ExecutorClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port)
				// Channels are secure by default (via SSL/TLS). For the example
				// we disable TLS to avoid
				// needing certificates.
				.usePlaintext(true));
	}

	/**
	 * Construct client for accessing Executor server using the existing
	 * channel.
	 */
	ExecutorClient(ManagedChannelBuilder<?> channelBuilder) {
		channel = channelBuilder.build();
		blockingStub = ExecutorGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void newblock(String block) {

		BlockRequest request = BlockRequest.newBuilder().setBlock(block).build();
		BooleanReply boolreply;
		try {
			boolreply = blockingStub.newblock(request);
			if(boolreply.getResult())
				System.out.println("true");
			else
				System.out.println("false");
			if (testHelper != null) {
				testHelper.onMessage(request);
			}
		} catch (StatusRuntimeException e) {
			warning("RPC failed: {0}", e.getStatus());
			if (testHelper != null) {
				testHelper.onRpcError(e);
			}
			return;
		}

	}

	public void query(String str) {

		QueryRequest request = QueryRequest.newBuilder().setMessage(str).build();
		StateReply statereply;
		try {
			statereply = blockingStub.query(request);
			System.out.println("stateroot: " + statereply.getStateRoot() + " stateheight: " + statereply.getStateHeight()
					+ " statetx: " + statereply.getStateTx());
			if (testHelper != null) {
				testHelper.onMessage(request);
			}
		} catch (StatusRuntimeException e) {
			warning("RPC failed: {0}", e.getStatus());
			if (testHelper != null) {
				testHelper.onRpcError(e);
			}
			return;
		}

	}

	public void verify(String stateroot, int stateheight, int statetx) {

		VerifyRequest request = VerifyRequest.newBuilder().setStateRoot(stateroot).setStateHeight(stateheight)
				.setStateTx(statetx).build();
		BooleanReply boolreply;
		try {
			boolreply = blockingStub.verify(request);
			if (boolreply.getResult())
				System.out.println("true");
			else
				System.out.println("false");
			if (testHelper != null) {
				testHelper.onMessage(request);
			}
		} catch (StatusRuntimeException e) {
			warning("RPC failed: {0}", e.getStatus());
			if (testHelper != null) {
				testHelper.onRpcError(e);
			}
			return;
		}

	}

	/** Issues several different requests and then exits. */
	public static void main(String[] args) throws InterruptedException {

		ExecutorClient client = new ExecutorClient("localhost", 50051);
		Block block = new Block();
		try {
			// newblock
			block.setPre_hash("pre_hash");
			block.setMerkle_root("merkle_root");
			block.setState_root("state_root");
			block.setState_height(0);
			block.setState_tx(0);
			block.setHeight(0);
			block.setSign("sign");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = sdf.format(new Date());
			block.setTimestamp(timestamp);
			block.setVersion("version");
			block.setExtra("extra");
			ArrayList<Transaction> transList = new ArrayList<Transaction>();
			block.setTrans(transList);
			block.setTx_length(transList.size());
			Blockheader header = new Blockheader(block);
			block.setHash(header.compute_hash());
			block.setExtra("extra");
			JSONObject jo = JSONObject.fromObject(block);

			client.newblock(jo.toString());

			client.query("");

			client.verify("", 0, 0);

		} finally {
			client.shutdown();
		}
	}

	private void info(String msg, Object... params) {
		logger.log(Level.INFO, msg, params);
	}

	private void warning(String msg, Object... params) {
		logger.log(Level.WARNING, msg, params);
	}

	interface TestHelper {
		/**
		 * Used for verify/inspect message received from server.
		 */
		void onMessage(Message message);

		/**
		 * Used for verify/inspect error received from server.
		 */
		void onRpcError(Throwable exception);
	}

	@VisibleForTesting
	void setTestHelper(TestHelper testHelper) {
		this.testHelper = testHelper;
	}
}
