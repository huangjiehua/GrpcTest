package com.buaa.lottery.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.HashCode;

import lombok.Data;

@Data   
public class Transaction implements Serializable{
	
	private static final long serialVersionUID = 4695627546411078836L;
	
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

	

}
