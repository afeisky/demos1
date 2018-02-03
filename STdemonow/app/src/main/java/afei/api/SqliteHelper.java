package afei.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by chaofei on 18-1-22.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SqliteHelper";
    private static final String DB_NAME = "a2";
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

    private static final String SQL_CRESTE_BK = "CREATE TABLE  IF NOT EXISTS " + TBK.TABLE_NAME +
            " (" +
            TBK.COLUMN_TYPE + " varchar(10), " +
            TBK.COLUMN_CODE + " varchar(10), " +
            TBK.COLUMN_DATE + " varchar(20), " +
            TBK.COLUMN_NAME + " varchar(30), " +
            TBK.COLUMN_NUMBER + " decimal(3), " +
            TBK.COLUMN_COUNT + " decimal(3), " +
            TBK.COLUMN_VOLUME + " decimal(3), " +
            TBK.COLUMN_AMOUNT + " decimal(3), " +
            TBK.COLUMN_TRADE + " decimal(3), " +
            TBK.COLUMN_CHANGEPRICE + " decimal(3), " +
            TBK.COLUMN_CHANGEPERCENT + " decimal(3), " +
            TBK.COLUMN_SYMBOL + " varchar(20), " +
            TBK.COLUMN_SNAME + " varchar(20), " +
            TBK.COLUMN_STRADE + " varchar(20), " +
            TBK.COLUMN_SCHANGEPRICE + " decimal(3), " +
            TBK.COLUMN_SCHANGEPERCENT + " decimal(3), " +
            TBK.COLUMN_URL + " varchar(50), " +
            TBK.COLUMN_CREATE + " varchar(20) "+
            " )";
            //TBK.COLUMN_CREATE + " varchar(20), "+
            //" Primary Key("+TBK.COLUMN_DATE+","+TBK.COLUMN_CODE+")  )";



    private static final String SQL_CRESTE_ST = "CREATE TABLE IF NOT EXISTS " + TST.TABLE_NAME +
            " (" +
            TST.COLUMN_CODE + " varchar(10), " +
            TST.COLUMN_DATE + " varchar(20), " +
            TST.COLUMN_NAME + " varchar(30), " +
            TST.COLUMN_BEGIN + " decimal(3), " +
            TST.COLUMN_NOW + " decimal(3), " +
            TST.COLUMN_PRICE + " decimal(3), " +
            TST.COLUMN_HIGH + " decimal(3), " +
            TST.COLUMN_LOW + " decimal(3), " +
            TST.COLUMN_GAP + " decimal(3), " +
            TST.COLUMN_VOLUME + " decimal(3), " +
            TST.COLUMN_MONEY + " decimal(3), " +
            TST.COLUMN_URL + " varchar(50), " +
            TST.COLUMN_CREATE + " varchar(20), "+
            " Primary Key("+TST.COLUMN_DATE+","+TST.COLUMN_CODE+")  )";
    private int createTable(SQLiteDatabase db){
        LogX.e(TAG,"==>"+SQL_CRESTE_BK);
        LogX.e(TAG,"==>"+SQL_CRESTE_ST);
        if (db==null){
            LogX.e(TAG,"==>db is null");
        }
        db.execSQL(SQL_CRESTE_BK);
        db.execSQL(SQL_CRESTE_ST);
        return 0;//0:success.
    }
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG,"onCreate 111");
        //String sql_message = "create table aa_message (uname varchar(50),sdate varchar(50))";
        createTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG,"onUpgrade 1");
    }

    //----


}

