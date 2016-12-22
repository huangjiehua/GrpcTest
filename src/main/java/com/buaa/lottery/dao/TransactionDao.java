package com.buaa.lottery.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import com.buaa.lottery.bean.Transaction;

public class TransactionDao {

	//将区块链中的交易存入数据库中
	public String insertAllTrans(Map map) {
		
		List<Transaction> trans = (List<Transaction>) map.get("list");
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO transaction ");
		sb.append("(tran_hash,block_hash,type,timestamp,sequence,sign,version,extra,tranSeq,data) ");
		sb.append("VALUES ");
		MessageFormat mf = new MessageFormat("(#'{'list[{0}].tran_hash},"+ "#'{'list[{0}].block_hash}, " + "#'{'list[{0}].type}, " + "#'{'list[{0}].timestamp}, "
				+ "#'{'list[{0}].sequence}, " + "#'{'list[{0}].sign}, " + "#'{'list[{0}].version}, "
				+ "#'{'list[{0}].extra}, " + "#'{'list[{0}].tranSeq} ," + "#'{'list[{0}].data} " + ")");
		for (int i = 0; i < trans.size(); i++) {
			sb.append(mf.format(new String[] { String.valueOf(i) }));
			if (i < trans.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
