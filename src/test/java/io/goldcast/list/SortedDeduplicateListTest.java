package io.goldcast.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SortedDeduplicateListTest {

    private SortedDeduplicateList<String> list;

    @BeforeEach
    public void beforeEach() {
        list = new SortedDeduplicateList<>();
    }


    @Test
    public void testSentinelsAreCreated() {
        Assertions.assertNotNull(list.head);
        Assertions.assertNotNull(list.tail);
        Assertions.assertEquals(list.head.next, list.tail);
        Assertions.assertEquals(list.tail.prev, list.head);
    }

    @Test
    public void testAddingAValueInsertsItAtTheFirstPlace() {
        list.increment("A");

        Assertions.assertEquals(1, list.head.next.keys.size());
        Assertions.assertEquals(1, list.tail.prev.keys.size());
    }

    @Test
    public void testIncrementingIncreasesTheCount() {
        list.increment("A");
        Assertions.assertEquals(1, list.lookup.get("A").count);

        list.increment("A");
        Assertions.assertEquals(2, list.lookup.get("A").count);

        list.increment("B");
        Assertions.assertEquals(1, list.lookup.get("B").count);

        list.increment("C");
        Assertions.assertEquals(2, list.lookup.get("A").count);
        Assertions.assertEquals(1, list.lookup.get("B").count);
        Assertions.assertEquals(1, list.lookup.get("C").count);

        list.increment("B");
        Assertions.assertEquals(2, list.lookup.get("A").count);
        Assertions.assertEquals(2, list.lookup.get("B").count);
        Assertions.assertEquals(1, list.lookup.get("C").count);

    }

    @Test
    public void testNodeAreSplitAndMergedWhenIncremented() {
        list.increment("A");
        list.increment("B");
        list.increment("A");

        Node<String> nodeA = list.lookup.get("A");
        Node<String> nodeB = list.lookup.get("B");

        Assertions.assertEquals(nodeA.prev, nodeB);
        Assertions.assertEquals(nodeB.next, nodeA);

        list.increment("B");
        list.increment("B");
        list.increment("B");

        nodeA = list.lookup.get("A");
        nodeB = list.lookup.get("B");

        Assertions.assertEquals(nodeB.prev, nodeA);
        Assertions.assertEquals(nodeA.next, nodeB);

        list.increment("A");
        list.increment("A");

        nodeA = list.lookup.get("A");
        nodeB = list.lookup.get("B");
        Assertions.assertEquals(nodeB, nodeA);
    }

    @Test
    public void testNodeAreSplitAndMergedWhenDecremented() {
        list.increment("A");
        list.increment("B");

        // directly increment count of both
        Node<String> node = list.lookup.get("A");
        node.count = 10;

        list.decrement("B");
        Node<String> nodeA = list.lookup.get("A");
        Node<String> nodeB = list.lookup.get("B");

        Assertions.assertEquals(nodeA.prev, nodeB);
        Assertions.assertEquals(nodeB.next, nodeA);

        list.decrement("A");

        nodeA = list.lookup.get("A");
        nodeB = list.lookup.get("B");
        Assertions.assertEquals(nodeB, nodeA);

        list.decrement("A");
        list.decrement("A");

        nodeA = list.lookup.get("A");
        nodeB = list.lookup.get("B");

        Assertions.assertEquals(nodeB.prev, nodeA);
        Assertions.assertEquals(nodeA.next, nodeB);

    }

    @Test
    public void testDecreasingCountToLessThanZero() {
        // TODO Implementation pending
    }

    @Test
    public void testIncreasingCountToMoreThanInfinityShouldBehandledGracefully() {
        // Won't work
        // TODO Implementation pending
    }

}
