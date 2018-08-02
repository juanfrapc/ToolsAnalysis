package Model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class Variant implements Iterable {

    public static final int CHROM = 0;
    public static final int POS = 1;
    public static final int ID = 2;
    public static final int REF = 3;
    public static final int ALT = 4;
    public static final int QUAL = 5;
    public static final int FILTER = 6;
    public static final int GENOTYPE = 7;

    private final int nParam = 11;
    private final String[] params;


    public Variant(String[] params) {
        this.params = params;
    }

    public int getSize() {
        return nParam;
    }

    public String getParam(int param) {
        return params[param];
    }

    public void setGENOTYPE(String genotype) {
        params[GENOTYPE] = genotype;
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return new Iterator() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < nParam;
            }

            @Override
            public String next() {
                return params[i++];
            }
        };
    }

    @Override
    public String toString() {
        return "Variant{" +
                "nParam=" + nParam +
                ", params=" + Arrays.toString(params) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variant)) return false;
        Variant variant = (Variant) o;
        return Arrays.equals(params, variant.params);
    }

}
