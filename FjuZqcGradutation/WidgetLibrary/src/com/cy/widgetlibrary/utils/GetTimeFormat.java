package com.cy.widgetlibrary.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ejianshen on 15/10/31.
 */
public class GetTimeFormat {
    private static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format1=new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat format2=new SimpleDateFormat("HH");
    private static Date timeCurrent,timeBegin;
    private static long timeBetween,day,hour,min,s;

    /**
     *
     * @param currentTime 当前时间
     * @param beginTime 创建时间
     * @return
     */
    public static String getTimeFormat(String currentTime,String beginTime){
        try {
             timeCurrent=format.parse(currentTime);
             timeBegin=format.parse(beginTime);
             timeBetween=timeCurrent.getTime()-timeBegin.getTime();
             day=timeBetween/(24*60*60*1000);
             hour = (timeBetween/(60*60*1000)-day*24);
             min=((timeBetween/(60*1000))-day*24*60-hour*60);
             s=(timeBetween/1000-day*24*60*60-hour*60*60-min*60);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(day>=31){
            return beginTime;
        }else if(day>=1&&day<31){
            return day+"天前";
        }else{
            int timeHourN=Integer.parseInt(format2.format(timeCurrent));
            int timeHourB=Integer.parseInt(format2.format(timeBegin));
            if (timeHourN-timeHourB<0){
                return "昨天"+format1.format(timeBegin);
            }else if(timeHourN-timeHourB==0){
                SimpleDateFormat formatMin=new SimpleDateFormat("mm");
                int timeMinN=Integer.parseInt(formatMin.format(timeCurrent));
                int timeMinB=Integer.parseInt(formatMin.format(timeBegin));
                if(timeMinB-timeMinN>0){
                    return "昨天"+format1.format(timeBegin);
                }else{
                    return format1.format(timeBegin);
                }
            }else{
                return format1.format(timeBegin);
            }
        }
    }
}
