import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


// FIXME this class required more work
public class ListStatsCounterTest {

    private StatsCounter<String> counter;

    @BeforeEach
    public void beforeEach() {
        counter = new ListStatsCounter<>();
    }

    @Test
    public void testEmptyCounterShouldReturnNothing() {
        assertFalse(counter.getMin().isPresent());
        assertFalse(counter.getMax().isPresent());
    }

    @Test
    public void testMinValueIsReturnedCorrectly() {
        counter.increment("A");

        assertEquals("A", counter.getMin().get());

        counter.increment("B");
        assertTrue(Arrays.asList("A", "B").contains(counter.getMin().get()));

        counter.increment("B");
        assertEquals("A", counter.getMin().get());

        counter.increment("A");

    }

    @Test
    @RepeatedTest(15)
    public void testStateIsMaintainedInRandomTesting() {
        Map<String, Integer> counts = new HashMap<>();
        char[] seed = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();

        for (char ch : seed) {
            counts.put(String.valueOf(ch), 0);
        }


        Random rand = new Random();

        for (int i = 0; i < 1000 + rand.nextInt(10000); i++) {
            boolean shouldIncrement = rand.nextBoolean();
            String key = String.valueOf(seed[rand.nextInt(seed.length)]);
            if (shouldIncrement) {
                counter.increment(key);
                counts.put(key, counts.get(key) + 1);
            } else {
                counter.decrement(key);
                counts.put(key, Math.max(counts.get(key) - 1, 0));
            }
        }


        final List<String> zeroKeys =
                counts.entrySet().stream()
                        .filter(i -> i.getValue() <= 0)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
        zeroKeys.forEach(counts::remove);

        OptionalInt min = counts.values().stream().mapToInt(value -> value).min();
        OptionalInt max = counts.values().stream().mapToInt(value -> value).max();

        final List<String> minElements = counts.entrySet().stream()
                .filter(i -> i.getValue() == min.getAsInt())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        final List<String> maxElements = counts.entrySet().stream()
                .filter(i -> i.getValue() == max.getAsInt())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        assertTrue(minElements.contains(counter.getMin().get()));
        assertTrue(maxElements.contains(counter.getMax().get()));


    }

}
