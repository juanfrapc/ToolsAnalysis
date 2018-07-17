package Model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeFormat {

    @Test
    void americanTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.now();
        assert(!dtf.format(localDate).contains("/"));
        assert(dtf.format(localDate).length() == 8);
        assert(Integer.parseInt(dtf.format(localDate).substring(0,4)) >= 2018 );
        assert(Integer.parseInt(dtf.format(localDate).substring(4,6)) >= 1 && Integer.parseInt(dtf.format(localDate).substring(4,6)) <= 12 );
        assert(Integer.parseInt(dtf.format(localDate).substring(6,8)) >= 1 && Integer.parseInt(dtf.format(localDate).substring(6,8)) <= 31 );
    }
}
