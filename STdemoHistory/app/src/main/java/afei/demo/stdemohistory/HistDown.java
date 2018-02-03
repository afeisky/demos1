package afei.demo.stdemohistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import afei.demo.stdemohistory.Api.DataParse;
import afei.demo.stdemohistory.Api.FileX;

/**
 * Created by chaofei on 18-1-12.
 */

public class HistDown {

    private String TAG = "[ShDown]";
    public static String ACTION_UPDATE_UI="afei.demo.stdemohistory";
    private ArrayList<String> listCode = new ArrayList<String>();
    private int nTryAgain=5;
    private Context mContext;
    private List<cURLs> listUrlx=new ArrayList<cURLs>();
    private int indexListUrls=0;
    private static int countConnect=0; //
    private SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
    //String url1="http://q.stock.sohu.com/hisHq?code=cn_0001008&start=20170901&end=20171013&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
    public String urlSohu1 = "http://q.stock.sohu.com/hisHq?code=cn_" ;
    public void start(Context context,String sBegin,String sEnd){
        this.mContext=context;

        //get listcode?

        //listCode

        File f = Environment.getExternalStorageDirectory();
        String workDir = f.getAbsolutePath() + "/DownloadData";
        f = new File(workDir);
        if (!f.exists()) {
            f.mkdir();
        }
        String sohuDir = f.getAbsolutePath() + "/sohu_"+sBegin+"_"+sEnd;
        Logw("dir="+sohuDir);
        f = new File(sohuDir);
        if (!f.exists()) {
            f.mkdir();
        }
        String codelist= getCodeFile(workDir,sohuDir);

        f=new File(codelist);
        if (codelist.length()>0 && f.exists() && f.length()>5){
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsolutePath())));
                String line;
                cURLs u = new cURLs();
                int n=0,count=0;
                while ((line = bf.readLine()) != null) {
                    if (line.length()>0) {
                        n++;
                        u = new cURLs();
                        u.url = getURL(line,sBegin,sEnd);
                        u.filePathName = sohuDir + "/" + line+".json";
                        f=new File(u.filePathName);
                        if (f.exists() && f.length()>200){
                            //if (readShFile(u.filePathName))
                            //    break;
                            continue;
                        }else {
                            u.nTry = 5; // >=2;
                            listUrlx.add(u);
                            //Logw("=> " + u.url);
                            //Logw("=> " + u.filePathName);
                            Logw("["+n+"] " + line + " [" + sBegin+ "," + sEnd+";"+u.url);
                            count++;
                        }
                        //if (count>10) break; //temp test

                    }
                }
                bf.close();
                //goon();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Loge("Error: no codelist!");
            return ;
        }

    }
    private boolean readShFile(String filepathname){

        String fileLines= FileX.readLines(filepathname,"gb2312");
        if (fileLines.length()>0){
            Log.w(TAG,fileLines.substring(fileLines.length()-200,fileLines.length()));
            DataParse.parseShJsonstring(fileLines);
        }

        return true;

    }

    private List<String> codelist =new ArrayList<>();
    public String getCodeFile(String workDir,String sohuDir){
        String resultStr="";
        String dir = workDir + "/download_sina/sina_bk";
        String filePathName=sohuDir+"/codelist.txt";
        Loge("---:"+dir);
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


        try {
            Loge("22:"+filePathName);
            if (sina_bk_hsa==null){
                Loge("22222:");
            }else{
                Loge("33333:"+sina_bk_hsa.size());
            }
            if (sina_bk_hsa != null && sina_bk_hsa.size() > 0) {
                Loge("33:"+sina_bk_hsa.get(sina_bk_hsa.size()-1));
                parseList(sina_bk_hsa.get(sina_bk_hsa.size()-1));
            }
            if (sina_bk_hsb != null && sina_bk_hsb.size() > 0) {

                Loge("33:"+sina_bk_hsb.get(sina_bk_hsb.size()-1));
                parseList(sina_bk_hsb.get(sina_bk_hsb.size()-1));
            }
            //if (sina_bk_sza != null && sina_bk_sza.size() > 0) {

            //    Loge("33:"+sina_bk_sza.get(sina_bk_sza.size()-1));
            //    parseList(sina_bk_sza.get(sina_bk_sza.size()-1));
            //}
            //if (sina_bk_szb != null && sina_bk_szb.size() > 0) {
            //    Loge("33:"+sina_bk_szb.get(sina_bk_szb.size()-1));
            //    parseList(sina_bk_szb.get(sina_bk_szb.size()-1));
            //}
            f=new File(filePathName);
            if (f.exists()){
                f.delete();
            }
            OutputStreamWriter out = null;
            out = new OutputStreamWriter(new FileOutputStream(filePathName), "gb2312");
            if (codelist!=null) {
                for (int i = 0; i < codelist.size(); i++) {
                    out.write(codelist.get(i)+"\n");
                }
            }
            out.close();
            Logw("codelist="+codelist.size());
        }catch (Exception e){
            Loge("Error2:"+e.getMessage());
        }

        return filePathName;
    }
    private boolean parseList(String filepathname){
        File f=new File(filepathname);
        if (f.isFile() && f.exists()) {
            try {

                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepathname)));
                        String line;
                        while ((line = bf.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        bf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                JSONObject json =new JSONObject(stringBuilder.toString());
                JSONArray jsonItems = json.getJSONArray("items");
                Loge("json lines:"+jsonItems.length());
                for (int i=0;i<jsonItems.length();i++){
                    JSONArray item= jsonItems.getJSONArray(i);
                    codelist.add(item.getString(0).substring(2));
                }
                //Loge("json:"+json.toString());
            }catch (Exception e){
                Loge("Error3:"+e.getMessage());
            }
        }
        //out.write(ss);
        return true;
    }
    private String getURL(String code,String begin,String end){
        try {
            begin =yyyymmdd.format(yyyy_mm_dd.parse(begin));
            end =yyyymmdd.format(yyyy_mm_dd.parse(end));
        }catch (Exception e){
            Logw("Error Date Format: "+begin+","+end);
            return "";
        }
        //Logw("getURL: " + code + " [" + begin+ "," + end);
        String url=urlSohu1+code;
        url += "&start=" + begin;
        url+= "&end=" + end;
        url += "&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
        return url;
    }

    private boolean down(String url){

        return true;
    }

    private void goon(){
        if (!(indexListUrls<listUrlx.size())){
            indexListUrls=0;
            boolean success=true;
            for (int i=0;i<listUrlx.size();i++){
                String filepathname=listUrlx.get(i).filePathName;
                File f=new File(filepathname);
                if (f.exists() && f.length()>200){
                }else{
                    success=false;
                }
            }
            if (success) {
                Logw("goon Done! ");
                return;
            }
        }
        Logw("goon ------"+indexListUrls);
        String filepathname=listUrlx.get(indexListUrls).filePathName;
        File f=new File(filepathname);
        while (f.exists() && f.length()>200){
            indexListUrls++;
            if (indexListUrls<listUrlx.size()){
                filepathname=listUrlx.get(indexListUrls).filePathName;
                f=new File(filepathname);
            }else{
                goon(); //again.
                return ;
            }
        }
        String url=listUrlx.get(indexListUrls).url;
        String encode=listUrlx.get(indexListUrls).encode;
        downTask task1 = new downTask(url,filepathname,encode);
        task1.start();
    }
    private void down_cb(Message msg){
        int result = msg.getData().getInt("result");
        String data = msg.getData().getString("data");
        Logw("down_cb ------["+indexListUrls+"], "+result);
        if (result==0){ //success
            indexListUrls++;
            if (indexListUrls<listUrlx.size()) {
                listUrlx.get(indexListUrls).nTry = 0;
            }
        }else{
            if (listUrlx.get(indexListUrls).nTry>1){
                listUrlx.get(indexListUrls).nTry--;
            }else if (indexListUrls < listUrlx.size()){
                indexListUrls++;
            }
        }
        countConnect++;
        if (countConnect>15) {
            countConnect=0;
            startTimer(10000);
        }
        goon();

    }
    //------
    private void startTimer(int seconds) {
        if (mTimer != null) {
            if (mTimerTask != null) {
                mTimerTask.cancel();//remove mTimerTask
            }
        }
        mTimerTask = new MyTimerTask(); // create new mTimerTask
        mTimer.schedule(mTimerTask, seconds); // 1s后执行task,经过10minutes再次执行
    }
    private Timer mTimer = new Timer(true);
    private MyTimerTask mTimerTask;
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            goon();
        }
    }
    //-----
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                down_cb(msg);
            }

        }
    };
    /**
     * 多线程文件下载
     */
    private void sendMessage(int result,int len,int maxlen,String filePathName,String comment,String data){
        Message msg = new Message();
        msg.what = 1;
        msg.getData().putInt("result",result);
        msg.getData().putInt("len", len);
        msg.getData().putInt("maxLen", maxlen);
        msg.getData().putString("filePathName", filePathName);
        msg.getData().putString("comment", comment);
        msg.getData().putString("data", data);
        mHandler.sendMessage(msg);
    }
    private void sendMessage(int what){
        Message msg = new Message();
        msg.what = 2;
        mHandler.sendMessage(msg);
    }
    class downTask extends Thread {
        private String urlD="";// 下载链接地址
        private String encode="utf-8";// "utf-8"/"gb2312"
        private String filePathName="";// 保存文件路径地址
        private int blocSize=1024;//
        public downTask(String downloadUrl, String filepath,String encode) {
            this.urlD = downloadUrl;
            this.filePathName = filepath;
            this.blocSize=248000;//
            this.encode = encode;
        }

        @Override
        public void run() {
            try {
                if (urlD.length()==0){
                    Logd("Error: downloadUrl is null! ");
                    return;
                }
                updateUI(urlD,0);
                //URL url = new URL("https://www.baidu.com/bd_logo1.png");
                URL url = new URL(urlD);
                HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
                conn1.setRequestMethod("GET");
                conn1.setInstanceFollowRedirects(false);
                int timeout = 20000;
                if (timeout > 0) {
                    conn1.setConnectTimeout(timeout);
                    conn1.setReadTimeout(timeout);
                }
                //Sets the flag indicating whether this URLConnection allows input. It cannot be set after the connection is established.
                conn1.setDoInput(true);
                InputStream in = null;
                int rspCode = conn1.getResponseCode();
                Logd( "+++++" + rspCode);
                if (rspCode == HttpURLConnection.HTTP_OK) {
                    updateUI(urlD,10);
                    in = conn1.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] arr = new byte[1024];
                    int len = 0;
                    while ((len = in.read(arr)) != -1) {
                        bos.write(arr, 0, len);
                    }
                    byte[] b = bos.toByteArray();
                    String ss = new String(b, encode);//"utf-8" "gb2312"
                    Logd( "==" + ss);
                    Logd( "==,filePathName=" + filePathName);
                    updateUI(urlD,100);
                    if (filePathName.length()>0){
                        OutputStreamWriter out = null;
                        out = new OutputStreamWriter(new FileOutputStream(filePathName), encode);
                        out.write(ss);
                        out.close();
                    }
                    sendMessage(0,100,100,"","",ss);

                }else{
                    updateUI(urlD,0);
                    sendMessage(1,100,100,"","","");
                }
                //关闭流
                if (in != null) {
                    in.close();
                }
            }  catch (Exception e) {
                Loge("[run] Error:"+e.getMessage());
                updateUI("[run] Error:"+e.getMessage(),0);
                sendMessage(1,100,100,"","","");
            }

        }
    }
    private void updateUI(String comment,int percent){
        Log.w(TAG,"11:"+comment);
        Intent intent = new Intent(ACTION_UPDATE_UI);
        intent.putExtra("type",1);
        intent.putExtra("comment",comment);
        intent.putExtra("percent",percent);//String value = intent.getStringExtra("type");
        mContext.sendBroadcast(intent);
    }
    private void updateUI(int type,String comment,int percent){
        Log.w(TAG,"22:"+comment+",percent:"+percent);
        Intent intent = new Intent(ACTION_UPDATE_UI);
        intent.putExtra("type",type);
        intent.putExtra("comment",comment);
        intent.putExtra("percent",percent);//String value = intent.getStringExtra("type");
        mContext.sendBroadcast(intent);
    }

    class cURLs {
        String type="";
        String url1="";
        String url2="";
        String url3="";
        String url4="";
        String url="";
        String encode="GB2312";//GB2312 //"utf-8"
        int nTry=0;
        int count=0;
        String filePathName="";
        JSONObject jsono;
        JSONArray jsona;
        int timeBegin=0;
        int timeEnd=0;
    }

    private void Logd(String str){
        Log.e(TAG, str);
    }
    private void Logw(String str){
        Log.w(TAG, str);
    }
    private void Loge(String str){
        Log.e(TAG, str);
    }


}
