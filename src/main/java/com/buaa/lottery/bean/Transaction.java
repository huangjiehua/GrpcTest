package com.buaa.lottery.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.HashCode;

import lombok.Data;

@Data   
public class Transaction implements Serializable, Comparable<Transaction>{
	
	private static final long serialVersionUID = 4695627546411078836L;
	
/*	private int id;
	private int lottery_number;
	private int issue_id;
	private int location;
	private String seller;
	private Date sold_time;
	private int block_id;
	//未被选中建块的次数
	private int notselected;
	
	//For smart contract
	private int ttype; //交易类型：0，创建account；1，普通交易（包括由account发出的交易）；……
	private String from;//源地址
	private String to;//目的地址
	private JSONObject data;//带有的数据，供合约执行使用
	private String block_hash;
	private String type;
	private String timestamp;
	private int sequence;
	private String sign;
	private String version;
	private String extra;
	private String data;
	
	public Transaction(){
		
	}*/
	
	private String block_hash;
	private String tran_hash;
	private String type;
	private Timestamp timestamp;
	private int sequence;
	private String sign;
	private String version;
	private String extra;
	private String data;
	private Integer tranSeq;
	
	public Transaction(String _block_hash, String _tran_hash, String _type, Timestamp _timestamp, 
			int _sequence, String _sign, String _version, String _extra, String _data) {
		block_hash = _block_hash;
		tran_hash = _tran_hash;
		type = _type;
		timestamp = _timestamp;
		sequence = _sequence;
		sign = _sign;
		version = _version;
		extra = _extra;
		data = _data;
	}
	
	public Transaction() {
		
	}
	
    public String getData() {
    	return data;
    }

	
	@Override
	public int compareTo(Transaction arg0) {
		return this.getTranSeq().compareTo(arg0.getTranSeq());
	}
	
	/*public int hashCode(){
		
		return (String.valueOf(lottery_number)+String.valueOf(issue_id)).hashCode();
		
	}*/
}
