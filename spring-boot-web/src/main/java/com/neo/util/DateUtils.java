package com.neo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String YYYYMMDD =  "yyyy-MM-dd";

    public static String getDateYyyyMmDd(){
        return new SimpleDateFormat(YYYYMMDD).format(new Date());
    }
}
