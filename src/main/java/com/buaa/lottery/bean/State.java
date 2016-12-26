package com.buaa.lottery.bean;


import lombok.Data;

@Data
public class State {
	private String state_root;
	private int state_height;
	private int state_tx;
	public State(String _state_root, int _state_height, int _state_tx){
		state_root = _state_root;
		state_height = _state_height;
		state_tx = _state_tx;
	}
	public State() {
		// TODO Auto-generated constructor stub
	}
}
