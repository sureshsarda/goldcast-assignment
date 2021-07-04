package io.goldcast.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A doubly linked list that maintains elements with their counts in sorted order
 *
 * @param <T>
 */
public class SortedDeduplicateList<T> {

    /*
    Implementation Notes:

    - Uses a hashmap to store the nodes in the linked list for faster retrival.
    - Nodes are merged on the basis of count value
    - Head and tail sentinels are used to avoid special boundary cases
     */

    protected final Node<T> head;
    protected final Node<T> tail;

    protected final Map<T, Node<T>> lookup;


    public SortedDeduplicateList() {
        lookup = new HashMap<>();


        // sentinel, count will always be 0 and infinity
        head = new Node<>();
        tail = new Node<>();
        tail.count = Integer.MAX_VALUE;
        head.next = tail;
        tail.prev = head;
    }

    public boolean contains(T t) {
        return lookup.containsKey(t);
    }

    protected void tryAdding(T t) {
        assert !contains(t);
        // since it's not present, the value will always be 1
        // so this is either the second node or we have to insert one
        Node<T> targetNode = head.next.count == 1 ? head.next : insertAfter(head);
        targetNode.keys.add(t);
        lookup.put(t, targetNode);
    }

    public void increment(T t) {
        if (contains(t)) {
            Node<T> currentNode = lookup.get(t);
            int targetCount = currentNode.count + 1;

            if (currentNode.next.count != targetCount) {
                insertAfter(currentNode).count = targetCount;
            }

            currentNode.next.keys.add(t);
            currentNode.keys.remove(t);
            lookup.put(t, currentNode.next);

            if (currentNode.keys.isEmpty()) {
                remove(currentNode);
            }
        } else {
            tryAdding(t);
        }
    }

    public void decrement(T t) {
        if (contains(t)) {
            Node<T> node = lookup.get(t);

            int targetCount = node.count - 1;


            if (targetCount != 0) {
                if (node.prev.count != targetCount) {
                    insertAfter(node.prev).count = targetCount;
                }

                node.prev.keys.add(t);
                lookup.put(t, node.prev);
            } else {
                lookup.remove(t);
            }

            node.keys.remove(t);

            if (node.keys.isEmpty()) {
                remove(node);
            }
        } else {
            throw new IllegalArgumentException("Cannot decrement a key that is not present. Key: " + t);
        }
    }

    public Set<T> first() {
        return isEmpty() ? Collections.emptySet() : head.next.keys;
    }

    public Set<T> last() {
        return isEmpty() ? Collections.emptySet() : tail.prev.keys;
    }

    private boolean isEmpty() {
        return head.next == tail && tail.prev == head;
    }

    private void remove(Node<T> node) {
        // don't remove the first or the last node
        assert node.prev != null && node.next != null;

        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private Node<T> insertAfter(Node<T> node) {
        // never insert after that last node
        assert node.next != null;

        Node<T> newNode = new Node<>();
        newNode.count = 1;

        node.next.prev = newNode;
        newNode.next = node.next;

        node.next = newNode;
        newNode.prev = node;

        return newNode;
    }
}

