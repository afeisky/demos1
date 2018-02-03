package afei.demo.stdemohistory;

import android.content.Context;
import android.content.Intent;
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

public class NowDown {

    private String TAG = "[NowDown]";
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
    String sn_url = "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
    String sse_url = "http://yunhq.sse.com.cn:32041/v1/sh1/list/exchange/equity?select=code%2Cname%2Copen%2Chigh%2Clow%2Clast%2Cprev_close%2Cchg_rate%2Cvolume%2Camount%2Ctradephase%2Cchange%2Camp_rate&order=&begin=0&end=5000&_=1501664482391";

    public void start(Context context){
        this.mContext=context;

        //get listcode?

        //listCode

        File f = Environment.getExternalStorageDirectory();
        String workDir = f.getAbsolutePath() + "/DownloadData";
        f = new File(workDir);
        if (!f.exists()) {
            f.mkdir();
        }
        String snbk_now = f.getAbsolutePath() + "/snbk_now";
        Logw("dir="+snbk_now);
        f = new File(snbk_now);
        if (!f.exists()) {
            f.mkdir();
        }
        //String codelist= getCodeFile(workDir,snbk_now);
        cURLs u = new cURLs();
        //f=new File(codelist);
            try{

                u = new cURLs();
                u.type = "bknode";
                u.filenamekey = snbk_now+"gnbk";
                u.url1 = sn_url;
                u.url2 = "[[\"bknode\",\"gainianbankuai\",\"\",0]]";
                u.url3 = "gnbk";
                u.nTry = 0;
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "bknode";
                u.filenamekey = snbk_now+"/bknode";
                u.url1 = sn_url;
                u.url2 = "[[\"bknode\",\"\",\"\",0]]";
                u.url3 = "node";
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "bkshy";
                u.filenamekey = snbk_now+"bkshy";
                u.url1 = sn_url;
                u.url2 = "[[\"bkshy\",\"\",0]]";
                u.url3 = "shy";
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "sse";
                u.filenamekey = snbk_now+"sse";
                u.url1 = sse_url;
                u.encode = "gb2312";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                goon();
            } catch (Exception e) {
                e.printStackTrace();
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
        downTask task1 = new downTask();
        task1.start();
    }
    private void down_cb(Message msg){
        int result = msg.getData().getInt("result");
        String data = msg.getData().getString("data");
        Logw("down_cb ------["+indexListUrls+"], "+result);
        indexListUrls++;
        //countConnect++;
        //if (countConnect>15) {
        //    countConnect=0;
        //    startTimer(10000);
        //}
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
        private String comment="";
        private int blocSize=248000;
        private cURLs u=null;
        public downTask(){
            try {
                //while (true) {
                    u = listUrlx.get(indexListUrls);
                    if (u == null) {
                        return;
                    }
                    String url = "";
                    if (u.type.equals("bknode") || u.type.equals("bkshy")) {
                        u.url = u.url1 + u.url2;
                        ///if (!(time1>=u.timeBegin || time1<=u.timeEnd)){
                        ///    nListUrls++;//go next
                        //}
                        //break;
                    } else if (u.type.equals("sse")) {
                        u.url = u.url1;
                        ///if (!(time1>=u.timeBegin || time1<=u.timeEnd)){
                        ///    nListUrls++;//go next
                        ///}
                        //break;
                    }

                //}
            }catch (Exception e){
                Loge("Error: downTask init fail!"+e.getStackTrace().toString());
            }
        }
        @Override
        public void run() {
            try {
                if (u.url.length()==0){
                    Logd("Error: downloadUrl is null! ");
                    return;
                }
                String timeNow=new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
                u.filePathName=u.filenamekey+"_"+timeNow+".json";
                updateUI(u.url,0);
                //URL url = new URL("https://www.baidu.com/bd_logo1.png");
                URL url = new URL(u.url);
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
                    updateUI(u.url,10);
                    in = conn1.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] arr = new byte[1024];
                    int len = 0;
                    while ((len = in.read(arr)) != -1) {
                        bos.write(arr, 0, len);
                    }
                    byte[] b = bos.toByteArray();
                    String ss = new String(b, u.encode);//"utf-8" "gb2312"
                    Logd( "==" + ss);
                    Logd( "==,filePathName=" + u.filePathName);
                    updateUI(u.url,100);
                    if (u.filePathName.length()>0){
                        OutputStreamWriter out = null;
                        out = new OutputStreamWriter(new FileOutputStream(u.filePathName), u.encode);
                        out.write(ss);
                        out.close();
                    }
                    sendMessage(0,100,100,"","",ss);

                }else{
                    updateUI(u.url,0);
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
        String filenamekey="";
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
