package demo;

import demo.api.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NowParser {
    private static String TAG="NowParser";

    public void unzipFileAndInput(String zipfilepath, String destFolder){
        ArrayList<String> filenames=null;
        //---
        //if (true)
        //    return;
        filenames =ZipUtils.upZipFile(new File(zipfilepath), destFolder);
        LogX.w(TAG,"111:"+filenames);
        if (filenames.size()>0){
            for (String s:filenames){
                parserByFilename(new File(destFolder+"/"+s),s);
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
    public void parserByFilename(File f,String filename){
        LogX.d(TAG, "f:" +f.getAbsolutePath());
        LogX.d(TAG, "filename:" +filename);
        if (f==null) {
            LogX.e(TAG, " parserFile1: not found file." +f.getAbsolutePath());
            return;
        }
        if (!f.exists()){
            return;
        }
        LogX.d(TAG,"parserFile1: filename: "+filename);
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
                        parserType(f,ss[0],stime);
                    }
                }
            }

        }
        //LogX.w(TAG,"go:"+f.getAbsolutePath()+filename);
    }
    public void parserType(File f,String key,String stime){
        if (key.equals("gnbk") || key.equals("bknode")|| key.equals("bkshy")){
            new NowBK().parserFileBK(f,stime,key);
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

    public boolean parserFileSSE(File f,String stime){
        LogX.w(TAG,"parserFileSSE:"+stime+f.getAbsolutePath());

        File ff=new File(f.getParent()+"/_fail.txt");

        String data=FileX.getJsonString(f.getAbsolutePath(),"gb2312");
        if (data==null || data.length()==0){
            return false;
        }
        try {
            JSONObject json=null;
            String sql="";
            LogX.d(TAG,"parseJsonSSE: ="+data.lastIndexOf("}")+","+data.length());
            if (data.indexOf("{")==0 ) {//&& (data.length()-data.lastIndexOf("}"))==1) {
                json = new JSONObject(data);
            }else{
                return false;
            }
            if (json==null){
                return false;
            }
            LogX.d(TAG,"parseJsonSSE: "+json.toString().substring(0,60));
            int count = json.getInt("end");
            int idate = json.getInt("date");
            int itime = json.getInt("time");
            Date time;

            String atime=String.valueOf(itime);
            if (atime.length()<"092213".length()){
                atime="0"+atime;
            }
            String str=String.valueOf(idate)+"_"+atime;
            LogX.d(TAG,"stime="+stime+" ,count="+count+" "+str);
            stime=yyyy2yyyy(str);
            //LogX.d(TAG,"stime="+stime+" ,count="+count);
            sql="select count(*) as count from "+TSSE.TABLE_NAME+" where DATE_FORMAT(time,'%Y-%m-%d %H:%i:%s')= DATE_FORMAT('" + stime + "','%Y-%m-%d %H:%i:%s')";
            LogX.d(TAG,sql);
            ResultSet rs= Global.getJdbc().query(sql);
            if (rs.next()){
                rs.previous();
            }
            if (rs.next()){
                int n = rs.getInt(1);   //volume
                if (n==count){
                    return false;
                }
            }

            JSONArray jlist = json.getJSONArray("list");
            //LogX.d(TAG,"json lines:" + jsonItems.length());
            //NumberFormat nf1 = new DecimalFormat("#,####,####");
            String date = "";
            String bkname = "";
            for (int i = 0; i < jlist.length(); i++) {
                JSONArray item = jlist.getJSONArray(i);
                String a0 = item.getString(0); //code
                String a1 = item.getString(1);  //name
                double a2 = item.getDouble(2); //begin
                double a3 = item.getDouble(3);  //high
                double a4 = item.getDouble(4);  //low
                double a5 = item.getDouble(5); //price //new
                double a6 = item.getDouble(6);  //last
                double a7 = item.getDouble(7); //percent
                long a8 = item.getLong(8); // per v   //volume
                long a9 = item.getLong(9);// (int) Math.rint(a5 / 10000)  //money
                String a10 = item.getString(10);  //note
                double a11 = item.getDouble(11);  //gap
                double a12 = item.getDouble(12);  //wave
                //if (!a0.equalsIgnoreCase("600000")){continue; }

                LogX.d(TAG,i+":" + a0+","+a1);
                sql="insert into "+TSSE.TABLE_NAME;
                sql+="(";
                sql+=TSSE.COLUMN_CODE+",";
                sql+=TSSE.COLUMN_TIME+",";
                sql+=TSSE.COLUMN_NAME+",";
                sql+=TSSE.COLUMN_BEGIN+",";
                sql+=TSSE.COLUMN_HIGH+",";
                sql+=TSSE.COLUMN_LOW+",";
                sql+=TSSE.COLUMN_NEW+",";
                sql+=TSSE.COLUMN_LAST+",";
                sql+=TSSE.COLUMN_PERCENT+",";
                sql+=TSSE.COLUMN_VOLUME+",";
                sql+=TSSE.COLUMN_MONEY+",";
                sql+=TSSE.COLUMN_NOTE+",";
                sql+=TSSE.COLUMN_GAP+",";
                sql+=TSSE.COLUMN_WAVE+",";
                sql+=TSSE.COLUMN_URL+",";
                sql+=TSSE.COLUMN_CREATE;
                sql+=")";
                sql+="values(";
                sql += "\"" + a0 + "\",";
                sql += "\"" + stime + "\",";
                sql += "\"" + a1 + "\",";
                sql += a2 + ",";
                sql += a3 + ",";
                sql += a4 + ",";
                sql += a5 + ",";   //new
                sql += a6 + ",";
                sql += a7 + ",";
                sql += a8 + ",";
                sql += a9 + ",";
                sql += "\"" +a10 + "\",";
                sql += a11 + ",";
                sql += a12 + ",";
                sql += "\"SSE\",";//data_from URL
                sql += "\"" + stime + "\""; //create_time
                sql += ")";
                LogX.d(TAG,"[run]->: "+sql);
                Global.getJdbc().update(sql);
                String sqlf=sql;
                //checkrecord();
                sql="select ";
                sql+=TSSE.COLUMN_CODE+",";
                sql+=TSSE.COLUMN_TIME+",";
                sql+=TSSE.COLUMN_NAME+",";
                sql+=TSSE.COLUMN_BEGIN+",";
                sql+=TSSE.COLUMN_HIGH+",";
                sql+=TSSE.COLUMN_LOW+",";
                sql+=TSSE.COLUMN_NEW+",";
                sql+=TSSE.COLUMN_LAST+",";
                sql+=TSSE.COLUMN_PERCENT+",";
                sql+=TSSE.COLUMN_VOLUME+",";
                sql+=TSSE.COLUMN_MONEY+",";
                sql+=TSSE.COLUMN_NOTE+",";
                sql+=TSSE.COLUMN_GAP+",";
                sql+=TSSE.COLUMN_WAVE+",";
                sql+=TSSE.COLUMN_URL+",";
                sql+=TSSE.COLUMN_CREATE;
                sql+=" from "+TSSE.TABLE_NAME+" where ";
                sql+=TSSE.COLUMN_CODE+"=\""+a0 + "\" and ";
                sql+=TSSE.COLUMN_TIME+"=\""+stime + "\"";
                LogX.d(TAG,sql);
                rs= Global.getJdbc().query(sql);
                if (rs.next()){
                    rs.previous();
                }
                if (rs.next()){
                    long volume = rs.getLong(TSSE.COLUMN_VOLUME);   //volume
                    long money = rs.getLong(TSSE.COLUMN_MONEY); // (int) Math.rint(a5 / 10000)  //money
                    if (volume==a8 && money==a9){

                    }else{
                        LogX.e(TAG,"[FileSSE] Fail insert: volume:"+a8+","+volume+";  money:"+a9+","+money);
                    }
                }else{
                    LogX.e(TAG,"[FileSSE] Fail insert:"+stime+","+a0);
                    FileX.add2File(ff.getAbsolutePath(),sqlf+"\n");//writeLines
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            LogX.e(TAG,"[FileSSE] Error:"+e.getMessage());
        }
        return true;
    }





}
