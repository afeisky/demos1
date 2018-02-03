package afei.demo.stdemohistory;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import afei.demo.stdemohistory.Api.DbContentProvider;
import afei.demo.stdemohistory.Api.LogX;
import afei.demo.stdemohistory.Api.TBK;

/**
 * Created by chaofei on 18-1-23.
 */

public class Input2db {
    private static final String TAG="Input2db";
    private DbContentProvider contentProvider;
    public int start(Context context){
        LogX.w(TAG, "init: 111");
        //SqliteHelperContext dbContext = new SqliteHelperContext(this);
        //SqliteHelper dbHelper = new SqliteHelper(dbContext);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);
        contentProvider=new DbContentProvider();
/*

        TST bk=new TST();
        ContentValues values = new ContentValues();
        values.put(TST.COLUMN_CODE, "00001a");
        values.put(TST.COLUMN_DATE, "2018-01-01");
        values.put(TST.COLUMN_NAME, "Test1");
        values.put(TST.COLUMN_BEGIN, 10.12);
        values.put(TST.COLUMN_NOW, 10.13);
        values.put(TST.COLUMN_PRICE, 10.14);
        values.put(TST.COLUMN_HIGH, 10.15);
        values.put(TST.COLUMN_LOW, 10.16);
        values.put(TST.COLUMN_GAP, 10.17);
        values.put(TST.COLUMN_VOLUME, 1000000.98);
        values.put(TST.COLUMN_MONEY, 500000.78);
        values.put(TST.COLUMN_URL, "si");
        values.put(TST.COLUMN_CREATE, "2018-01-12");
        contentProvider.insertBK(values);
*/

        TBK bk=new TBK();
        ContentValues values = new ContentValues();
        values.put(TBK.COLUMN_CODE, "00001a");
        values.put(TBK.COLUMN_DATE, "2018-01-01");
        values.put(TBK.COLUMN_NAME, "Test1");
        values.put(TBK.COLUMN_NUMBER, 10.12);
        values.put(TBK.COLUMN_NUMBER, 10.13);
        values.put(TBK.COLUMN_VOLUME, 10.14);
        values.put(TBK.COLUMN_AMOUNT, 10.15);
        values.put(TBK.COLUMN_TRADE, 10.16);
        values.put(TBK.COLUMN_CHANGEPRICE, 10.17);
        values.put(TBK.COLUMN_SYMBOL, "aaa");
        values.put(TBK.COLUMN_SNAME, "bbb");
        values.put(TBK.COLUMN_CHANGEPRICE, 10.17);
        values.put(TBK.COLUMN_SCHANGEPERCENT, 1000000.98);
        values.put(TBK.COLUMN_SCHANGEPERCENT, 500000.78);
        values.put(TBK.COLUMN_URL, "si");
        values.put(TBK.COLUMN_CREATE, "2018-01-12");
        contentProvider.insertBK(values);

        LogX.w(TAG, "init: 222");

        return 0;
    }
    private List<String> codelist =new ArrayList<>();
    public String getCodeFile(String workDir,String sohuDir){
        String resultStr="";
        try{
            String dir = workDir + "/download_sina/sina_bk";
            File f = new File(dir);
            File[] flist = f.listFiles();
            List<String> sina_bk_hsa = new ArrayList<String>();
            List<String> sina_bk_hsb = new ArrayList<String>();
            List<String> sina_bk_sza = new ArrayList<String>();
            List<String> sina_bk_szb = new ArrayList<String>();
            List<String> sina_bk_gnbk = new ArrayList<String>();
            List<String> sina_bk_node = new ArrayList<String>();
            List<String> sina_bk_shy = new ArrayList<String>();
            for (int i = 0; i < flist.length; i++) {
                //Logd( i + ":" + flist[i].getAbsolutePath()+" size:"+flist[i].length());
                String name=flist[i].getName();
                String str=flist[i].getAbsolutePath();
                if (name.indexOf("sina_bk_hsa")==0){
                    sina_bk_hsa.add(str);
                }
                if (name.indexOf("sina_bk_hsb")==0){
                    sina_bk_hsb.add(str);
                }
                if (name.indexOf("sina_bk_sza")==0){
                    sina_bk_sza.add(str);
                }
                if (name.indexOf("sina_bk_szb")==0){
                    sina_bk_szb.add(str);
                }
                if (name.indexOf("sina_bk_gnbk")==0){
                    sina_bk_gnbk.add(str);
                    parseJson(str);
                }
                if (name.indexOf("sina_bk_node")==0){
                    sina_bk_node.add(str);
                }
                if (name.indexOf("sina_bk_shy")==0){
                    sina_bk_shy.add(str);
                }
            }
            Collections.sort(sina_bk_hsa);
            Collections.sort(sina_bk_hsb);
            Collections.sort(sina_bk_sza);
            Collections.sort(sina_bk_szb);
            Collections.sort(sina_bk_gnbk);
            Collections.sort(sina_bk_node);
            Collections.sort(sina_bk_shy);


        }catch (Exception e){
            LogX.e(TAG,"Error:"+e.getMessage());
        }
        return "";

    }






    private int parseJson(String filepathname){
        try {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepathname)));
                String line;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bf.close();

                JSONObject json =new JSONObject(stringBuilder.toString());
                JSONArray jsonItems = json.getJSONArray("items");
                LogX.w(TAG,"json lines:"+jsonItems.length());
                for (int i=0;i<jsonItems.length();i++){
                    JSONArray item= jsonItems.getJSONArray(i);
                    TBK bk=new TBK();
                    ContentValues values = new ContentValues();
                    values.put(TBK.COLUMN_CODE, "00001a");
                    values.put(TBK.COLUMN_DATE, "2018-01-01");
                    values.put(TBK.COLUMN_NAME, "Test1");
                    values.put(TBK.COLUMN_NUMBER, 10.12);
                    values.put(TBK.COLUMN_NUMBER, 10.13);
                    values.put(TBK.COLUMN_VOLUME, 10.14);
                    values.put(TBK.COLUMN_AMOUNT, 10.15);
                    values.put(TBK.COLUMN_TRADE, 10.16);
                    values.put(TBK.COLUMN_CHANGEPRICE, 10.17);
                    values.put(TBK.COLUMN_SYMBOL, "aaa");
                    values.put(TBK.COLUMN_SNAME, "bbb");
                    values.put(TBK.COLUMN_CHANGEPRICE, 10.17);
                    values.put(TBK.COLUMN_SCHANGEPERCENT, 1000000.98);
                    values.put(TBK.COLUMN_SCHANGEPERCENT, 500000.78);
                    values.put(TBK.COLUMN_URL, "si");
                    values.put(TBK.COLUMN_CREATE, "2018-01-12");
                    contentProvider.insertBK(values);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            LogX.e(TAG,"Error:"+e.getStackTrace());
        }


        return 0;

    }







}
