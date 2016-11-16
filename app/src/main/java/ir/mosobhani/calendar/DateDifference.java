package ir.mosobhani.calendar;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDifference {

    public String time(String date) {
        Date d1 = null;
        Date d2 = null;
        long diff;
        long diffSeconds;
        long diffMinutes;
        long diffHours;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        d1 = c.getTime();

// Custom date format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            //d1 = format.parse("2016-04-09 03:47:12");
            d2 = format.parse(date);
            //d2 = format.parse("2016-04-02 03:47:12");
        } catch (ParseException e) {

                Log.i("Date Diff", "Date Format Not Set: " + date);
            return "";
        }
// Get msec from each, and subtract.
        diff = d1.getTime() - d2.getTime();
        diffSeconds = diff / 1000 % 60;
        diffMinutes = diff / (60 * 1000) % 60;
        diffHours = diff / (60 * 60 * 1000);
        long timeArray[] = new long[3];
        timeArray[0] = diffHours;
        timeArray[1] = diffMinutes;
        timeArray[2] = diffSeconds;


        long days;
        long weeks;

        if (timeArray[0] != 0) {
            if (timeArray[0] >= 24) {
                days = timeArray[0] / 24;
                if (days >= 7) {
                    weeks = days / 7;
                    date = String.valueOf(weeks) + "هفته پیش ";
                } else {
                    date = String.valueOf(days) + " روز پیش";
                }
            } else {
                date = String.valueOf(timeArray[0]) + "ساعت پیش ";
            }

        } else {
            if (timeArray[1] != 0) {
                date = String.valueOf(timeArray[1]) + " " +  "دقیقه پیش ";
            } else {
                date = String.valueOf(timeArray[2]) + " " + "ثانیه پیش ";
            }
        }

        return date;
    }

}
