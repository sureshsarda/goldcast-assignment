import io.goldcast.list.SortedDeduplicateList;

import java.util.Optional;

public class ListStatsCounter<T extends Comparable<T>> implements StatsCounter<T> {

    protected final SortedDeduplicateList<T> list;

    protected ListStatsCounter() {
        list = new SortedDeduplicateList<>();
    }


    @Override
    public void increment(T key) {
        list.increment(key);
    }

    @Override
    public void decrement(T key) {
        if (list.contains(key)) {
            list.decrement(key);
        }
    }

    @Override
    public Optional<T> getMax() {
        return list.last().stream().findFirst();
    }

    @Override
    public Optional<T> getMin() {
        return list.first().stream().findFirst();
    }
}


