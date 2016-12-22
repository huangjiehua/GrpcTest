package com.buaa.lottery.util;

import java.io.Serializable;


public interface RLPElement extends Serializable {

    byte[] getRLPData();
}
