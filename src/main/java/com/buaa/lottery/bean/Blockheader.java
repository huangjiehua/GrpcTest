package com.buaa.lottery.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.buaa.lottery.util.Utils;

import lombok.Data;

@Data
public class Blockheader implements Serializable {
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

	public Blockheader() {

	}

	public Blockheader(Block block) {
		pre_hash = block.getPre_hash();
		state_root = block.getState_root();
		state_height = block.getState_height();
		state_tx = block.getState_tx();
		merkle_root = block.getMerkle_root();
		tx_length = block.getTx_length();
		height = block.getHeight();
		sign = block.getSign();
		version = block.getVersion();
		extra = block.getExtra();
		hash = block.getHash();
		timestamp = block.getTimestamp();
	}

	public String compute_hash() {
		StringBuilder input = new StringBuilder();
		List<String> header = new ArrayList<String>();

		header.add(pre_hash);
		header.add(state_root);
		header.add(String.valueOf(state_height));
		header.add(String.valueOf(state_tx));
		header.add(merkle_root);
		header.add(String.valueOf(tx_length));
		header.add(String.valueOf(height));
		header.add(sign);
		header.add(version);
		// extra和hash不用计算在内
		header.add(timestamp);
		// times是干什么用的
		for (int i = 0; i < header.size(); i++) {
			input.append(header.get(i));
		}

		return Utils.hash_md5(input.toString());
	}
}
