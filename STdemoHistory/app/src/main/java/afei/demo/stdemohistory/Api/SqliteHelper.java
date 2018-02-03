package afei.demo.stdemohistory.Api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by chaofei on 18-1-22.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SqliteHelper";
    private static final String DB_NAME = "a1";
    private static final String DB_DIR = "/DownloadData";
    private static final int DB_VERSION = 1;
    private static final boolean DEBUG = true;

    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //super(context, getDbName(context), null, DB_VERSION);
    }
    /*
    public SqliteHelper(Context context,String db_dir,String db_name) {
        super(context, getDbName(context,db_dir,db_name), null, DB_VERSION);
    }
    private static String getDbName(Context context){
        boolean isSdcardEnable =false;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            isSdcardEnable = true;
        }
        String dbPath = null;
        Log.e(TAG,"getDbName isSdcardEnable="+isSdcardEnable);
        if(isSdcardEnable){
            dbPath =Environment.getExternalStorageDirectory().getPath()+DB_DIR+"/";
        }else{
            dbPath =context.getFilesDir().getPath() + DB_DIR+"/";
        }
        Log.e(TAG,"getDbName "+dbPath);
        File dbp = new File(dbPath);
        if(!dbp.exists()){
            dbp.mkdirs();
        }
        if (dbPath=="") {
            Log.e(TAG,"getDbName Fail!");
            return "";
        }else{
            String databasename = dbPath + DB_NAME;
            Log.e(TAG,"getDbName Success!"+databasename);
            return databasename;
        }
    }

    private static String getDbName(Context context,String db_dir,String db_name){
        boolean isSdcardEnable =false;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            isSdcardEnable = true;
        }
        String dbPath = null;
        Log.e(TAG,"getDbName isSdcardEnable="+isSdcardEnable);
        if(isSdcardEnable){
            dbPath =Environment.getExternalStorageDirectory().getPath()+db_dir+"/";
        }else{
            dbPath =context.getFilesDir().getPath() + db_dir+"/";
        }
        Log.e(TAG,"getDbName "+dbPath);
        File dbp = new File(dbPath);
        if(!dbp.exists()){
            dbp.mkdirs();
        }
        if (dbPath=="") {
            Log.e(TAG,"getDbName Fail!");
            return "";
        }else{
            String databasename = dbPath + db_name;
            Log.e(TAG,"getDbName Success!"+databasename);
            return databasename;
        }
    }
    */
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG,"onCreate 111");
        //String sql_message = "create table aa_message (uname varchar(50),sdate varchar(50))";
        String sql_message = "insert into aa_message values(\"bb\",\"2018-01-23\")";
        db.execSQL(sql_message);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG,"onUpgrade 1");
    }

    //----


}

