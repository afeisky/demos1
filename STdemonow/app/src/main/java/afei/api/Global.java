package afei.api;

/**
 * Created by chaofei on 18-1-22.
 */

public class Global {
    public static final String WORK_DIR ="/DownloadData";
    public static String workPath ="";
    private static DbContentProvider contentProvider=null;

    public static DbContentProvider getContentProvider(){
        if (contentProvider==null) {
            contentProvider = new DbContentProvider();
            contentProvider.onCreate();
        }
        return contentProvider;
    }


}
