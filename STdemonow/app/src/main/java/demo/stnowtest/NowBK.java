package demo.stnowtest;

import android.content.ContentValues;

import demo.api.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NowBK {
    private static String TAG="NowBK";
    public void getBKdata(String foldName,String sendOKfilename){
        String stime="";
        try {
            JSONObject json=Global.getContentProvider().queryBK111();
            //LogX.w(TAG, json.toString());
            LogX.w(TAG,"getBKdata()1: "+TimeX.getToday());
            String cmdKey="cmdbk";
            File fzip=new File(Global.workPath+"/"+cmdKey+","+""+".zip");
            File ftxt=new File(Global.workPath+"/"+cmdKey+","+""+".txt");
            FileX.writeLines(ftxt.getAbsolutePath(),json.toString(),"utf-8");
            List<File> files = new ArrayList<File>();
            files.add(ftxt);
            LogX.w(TAG,"getBKdata()2: "+TimeX.getToday());
            new ZipUtils().zipFiles(files, fzip);
            LogX.w(TAG,"getBKdata()2: "+fzip.getAbsolutePath());
            files.clear();
            files.add(fzip);
            LogX.w(TAG,"getBKdata()3: "+TimeX.getToday());
            long startTime = System.currentTimeMillis();
            new MailCmd().cmdReponse(cmdKey+",1,"+json.getString("time"), js2table(json),files,foldName,cmdKey);
            long endTime = System.currentTimeMillis();
            long startend=(endTime-startTime)/1000;
            if (startend>6){
                LogX.w(TAG, "db cmdReponse time: "+startend+" s. (>6s)");
            }else{
                LogX.w(TAG, "db cmdReponse time: "+startend+" s.");
            }
            if (sendOKfilename.length()>0) {
                new MailCmd().cmdSave(null,cmdKey+",1,"+json.getString("time"), js2table(json),files,Global.getdailyBKDir());
                File f=new File(sendOKfilename);
                try {
                    f.createNewFile();
                    LogX.w(TAG,"getBKdata()4: "+TimeX.getToday() +" saveOK.");
                } catch (Exception e) {
                    LogX.e(TAG, e.getStackTrace() + "");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LogX.e(TAG, "Error: database has error！"+e.getStackTrace());
            e.printStackTrace();
        }

    }
    private String js2table(JSONObject json){

        String stime="";
        try {
            //"{\"time\":\"\",\"gn\":[],,\"hy\":[],,\"new\":[]}"
            JSONArray gn=json.getJSONArray("gn");
            JSONArray hy=json.getJSONArray("hy");
            JSONArray ne=json.getJSONArray("ne");
            int gnlen=gn.length();
            int hylen=hy.length();
            int nelen=ne.length();
            int n=0;
            String tb="<table border='1'>";
            JSONArray one;
            while (n<gnlen || n < hylen || n < nelen){
                tb+="<tr>";
                if (n<gnlen) {
                    one=gn.getJSONArray(n);
                    tb+="<td>";
                    tb += one.get(0).toString();
                    tb+="</td><td>";
                    tb += one.get(1).toString();
                    tb+="</td>";
                }else{
                    tb+="";
                }
                if (n<hylen) {
                    one=hy.getJSONArray(n);
                    tb+="<td>";
                    tb += one.get(0).toString();
                    tb+="</td><td>";
                    tb += one.get(1).toString();
                    tb+="</td>";
                }else{
                    tb+="";
                }
                if (n<nelen) {
                    one=ne.getJSONArray(n);
                    tb+="<td>";
                    tb += one.get(0).toString();
                    tb+="</td><td>";
                    tb += one.get(1).toString();
                    tb+="</td>";
                }else{
                    tb+="";
                }
                tb+="</tr>";
                n++;
            }
            tb+="</table>";
            //LogX.w(TAG, tb);
            return tb;
        }catch (Exception e){
            LogX.e(TAG, "Error: database has error！"+e.getStackTrace());
            e.printStackTrace();
        }
        return "";
    }


    public boolean parserFileBK(File f,String stime,String key){
        LogX.w(TAG,"parserFileBK:"+key+stime+f.getAbsolutePath());
        Date time;

        String data=FileX.getJsonString(f.getAbsolutePath(),"utf-8");
        if (data==null || data.length()==0){
            return false;
        }
        try {
            JSONObject json;
            LogX.w(TAG,"parserFileBK:"+data.substring(0,100));
            LogX.w(TAG,"parserFileBK:"+data.indexOf("["));
            if (data.indexOf("[")==0) {
                JSONArray json1 = new JSONArray(data);
                json = json1.getJSONObject(0);
            }else{
                json = new JSONObject(data);
            }
            //LogX.w(TAG,json.toString());
            JSONArray jsonItems = json.getJSONArray("items");
            LogX.d(TAG,"----------json lines:" + jsonItems.length());
            //NumberFormat nf1 = new DecimalFormat("#,####,####");
            String date = "";
            String bkname = "";
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONArray item = jsonItems.getJSONArray(i);
                String a1 = item.getString(0).replace("\n", "");
                String a2 = item.getString(1);
                bkname=a2.substring(0,a2.indexOf("_"));
                /*
                if (a2.indexOf("gn_")==0) {
                    bkname = a2.substring(0, "gn_".length()-1);
                    a2 = a2.substring(3, a2.length());
                }else if (a2.indexOf("new_")==0) {
                    bkname = a2.substring(0, "new_".length()-1);
                    a2 = a2.substring(3, a2.length());
                }else if (a2.indexOf("hangye_")==0) {
                    bkname = a2.substring(0, "hangye_".length()-1);
                    a2 = a2.substring(3, a2.length());
                }else{
                    LogX.w(TAG,"warring: ?");
                    bkname="";
                }
                */
                int a3 = item.getInt(2);
                int a4 = item.getInt(3);
                int a5 = item.getInt(4);// / 10000;
                String a5s = (int) Math.rint(a5 / 10000) + "w";//nf1.format((int)Math.rint(line.a5/10000))+"w";
                int a6 = item.getInt(5);
                //a6 = item.getInt(5) / 10000;
                double a7 = item.getDouble(6);
                double a8 = item.getDouble(7); // per v
                double a9 = item.getDouble(8);//per
                String a10 = item.getString(9);
                //a10 = a10.substring(2, a10.length());
                String a11 = item.getString(10);
                double a12 = 0;
                double a13 = 0;
                double a14 = 0;
                if (a10.length()>0) {
                    a12 = item.getDouble(11);
                    a13 = item.getDouble(12);
                    a14 = item.getDouble(13);
                }
                //LogX.d(TAG,i+":" +","+a2);
                //LogX.d(TAG,i+":" + a1+","+a2);
                /*
                    String sql = "insert into " + TBK.TABLE_NAME;
                    sql += "(";
                    sql += TBK.COLUMN_TYPE + ",";
                    sql += TBK.COLUMN_TIME + ",";
                    sql += TBK.COLUMN_CODE + ",";
                    sql += TBK.COLUMN_NAME + ",";
                    sql += TBK.COLUMN_NUMBER + ",";
                    sql += TBK.COLUMN_COUNT + ",";
                    sql += TBK.COLUMN_VOLUME + ",";
                    sql += TBK.COLUMN_AMOUNT + ",";
                    sql += TBK.COLUMN_TRADE + ",";
                    sql += TBK.COLUMN_CHANGEPRICE + ",";
                    sql += TBK.COLUMN_CHANGEPERCENT + ",";
                    sql += TBK.COLUMN_SYMBOL + ",";
                    sql += TBK.COLUMN_SNAME + ",";
                    sql += TBK.COLUMN_STRADE + ",";
                    sql += TBK.COLUMN_SCHANGEPRICE + ",";
                    sql += TBK.COLUMN_SCHANGEPERCENT + ",";
                    sql += TBK.COLUMN_URL + ",";
                    sql += TBK.COLUMN_CREATE;
                    sql += ")";
                    sql += "values(";
                    sql += "\"" + bkname + "\",";
                    sql += "\"" + stime + "\",";
                    sql += "\"" + a2 + "\",";
                    sql += "\"" + a1 + "\",";
                    sql += a3 + ",";
                    sql += a4 + ",";
                    sql += a5 + ",";
                    sql += a6 + ",";
                    sql += a7 + ",";
                    sql += a8 + ",";
                    sql += a9 + ",";
                    sql += "\"" + a10 + "\","; //COLUMN_SYMBOL
                    sql += "\"" + a11 + "\","; //COLUMN_SNAME
                    sql += a12 + ",";  //COLUMN_STRADE
                    sql += a13 + ",";  //COLUMN_SCHANGEPRICE
                    sql += a14 + ",";  //COLUMN_SCHANGEPERCENT
                    sql += "\"" + "sn" + "\",";  //COLUMN_URL
                    sql += "\"" + stime + "\"";  //
                    sql += ")";
                    if (i <= 2)
                        LogX.w(TAG, "[run]->: " + sql);
                    Global.getJdbc().update(sql);
                */

                    TBK bk = new TBK();
                    ContentValues values = new ContentValues();
                    values.put(TBK.COLUMN_TYPE, bkname);
                    values.put(TBK.COLUMN_CODE, a2);
                    values.put(TBK.COLUMN_TIME, stime);
                    values.put(TBK.COLUMN_NAME, a1);
                    values.put(TBK.COLUMN_NUMBER, a3);
                    values.put(TBK.COLUMN_COUNT, a4);
                    values.put(TBK.COLUMN_VOLUME, a5);
                    values.put(TBK.COLUMN_AMOUNT, a6);
                    values.put(TBK.COLUMN_TRADE, a7);
                    values.put(TBK.COLUMN_CHANGEPRICE, a8);
                    values.put(TBK.COLUMN_CHANGEPERCENT, a9);
                    values.put(TBK.COLUMN_SYMBOL, a10);
                    values.put(TBK.COLUMN_SNAME, a11);
                    values.put(TBK.COLUMN_STRADE, a12);
                    values.put(TBK.COLUMN_SCHANGEPRICE, a13);
                    values.put(TBK.COLUMN_SCHANGEPERCENT, a14);
                    values.put(TBK.COLUMN_URL, "si");
                    values.put(TBK.COLUMN_CREATE, stime);
                    //LogX.e(TAG, "[run]->:" + values.toString());
                    Global.getContentProvider().insertBK(values);

            }
        }catch (Exception e){
            LogX.d(TAG,"[FileBK] Error:"+e.getStackTrace());
        }finally {
            return true;
        }

    }

}
