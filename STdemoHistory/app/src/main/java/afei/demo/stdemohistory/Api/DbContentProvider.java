package afei.demo.stdemohistory.Api;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by chaofei on 18-1-22.
 */

public class DbContentProvider extends ContentProvider {

    private static final String TAG="DbContentProvider";
    private static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    private SqliteHelper mysqlite;
    private SQLiteDatabase db;
    private static final int MATCH_CODE_1 = 100;
    private static final int MATCH_CODE_2 = 101;
    static {
        //uriMatcher.addURI(UserInfo.AUTOR, "userinfo", MATCH_CODE_1);
        //uriMatcher.addURI(UserInfo.AUTOR, "userinfoall", MATCH_CODE_2);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int number = 0;
        if (uriMatcher.match(uri) == 1) {
            number = db.delete("user_info",
                    selection, selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return number;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String maxId = "select max(id) id from user_info";
        Cursor cursor = db.rawQuery(maxId, null);
        cursor.moveToFirst();
        int userid = cursor.getInt(0) + 1;
        if (uriMatcher.match(uri) == MATCH_CODE_1) {
           // db.execSQL("insert into user_info values(?,?,?)",
           //         new String[] { String.valueOf(userid), "", 15 });
        }
        return uri;
    }

    @Override
    public boolean onCreate() {
        SqliteHelperContext dbContext = new SqliteHelperContext(getContext());
        mysqlite = new SqliteHelper(dbContext);
        db = mysqlite.getWritableDatabase();
        createTable();
        LogX.e(TAG,"onCreate()");
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String str = "select name,age from user_info";
        if (uriMatcher.match(uri) == 1) {
            str += " where " + selection;
        }
        Cursor cursor = db.rawQuery(str, selectionArgs);
        return cursor;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int number = 0;
        if (uriMatcher.match(uri) == 1) {
            number = db.update("user_info", values, selection,
                    selectionArgs);
        }
        return number;
    }

    private static final String SQL_CRESTE_BK = "CREATE TABLE  IF NOT EXISTS " + TBK.TABLE_NAME +
            " (" +
            TBK.COLUMN_CODE + " varchar(10), " +
            TBK.COLUMN_DATE + " varchar(20), " +
            TBK.COLUMN_NAME + " varchar(30), " +
            TBK.COLUMN_NUMBER + " decimal(3), " +
            TBK.COLUMN_COUNT + " decimal(3), " +
            TBK.COLUMN_VOLUME + " decimal(3), " +
            TBK.COLUMN_AMOUNT + " decimal(3), " +
            TBK.COLUMN_TRADE + " decimal(3), " +
            TBK.COLUMN_CHANGEPRICE + " decimal(3), " +
            TBK.COLUMN_SYMBOL + " varchar(20), " +
            TBK.COLUMN_SNAME + " varchar(20), " +
            TBK.COLUMN_SCHANGEPRICE + " decimal(3), " +
            TBK.COLUMN_SCHANGEPERCENT + " decimal(3), " +
            TBK.COLUMN_URL + " varchar(50), " +
            TBK.COLUMN_CREATE + " varchar(20) "+
            " )";



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
            TST.COLUMN_CREATE + " varchar(20) "+
            " )";
    private int createTable(){
        LogX.e(TAG,"==>"+SQL_CRESTE_BK);
        LogX.e(TAG,"==>"+SQL_CRESTE_ST);
        if (db==null){
            LogX.e(TAG,"==>db is null");
        }
        db.execSQL(SQL_CRESTE_BK);
        db.execSQL(SQL_CRESTE_ST);
        return 0;//0:success.
    }

    public int insertBK(ContentValues values){
        long rowId = db.insert(TBK.TABLE_NAME, null, values);
        LogX.e(TAG,"="+rowId);
        if(rowId > 0) {

        }
        return 0;//0:success.
    }
    public int insertST(ContentValues values){
        long rowId = db.insert(TST.TABLE_NAME, null, values);
        LogX.e(TAG,"="+rowId);
        if(rowId > 0) {

        }
        return 0;//0:success.
    }
}
