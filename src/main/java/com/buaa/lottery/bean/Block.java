package com.buaa.lottery.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Block implements Serializable, Comparable<Block> {
	
	private static final long serialVersionUID = 4695627546411078831L;
	
	/*private int id;
	private int pid;//前一个block的id
//	private String nonce;
//	private String difficulty;
	private String merkle_root;
	private int tx_lenth;
	private String hash;
	private String timestamp;
	private ArrayList<Transaction> trans;*/
	
	private String pre_hash;
	private String state_root;
	//private String pre_state_root;
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
	private Times times;
	
	
//	private byte bitarray[] = new byte[300];
	
	public Block(){
		
	}
	
	/*@Override
	public String toString() {
		return "Block [id=" + id + ", pid=" + pid + ", merkle_root="
				+ merkle_root + ", tx_lenth=" + tx_lenth + ", hash=" + hash
				+ ", timestamp=" + timestamp + "]";
	}*/
	
	@Override
	public int compareTo(Block b) {
		// TODO Auto-generated method stub
		if(
				this.merkle_root.equals(b.merkle_root) &&
				this.tx_length == b.tx_length &&
				this.tx_length == b.tx_length && 
				this.hash == b.hash
		){
			Map<Integer, Transaction> map = new HashMap<Integer, Transaction>();
			for (Transaction tran : this.trans) {
				map.put(tran.hashCode(), tran);
			}
			for (Transaction tran : b.trans) {
				if (map.get(tran.hashCode()) == null || map.get(tran.hashCode()).compareTo(tran) != 0) {
					return -1;
				}
			}
		}
		return 0;
	}


	
}
