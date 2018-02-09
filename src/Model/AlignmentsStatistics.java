package Model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Model.Alignment.FLAG;
import static Model.Alignment.MAPQ;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.toBinaryString;

public class AlignmentsStatistics {

    private final AtomicLong total;
    private final AtomicLong unmapped;
    private final AtomicLong multiplyMapped;

    private final AtomicLong totalMapQ;
    private final AtomicLong unmappedMapQ;
    private final AtomicLong multiplyMapQ;

    private final ConcurrentMap<Integer, Long> totalMapQDistribution;
    private final ConcurrentMap<Integer, Long> unmappedMapQDistribution;
    private final ConcurrentMap<Integer, Long> multiplyMapQDistribution;

    public AlignmentsStatistics() {
        total = new AtomicLong();
        unmapped = new AtomicLong();
        multiplyMapped = new AtomicLong();

        totalMapQ = new AtomicLong();
        unmappedMapQ = new AtomicLong();
        multiplyMapQ = new AtomicLong();

        totalMapQDistribution = new ConcurrentHashMap<>();
        unmappedMapQDistribution = new ConcurrentHashMap<>();
        multiplyMapQDistribution = new ConcurrentHashMap<>();
    }

    public Map<Integer, Long> getTotalMapQDistribution() {
        return totalMapQDistribution;
    }

    public Map<Integer, Long> getUnmappedMapQDistribution() {
        return unmappedMapQDistribution;
    }

    public Map<Integer, Long> getMultiplyMapQDistribution() {
        return multiplyMapQDistribution;
    }

    public Map<Integer, Long> getUniquelyMapQDistribution() {
        Map<Integer, Long> intermediate = Stream.concat(totalMapQDistribution.entrySet().stream(), unmappedMapQDistribution.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // The key
                        Map.Entry::getValue, // The value
                        // The "merger"
                        (value1, value2) -> value1 - value2
                        )
                );
        intermediate = Stream.concat(intermediate.entrySet().stream(), multiplyMapQDistribution.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // The key
                        Map.Entry::getValue, // The value
                        // The "merger"
                        (value1, value2) -> value1 - value2
                        )
                );
        intermediate.values().removeAll(Collections.singleton(0L));
        return intermediate;
    }

    public long getTotal() {
        return total.longValue();
    }

    public long getUnmapped() {
        return unmapped.longValue();
    }

    public long getMultiplyMapped() {
        return multiplyMapped.longValue();
    }

    public long getUniquelyMapped() {
        return total.get() - unmapped.get() - multiplyMapped.get();
    }

    public long getTotalMapQ() {
        return totalMapQ.longValue();
    }

    public long getUnmappedMapQ() {
        return unmappedMapQ.longValue();
    }

    public long getMultiplyMapQ() {
        return multiplyMapQ.longValue();
    }

    public long getUniquelyMapQ() {
        return totalMapQ.get() - unmappedMapQ.get() - multiplyMapQ.get();
    }

    public void update(Alignment alignment) {
        String flag = toBinaryString(parseInt(alignment.getParam(FLAG)));
        int mapQ = parseInt(alignment.getParam(MAPQ));

        if (flag.charAt(flag.length() - 3) == '1') increaseUnmapped(mapQ);
        else if (alignment.checkOptionalParm("XA:Z:")) increaseMultiplyMapped(mapQ);
        else increaseTotal(mapQ);
    }

    private synchronized void increaseTotal(int mapQ) {
        total.incrementAndGet();
        totalMapQ.addAndGet(mapQ);
        totalMapQDistribution.compute(mapQ, (integer, aLong) -> aLong == null ? 1 : aLong + 1);
    }

    private synchronized void increaseUnmapped(int mapQ) {
        increaseTotal(mapQ);
        unmapped.incrementAndGet();
        unmappedMapQ.addAndGet(mapQ);
        unmappedMapQDistribution.compute(mapQ, (integer, aLong) -> aLong == null ? 1 : aLong + 1);
    }

    private synchronized void increaseMultiplyMapped(int mapQ) {
        increaseTotal(mapQ);
        multiplyMapped.incrementAndGet();
        multiplyMapQ.addAndGet(mapQ);
        multiplyMapQDistribution.compute(mapQ, (integer, aLong) -> aLong == null ? 1 : aLong + 1);
    }


}
