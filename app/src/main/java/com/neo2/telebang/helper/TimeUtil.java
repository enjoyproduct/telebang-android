package com.neo2.telebang.helper;

import com.neo2.telebang.app.AppConstant;

import org.joda.time.DateTime;


/**
 * Created by Home on 11-Mar-16.
 */
public class TimeUtil {
    public static String getDateFormat(long time) {
        DateTime newsTime = new DateTime(time * 1000L);
        String dateText = newsTime.toString(AppConstant.DATE_FORMAT);
        return dateText;
    }

    public static String getDateTimeFormat(long time) {
        DateTime newsTime = new DateTime(time * 1000L);
        String dateText = newsTime.toString(AppConstant.DATE_TIME_FORMAT);
        return dateText;
    }
    public static int getCurrentTimeStamp() {  ////good, get timestamp by second

        double timestamp = System.currentTimeMillis() / 1000f;

        return (int) timestamp;
    }
}
