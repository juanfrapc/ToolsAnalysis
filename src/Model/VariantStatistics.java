package Model;

import java.util.concurrent.atomic.AtomicLong;

import static Model.Variant.CHROM;
import static Model.Variant.FILTER;
import static Model.Variant.GENOTYPE;

public class VariantStatistics implements Statistics{

    private final AtomicLong total;
    private final AtomicLong falsePositive;


    public VariantStatistics() {
        this.falsePositive = new AtomicLong();
        this.total = new AtomicLong();
    }

    public long getTotal() {
        return total.longValue();
    }

    public long getFalsePositive() {
        return falsePositive.longValue();
    }

    public void update(Variant variant, boolean woman) {
        if (!variant.getParam(FILTER).equals("PASS") && !variant.getParam(FILTER).equals("\\.")) return;
        total.incrementAndGet();
        if (woman) {
            if (variant.getParam(CHROM).equals("chrY")) falsePositive.incrementAndGet();
        } else {
            if (variant.getParam(CHROM).equals("chrY") && variant.getParam(GENOTYPE).equals("HET")) falsePositive.incrementAndGet();
            if (variant.getParam(CHROM).equals("chrX") && variant.getParam(GENOTYPE).equals("HET")) falsePositive.incrementAndGet();
        }


    }

    public void clear() {
        total.set(0);
        falsePositive.set(0);
    }
}
