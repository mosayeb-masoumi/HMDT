package com.rahbarbazaar.checkpanel.utilities;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class Time {

    public static String getNowPersianDate() {
        PersianDate pDate = new PersianDate();
        new PersianDateFormat("yyyy/mm/dd").format(pDate);
//        return pDate.getShYear() + "/" + pDate.getShMonth() + "/" + pDate.getShDay();
        return (String.valueOf(pDate.getShYear()).length() < 2 ? "0" + pDate.getShYear() : String.valueOf(pDate.getShYear())) + "/" +
                (String.valueOf(pDate.getShMonth()).length() < 2 ? "0" + pDate.getShMonth() : String.valueOf(pDate.getShMonth())) + "/" +
                (String.valueOf(pDate.getShDay()).length() < 2 ? "0" + pDate.getShDay() : String.valueOf(pDate.getShDay()));
    }

    public static String getNowTime() {
        PersianDate pDate = new PersianDate();
        new PersianDateFormat("yyyy/mm/dd").format(pDate);
        return pDate.getHour() + ":" + pDate.getMinute() + ":" + pDate.getSecond();
    }



    public static String minutesToHours(int minutes) {
        int hour = 0;
        while (minutes >= 60) {
            hour++;
            minutes -= 60;
        }
//        return (hour > 0 ? hour + " ساعت " : "") + (minutes > 0 ? minutes + " دقیقه" : "");
        return (hour>0 ? hour+" " +  "ساعت":"")+" " +(minutes> 0 ? " و " +minutes+" "+ "دقیقه":"");

    }

    public static String minutesToHours(String stringMinutes) {
        if (stringMinutes == null || stringMinutes.trim().equals(""))
            return "0";
        else {
            return minutesToHours(Integer.parseInt(stringMinutes.trim()));
        }
    }
}
