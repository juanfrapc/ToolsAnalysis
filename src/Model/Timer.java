package Model;

import java.util.Date;

public class Timer {

    private Date start;
    private Date stop;

    public void start(){
        this.start = new Date();
    }

    public void stop(){
        this.stop=new Date();
    }

    public void reset(){
        this.start = new Date();
        this.stop = null;
    }

    static long[] getDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return new long[]{elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
    }

    public String report(){
        long[] difference = getDifference(start, stop);
        String report = "Time elapsed: ";
        report += difference[0] + " days, ";
        report += difference[1] + " hours, ";
        report += difference[2] + " minutes, ";
        report += difference[3] + " seconds";
        return report;
    }

}