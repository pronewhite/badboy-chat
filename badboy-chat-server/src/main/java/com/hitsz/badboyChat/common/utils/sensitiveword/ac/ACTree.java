package com.hitsz.badboyChat.common.utils.sensitiveword.ac;

import java.util.*;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/22 15:39
 */
public class ACTree {

    private ACTreeNode root;

    public ACTree(List<String> words){
        root = new ACTreeNode();
        for (String word : words) {
            addWord(word);
        }
        initFailover();
    }
    // 初始化失败之后跳转的节点
    private void initFailover() {
        // 第一层的failover指向root
        Queue<ACTreeNode> queue = new LinkedList<>();
        Map<Character, ACTreeNode> children = root.getChildren();
        for (Map.Entry<Character, ACTreeNode> entry : children.entrySet()) {
            ACTreeNode value = entry.getValue();
            value.setFailover(root);
            queue.offer(value);
        }
        // 层次遍历
        while(!queue.isEmpty()){
            ACTreeNode parentNode = queue.poll();
            for(Map.Entry<Character, ACTreeNode> entry : parentNode.getChildren().entrySet()){
                ACTreeNode childNode = entry.getValue();
                ACTreeNode failover = parentNode.getFailover();
                if(failover != null && (!failover.hasChild(entry.getKey()))){
                    failover = failover.getFailover();
                }
                if(failover == null){
                    // 表示回退到了根节点
                    childNode.setFailover(root);
                }else{
                    childNode.setFailover(failover.childOf(entry.getKey()));
                }
                queue.offer(childNode);
            }
        }

    }

    private void addWord(String word) {
        ACTreeNode wordNode = root;
        char[] words = word.toCharArray();
        for (int i = 0; i < words.length; i++) {
            wordNode.addChildrenIfAbsent(words[i]);
            wordNode = wordNode.childOf(words[i]);
            // 设置深度
            wordNode.setDepth(i + 1);
        }
        wordNode.setLeaf(true);
    }

    public List<MatchResult> match(String text){
        List<MatchResult> result = new ArrayList<>();
        ACTreeNode node = root;
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            while(!node.hasChild(aChar) && node.getFailover() != null){
                node = node.getFailover();
            }
            if(node.hasChild(aChar)){
                node = node.childOf(aChar);
                if(node.isLeaf()){
                    result.add(new MatchResult(i - node.getDepth() + 1, i));
                    // 多模匹配，查询文本中包含的其他敏感词
                    node = node.getFailover();
                }
            }
        }
        return result;
    }
}
