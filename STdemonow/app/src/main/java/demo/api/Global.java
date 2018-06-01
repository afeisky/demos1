package demo.api;

import java.io.File;

/**
 * Created by chaofei on 18-1-22.
 */

public class Global {
    public static final String TAG ="Global";
    public static final String WORK_DIR ="/A";
    public static String workPath ="";
    public static final String ENCODE ="utf-8";
    public static String strNofity="";

    public static boolean init(){
        LogX.init();
        getDirPRE();
        return true;
    }
    public static String getWorkPath(){
        return workPath;
    }

    private static DbContentProvider contentProvider=null;

    public static DbContentProvider getContentProvider(){
        if (contentProvider==null) {
            contentProvider = new DbContentProvider();
            contentProvider.onCreate();
        }
        return contentProvider;
    }
    public static String TEAM_PRE =""; //AB
    public static void SetDirPRE(String team){
        File f = new File(Global.getWorkPath() + "/" + "set.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileX.writeLines(f.getAbsolutePath(),team,ENCODE);
        }catch (Exception e){
            LogX.e(TAG,"create file fail, "+f.getAbsolutePath());
            TEAM_PRE="AA";
        }
        TEAM_PRE=team;
    }
    public static String getDirPRE(){
        if (TEAM_PRE.length()>0){
            return TEAM_PRE;
        }
        File f = new File(Global.getWorkPath() + "/" + "set.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
                FileX.writeLines(f.getAbsolutePath(),"AA",ENCODE);
            }
            TEAM_PRE=FileX.readLines(f.getAbsolutePath(),ENCODE);
            LogX.w(TAG,"("+TEAM_PRE+")");
            TEAM_PRE=TEAM_PRE.replace("\n","");
        }catch (Exception e){
            LogX.e(TAG,"create file fail, "+f.getAbsolutePath());
            TEAM_PRE="AA";
        }finally {
            return TEAM_PRE;
        }
    }
    public static String getNowBKDir(){
        return TEAM_PRE+"nowBK";
    }
    public static String getdailyBKDir(){
        return TEAM_PRE+"dailyBK";
    }
    public static String getDailySSEDir(){
        return TEAM_PRE+"dailySSE";
    }

}
