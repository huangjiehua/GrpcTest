package com.buaa.lottery.trie;

import com.buaa.lottery.db.ByteArrayWrapper;
import com.buaa.lottery.util.Values;

import java.util.HashSet;
import java.util.Set;


public class CollectFullSetOfNodes implements TrieImpl.ScanAction {
    Set<ByteArrayWrapper> nodes = new HashSet<>();

    @Override
    public void doOnNode(byte[] hash, Values node) {
        nodes.add(new ByteArrayWrapper(hash));
    }

    public Set<ByteArrayWrapper> getCollectedHashes() {
        return nodes;
    }
}
