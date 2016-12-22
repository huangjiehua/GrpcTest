package com.buaa.lottery.trie;

import java.util.HashSet;
import java.util.*;

import com.buaa.lottery.util.Values;

public class CollectFullSetOfLeafNode implements TrieImpl.SimpleScanAction {
    List<Values> nodes = new ArrayList<Values>();

    @Override
    public void doOnNode(Values node) {
        nodes.add(node);
    }

    public List<Values> getCollectedNodes() {
        return nodes;
    }
}