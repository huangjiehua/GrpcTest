package com.buaa.lottery.transaction;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import com.buaa.lottery.bean.Transaction;
import com.buaa.lottery.compute.ComputeClient;
import com.buaa.lottery.datasource.LevelDbDataSource;
import com.buaa.lottery.executor.ExecutorClient;
import com.buaa.lottery.trie.CollectFullSetOfLeafNode;
import com.buaa.lottery.trie.DmgTrieImpl;
import com.buaa.lottery.trie.TrieImpl;
import com.buaa.lottery.util.Utils;
import com.buaa.lottery.util.Values;
import com.buaa.lottery.vm.Cloader;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
@Slf4j
public class Compute {
	private static final Logger logger = LoggerFactory.getLogger("transaction");
	private LevelDbDataSource levelDb = new LevelDbDataSource("triedb");
	
	public static void apply_transactions(TrieImpl trie, List<Transaction> txs) {
		Iterator it = txs.iterator();
		// hasNext是取值取的是当前值.他的运算过程是判断下个是否有值如果有继续.
		while (it.hasNext()) {
			execute(trie, ((Transaction) it.next()).getData());
		}
	}
	
	public static String execute(TrieImpl trie, String tx) {
		Cloader cl=new Cloader(); 
		JSONObject jo = JSONObject.fromObject(tx);
		Map<String, String> map = new HashMap<String, String>();
		String result = "";
		ComputeClient client = new ComputeClient("localhost", 50052);
//		log.info("transaction: "+jo.toString());
		try {
			map.put("method", jo.getString("method"));
			map.put("parameter", jo.getString("parameter"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (map.get("method")) {
		case "Mount": {
			result = "";
			try {
				result = client.compute(tx);

			} finally {
				try {
					client.shutdown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		}
		case "Purchase": {
			result = "";
			try {
				result = client.compute(tx);

			} finally {
				try {
					client.shutdown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		}
		case "Payback": {

			result = "";
			// transaction_data
			jo = JSONObject.fromObject(map.get("parameter"));
			CollectFullSetOfLeafNode o = new CollectFullSetOfLeafNode();
			trie.scanLeaf(trie.getRootHash(), o);
			List<Values> nodes = o.getCollectedNodes();
			Iterator<Values> it = nodes.iterator();
			Values value;
			List<String> lists = null;
			while (it.hasNext()) {

				Values v = (Values) it.next();
				List<Object> list = v.asList();
				if (list.size() == 2) {
					value = new Values(list.get(1));
				} else {
					value = new Values(list.get(16));

				}
				try {
					JSONObject obj = JSONObject.fromObject(value.toString());
					// com.alibaba.fastjson.JSONObject obj =
					// com.alibaba.fastjson.JSONObject.parseObject(value.toString());
					if (obj.containsKey("storage")) {
						obj = JSONObject.fromObject(obj.getString("storage"));
					}

					lists = new ArrayList<String>();

					if (obj.containsKey("projectID")) {
						String sss = obj.getString("projectID");
						lists.add(sss);
						String rlpdata = DmgTrieImpl.get32(trie, sss, "storage");
						if (rlpdata.equals("")) {
							log.error("null project");
							break;
						}
						JSONObject ja = JSONObject.fromObject(rlpdata);
						// execute the code
						byte[] bt = Utils.hexStringToBytes(ja.getString("project_code"));
						int count = bt.length;
						try {
							Class clazz=cl.load(bt, "com.buaa.lottery.vm.Project");
							Constructor<?> cs[] = clazz.getConstructors();
							Object oj = cs[0].newInstance(trie, ja.toString(), jo.toString());
							result = (String) clazz.getMethod("payback", null).invoke(oj, null);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
						if (result != "") {
							log.info("execute transaction " + map.get("method"));
							log.info(result);
						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

//			for (int x = 0; x < lists.size(); x++) {
//				log.info("project list ==========" + lists.get(x));
//			}
//			break;
			result = "pay back";
			return result;
		}
		case "OrderInsert": {
			JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
			DmgTrieImpl.update32(trie, jo1.getString("id"), jo1.toString());
//			log.info("OrderInsert=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id"))));
//			break;
			result = "OrderInsert=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id")));
			return result;
		}
		case "Charge": {
			JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
			DmgTrieImpl.update32(trie, jo1.getString("id"), jo1.toString());
//			log.info("Charge=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id"))));
//			break;
			result = "Charge=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id")));
			return result;
		}
		case "WithdrawInsert": {
			JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
			DmgTrieImpl.update32(trie, jo1.getString("id"), jo1.toString());
//			log.info("WithdrawInsert=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id"))));
//			break;
			result = "WithdrawInsert=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id")));
			return result;
		}
		case "NewUser": {
			JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
			DmgTrieImpl.update32(trie, jo1.getString("userID"), jo1.toString());
//			log.info("NewUser=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("userID"))));
//			break;
			result = "NewUser=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("userID")));
			return result;
		}
		case "NewTable": {
			JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
			DmgTrieImpl.update32(trie, jo1.getString("id"), jo1.toString());
//			log.info("NewTable=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id"))));
//			break;
			result = "NewTable=======" + new String(DmgTrieImpl.get32(trie, jo1.getString("id")));
			return result;
		}
		default:
//			log.error("wrong method name");
			result = "wrong method name";
			return result;
		}
	}
}