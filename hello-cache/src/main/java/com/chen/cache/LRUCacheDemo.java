package com.chen.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义实现 LRU 缓存
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-06-24
 */
@Slf4j
public class LRUCacheDemo {

    private int cacheSize;
    private int currentSize;
    private CacheNode head;
    private CacheNode tail;
    private Map<Integer, CacheNode> nodes;

    class CacheNode {
        CacheNode prev;
        CacheNode next;
        int key;
        int value;

        @Override
        public String toString() {
            return "CacheNode{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    public LRUCacheDemo(int cacheSize) {
        this.cacheSize = cacheSize;
        currentSize = 0;
        nodes = new HashMap<>(cacheSize);
    }

    public void put(Integer key, Integer value) {
        if (nodes.get(key) == null) {
            CacheNode node = new CacheNode();
            node.key = key;
            node.value = value;
            nodes.put(key, node);
            moveToHead(node);
            if (currentSize > cacheSize) {
                removeTail();
            } else {
                currentSize++;
            }
        } else {
            CacheNode node = nodes.get(key);
            moveToHead(node);
            node.value = value;
        }
    }

    private void removeTail() {
        if (tail != null) {
            nodes.remove(tail.key);
            if (tail.prev != null) {
                tail.prev.next = null;
                tail = tail.prev;
            }
        }
    }

    private void moveToHead(CacheNode node) {
        // 链表中间的元素
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }

        // 移动到表头
        node.prev = null;
        if (head == null) {
            head = node;
        } else {
            node.next = head;
            head.prev = node;
        }

        head = node;
        // 更新tail
        if (tail == node) {
            tail = null;
        }

        // 如果缓存就一个元素
        if (tail == null) {
            tail = node;
        }
    }

    public int get(int key) {
        if (nodes.get(key) != null) {
            CacheNode node = nodes.get(key);
            moveToHead(node);
            return node.value;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "LRUCacheDemo{" +
                "cacheSize=" + cacheSize +
                ", currentSize=" + currentSize +
                ", head=" + head +
                ", tail=" + tail +
                ", nodes=" + nodes +
                '}';
    }

    public static void main(String[] args) {
        LRUCacheDemo cache = new LRUCacheDemo(10);
        for (int i = 0; i < 15; i++) {
            cache.put(i, i);
        }
        log.info(cache.toString());
    }
}
