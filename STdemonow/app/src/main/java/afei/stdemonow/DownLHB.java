package afei.stdemonow;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import afei.api.FileX;
import afei.api.Global;
import afei.api.LogX;
import afei.api.TBK;
import afei.api.TSSE;

/**
 * Created by chaofei on 18-1-12.
 */

public class DownLHB {
    private String TAG = "[DownLHB]";
    private ArrayList<String> listCode = new ArrayList<String>();
    private int nTryAgain=5;
    private Context mContext;
    private List<cURLs> listUrlx=new ArrayList<cURLs>();
    private int indexListUrls=0;
    private static int countConnect=0; //
    private SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat yyMMdd_HHmmss=new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat yyyyMMdd_HHmmss=new SimpleDateFormat("yyyyMMdd_HHmmss");
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String dirlhb="";
    public DownLHB(Context context){
        this.mContext=context;
    }
    public void start2() {
        if (init_dir()) {
            init_date();
            goon();
        }
    }

    private boolean init_dir(){
        if (Global.workPath.length()==0){
            File f = Environment.getExternalStorageDirectory();
            Global.workPath = f.getAbsolutePath() + Global.WORK_DIR;
            f = new File(Global.workPath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        if (Global.workPath.length()==0){
            LogX.e(TAG,"Error: no found workDir.");
            return false;
        }
        dirlhb = Global.workPath + "/download_lhb";
        File f = new File(dirlhb);
        if (!f.exists()) {
            f.mkdir();
        }

        return true;
    }

    private void init_date(){
        cURLs u = new cURLs();
        String hdate="2018-02-09";
        indexListUrls=0;
        try {
            Date date=yyyy_mm_dd.parse(hdate);
            String sdate=new SimpleDateFormat("yyMMdd").format(date);

            u = new cURLs();
            u.type = "lhb_c";
            u.filePathName = dirlhb + "/" + u.type+"_"+hdate+".txt";
            u.url = "http://www.szse.cn/szseWeb/common/szse/files/text/nmTxt/gk/nm_jy"+sdate+".txt";;
            u.encode = "gb2312";
            u.count = 0;
            u.nTry = 0;
            listUrlx.add(u);
            u = new cURLs();
            u.type = "lhb_zx";
            u.filePathName = dirlhb + "/" + u.type+"_"+hdate+".txt";
            u.url= "http://www.szse.cn/szseWeb/common/szse/files/text/smeTxt/gk/sme_jy"+sdate+".txt";;
            u.encode = "gb2312";
            u.count = 0;
            u.nTry = 0;
            listUrlx.add(u);
            u = new cURLs();
            u.type = "lhb_sa";
            u.filePathName = dirlhb + "/" + u.type+"_"+hdate+".txt";
            u.url = "http://www.szse.cn/szseWeb/common/szse/files/text/jy/jy"+sdate+".txt";;
            u.encode = "gb2312";
            u.count = 0;
            u.nTry = 0;
            listUrlx.add(u);
            u = new cURLs();
            u.type = "lhb_ha";
            u.filePathName = dirlhb + "/" + u.type+"_"+hdate+".txt";
            u.url = "http://query.sse.com.cn/infodisplay/showTradePublicFile.do?jsonCallBack=jsonpCallback35256&isPagination=false&dateTx="+hdate;//https://query.sse.com.cn/infodisplay/showTradePublicFile.do?jsonCallBack=jsonpCallback&isPagination=false&dateTx="+hdate;//dateTx=2018-02-08
            u.encode = "gb2312";
            u.count = 0;
            u.nTry = 0;
            listUrlx.add(u);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goon(){
        if (indexListUrls<listUrlx.size()) {
            LogX.w(TAG, "goon: [" + indexListUrls);
            downTask t = new downTask();
            t.start();
        }else{
            LogX.w(TAG, "Finish!");
        }
    }

    private boolean isRunning=false;
    public void stop(){
        isRunning=false;
    }
    public void start(){
        isRunning=true;
             new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isRunning) {
                            start2();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    private boolean callback(Message msg,int flag){
        String filepathname = listUrlx.get(indexListUrls).filePathName;
        String data=null;
        try{
            if (filepathname.length()>0){
                data= FileX.readLines(filepathname,"utf-8");
            }
            if (data==null || data.length()==0){
                return false;
            }
        }catch (Exception e){
            LogX.e(TAG,"Error: [parseJsonLhb] "+e.getMessage());
        }
        return true;
    }

    class downTask extends Thread {
        private int blocSize = 248000;
        private cURLs u;

        public downTask() {
        }

        @Override
        public void run() {
            try {
                //URL url = new URL("https://www.baidu.com/bd_logo1.png");
                cURLs u=listUrlx.get(indexListUrls);
                URL url = new URL(u.url);
                HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
                conn1.setRequestMethod("GET");
                conn1.setInstanceFollowRedirects(false);
                int timeout = 20000;
                if (timeout > 0) {
                    conn1.setConnectTimeout(timeout);
                    conn1.setReadTimeout(timeout);
                }
                LogX.d(TAG, "" + u.url);
                //Sets the flag indicating whether this URLConnection allows input. It cannot be set after the connection is established.
                conn1.setDoInput(true);
                InputStream in = null;
                int rspCode = conn1.getResponseCode();
                LogX.d(TAG, "+++++" + rspCode);
                if (rspCode == HttpURLConnection.HTTP_OK) {

                    in = conn1.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] arr = new byte[1024];
                    int len = 0;
                    while ((len = in.read(arr)) != -1) {
                        bos.write(arr, 0, len);
                    }
                    byte[] b = bos.toByteArray();
                    String ss = new String(b, u.encode);//"utf-8" "gb2312"
                    LogX.d(TAG, "==" + ss.substring(0, (ss.length() > 100) ? 100 : 0));
                    LogX.d(TAG, "filePathName=" + u.filePathName);
                    OutputStreamWriter out = null;
                    out = new OutputStreamWriter(new FileOutputStream(u.filePathName), u.encode);
                    out.write(ss);
                    out.close();
                    sendMessage(0);
                } else {
                    sendMessage(1);
                }
                //关闭流
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                LogX.e(TAG, "[run] Error1:" + e.getMessage());
                sendMessage(1);
            }
        }

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

        }
    }
    //-----
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

                LogX.d(TAG, "1111");
                callback(msg,msg.what);//
                indexListUrls++;
                goon();


        }
    };
    private void sendMessage(int what){
        Message msg = new Message();
        msg.what =what;
        mHandler.sendMessage(msg);
    }
    private void updateUI(String comment,int percent){
        LogX.w(TAG,"11:"+comment);
        Intent intent = new Intent(BootupReceiver.ACTION_UPDATE_UI);
        intent.putExtra("type",1);
        intent.putExtra("comment",comment);
        intent.putExtra("percent",percent);//String value = intent.getStringExtra("type");
        mContext.sendBroadcast(intent);
    }
    private void updateUI(int type,String comment,int percent){
        LogX.w(TAG,"22:"+comment+",percent:"+percent);
        Intent intent = new Intent(BootupReceiver.ACTION_UPDATE_UI);
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
        String encode="utf-8";//GB2312 //"utf-8"
        int nTry=0;
        int count=0;
        String filePathName="";
        String filenamekey="";
        String codekey="";
        JSONObject jsono;
        JSONArray jsona;
        int timeBegin=0;
        int timeEnd=0;
    }
    

}
