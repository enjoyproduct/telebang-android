package com.inspius.yo365.helper;

import com.inspius.yo365.app.AppConstant;

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
}
