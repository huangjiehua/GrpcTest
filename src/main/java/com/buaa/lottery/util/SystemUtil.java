package com.buaa.lottery.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import reactor.core.Reactor;
import reactor.event.Event;

import com.buaa.lottery.bean.Transaction;
import com.buaa.lottery.constants.SystemConstant;
import com.buaa.lottery.crypto.HashUtil;

public class SystemUtil {
	

	public static String transKeyInRedis(Transaction ts) {
//		return ts.getIssue_id() + "" + ts.getLocation() + ""
//				+ ts.getLottery_number() + "" + ts.getSeller();
		
		StringBuilder key = new StringBuilder();
		
		key.append(ts.getType());
		key.append(ts.getTimestamp());
		key.append(String.valueOf(ts.getSequence()));
		key.append(ts.getSign());
		key.append(ts.getVersion());

		key.append(ts.getData());

		
		byte[] hash = HashUtil.sha3(key.toString().getBytes());
		return hash.toString();
	}
	
	public static int hash(String key){
		int h = key.hashCode();
		h = h > 0 ? h : h*(-1);
		return (key == null) ? 0 : (h  ^ (h >>> 16));

	}
	
	public static byte[] orOperation(byte[] b1,byte[] b2 ){
		if(b1.length!=b2.length){
			return null;
		}
		for(int i=0;i<b1.length;i++){
			b1[i]=(byte) (b1[i]|b2[i]);
		}
		return b1;
	}
	
//	public static String joinString(String height, String round, String tail, String split){
//		return height+split+round+split+tail;
//	}
	
	/**
	 * 拼接字符串
	 * @param split
	 * @param paras
	 * @return
	 */
	public static String joinString(String split, String ... paras){
		return String.join(split, paras);
	}
	
	public static String[] splitString(String str, String spliter) {
		// TODO Auto-generated method stub
		return str.split(spliter);
	}
}
