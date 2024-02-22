package com.hitsz.badboyChat.common.utils.sensitiveword.ac;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/22 15:39
 */
@Data
public class ACTreeNode {

    // 子节点
    private Map<Character, ACTreeNode> children = Maps.newHashMap();

    // 匹配过程中，如果模式串不匹配，模式串指针会回退到failover继续进行匹配
    private ACTreeNode failover = null;

    private int depth;

    private boolean isLeaf = false;

    public void addChildrenIfAbsent(char c) {
        children.computeIfAbsent(c, (key) -> new ACTreeNode());
    }

    public ACTreeNode childOf(char c) {
        return children.get(c);
    }

    public boolean hasChild(char c) {
        return children.containsKey(c);
    }

    @Override
    public String toString() {
        return "ACTrieNode{" +
                "failover=" + failover +
                ", depth=" + depth +
                ", isLeaf=" + isLeaf +
                '}';
    }
}
