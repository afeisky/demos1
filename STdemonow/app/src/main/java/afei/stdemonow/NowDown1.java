package afei.stdemonow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import afei.api.Global;
import afei.api.LogX;
import afei.api.TBK;

/**
 * Created by chaofei on 18-1-12.
 */

public class NowDown1 {

    private String TAG = "[NowDown]";
    public static String ACTION_ALARM="afei.stdemonow.alamtimer.ok";
    private ArrayList<String> listCode = new ArrayList<String>();
    private int nTryAgain=5;
    private Context mContext;
    private List<cURLs> listUrlx=new ArrayList<cURLs>();
    private int indexListUrls=0;
    private static int countConnect=0; //
    private SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat yyMMdd_HHmmss=new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //String url1="http://q.stock.sohu.com/hisHq?code=cn_0001008&start=20170901&end=20171013&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
    public String urlSohu1 = "http://q.stock.sohu.com/hisHq?code=cn_" ;
    String sn_url = "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
    String sse_url = "http://yunhq.sse.com.cn:32041/v1/sh1/list/exchange/equity?select=code%2Cname%2Copen%2Chigh%2Clow%2Clast%2Cprev_close%2Cchg_rate%2Cvolume%2Camount%2Ctradephase%2Cchange%2Camp_rate&order=&begin=0&end=5000&_=1501664482391";
    //private DbContentProvider contentProvider;
    public NowDown1(Context context){
        this.mContext=context;
        //get listcode?

        //listCode
        String snbk_now = Global.workPath + "/snbk_now";
        //String codelist= getCodeFile(workDir,snbk_now);
        cURLs u = new cURLs();
        //f=new File(codelist);
            try{
                u = new cURLs();
                u.type = "bknode";
                u.filenamekey = snbk_now+"/gnbk";
                u.codekey="gn_";
                u.url1 = sn_url;
                u.url2 = "[[\"bknode\",\"gainianbankuai\",\"\",0]]";
                u.url3 = "gnbk";
                u.url = u.url1 + u.url2;
                u.nTry = 0;
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "bknode";
                u.filenamekey = snbk_now+"/bknode";
                u.codekey="new_";
                u.url1 = sn_url;
                u.url2 = "[[\"bknode\",\"\",\"\",0]]";
                u.url3 = "node";
                u.url = u.url1 + u.url2;
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "bkshy";
                u.filenamekey = snbk_now+"/bkshy";
                u.codekey="hangye_";
                u.url1 = sn_url;
                u.url2 = "[[\"bkshy\",\"\",0]]";
                u.url3 = "shy";
                u.url = u.url1 + u.url2;
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "sse";
                u.filenamekey = snbk_now+"/sse";
                u.url1 = sse_url;
                u.url = u.url1;
                u.encode = "gb2312";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);

            } catch (Exception e) {
                e.printStackTrace();
            }

    }
    public void start2(){
        int index=0;
        for (cURLs u:listUrlx) {
            LogX.e( TAG,"start2:  "+index + "======" + index);
            downTask t = new downTask(u,index);
            t.start();
            index++;
            //break;
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
    public void start3(){
        String resultStr="";
        File f = Environment.getExternalStorageDirectory();
        String workDir = f.getAbsolutePath() + "/DownloadData";
        String dir = workDir + "/snbk_now1";
        f = new File(dir);
        File[] flist = f.listFiles();
        ArrayList sina_bk_gnbk = new ArrayList();
        ArrayList sina_bk_node = new ArrayList();
        ArrayList sina_bk_shy = new ArrayList();
        for (int i = 0; i < flist.length; i++) {
            //LogX.d( TAG,i + ":" + flist[i].getName()+" size:"+flist[i].length());
            String strname=flist[i].getName();
            String str=flist[i].getAbsolutePath();
            if (strname.indexOf("gnbk")==0){

                sina_bk_gnbk.add(str);
            }
            if (strname.indexOf("bknode")==0){
                sina_bk_node.add(str);
            }
            if (strname.indexOf("bkshy")==0){
                sina_bk_shy.add(str);
            }
        }
        Collections.sort(sina_bk_gnbk);
        Collections.sort(sina_bk_node);
        Collections.sort(sina_bk_shy);
        LogX.w( TAG,"sina_bk_gnbk.size() :"+ sina_bk_gnbk.size());
        LogX.w( TAG,"sina_bk_node.size() :"+ sina_bk_node.size());
        LogX.w( TAG,"sina_bk_shy.size() :"+ sina_bk_shy.size());
        if (sina_bk_gnbk.size()>0)
            for (int i=0;i<sina_bk_gnbk.size()&&isRunning;i++){
                String fn= sina_bk_gnbk.get(i).toString();
                Message msg = new Message();
                msg.getData().putString("filePathName", fn);
                msg.getData().putString("codekey", "gn_");
                msg.getData().putString("data", "");
                msg.getData().putString("time", fn.substring(fn.length()-5-13,fn.length()-5));//gnbk_180125_173236.json
                parseJson1(msg);

                //break;
            }
        if (sina_bk_node.size()>0)
            for (int i=0;i<sina_bk_node.size()&&isRunning;i++){
                String fn= sina_bk_node.get(i).toString();
                Message msg = new Message();
                msg.getData().putString("filePathName", fn);
                msg.getData().putString("codekey", "new_");
                msg.getData().putString("data", "");
                msg.getData().putString("time", fn.substring(fn.length()-5-13,fn.length()-5));//gnbk_180125_173236.json
                parseJson1(msg);
                //break;
            }
        if (sina_bk_shy.size()>0)
            for (int i=0;i<sina_bk_shy.size()&&isRunning;i++){
                String fn= sina_bk_shy.get(i).toString();
                Message msg = new Message();
                msg.getData().putString("filePathName", fn);
                msg.getData().putString("codekey", "hangye_");
                msg.getData().putString("data", "");
                msg.getData().putString("time", fn.substring(fn.length()-5-13,fn.length()-5));//gnbk_180125_173236.json
                parseJson1(msg);
                //break;
            }
    }
    private String getJsonString(String filepathname){
        String data="";
        //String filepathname=Global.workPath+"/snbk_now/gnbk_180125_174529.json";
        LogX.w(TAG,"get="+filepathname);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepathname)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            bf.close();
            data=stringBuilder.toString();
            return data;
        }catch (Exception e){
            LogX.e(TAG,"Error: downTask get ! "+e.getMessage());
        }
        return data;
    }

    private boolean parseJson1(Message msg){
        String filepathname = msg.getData().getString("filePathName");
        String codekey = msg.getData().getString("codekey");
        String data = "";//msg.getData().getString("data");
        String stime = msg.getData().getString("time");
        Date time;
        LogX.w(TAG,filepathname+"="+filepathname);
        LogX.w(TAG,data+"="+data);
        LogX.w(TAG,stime+"="+stime);
        if (codekey.length()==0){
            return false;
        }
        try {
            time=yyMMdd_HHmmss.parse(stime);
            stime = yyyy_mm_dd_hhmmss.format(yyMMdd_HHmmss.parse(stime));
            //Timestamp timestamp = new Timestamp(time.getTime());
        }catch (Exception e){
            LogX.e(TAG,"Error:"+e.getMessage());
            return false;
        }
        if (filepathname.length()>0){
            data=getJsonString(filepathname);
        }
        if (data==null || data.length()==0){
            return false;
        }
        try {
            JSONObject json;
            if (data.indexOf("[")==0) {
                JSONArray json1 = new JSONArray(data);
                json = json1.getJSONObject(0);
            }else{
                json = new JSONObject(data);
            }
            //LogX.w(TAG,json.toString());
            JSONArray jsonItems = json.getJSONArray("items");
            //LogX.d(TAG,"json lines:" + jsonItems.length());
            //NumberFormat nf1 = new DecimalFormat("#,####,####");
            String date = "";
            String bkname = "";
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONArray item = jsonItems.getJSONArray(i);
                String a1 = item.getString(0).replace("\n", "");
                String a2 = item.getString(1);
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
                a10 = a10.substring(2, a10.length());
                String a11 = item.getString(10);
                double a12 = item.getDouble(11);
                double a13 = item.getDouble(12);
                double a14 = item.getDouble(13);

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

            }
            Global.getContentProvider().queryBK1(codekey,stime,0);
        }catch (Exception e){
            LogX.e(TAG,"[run] Error2:"+e.getMessage());
            sendMessage(1,100,100,"","","");
        }
        return true;
    }
    class downTask extends Thread {
        private int blocSize = 248000;
        private cURLs u;
        private int index=0;
        public downTask(cURLs u,int index) {
            try {
                this.index=index;
                this.u=u;
            } catch (Exception e) {
                LogX.e(TAG,"Error: downTask init fail! ["+index+"]" + e.getStackTrace().toString());
            }
        }

        @Override
        public void run() {
            if (u == null) {
                return;
            }
            String timeNow=yyMMdd_HHmmss.format(new Date());
            u.filePathName=u.filenamekey+"_"+timeNow+".json";
            //u.filePathName=u.filenamekey+"_180129_110536.json";
            LogX.w(TAG,"u.filePathName="+u.filePathName);
            LogX.w(TAG,"u.url="+u.url);
            updateUI(u.url,0);
            if (true){
                //sendMsg_json1(3,u.filePathName,"afdsfsdf",u.codekey,timeNow);
                //return;
            }
            try {
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
                LogX.d( TAG,"+++++" + rspCode);
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
                    LogX.d( TAG,"==" + ss.substring(0,(ss.length()>100)?100:0));
                    LogX.d( TAG,"==,filePathName=" + u.filePathName);
                    //updateUI(u.url,100);
                    updateUI(u.filePathName,100);
                    //if (u.filePathName.length()>0){
                        OutputStreamWriter out = null;
                        out = new OutputStreamWriter(new FileOutputStream(u.filePathName), u.encode);
                        out.write(ss);
                        out.close();
                    //}
                    if (!u.type.equalsIgnoreCase("sse")) {
                        //parseJson1(ss, timeNow);
                        //parse1();
                        sendMsg_json1(3,u.filePathName,ss,u.codekey,timeNow);
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
                LogX.e(TAG,"[run] Error1:"+e.getMessage());
                updateUI("[run] Error1:"+e.getMessage(),0);
                sendMessage(1,100,100,"","","");
            }
        }

        private void parse1111(){
            TBK bk=new TBK();
            ContentValues values = new ContentValues();
            values.put(TBK.COLUMN_CODE, "00001b");
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
            Global.getContentProvider().insertBK(values);
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
            if (msg.what == 1) {

            }else if (msg.what == 3) {
                parseJson1(msg);
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
    private void sendMsg_json1(int result,String filePathName,String data,String codekey,String time){
        Message msg = new Message();
        msg.what = 3;
        msg.getData().putString("filePathName", filePathName);
        msg.getData().putString("codekey", codekey);
        msg.getData().putString("data", data);
        msg.getData().putString("time", time);
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
