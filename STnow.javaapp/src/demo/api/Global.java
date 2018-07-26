package demo.api;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chaofei on 18-1-22.
 */

public class Global {
    public static final String WORK_DIR = "A";
    public static String workPath = "";
    private static Jdbc jdbc = null;

    public static final String dailyRootDir = "AA";
    public static final String codelistDir = "BB";
    public static int doneSn2d=0;
    public static int doneSnBK=0;

    public static Jdbc getJdbc() {
        if (jdbc == null) {
            jdbc = new Jdbc(false);
            if (!jdbc.init(workPath)) {
                return null;
            }
        }
        return jdbc;
    }

    public static String getDailyParentDir() {
        String dir = workPath + "/" + dailyRootDir;
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
        if (!f.isDirectory()) {
            return "";
        }
        return dir;
    }

    public static String getDailyDir() {
        String dir = getDailyParentDir();
        if (dir.length() > 0) {
            dir = getDailyParentDir() + "/" + TimeX.get_yyyymmdd();
        }
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
        if (!f.isDirectory()) {
            return "";
        }
        return dir;
    }


}
