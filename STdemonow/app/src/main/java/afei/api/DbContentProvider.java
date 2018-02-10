package afei.api;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import afei.stdemonow.R;

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
        createDbIfNotExist();
        return true;
    }
    private boolean createDbIfNotExist(){
        if (db==null) {
            SqliteHelperContext dbContext = new SqliteHelperContext(getContext());
            mysqlite = new SqliteHelper(dbContext);
            db = mysqlite.getWritableDatabase();
            LogX.e(TAG, "createDbIfNotExist()");
        }
        return (!(db==null));
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



    private String BK_deleteMore="delete from snbk where date in(select date from snbk order by date limit 2) and (select count(*) from snbk)>50000";
    public int insertBK(ContentValues values){
        //LogX.e(TAG,"==>"+values);
        long rowId = db.insert(TBK.TABLE_NAME, null, values);
        //LogX.e(TAG,"="+rowId);
        if(rowId > 0) {
            db.execSQL(BK_deleteMore);
        }
        return 0;//0:success.
    }
    public int insertST(ContentValues values){
        long rowId = db.insert(TST.TABLE_NAME, null, values);
        //LogX.e(TAG,"="+rowId);
        if(rowId > 0) {

        }
        return 0;//0:success.
    }
    public int insertSSE(ContentValues values){
        long rowId = db.insert(TSSE.TABLE_NAME, null, values);
        //LogX.e(TAG,"="+rowId);
        if(rowId > 0) {

        }
        return 0;//0:success.
    }
    //select code,name,number,count,volume,amount,trade,symbol,sname,strade,scgpercent from snbk where date="2018-01-24 17:07:17" and substr(code,0,4)=="gn_" order by chgpercent DESC
    //select code,name,number,count,volume,amount,trade,symbol,sname,strade,scgpercent from snbk where date="2018-01-24 17:07:17" and substr(code,0,8)=="hangye_" order by chgpercent DESC
    //select code,name,number,count,volume,amount,trade,symbol,sname,strade,scgpercent from snbk where date="2018-01-24 17:07:17" and substr(code,0,5)=="new_" order by chgpercent DESC
    //private final String queryBK1Str="select code,name,number,count,volume,amount,trade,symbol,sname,strade,scgpercent from snbk where date=\"?\" and substr(code,0,?)==\"?\" order by chgpercent DESC";
    public Cursor queryBK1(String codeKey,String date,int type){
        Cursor c=null;
        int len=codeKey.length()+1;
        String table="snbk";
        //String[] colums=new String[]{"code","name","number","count","volume","amount","trade","symbol","sname","strade","scgpercent"};
        String[] colums=new String[]{TBK.COLUMN_CODE,TBK.COLUMN_NAME,
                TBK.COLUMN_NUMBER,TBK.COLUMN_COUNT,
                TBK.COLUMN_VOLUME,TBK.COLUMN_AMOUNT,
                TBK.COLUMN_TRADE,TBK.COLUMN_CHANGEPERCENT,
                TBK.COLUMN_SYMBOL,TBK.COLUMN_SNAME,
                TBK.COLUMN_STRADE,TBK.COLUMN_SCHANGEPERCENT};
        String selection=TBK.COLUMN_DATE+"=? and substr("+TBK.COLUMN_CODE+",0,?)==?";//String selection="date=\"?\" and substr(code,0,?)==\"?\"";
        String[] selectionArgs=new String[]{date,Integer.toString(len),codeKey};
        String orderBy=TBK.COLUMN_CHANGEPERCENT+" DESC";//String orderBy="chgpercent DESC";
        String limit="300";
        LogX.w(TAG, "111="+selection);
        LogX.w(TAG, "111="+selectionArgs.length);
        try {
            c = db.query(table, colums,
                    selection, selectionArgs,
                    null, null, orderBy,
                    limit);

            //if (c.moveToFirst()) {
            //    for (int i = 0; i < c.getCount(); i++) {
            //        LogX.e(TAG,c.getString());
            //        c.moveToNext();
            //    }
            //
            OutputStreamWriter out = null;
            String fname="/storage/emulated/0/DownloadData/snbk_now/out.txt";
            out = new OutputStreamWriter(new FileOutputStream(fname), "utf-8");
            LogX.w(TAG, "222");
            int n=0;
            if (c != null) {
                LogX.w(TAG, "333");
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    int COLUMN_CODE = c.getColumnIndex(TBK.COLUMN_CODE);
                    int COLUMN_NAME = c.getColumnIndex(TBK.COLUMN_NAME);
                    int COLUMN_NUMBER = c.getColumnIndex(TBK.COLUMN_NUMBER);
                    int COLUMN_COUNT = c.getColumnIndex(TBK.COLUMN_COUNT);
                    int COLUMN_VOLUME = c.getColumnIndex(TBK.COLUMN_VOLUME);
                    int COLUMN_AMOUNT = c.getColumnIndex(TBK.COLUMN_AMOUNT);
                    int COLUMN_TRADE = c.getColumnIndex(TBK.COLUMN_TRADE);
                    int COLUMN_CHANGEPERCENT = c.getColumnIndex(TBK.COLUMN_CHANGEPERCENT);
                    int COLUMN_SYMBOL = c.getColumnIndex(TBK.COLUMN_SYMBOL);
                    int COLUMN_SNAME = c.getColumnIndex(TBK.COLUMN_SNAME);
                    int COLUMN_STRADE = c.getColumnIndex(TBK.COLUMN_STRADE);
                    int COLUMN_SCHANGEPERCENT = c.getColumnIndex(TBK.COLUMN_SCHANGEPERCENT);

                    String vCOLUMN_CODE = c.getString(COLUMN_CODE);
                    String vCOLUMN_NAME = c.getString(COLUMN_NAME);
                    int vCOLUMN_NUMBER = c.getInt(COLUMN_NUMBER);
                    int vCOLUMN_COUNT = c.getInt(COLUMN_COUNT);
                    double vCOLUMN_VOLUME = c.getDouble(COLUMN_VOLUME);
                    double vCOLUMN_AMOUNT = c.getDouble(COLUMN_AMOUNT);
                    double vCOLUMN_TRADE = c.getDouble(COLUMN_TRADE);
                    double vCOLUMN_CHANGEPERCENT = c.getDouble(COLUMN_CHANGEPERCENT);
                    String vCOLUMN_SYMBOL = c.getString(COLUMN_SYMBOL);
                    String vCOLUMN_SNAME = c.getString(COLUMN_SNAME);
                    double vCOLUMN_STRADE = c.getDouble(COLUMN_STRADE);
                    double vCOLUMN_SCHANGEPERCENT = c.getDouble(COLUMN_SCHANGEPERCENT);
                    out.write(vCOLUMN_CODE +","+ vCOLUMN_CHANGEPERCENT +","+ vCOLUMN_NUMBER +","+ vCOLUMN_COUNT +","+ vCOLUMN_SYMBOL );
                    LogX.w(TAG, vCOLUMN_CODE +","+ vCOLUMN_CHANGEPERCENT +","+ vCOLUMN_NUMBER +","+ vCOLUMN_COUNT +","+ vCOLUMN_SYMBOL );
                    if (n>5)
                        break;
                }
            }
            out.close();
        }catch (Exception e){
            LogX.e(TAG, e.getMessage());
        }

        return c;

    }
    //select date,code from snbk where date in (select date from snbk order by date DESC limit 1)   order by chgpercent DESC
    //select a.date,code from snbk as a,(select date from snbk order by date DESC limit 1) as b where a.date=b.date order by chgpercent DESC
    public boolean queryBK11(String codeKey, String date, int type, List<Map<String,Object>> datalist){
        Cursor c=null;
        int len=codeKey.length()+1;
        String table="snbk";
        //String[] colums=new String[]{"code","name","number","count","volume","amount","trade","symbol","sname","strade","scgpercent"};
        String[] colums=new String[]{TBK.COLUMN_DATE,TBK.COLUMN_CODE,TBK.COLUMN_NAME,
                TBK.COLUMN_NUMBER,TBK.COLUMN_COUNT,
                TBK.COLUMN_VOLUME,TBK.COLUMN_AMOUNT,
                TBK.COLUMN_TRADE,TBK.COLUMN_CHANGEPERCENT,
                TBK.COLUMN_SYMBOL,TBK.COLUMN_SNAME,
                TBK.COLUMN_STRADE,TBK.COLUMN_SCHANGEPERCENT};
        String selection=TBK.COLUMN_DATE+" in (select "+TBK.COLUMN_DATE+" from "+table+" order by "+TBK.COLUMN_DATE+" DESC limit 1) and substr("+TBK.COLUMN_CODE+",0,?)==?";//String selection="date=\"?\" and substr(code,0,?)==\"?\"";
        String[] selectionArgs=new String[]{Integer.toString(len),codeKey};
        String orderBy=TBK.COLUMN_CHANGEPERCENT+" DESC";//String orderBy="chgpercent DESC";
        String limit="300";
        LogX.w(TAG, "111="+selection);
        LogX.w(TAG, "111="+selectionArgs.length);
        try {
            c = db.query(table, colums,
                    selection, selectionArgs,
                    null, null, orderBy,
                    limit);

            //if (c.moveToFirst()) {
            //    for (int i = 0; i < c.getCount(); i++) {
            //        LogX.e(TAG,c.getString());
            //        c.moveToNext();
            //    }
            //
            LogX.w(TAG, "222");
            int n=0;
            if (c != null) {
                LogX.w(TAG, "333");
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    int COLUMN_DATE = c.getColumnIndex(TBK.COLUMN_DATE);
                    int COLUMN_CODE = c.getColumnIndex(TBK.COLUMN_CODE);
                    int COLUMN_NAME = c.getColumnIndex(TBK.COLUMN_NAME);
                    int COLUMN_NUMBER = c.getColumnIndex(TBK.COLUMN_NUMBER);
                    int COLUMN_COUNT = c.getColumnIndex(TBK.COLUMN_COUNT);
                    int COLUMN_VOLUME = c.getColumnIndex(TBK.COLUMN_VOLUME);
                    int COLUMN_AMOUNT = c.getColumnIndex(TBK.COLUMN_AMOUNT);
                    int COLUMN_TRADE = c.getColumnIndex(TBK.COLUMN_TRADE);
                    int COLUMN_CHANGEPERCENT = c.getColumnIndex(TBK.COLUMN_CHANGEPERCENT);
                    int COLUMN_SYMBOL = c.getColumnIndex(TBK.COLUMN_SYMBOL);
                    int COLUMN_SNAME = c.getColumnIndex(TBK.COLUMN_SNAME);
                    int COLUMN_STRADE = c.getColumnIndex(TBK.COLUMN_STRADE);
                    int COLUMN_SCHANGEPERCENT = c.getColumnIndex(TBK.COLUMN_SCHANGEPERCENT);
                    String vCOLUMN_DATE = c.getString(COLUMN_DATE);
                    String vCOLUMN_CODE = c.getString(COLUMN_CODE);
                    String vCOLUMN_NAME = c.getString(COLUMN_NAME);
                    int vCOLUMN_NUMBER = c.getInt(COLUMN_NUMBER);
                    int vCOLUMN_COUNT = c.getInt(COLUMN_COUNT);
                    double vCOLUMN_VOLUME = c.getDouble(COLUMN_VOLUME);
                    double vCOLUMN_AMOUNT = c.getDouble(COLUMN_AMOUNT);
                    double vCOLUMN_TRADE = c.getDouble(COLUMN_TRADE);
                    double vCOLUMN_CHANGEPERCENT = c.getDouble(COLUMN_CHANGEPERCENT);
                    String vCOLUMN_SYMBOL = c.getString(COLUMN_SYMBOL);
                    String vCOLUMN_SNAME = c.getString(COLUMN_SNAME);
                    double vCOLUMN_STRADE = c.getDouble(COLUMN_STRADE);
                    double vCOLUMN_SCHANGEPERCENT = c.getDouble(COLUMN_SCHANGEPERCENT);
                    Map<String,Object> map1=new HashMap<String,Object>();
                    map1.put("image", R.mipmap.ic_launcher);
                    map1.put("time",vCOLUMN_DATE);
                    map1.put("code",vCOLUMN_CODE);
                    map1.put("percent",vCOLUMN_CHANGEPERCENT);
                    datalist.add(map1);
                    LogX.w(TAG, vCOLUMN_CODE +","+ vCOLUMN_CHANGEPERCENT +","+ vCOLUMN_NUMBER +","+ vCOLUMN_COUNT +","+ vCOLUMN_SYMBOL );
                }
            }

        }catch (Exception e){
            LogX.e(TAG, e.getMessage());
            return false;
        }
        return true;

    }
    public JSONObject queryBK12(String codeKey, String date, int type){
        Cursor c=null;
        int len=codeKey.length()+1;
        String table="snbk";
        String sql1="select * "//+TBK.COLUMN_DATE+","+TBK.COLUMN_CODE+","+TBK.COLUMN_NAME
                +" from "+table+" "
                +" where substr("+TBK.COLUMN_CODE+",0,"+len+")=\""+codeKey+"\" and "+TBK.COLUMN_DATE+" in (select "+TBK.COLUMN_DATE+" from "+table
                +" order by "+TBK.COLUMN_DATE+" DESC limit 1)"
                +"order by "+TBK.COLUMN_CHANGEPERCENT+" DESC";
        String sql="select * "//+TBK.COLUMN_DATE+","+TBK.COLUMN_CODE+","+TBK.COLUMN_NAME
                +" from "+table+" "
                +" where substr("+TBK.COLUMN_CODE+",0,?)=? and "+TBK.COLUMN_DATE+" in (select "+TBK.COLUMN_DATE+" from "+table
                +" order by "+TBK.COLUMN_DATE+" DESC limit 1)"
                +"order by "+TBK.COLUMN_CHANGEPERCENT+" DESC";
        //LogX.w(TAG, "[ "+sql);
        try {
            JSONObject json=new JSONObject("{\"gn_\":{\"time\":\"\",\"data\":[]},\"new_\":{\"time\":\"\",\"data\":[]},\"hangye_\":{\"time\":\"\",\"data\":[]}}");
            codeKey="gn_";
            len=codeKey.length()+1;
            String[] selectionArgs=new String[]{len+"",codeKey};
            //c = db.rawQuery(sql1,null);
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                LogX.e(TAG, "1111--");
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    LogX.e(TAG, "2222--");
                    json.put(codeKey,json1);
                }
            }
            codeKey="new_";
            len=codeKey.length()+1;
            selectionArgs=new String[]{len+"",codeKey};
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    json.put(codeKey,json1);
                }
            }
            codeKey="hangye_";
            len=codeKey.length()+1;
            selectionArgs=new String[]{len+"",codeKey};
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    json.put(codeKey,json1);
                }
            }
            return json;
        }catch (Exception e){
            LogX.e(TAG,"Error:"+ e.getMessage());
            return null;
        }
    }
    public JSONObject queryBK11(){

        String codeKey="";
        int len;
        String table="snbk";
        String sql1="select * "//+TBK.COLUMN_DATE+","+TBK.COLUMN_CODE+","+TBK.COLUMN_NAME
                +" from "+table+" "
                +" where substr("+TBK.COLUMN_CODE+",0,?)=? and "+TBK.COLUMN_DATE+" in (select "+TBK.COLUMN_DATE+" from "+table
                +" order by "+TBK.COLUMN_DATE+" DESC limit 1)"
                +"order by "+TBK.COLUMN_CHANGEPERCENT+" DESC";
        String sql="select * "//+TBK.COLUMN_DATE+","+TBK.COLUMN_CODE+","+TBK.COLUMN_NAME
                +" from "+table+" "
                +" where substr("+TBK.COLUMN_CODE+",0,?)=? and abs("+TBK.COLUMN_CHANGEPERCENT+")>1 and "+TBK.COLUMN_DATE+" in (select "+TBK.COLUMN_DATE+" from "+table
                +" order by "+TBK.COLUMN_DATE+" DESC limit 1)"
                +"order by "+TBK.COLUMN_CHANGEPERCENT+" DESC ";

        //LogX.w(TAG, "[ "+sql);
        try {
            JSONObject json=new JSONObject("{\"gn_\":{\"time\":\"\",\"data\":[]},\"new_\":{\"time\":\"\",\"data\":[]},\"hangye_\":{\"time\":\"\",\"data\":[]}}");
            codeKey="gn_";
            len=codeKey.length()+1;
            String[] selectionArgs=new String[]{len+"",codeKey};
            Cursor c=null;
            //c = db.rawQuery(sql1,null);
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                LogX.e(TAG, "1111--");
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    LogX.e(TAG, "2222--");
                    json.put(codeKey,json1);
                }
            }
            codeKey="new_";
            len=codeKey.length()+1;
            selectionArgs=new String[]{len+"",codeKey};
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    json.put(codeKey,json1);
                }
            }
            codeKey="hangye_";
            len=codeKey.length()+1;
            selectionArgs=new String[]{len+"",codeKey};
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    json.put(codeKey,json1);
                }
            }
            return json;
        }catch (Exception e){
            LogX.e(TAG,"Error:"+ e.getMessage());
            return null;
        }
    }
    public JSONObject queryBK10(){

        String codeKey="";
        int len;
        String table="snbk";
        String sql="select * from ( select *,2 as z1,abs("+TBK.COLUMN_CHANGEPERCENT+")as z2 "//+TBK.COLUMN_DATE+","+TBK.COLUMN_CODE+","+TBK.COLUMN_NAME
                +" from "+table+" "
                +" where substr("+TBK.COLUMN_CODE+",0,?)=? and "+TBK.COLUMN_CHANGEPERCENT+">1 and "+TBK.COLUMN_DATE+" in (select "+TBK.COLUMN_DATE+" from "+table
                +" order by "+TBK.COLUMN_DATE+" DESC limit 1)"
                +"order by "+TBK.COLUMN_CHANGEPERCENT+" DESC )"
                +"union select * from ( select *,1 as z1,abs("+TBK.COLUMN_CHANGEPERCENT+")as z2 "//+TBK.COLUMN_DATE+","+TBK.COLUMN_CODE+","+TBK.COLUMN_NAME
                +" from "+table+" "
                +" where substr("+TBK.COLUMN_CODE+",0,?)=? and "+TBK.COLUMN_CHANGEPERCENT+"<-1 and "+TBK.COLUMN_DATE+" in (select "+TBK.COLUMN_DATE+" from "+table
                +" order by "+TBK.COLUMN_DATE+" DESC limit 1)"
                +"order by "+TBK.COLUMN_CHANGEPERCENT+" DESC ) order by z1 DESC,z2 DESC";

        LogX.w(TAG, "111: 1111--"+sql);
        try {
            JSONObject json=new JSONObject("{\"gn_\":{\"time\":\"\",\"data\":[]},\"new_\":{\"time\":\"\",\"data\":[]},\"hangye_\":{\"time\":\"\",\"data\":[]}}");
            codeKey="gn_";
            len=codeKey.length()+1;
            String[] selectionArgs=new String[]{len+"",codeKey,len+"",codeKey};
            Cursor c=null;
            //c = db.rawQuery(sql1,null);
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                LogX.w(TAG, "queryBK10: 222--");
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    LogX.w(TAG, "queryBK10: 333--");
                    json.put(codeKey,json1);
                }
            }
            codeKey="new_";
            len=codeKey.length()+1;
            selectionArgs=new String[]{len+"",codeKey,len+"",codeKey};
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    LogX.w(TAG, "queryBK10: 666--"+codeKey);
                    json.put(codeKey,json1);
                }
            }
            codeKey="hangye_";
            len=codeKey.length()+1;
            selectionArgs=new String[]{len+"",codeKey,len+"",codeKey};
            c = db.rawQuery(sql,selectionArgs);
            if (c != null) {
                JSONObject json1 =BK_getData(c,codeKey);
                if (json1!=null){
                    LogX.w(TAG, "queryBK10: 888--"+codeKey);
                    json.put(codeKey,json1);
                }
            }
            LogX.w(TAG, "queryBK10: 999--");
            return json;
        }catch (Exception e){
            LogX.e(TAG,"Error:"+ e.getMessage());
            return null;
        }
    }
    private JSONObject BK_getData(Cursor c,String codeKey){
        JSONObject json=new JSONObject();
        DecimalFormat decimalFormat=new DecimalFormat();
        decimalFormat.applyPattern("#0.00");
        try{
            JSONArray jarry=new JSONArray();
            int n=0;
            String sTime="";
            LogX.e(TAG,"get111="+c.getCount());
            int count=0;
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                int COLUMN_DATE = c.getColumnIndex(TBK.COLUMN_DATE);
                int COLUMN_CODE = c.getColumnIndex(TBK.COLUMN_CODE);
                int COLUMN_NAME = c.getColumnIndex(TBK.COLUMN_NAME);
                int COLUMN_NUMBER = c.getColumnIndex(TBK.COLUMN_NUMBER);
                int COLUMN_COUNT = c.getColumnIndex(TBK.COLUMN_COUNT);
                //int COLUMN_VOLUME = c.getColumnIndex(TBK.COLUMN_VOLUME);
                //int COLUMN_AMOUNT = c.getColumnIndex(TBK.COLUMN_AMOUNT);
                //int COLUMN_TRADE = c.getColumnIndex(TBK.COLUMN_TRADE);
                int COLUMN_CHANGEPERCENT = c.getColumnIndex(TBK.COLUMN_CHANGEPERCENT);
                //int COLUMN_SYMBOL = c.getColumnIndex(TBK.COLUMN_SYMBOL);
                //int COLUMN_SNAME = c.getColumnIndex(TBK.COLUMN_SNAME);
                //int COLUMN_STRADE = c.getColumnIndex(TBK.COLUMN_STRADE);
                //int COLUMN_SCHANGEPERCENT = c.getColumnIndex(TBK.COLUMN_SCHANGEPERCENT);
                String vCOLUMN_DATE = c.getString(COLUMN_DATE);
                String vCOLUMN_CODE = c.getString(COLUMN_CODE);
                String vCOLUMN_NAME = c.getString(COLUMN_NAME);
                int vCOLUMN_NUMBER = c.getInt(COLUMN_NUMBER);
                int vCOLUMN_COUNT = c.getInt(COLUMN_COUNT);
                //double vCOLUMN_VOLUME = c.getDouble(COLUMN_VOLUME);
                //double vCOLUMN_AMOUNT = c.getDouble(COLUMN_AMOUNT);
                //double vCOLUMN_TRADE = c.getDouble(COLUMN_TRADE);
                double vCOLUMN_CHANGEPERCENT = c.getDouble(COLUMN_CHANGEPERCENT);
                //String vCOLUMN_SYMBOL = c.getString(COLUMN_SYMBOL);
                //String vCOLUMN_SNAME = c.getString(COLUMN_SNAME);
                //double vCOLUMN_STRADE = c.getDouble(COLUMN_STRADE);
                //double vCOLUMN_SCHANGEPERCENT = c.getDouble(COLUMN_SCHANGEPERCENT);
                //Map<String,Object> map1=new HashMap<String,Object>();
                String sCOLUMN_CHANGEPERCENT=decimalFormat.format(vCOLUMN_CHANGEPERCENT);
                vCOLUMN_CHANGEPERCENT=Double.parseDouble(sCOLUMN_CHANGEPERCENT);
                vCOLUMN_DATE=vCOLUMN_DATE.substring(11,vCOLUMN_DATE.length());
                if (sTime.length()==0){
                    sTime = vCOLUMN_DATE;
                }
                if (vCOLUMN_NAME.length()>4){
                    vCOLUMN_NAME=vCOLUMN_NAME.substring(0,4);
                }
                if (vCOLUMN_CODE.length()>8){
                    vCOLUMN_CODE=vCOLUMN_CODE.substring(vCOLUMN_CODE.length()-8,vCOLUMN_CODE.length());
                }
                JSONArray ja=new JSONArray();
                ja.put(0,vCOLUMN_NAME);
                ja.put(1,vCOLUMN_CHANGEPERCENT);
                ja.put(2,vCOLUMN_COUNT);
                ja.put(3,vCOLUMN_NUMBER);
                jarry.put(n,ja);
                if (vCOLUMN_CHANGEPERCENT>0){
                    count++;
                }
                n++;
                //LogX.w(TAG, vCOLUMN_CODE +","+ vCOLUMN_CHANGEPERCENT +","+ vCOLUMN_NUMBER +","+ vCOLUMN_COUNT +","+ vCOLUMN_SYMBOL );
                //LogX.w(TAG, vCOLUMN_CODE+" ,  "+vCOLUMN_CHANGEPERCENT+"");
            }
            json.put("time",sTime);
            json.put("data",jarry);
            json.put("countd1",count);
            //LogX.w(TAG, jarry.toString());
            LogX.e(TAG,""+json.toString());
            String filepathname=Global.workPath+"/snbk.txt";
            FileX.writeLines(filepathname,json.toString(),"utf-8");
            return json;
        }catch (Exception e){
            LogX.e(TAG, "Error:"+e.getMessage());
            return null;
        }
    }

    public JSONObject querySSE10(){
        String table = TSSE.TABLE_NAME;//"ssen";
        String percent = TSSE.COLUMN_PERCENT;//"percent";
        String date = TSSE.COLUMN_DATE;//"date";
        String sql = "select 10 as type,count(*) as count,date from "+table+" where "+percent+">=10.1 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 9 as type,count(*) as count,date from "+table+" where "+percent+">=9 and "+percent+"<10.1 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 8 as type,count(*) as count,date from "+table+" where "+percent+">=8 and "+percent+"<9  and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 7 as type,count(*) as count,date from "+table+" where "+percent+">=7 and "+percent+"<8 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 6 as type,count(*) as count,date from "+table+" where "+percent+">=6 and "+percent+"<7  and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 5 as type,count(*) as count,date from "+table+" where "+percent+">=5 and "+percent+"<6   and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 4 as type,count(*) as count,date from "+table+" where "+percent+">=4 and "+percent+"<5  and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 3 as type,count(*) as count,date from "+table+" where "+percent+">=3 and "+percent+"<4  and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 2 as type,count(*) as count,date from "+table+" where "+percent+">=2 and "+percent+"<3 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 1 as type,count(*) as count,date from "+table+" where "+percent+">=1 and "+percent+"<2 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select 0 as type,count(*) as count,date from "+table+" where "+percent+">=0 and "+percent+"<1 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -11 as type,count(*) as count,date from "+table+" where "+percent+"<=-10.1 and "+date+" in (select "+date+" from snbk order by "+date+" DESC limit 1)"
                +" union select -10 as type,count(*) as count,date from "+table+" where "+percent+"<=-9 and "+percent+">-10.1 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -9 as type,count(*) as count,date from "+table+" where "+percent+"<=-8 and "+percent+">-9 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -8 as type,count(*) as count,date from "+table+" where "+percent+"<=-7 and "+percent+">-8 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -7 as type,count(*) as count,date from "+table+" where "+percent+"<=-6 and "+percent+">-7 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -6 as type,count(*) as count,date from "+table+" where "+percent+"<=-5 and "+percent+">-6 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -5 as type,count(*) as count,date from "+table+" where "+percent+"<=-4 and "+percent+">-5 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -4 as type,count(*) as count,date from "+table+" where "+percent+"<=-3 and "+percent+">-4 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -3 as type,count(*) as count,date from "+table+" where "+percent+"<=-2 and "+percent+">-3 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -2 as type,count(*) as count,date from "+table+" where "+percent+"<=-1 and "+percent+">-2 and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)"
                +" union select -1 as type,count(*) as count,date from "+table+" where "+percent+"<0 and "+percent+">-1  and "+date+" in (select "+date+" from "+table+" order by "+date+" DESC limit 1)";
        String sql100=" SELECT CASE "+
                " WHEN percent>=9.94  THEN 100 "+
                " WHEN percent<=-9.94  THEN -100"+
                " ELSE 999 END percent,"+
                " COUNT(*) as count FROM "+table+" WHERE date in (select date from "+table+" order by date DESC limit 1)"+
                " GROUP BY CASE"+
                " WHEN percent>=9.94   THEN 100 "+
                " WHEN percent<=-9.94  THEN -100"+
                " ELSE 999 END";
        Cursor c=null;
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();
        LogX.w(TAG, "querySSE10: 11" );
        try {
            c = db.rawQuery(sql, null);
            if (c != null) {
                try {
                    LogX.w(TAG, "querySSE10: 55" );
                    int nCount = 0;
                    String sdate = "";
                    int n = 0;
                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        String cType = c.getString(0);
                        int cCount = c.getInt(1);
                        String cDate = c.getString(2);
                        //LogX.e(TAG,n+":"+cType+", "+cCount+", "+cDate);
                        if (cDate != null) {
                            sdate = cDate;
                            if (sdate.length() > 0 && !sdate.equalsIgnoreCase(cDate)) {
                                LogX.e(TAG, "Error:" + "error date." + sdate + " != ? " + cDate);
                                break;
                            }
                        }
                        JSONArray ja = new JSONArray();
                        ja.put(0, cType);
                        ja.put(1, cCount);
                        jarray.put(n, ja);
                        nCount += cCount;
                        n++;
                    }
                    json.put("date", sdate);
                    json.put("count", nCount);
                } catch (Exception e) {
                    LogX.e(TAG, "Error: querySSE10:" + e.getMessage());
                    return null;
                }
            }
            c.close();
            c = db.rawQuery(sql100, null);
            if (c != null) {
                try {
                    LogX.w(TAG, "querySSE10: 77" );
                    int nCount = 0;
                    String sdate = "";
                    int n = 0;
                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        String cType = c.getString(0);
                        int cCount = c.getInt(1);
                        //String cDate = c.getString(2);
                        JSONArray ja = new JSONArray();
                        ja.put(0, cType);
                        ja.put(1, cCount);
                        jarray.put(n, ja);
                        nCount += cCount;
                        n++;
                    }

                } catch (Exception e) {
                    LogX.e(TAG, "Error: querySSE10:" + e.getMessage());
                    return null;
                }
            }
            c.close();
            json.put("list", jarray);
        }catch (Exception e){
            LogX.e(TAG, "Error: querySSE10:" + e.getMessage());
            return null;
        }
        LogX.w(TAG, "querySSE10: 99" );
        LogX.e(TAG,""+json.toString());
        String filepathname=Global.workPath+"/ssenow.txt";
        FileX.writeLines(filepathname,json.toString(),"gb2312");
        return json;
    }
}
