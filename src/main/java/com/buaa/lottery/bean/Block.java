package com.buaa.lottery.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Block implements Serializable {
	
	private static final long serialVersionUID = 4695627546411078831L;
	
	private String pre_hash;
	private String state_root;
	private int state_height;
	private int state_tx;
	private String merkle_root;
	private int tx_length;
	private int height;
	private String sign;
	private String version;
	private String extra;
	private String hash;
	private String timestamp;
	private ArrayList<Transaction> trans;
	
	public Block(){
		
	}	

}
