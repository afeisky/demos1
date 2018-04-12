package afei;

import afei.api.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NowParser {
    private static String TAG="NowParser";
    public ArrayList<String> unzipFile(String zipfilepath,String destFolder){
        try {
            return ZipUtils.upZipFile(new File(zipfilepath), destFolder);
        }catch (Exception e){
            LogX.e(TAG, "Error: " + e.getStackTrace());
        }
        return null;
    }
    public void input(String zipfilepath,String destFolder){
        ArrayList<String> filenames=null;

        //---
        //if (true)
        //    return;
        filenames =unzipFile(zipfilepath, destFolder);
        LogX.w(TAG,"111:"+filenames);
        if (filenames.size()>0){
            for (String s:filenames){
                parserFile1(new File(destFolder+"/"+s),s);
                //break;///////////////
            }
        }else{
            LogX.w(TAG,"Not found file!");
            return;
        }
    }
    private SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private SimpleDateFormat yyMMdd_HHmmss = new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void parserFile1(File f,String filename){
        if (f==null)
            return;
        if (!f.exists()){
            return;
        }
        //LogX.w(TAG,"filename: "+filename);
        String s=filename.substring(0,filename.indexOf("."));
        if (s.length()==0) {
            s = filename;
        }
        String[] ss=s.split(",");
        if (ss.length>1){
            //LogX.w(TAG,"filename:"+ss[0]+","+ss[1]);
            String DATEKeyStr="180410_090435";
            if (ss[1].length()==DATEKeyStr.length()){
                if (f!=null && f.exists()){
                    String stime=yy2yyyy(ss[1]);
                    if (stime.length()>=DATEKeyStr.length()){
                        parserFile2(f,ss[0],stime);
                    }
                }
            }

        }
        //LogX.w(TAG,"go:"+f.getAbsolutePath()+filename);
    }
    private void parserFile2(File f,String key,String stime){
        if (key.equals("gnbk") || key.equals("bknode")|| key.equals("bkshy")){
            parserFileBK(f,stime,key);
        }else if (key.equals("sse")){
            parserFileSSE(f,stime);
        }

    }
    private String yy2yyyy(String yymm){
        try {
            String yyyymm = null;
            yyyymm = yyyy_mm_dd_hhmmss.format(yyMMdd_HHmmss.parse(yymm).getTime());
            //LogX.w(TAG,"yyyymm:"+yyyymm);
            return yyyymm;
        }catch (Exception e){
            LogX.e(TAG, "Error: " + e.getStackTrace());
            return "";
        }
    }

    private String yyyy2yyyy(String yymm){
        try {
            String yyyymm = null;
            yyyymm = yyyy_mm_dd_hhmmss.format(yyyyMMdd_HHmmss.parse(yymm).getTime());
            //LogX.w(TAG,"yyyymm:"+yyyymm);
            return yyyymm;
        }catch (Exception e){
            LogX.e(TAG, "Error: " + e.getStackTrace());
            return "";
        }
    }
    private boolean parserFileBK(File f,String stime,String key){
        LogX.w(TAG,"parserFileBK:"+key+stime+f.getAbsolutePath());
        Date time;

        String data=getJsonString(f.getAbsolutePath(),"utf-8");
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
                LogX.d(TAG,i+":" +","+a2);
                //LogX.d(TAG,i+":" + a1+","+a2);

                String sql="insert into snbk ";
                sql+="(";
                sql+=TBK.COLUMN_TYPE+",";
                sql+=TBK.COLUMN_TIME+",";
                sql+=TBK.COLUMN_CODE+",";
                sql+=TBK.COLUMN_NAME+",";
                sql+=TBK.COLUMN_NUMBER+",";
                sql+=TBK.COLUMN_COUNT+",";
                sql+=TBK.COLUMN_VOLUME+",";
                sql+=TBK.COLUMN_AMOUNT+",";
                sql+=TBK.COLUMN_TRADE+",";
                sql+=TBK.COLUMN_CHANGEPRICE+",";
                sql+=TBK.COLUMN_CHANGEPERCENT+",";
                sql+=TBK.COLUMN_SYMBOL+",";
                sql+=TBK.COLUMN_SNAME+",";
                sql+=TBK.COLUMN_STRADE+",";
                sql+=TBK.COLUMN_SCHANGEPRICE+",";
                sql+=TBK.COLUMN_SCHANGEPERCENT+",";
                sql+=TBK.COLUMN_URL+",";
                sql+=TBK.COLUMN_CREATE;
                sql+=")";
                sql+="values(";
                sql+="\""+bkname+"\",";
                sql+="\""+stime+"\",";
                sql+="\""+a2+"\",";
                sql+="\""+a1+"\",";
                sql+=a3+",";
                sql+=a4+",";
                sql+=a5+",";
                sql+=a6+",";
                sql+=a7+",";
                sql+=a8+",";
                sql+=a9+",";
                sql+="\""+a10+"\","; //COLUMN_SYMBOL
                sql+="\""+a11+"\","; //COLUMN_SNAME
                sql+=a12+",";  //COLUMN_STRADE
                sql+=a13+",";  //COLUMN_SCHANGEPRICE
                sql+=a14+",";  //COLUMN_SCHANGEPERCENT
                sql+="\""+"sn"+"\",";  //COLUMN_URL
                sql+="\""+stime+"\"";  //
                sql+=")";
                LogX.e(TAG,"[run]->: "+sql);
                Global.jdbc.update(sql);
                /*
                TBK bk=new TBK();
                ContentValues values = new ContentValues();
                values.put(TBK.COLUMN_TYPE,bkname);
                values.put(TBK.COLUMN_CODE,a2);
                values.put(TBK.COLUMN_DATE, stime);
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
                LogX.e(TAG,"[run]->:"+values.toString());
                Global.getContentProvider().insertBK(values);
                */
            }
        }catch (Exception e){
            LogX.e(TAG,"[FileBK] Error:"+e.getStackTrace());
        }
        return true;
    }


    private boolean parserFileSSE(File f,String stime){
        LogX.w(TAG,"parserFileSSE:"+stime+f.getAbsolutePath());

        String data=getJsonString(f.getAbsolutePath(),"gb2312");
        if (data==null || data.length()==0){
            return false;
        }
        try {
            JSONObject json=null;
            LogX.w(TAG,"parseJsonSSE: ="+data.lastIndexOf("}")+","+data.length());
            if (data.indexOf("{")==0 ) {//&& (data.length()-data.lastIndexOf("}"))==1) {
                json = new JSONObject(data);
            }else{
                return false;
            }
            if (json==null){
                return false;
            }
            LogX.w(TAG,"parseJsonSSE: "+json.toString().substring(0,60));
            int count = json.getInt("end");
            int idate = json.getInt("date");
            int itime = json.getInt("time");
            Date time;
            LogX.w(TAG,"stime="+stime+" ,count="+count);
            stime=yyyy2yyyy(String.valueOf(idate)+"_"+String.valueOf(itime));
            LogX.w(TAG,"stime="+stime+" ,count="+count);

            JSONArray jlist = json.getJSONArray("list");
            //LogX.d(TAG,"json lines:" + jsonItems.length());
            //NumberFormat nf1 = new DecimalFormat("#,####,####");
            String date = "";
            String bkname = "";
            for (int i = 0; i < 0/*jlist.length()*/; i++) {
                JSONArray item = jlist.getJSONArray(i);
                String a0 = item.getString(0);
                String a1 = item.getString(1);
                double a2 = item.getDouble(2);
                double a3 = item.getDouble(3);
                double a4 = item.getDouble(4);
                double a5 = item.getDouble(5);
                double a6 = item.getDouble(6);
                double a7 = item.getDouble(7); //percent
                double a8 = item.getDouble(8); // per v
                double a9 = item.getDouble(9);// (int) Math.rint(a5 / 10000)
                String a10 = item.getString(10);
                double a11 = item.getDouble(11);
                double a12 = item.getDouble(12);
                LogX.d(TAG,i+":" + a0+","+a1);
            }
        }catch (Exception e){
            LogX.e(TAG,"[FileSSE] Error:"+e.getStackTrace());
        }
        return true;
    }


    private String getJsonString(String filepathname,String encode){
        String data="";
        //String filepathname=Global.workPath+"/snbk_now/gnbk_180125_174529.json";
        LogX.w(TAG,"get="+filepathname);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepathname),encode));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            bf.close();
            data=stringBuilder.toString();
            return data;
        }catch (Exception e){
            LogX.e(TAG,"Error: getJsonString() "+e.getStackTrace());
        }
        return data;
    }

}
