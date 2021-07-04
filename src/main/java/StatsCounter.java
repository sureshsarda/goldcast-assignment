import java.util.Optional;

/**
 * A statistic counter to keep summary of entries being increased and decreased.
 * Current it provides methods to return min and max, but it can be easily extended
 * to keep a track of mean, median and other stats about the keys
 *
 * @param <T>
 */
public interface StatsCounter<T extends Comparable<T>> {

    void increment(T key);

    void decrement(T key);

    Optional<T> getMax();

    Optional<T> getMin();
}
