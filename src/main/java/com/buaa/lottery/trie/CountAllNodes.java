package com.buaa.lottery.trie;

import com.buaa.lottery.util.Values;


public class CountAllNodes implements TrieImpl.ScanAction {

    int counted = 0;

    @Override
    public void doOnNode(byte[] hash, Values node) {
        ++counted;
    }

    public int getCounted() {
        return counted;
    }
}
