package com.buaa.lottery.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Times implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String block_hash;
	private int tx_length;
	private Timestamp startCompute;	//leader has been chosen and leader start compute block
	private Timestamp broadcast;	//computing block is finished and start broadcast the block
	private Timestamp blockReceived;	//block is received and start verify
	private Timestamp sendVote;	//verification is finished and send vote
	private Timestamp voteReceived;	//received quorum vote and start apply transaction
	private Timestamp storeBlock;	//start store block
	private Timestamp removeTrans;	//start remove transactions that have been executed from redis
	private Timestamp storeTrans;	//start store transactions that have been executed
	private Timestamp endTime;	//all procedures are finished
}
