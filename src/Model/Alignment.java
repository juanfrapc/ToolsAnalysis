package Model;

import java.util.Arrays;
import java.util.Iterator;

public class Alignment implements Iterable<Object>{


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
    public static final int QUAL= 10;

    private final int nParam = 11;
    private String[] params;

    public Alignment(String[] values) {
        params = values;
    }

    public String getParam(int param) {
        return params[param];
    }

    public int getSize() {
        return nParam;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i<nParam;
            }

            @Override
            public Object next() {
                return params[i++];
            }
        };
    }

    @Override
    public String toString() {
        return "Alignment: Alignment" + Arrays.toString(params);
    }
}
