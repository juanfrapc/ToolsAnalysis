package Model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static Model.Alignment.*;
import static java.lang.Integer.*;
import static java.lang.Integer.parseInt;

public class AlignmentStatistics {

    private AtomicLong total;
    private AtomicLong unmapped;
    private AtomicLong multiplyMapped;

    private AtomicLong totalMapQ;
    private AtomicLong unmappedMapQ;
    private AtomicLong multiplyMapQ;

    public AlignmentStatistics() {
        total = new AtomicLong();
        unmapped = new AtomicLong();
        multiplyMapped = new AtomicLong();

        totalMapQ = new AtomicLong();
        unmappedMapQ= new AtomicLong();
        multiplyMapQ = new AtomicLong();
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

    public long getTotalMapQ() {
        return totalMapQ.longValue();
    }

    public long getUnmappedMapQ() {
        return unmappedMapQ.longValue();
    }

    public long getMultiplyMapQ() {
        return multiplyMapQ.longValue();
    }

    public long getUniquelyMapped(){
        return total.get() - unmapped.get() - multiplyMapped.get();
    }

    public void update(Alignment alignment) {
        String flag = toBinaryString(parseInt(alignment.getParam(FLAG)));
        if (flag.charAt(flag.length() - 3) == '1') increaseUnmapped(parseInt(alignment.getParam(MAPQ)));
        else increaseMultiplyMapped(parseInt(alignment.getParam(MAPQ)));
    }

    private void increaseUnmapped(int mapQ) {
        total.incrementAndGet();
        totalMapQ.addAndGet(mapQ);
        unmapped.incrementAndGet();
        unmappedMapQ.addAndGet(mapQ);
    }

    private void increaseMultiplyMapped(int mapQ) {
        total.incrementAndGet();
        totalMapQ.addAndGet(mapQ);
        multiplyMapped.incrementAndGet();
        multiplyMapQ.addAndGet(mapQ);
    }


}
