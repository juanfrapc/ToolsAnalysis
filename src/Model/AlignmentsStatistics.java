package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static Model.Alignment.FLAG;
import static Model.Alignment.MAPQ;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.toBinaryString;

public class AlignmentsStatistics {

    private AtomicLong total;
    private AtomicLong unmapped;
    private AtomicLong multiplyMapped;

    private AtomicLong totalMapQ;
    private AtomicLong unmappedMapQ;
    private AtomicLong multiplyMapQ;

    private Map<Integer, Long> totalMapQDistribution;

    public AlignmentsStatistics() {
        total = new AtomicLong();
        unmapped = new AtomicLong();
        multiplyMapped = new AtomicLong();

        totalMapQ = new AtomicLong();
        unmappedMapQ = new AtomicLong();
        multiplyMapQ = new AtomicLong();
        totalMapQDistribution = new HashMap<>();
    }

    public Map<Integer, Long> getTotalMapQDistribution() { return totalMapQDistribution; }

    public long getTotal() {
        return total.longValue();
    }

    public long getUnmapped() {
        return unmapped.longValue();
    }

    public long getMultiplyMapped() {
        return multiplyMapped.longValue();
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

    public long getUniquelyMapped() {
        return total.get() - unmapped.get() - multiplyMapped.get();
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

    private void increaseUnmapped(int mapQ) {
        increaseTotal(mapQ);
        unmapped.incrementAndGet();
        unmappedMapQ.addAndGet(mapQ);
    }

    private void increaseMultiplyMapped(int mapQ) {
        increaseTotal(mapQ);
        multiplyMapped.incrementAndGet();
        multiplyMapQ.addAndGet(mapQ);
    }


}
