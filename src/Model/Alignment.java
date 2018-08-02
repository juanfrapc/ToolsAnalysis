package Model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class Alignment implements Iterable<String> {


    public static final int QNAME = 0;
    public static final int FLAG = 1;
    public static final int RNAME = 2;
    public static final int POS = 3;
    public static final int MAPQ = 4;
    public static final int CIGAR = 5;
    public static final int RNEXT = 6;
    public static final int PNEXT = 7;
    public static final int TLEN = 8;
    public static final int SEQ = 9;
    public static final int QUAL = 10;

    private final int nParam = 11;
    private final String[] params;

    Alignment(String[] values) {
        params = values;
    }

    public String getParam(int param) {
        return params[param];
    }

    public int getSize() {
        return nParam;
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
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

    boolean checkOptionalParm(String flag) {
        for (int i = nParam; i < params.length; i++) {
            if (params[i].startsWith(flag)) return true;
        }
        return false;
    }

    public String getOptionalParam(String flag) {
        for (int i = nParam; i < params.length; i++) {
            if (params[i].startsWith(flag)) return params[i];
        }
        return null;
    }

    @Override
    public String toString() {
        return "Alignment: Alignment" + Arrays.toString(params);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alignment)) return false;

        Alignment strings = (Alignment) o;

        return Arrays.equals(params, strings.params);
    }

}
