package afei.api;

/**
 * Created by chaofei on 18-1-22.
 */

public class Global {
    public static final String WORK_DIR ="A";
    public static String workPath ="";
    public static Jdbc jdbc;

    public static Jdbc getJdbc(){
        if (jdbc==null) {
            jdbc = new Jdbc();
        }
        return jdbc;
    }
}
