package com.rahbarbaazar.checkpanel.utilities;

public class ConvertEnDigitToFa {


    public static String convert(String enDigit){
        enDigit = enDigit
//                .replace("0", "٠")
//                .replace("1", "١")
//                .replace("2", "٢")
//                .replace("3", "٣")


                .replace("0", "۰")
                .replace("1", "۱")
                .replace("2", "۲")
                .replace("3", "۳")

                .replace("4", "۴")
                .replace("5", "۵")
                .replace("6", "۶")
                .replace("7", "۷")
                .replace("8", "۸")
                .replace("9", "۹");


//        '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹', '۰',
        return enDigit;
    }

}
