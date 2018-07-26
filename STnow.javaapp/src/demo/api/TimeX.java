package demo.api;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeX {
    private static String TAG="TimeX";

    public static SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat yymmdd_hhmmss = new SimpleDateFormat("yyMMdd_HHmmss");
    public static SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    public static String getToday(){
        try{
        return (new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date());
        }catch (Exception e){
            LogX.e(TAG,e.getStackTrace()+"");
        }
        return "";
    }

    public static String get_yyyy_mm_dd_hhmmss() {
        try {
            return yyyy_mm_dd_hhmmss.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String get_yymmdd_hhmmss() {
        try {
            return yymmdd_hhmmss.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String get_yyyymmdd() {
        try {
            return yyyymmdd.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
