package demo.api;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeX {
    private static String TAG="TimeX";
    public static String getToday(){
        try{
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        }catch (Exception e){
            LogX.e(TAG,e.getStackTrace()+"");
        }
        return "";
    }
    public static String fmtToday(){
        try{
            return (new SimpleDateFormat("yyyy-MM-dd_HHmmss")).format(new Date());
        }catch (Exception e){
            LogX.e(TAG,e.getStackTrace()+"");
        }
        return "";
    }
}
