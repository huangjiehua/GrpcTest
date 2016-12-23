package com.buaa.lottery.constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Data;


@Data
public class SystemConstant {

	public static final Boolean LEADER_MODE = true;

	public static final int NODECOUNT = 1;//节点数量
	public static final float MAJORITY = 2/3;
	public static final float MAJORITY_COUNT = 1;//投票通过的节点数量

	public static final int SLEEP_COUNT = 10;

	public static final int INIT_REDIS_TRSANS_COUNT = 1;

	public static final int BIT_LENGTH = 200000;
	public static final float REP_INIT = (float) 0.6;
	
	public static final float REP_LEAST=(float)-1;
	public static final Boolean IS_OPEN_FAKE_TRANS = false;
	public static final int FAKE_REDIS_INTERVAL = 1;
	public static final int FAKE_REDIS_PER_SECOND = 100;
	public static final int FAKE_REDIS_SECONDS = 100;
	
	public static MessageDigest md = null;

	public static int HEIGHT=1;
	public static int ROUND=0;
	
	public static int id = 1;
	
	public static String PREHASH="";
	public static String SIJNATURE="";
	static{
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
