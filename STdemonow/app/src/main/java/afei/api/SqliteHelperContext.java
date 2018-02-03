package afei.api;

/**
 * Created by chaofei on 18-1-22.
 */

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * 用于支持对存储在SD卡上的数据库的访问
**/
public class SqliteHelperContext extends ContextWrapper {
    private static final String TAG = "SqliteHelperContext";
    public SqliteHelperContext(Context base) {
        super(base);
    }

    @Override
    public File getDatabasePath(String name) {
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if (!sdExist) {
            Log.e(TAG, "Error: not found SDCARD!");
            return null;
        } else {
            String dbDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            dbDir += "/DownloadData";//数据库所在目录
            String dbPath = dbDir + "/" + name;
            File dirFile = new File(dbDir);
            if (!dirFile.exists())
                dirFile.mkdirs();

            boolean isFileCreateSuccess = false;
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                try {
                    isFileCreateSuccess = dbFile.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else
                isFileCreateSuccess = true;

            if (isFileCreateSuccess)
                return dbFile;
            else
                return null;
        }
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
     * android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}