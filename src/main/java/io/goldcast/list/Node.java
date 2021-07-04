package io.goldcast.list;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class Node<T> {
    Node<T> prev;
    Node<T> next;
    int count;

    Set<T> keys;

    Node(T key) {
        assert key != null;
        keys = new HashSet<>();
        keys.add(key);
    }

    Node() {
        keys = new HashSet<>();
    }

    protected Optional<T> getAny() {
        return keys.stream().findFirst();
    }


    @Override
    public String toString() {
        return String.format("%d %s", count, next);
    }
}
