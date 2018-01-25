package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

class TimerTest {

    private Date start;
    private Date stop;
    private Timer timer;

    @BeforeEach
    void init(){
        long increment = 4 * 1000;
        increment += 3*1000*60;
        increment += 2*1000*60*60;
        increment += 1000*60*60*24;
        start = new Date();
        stop = new Date(start.getTime() + increment);
        timer = new Timer();
    }

    @Test
    void getDifferenceTest() {
        long[] difference = Timer.getDifference(start, stop);
        assertArrayEquals("Error in time difference", new long[]{1,2,3,4}, difference);
    }

    @Test
    void reportTest() throws InterruptedException {
        timer.start();
        sleep(1500); // 1 second truncated
        timer.stop();
        assertEquals("Report not matching",
                "Time elapsed: 0 days, 0 hours, 0 minutes, 1 seconds", timer.report());

        timer.reset();
        sleep(1500);
        timer.stop();
        assertEquals("Report not matching",
                "Time elapsed: 0 days, 0 hours, 0 minutes, 1 seconds", timer.report());
    }



}