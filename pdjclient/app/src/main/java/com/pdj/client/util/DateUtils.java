package com.pdj.client.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by fengqin on 14/12/4.
 */
public class DateUtils {

    public static Date getToday(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
