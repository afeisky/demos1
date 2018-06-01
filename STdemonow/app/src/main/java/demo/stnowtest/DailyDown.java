package demo.stnowtest;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import demo.api.Global;
import demo.api.LogX;

import demo.api.MailCmd;
import demo.api.TimeX;
import demo.api.ZipUtils;



/**
 * Created by chaofei on 17-12-7.
 */

public class DailyDown{
    private String TAG = "[DailyDown]";

    private String dirpath = "";
    private int blocSize = 248000;//

    private SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String sn_url = "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
    String sse_url = "http://yunhq.sse.com.cn:32041/v1/sh1/list/exchange/equity?select=code%2Cname%2Copen%2Chigh%2Clow%2Clast%2Cprev_close%2Cchg_rate%2Cvolume%2Camount%2Ctradephase%2Cchange%2Camp_rate&order=&begin=0&end=5000&_=1501664482391";

    JSONObject json = null;
    private int LISTURLS_LEN=10;

    private List<cURLs> listUrlx=new ArrayList<cURLs>();
    private int nListUrls=0;
    private String g_url_date = "";
    private Date dateStartRun;
    private Date dateEndRun;
    public static boolean isRunning=false;
    private boolean isFinish=false;
    private Context mContext;
    public DailyDown(Context mContext){
        this.mContext=mContext;
        this.isRunning=false;
    }

    public void stop() {
        isRunning=false;
    }
    public boolean isRunning() {
        return isRunning;
    }

    private int timerCount=0;

    private void finish(){
        //Intent intent = new Intent(BootupReceiver.ACTION_END);
        //mContext.sendBroadcast(intent);
    }
    private boolean hasDown=false;
    private SimpleDateFormat HHmm = new SimpleDateFormat("HHmm");
    private boolean needDown(){
        String timenow=HHmm.format(new Date());
        int t= Integer.valueOf(timenow);
        if ((t<800 || t>1815)){
            if (!hasDown){
                return true;
            }
        }else{
            hasDown=false;
        }
        return false;
    }

    public void start1(String dir) {
        LogX.d(TAG,"start --->");
        if (!needDown())
            return;
        try {
            listUrlx.clear();
            nListUrls=0;
            int n = 0;
            cURLs u = null;
            if (false) {
                u = new cURLs();
                u.type = "lhb_c";
                u.url1 = "http://www.szse.cn/main/chinext/jygkxx/jygkxx/";
                u.encode = "utf-8";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "lhb_zx";
                u.url1 = "http://www.szse.cn/main/disclosure/news/scgkxx/";
                u.encode = "utf-8";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "lhb_sa";
                u.url1 = "http://www.szse.cn/main/sme/jytj/jygkxx/";
                u.encode = "utf-8";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
                u = new cURLs();
                u.type = "lhb_ha";
                u.url1 = "http://www.sse.com.cn/disclosure/diclosure/public/";
                u.encode = "utf-8";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
            }else {

                u = new cURLs();
                u.type = "sn";
                u.url1 = sn_url;
                u.url2 = "[[\"hq\",\"hs_a\",\"\",0,";
                u.url3 = ",500]]";
                u.url4 = "hsa";
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                u.jsono = new JSONObject("{\"code\":0,\"day\":\"\",\"count\":0,\"fields\":[],\"items\":[],\"n\":0}");
                listUrlx.add(u);
                u = new cURLs();
                u.type = "sn";
                u.url1 = sn_url;
                u.url2 = "[[\"hq\",\"sz_a\",\"\",0,";
                u.url3 = ",500]]";
                u.url4 = "sza";
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                u.jsono = new JSONObject("{\"code\":0,\"day\":\"\",\"count\":0,\"fields\":[],\"items\":[],\"n\":0}");
                listUrlx.add(u);
                u = new cURLs();
                u.type = "sn";
                u.url1 = sn_url;
                u.url2 = "[[\"hq\",\"hs_b\",\"\",0,";
                u.url3 = ",500]]";
                u.url4 = "hsb";
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                u.jsono = new JSONObject("{\"code\":0,\"day\":\"\",\"count\":0,\"fields\":[],\"items\":[],\"n\":0}");
                listUrlx.add(u);
                u = new cURLs();
                u.type = "sn";
                u.url1 = sn_url;
                u.url2 = "[[\"hq\",\"sz_b\",\"\",0,";
                u.url3 = ",500]]";
                u.url4 = "szb";
                u.count = 0;
                u.timeBegin = 1805;
                u.timeEnd = 800;
                u.nTry = 0;
                u.jsono = new JSONObject("{\"code\":0,\"day\":\"\",\"count\":0,\"fields\":[],\"items\":[],\"n\":0}");
                listUrlx.add(u);
                u = new cURLs();
                u.type = "bknode";
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
                u.url1 = sse_url;
                u.encode = "gb2312";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
            }
            for (cURLs item : listUrlx) {
                LogX.d(TAG,item.type + item.url1 + item.url2 + item.url3);
            }

            dirpath = dir+"/daily";
            File f = new File(dirpath);
            if (!f.exists()) {
                f.mkdir();
            }
            if (!f.exists()) {
                LogX.d(TAG,"Error:exists " + f.getAbsolutePath());
                return;
            }
            if (!f.canRead() && !f.canWrite()) {
                LogX.d(TAG,"Error:no permission " + f.getAbsolutePath());
                return;
            }
            if (false) {
                File[] flist = f.listFiles();
                int i = 0;
                if (flist != null) {
                    if (flist.length > 6)
                        i = flist.length - 6;
                    for (; i < flist.length; i++) {
                        LogX.d(TAG, i + ":" + flist[i].getName() + " size:" + flist[i].length());
                        //show_callback(i + ":" + flist[i].getName()+" size:"+flist[i].length());
                    }
                }
            }
            //write file:
            nListUrls = 0;
            g_url_date="";
            isRunning = true;
            show_callback("-------" + ymdhms.format(new Date()));
            dateStartRun=new Date();
            goon();

        }catch (Exception e){
            LogX.e(TAG,"[start]Error:"+e.getMessage());
        }
    }
    public boolean checkRunlongtime(){
        dateEndRun=new Date();
        long from =dateStartRun.getTime();
        long to = dateEndRun.getTime();
        int minutes = (int) ((to - from)/(1000 * 60));
        if (minutes>60){
            isRunning=false;
            g_url_date="";
            return true;
        }
        return false;
    }
    private void ss(JSONArray one) {
        try{
            String url = "";
            if (one.getString(0).equals("sn")) {
                int count = 0;
                url += one.getString(1) + one.getString(2) + count + one.getString(3);
            }
        }catch (Exception e){

        }
    }

    private boolean json_exist(JSONArray items,JSONArray jo){
        try {
            for (int i = 0; i < items.length(); i++) {
                JSONArray jitem = items.getJSONArray(i);
                if (jitem.getString(1).equalsIgnoreCase(jo.getString(1))) {
                    return true;
                }
            }
        }catch (Exception e){
            LogX.e(TAG,"Error: json_exist");
        }
        return false;
    }
    private void goon(){
        LogX.d(TAG,"goon()");
        if (!isRunning) {
            show_callback_finish("Cancel!");
            finish();
            //startTimer(18000);
            return; // exit DailyDown!
        }
        LogX.d(TAG," start: nListUrls=" + nListUrls+"/"+listUrlx.size());
        if (nListUrls>=listUrlx.size()){
            zipFile();
            show_callback_finish("Finish");
            isFinish=true;
            //start();
            isRunning=false;
            return;
        }

        cURLs u=listUrlx.get(nListUrls);
        LogX.d(TAG," start: nListUrls=" + nListUrls+":"+u.nTry);
        //downTask task1 = new downTask();
        //task1.start();
        down_1();
    }

    private void do1(int result,String data){
        try {
            //int result = msg.getData().getInt("result");
            //String data = msg.getData().getString("data");
            cURLs u=listUrlx.get(nListUrls);
            LogX.d(TAG," result:["+result+"], nListUrls=" + nListUrls+":"+u.nTry);
            u.nTry++;
            if (result == 0) {
                if (u.type.equals("sn")){
                    String day="";
                    if (data.indexOf("{\"code\":") == 0) {
                        LogX.d(TAG,"=" + data);
                        JSONObject js =new JSONObject(data);
                        int code = json.getInt("code");
                        String m = json.getString("msg");
                        LogX.d(TAG,"Error:" + "[" + code + "] " + m);
                    } else {
                        JSONArray jsonData = new JSONArray(data);
                        JSONObject json = jsonData.getJSONObject(0);
                        JSONArray jsonStat = json.getJSONArray("fields");
                        JSONArray jsonItems = json.getJSONArray("items");
                        int json_n=u.jsono.getInt("n");
                        int json_count=u.jsono.getInt("count");
                        if (jsonItems.length() == 0) {
                            json_n= u.jsono.getJSONArray("items").length(); //u.jsono.getInt("n");
                            json_count=u.jsono.getInt("count");
                            LogX.d(TAG, "json_count:" + "[" + u.jsono.getInt("n") + "/" + u.jsono.getInt("count"));
                            LogX.d(TAG, "json_count:" + "[" + json_n + "/" + u.jsono.getInt("count"));
                            if ((json_n == 0) || (json_n > 0 &&  json_n<json_count)) {
                                u.count--;
                            }
                        }else {
                            if (u.jsono.getInt("count") == 0) {
                                u.jsono.put("count", json.getInt("count"));
                            }
                            if (u.jsono.getString("day").length() == 0) {
                                u.jsono.put("day", json.getString("day"));
                                u.jsono.put("count", json.getInt("count"));
                            } else {
                                if (!u.jsono.getString("day").equals(json.getString("day"))) {
                                    LogX.d(TAG, "Error: " + u.jsono.getString("day") + " =?" + json.getString("day"));
                                }
                            }

                            u.jsono.put("fields", json.getJSONArray("fields"));
                            int n = 0;
                            JSONArray jsonItems1 = u.jsono.getJSONArray("items");
                            //jsonItems1.add(jsonItems);
                            for (int k = 0; k < jsonItems.length(); k++) {
                                JSONArray jo = jsonItems.getJSONArray(k);
                                if (!json_exist(jsonItems1, jo)) {
                                    jsonItems1.put(jo);
                                }
                            }
                            //save file:
                            day = u.jsono.getString("day");
                            g_url_date = day;
                            String filejsons = dirpath +"/sina_bk_" + u.url4 + "_" + day + "_" + u.count+ ".json";
                            String filejson = dirpath + "/sina_bk_" + u.url4 + "_" + day + ".json";
                            File fj=new File(filejson);
                            u.filePathName=filejson;
                            if (fj.exists() && fj.length()>10000) {
                                show_callback("Exist:" + u.url);
                                if (checkSendMail()){
                                    nListUrls=listUrlx.size();
                                }
                                nListUrls++;
                                goon();
                                return;
                            }
                            //FileOutputStream out = new FileOutputStream(filejsons);
                            //out.write(json.toString().getBytes());
                            //out.close();
                        }
                    }
                    day = u.jsono.getString("day");
                    LogX.d(TAG, "day:" + day);
                    int n = u.jsono.getJSONArray("items").length();
                    LogX.d(TAG, "json count:"  + n + "/" + u.jsono.getInt("count"));
                    if (day.length() > 0 && n==u.jsono.getInt("count")) {
                        u.jsono.put("n", n);
                        g_url_date = day;
                        String filejson = dirpath + "/sina_bk_" + u.url4 + "_" + day + ".json";
                        String filetxt = dirpath + "/sina_bk_" + u.url4 + "_" + day + ".txt";
                        try {
                            LogX.d(TAG, "->" +u.jsono.toString().getBytes());
                            show_callback("Success:" + u.url);
                            FileOutputStream out = new FileOutputStream(filejson);
                            out.write(u.jsono.toString().getBytes());
                            out.close();
                            u.filePathName=filejson;
                            //out = new FileOutputStream(filetxt);
                            //out.write(lista.toString().getBytes());
                            //out.close();
                            nListUrls++;
                        } catch (Exception e) {
                            LogX.d(TAG, "Error1: " +e.getMessage());
                        }
                    }else{
                        //if (u.nTry<1000) {
                            u.count++;
                        //}else{
                        //    nListUrls++;
                        //}
                    }
                    goon();
                }else if (u.type.equals("bknode") || u.type.equals("bkshy")) { //
                    if (data.indexOf("{\"code\":") == 0) {
                        LogX.d(TAG,"=" + data);
                        JSONObject js =new JSONObject(data);
                        int code = json.getInt("code");
                        String m = json.getString("msg");
                        LogX.d(TAG,"Error:" + "[" + code + "] " + m);
                    } else {
                        JSONArray jsonData = new JSONArray(data);// JSONArray.fromObject(data);
                        JSONObject json = jsonData.getJSONObject(0);
                        //JSONArray jsonStat = json.getJSONArray("fields");
                        JSONArray jsonItems = json.getJSONArray("items");
                        if (jsonItems.length() == 0) {
                            goon();
                            return;
                        }
                        json.put("day", ymdhms.format(new Date()));
                        String day = "";
                        if (g_url_date.length() > 0) {
                            day = g_url_date;
                        } else {
                            day = ymd.format(new Date());
                            g_url_date=day;
                        }
                        String filejson = dirpath + "/sina_bk_" + u.url3 + "_" + day + ".json";
                        File fj=new File(filejson);
                        if (false){//(fj.exists() && fj.length()>5000) {
                            show_callback("Exist:" + u.url);
                            nListUrls++;
                            goon();
                            return;
                        }
                        show_callback("Success:" + u.url);
                        FileOutputStream out = new FileOutputStream(filejson);
                        out.write(json.toString().getBytes());
                        out.close();
                        u.filePathName=filejson;
                        nListUrls++;
                    }
                    goon();
                }else if (u.type.equals("sse")) {
                    JSONObject json = new JSONObject(data);
                    String date = json.get("date").toString();
                    String time = json.get("time").toString();
                    String total = json.get("total").toString();
                    JSONArray list = json.getJSONArray("list");
                    Date d = new SimpleDateFormat("yyyyMMdd hhmmss").parse(date + " " + time); //"20171009 151509"
                    String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
                    LogX.d(TAG,"sse_"+date + "," + time + "," + create_time + "," + total);
                    String datetime = create_time;
                    String sql = "";
                    int count = 0;
                    String filejson = dirpath + "/" +"sse_"+ date+ ".json";
                    FileOutputStream out = new FileOutputStream(filejson);
                    out.write(json.toString().getBytes());
                    out.close();
                    show_callback("Success:" + u.url);
                    u.filePathName=filejson;
                    nListUrls++;
                    goon();
                }else if (u.type.substring(0,4).equals("lhb_")) {

                    String stime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    LogX.d(TAG,u.type+"_"+stime + "," + create_time + ",");
                    String datetime = create_time;
                    String sql = "";
                    int count = 0;
                    String filejson = dirpath + "/" +u.type+"_"+ stime+ ".html";
                    FileOutputStream out = new FileOutputStream(filejson);
                    out.write(data.getBytes());
                    out.close();
                    show_callback("Success:" + u.url);
                    u.filePathName=filejson;
                    nListUrls++;
                    goon();
                }

            } else {
                goon();
            }

        }catch (Exception e){
            LogX.e(TAG,"Error2!!!!:"+e.getMessage());
            goon();
        }
    }
    /*
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
            Message message = new Message();
            message.what = 2;
            mHandler.sendMessage(message);
        }
    }
    //private Handler mHandler = new Handler() {
     //   @Override
    //    public void handleMessage(Message msg) {
    //        if (msg.what == 1) {
    //            do1(msg);
    //        }if (msg.what == 2) {
    //            start1(dirpath);
    //        }

    //    }
    //};


     // 多线程文件下载

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
*/
    private void down_1(){
        int ret=1;
            cURLs u=null;
            while (true) {//check timeBegin->timeEnd,
                u = listUrlx.get(nListUrls);
                if (u == null) {
                    return;//exit;
                }
                String timeNow=new SimpleDateFormat("HHmm").format(new Date());
                int time1=Integer.valueOf(timeNow);
                String url = "";
                if (u.type.equals("sn")) {
                    u.url = u.url1 + u.url2 + u.count + u.url3;
                    //if (!(time1>=u.timeBegin || time1<=u.timeEnd)){
                    //    nListUrls++;//go next
                    //}
                    break;
                } else if (u.type.equals("bknode") || u.type.equals("bkshy")) {
                    u.url = u.url1 + u.url2;
                    ///if (!(time1>=u.timeBegin || time1<=u.timeEnd)){
                    ///    nListUrls++;//go next
                    //}
                    break;
                } else if (u.type.equals("sse")) {
                    u.url = u.url1;
                    ///if (!(time1>=u.timeBegin || time1<=u.timeEnd)){
                    ///    nListUrls++;//go next
                    ///}
                    break;
                } else if (u.type.substring(0,4).equals("lhb_")) {
                    u.url = u.url1;
                    ///if (!(time1>=u.timeBegin || time1<=u.timeEnd)){
                    ///    nListUrls++;//go next
                    ///}
                    String stime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    LogX.d(TAG,u.type+"_"+stime + "," + create_time + ",");
                    break;
                }
            }

            String comment=u.url.substring(u.url.length()-30,u.url.length())+" --> " +u.nTry;
        String urlD = u.url;
        String encode=u.encode;
            int nTry=u.nTry;
        String filePathName = "";
            int blocSize = 248000;//
        String ss="";
        try {

            if (urlD.length()==0){
                LogX.d(TAG,"Error: downloadUrl is null! ");
                return;//exit
            }
            show_callback(comment,0);
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
            LogX.d(TAG, "+++++" + rspCode);
            //conn1=reload(conn1);
            if (rspCode == HttpURLConnection.HTTP_OK) {
                show_callback(comment,10);
                in = conn1.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] arr = new byte[1024];
                int len = 0;
                while ((len = in.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                }
                byte[] b = bos.toByteArray();
                ss = new String(b, encode);//"utf-8" "gb2312"
                LogX.d( TAG,"==" + ss.substring(0,(ss.length()>100)?100:0));//LogX.d(TAG, "==" + ss);
                ret=0;
            }else{
            }
            //关闭流
            if (in != null) {
                in.close();
            }
        }  catch (Exception e) {
            LogX.e(TAG,"[run] Error:"+e.getStackTrace());
            e.printStackTrace();
        }finally {
            do1(ret,ss);
        }

    }



    public String getNewFile(){
        String resultStr="";
        File f = Environment.getExternalStorageDirectory();
        String dir = dirpath;// + "/download_sina/sina_bk";
        f = new File(dir);
        File[] flist = f.listFiles();
        ArrayList sina_bk_hsa = new ArrayList();
        ArrayList sina_bk_hsb = new ArrayList();
        ArrayList sina_bk_sza = new ArrayList();
        ArrayList sina_bk_szb = new ArrayList();
        ArrayList sina_bk_gnbk = new ArrayList();
        ArrayList sina_bk_node = new ArrayList();
        ArrayList sina_bk_shy = new ArrayList();
        for (int i = 0; i < flist.length; i++) {
            LogX.d(TAG, i + ":" + flist[i].getName()+" size:"+flist[i].length());
            String str=flist[i].getName();
            if (str.indexOf("sina_bk_hsa")==0){
                sina_bk_hsa.add(str);
            }
            if (str.indexOf("sina_bk_hsb")==0){
                sina_bk_hsb.add(str);
            }
            if (str.indexOf("sina_bk_sza")==0){
                sina_bk_sza.add(str);
            }
            if (str.indexOf("sina_bk_szb")==0){
                sina_bk_szb.add(str);
            }
            if (str.indexOf("sina_bk_gnbk")==0){
                sina_bk_gnbk.add(str);
            }
            if (str.indexOf("sina_bk_node")==0){
                sina_bk_node.add(str);
            }
            if (str.indexOf("sina_bk_shy")==0){
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

        resultStr+=sina_bk_hsa.get(sina_bk_hsa.size()-1)+"\n";
        resultStr+=sina_bk_hsb.get(sina_bk_hsb.size()-1)+"\n";
        resultStr+=sina_bk_sza.get(sina_bk_sza.size()-1)+"\n";
        resultStr+=sina_bk_szb.get(sina_bk_szb.size()-1)+"\n";
        resultStr+=sina_bk_gnbk.get(sina_bk_gnbk.size()-1)+"\n";
        resultStr+=sina_bk_node.get(sina_bk_node.size()-1)+"\n";
        resultStr+=sina_bk_shy.get(sina_bk_shy.size()-1)+"\n";

        f = new File(dirpath);
        ArrayList sse_file = new ArrayList();
        flist = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            String str=flist[i].getName();
            if (str.indexOf("sse_")==0) {
                sse_file.add(str);
            }
        }
        if (sse_file!=null && sse_file.size()>0 ) {
            Collections.sort(sse_file);
            resultStr += sse_file.get(sse_file.size()-1)+"\n";
        }

        LogX.d(TAG,""+resultStr);
        return resultStr;
    }

    private String mailFileName="";
    private String zipFilePathName="";
    private boolean zipFile(){
        mailFileName="stdaily" + g_url_date;
        zipFilePathName=dirpath + "/"+mailFileName+ ".zip";
        File fzip=new File(zipFilePathName);
        if (!fzip.exists()){
            List<File> files = new ArrayList<File>();
            int i=0;
            for (cURLs item:listUrlx){
                LogX.d(TAG,i+":"+item.filePathName);
                files.add(new File(item.filePathName));
                i++;
            }
            try {
                LogX.d(TAG,"zipfile:"+fzip.getAbsolutePath());
                show_callback("zipfile:"+zipFilePathName);
                new ZipUtils().zipFiles(files, fzip);
            }catch (Exception e){
                LogX.e(TAG,"Error:"+e.getMessage());
                return false;
            }
        }
        sendMail();//
        return true;
    }
    private boolean checkSendMail(){
        mailFileName="stdaily" + g_url_date;
        String filename=dirpath + "/"+mailFileName+ ".sendok";
        File fok=new File(filename);
        LogX.d(TAG,"fok:"+fok.getAbsolutePath());
        if (fok.exists()){
            show_callback("mail have send!  "+g_url_date);
            hasDown=true;
            return true;
        }
        return false;
    }
    private void sendMail(){
        new Thread() {public void run() {sendJavaMail();}}.start();
    }

    private void sendJavaMail() {
        String filename=dirpath + "/"+mailFileName+ ".sendok";
        File fok=new File(filename);
        LogX.d(TAG,"fok:"+fok.getAbsolutePath());
        if (fok.exists()){
            LogX.d(TAG,"fok: exist!");
            AService.sendBroadDownDone(0,"DailySSE exist");
            hasDown=true;
            return;
        }

        List<File> files = new ArrayList<File>();
        files.add(new File(zipFilePathName));
        String subject = "title";
        String content = "content";
        if (files != null) {
            subject = files.get(0).getName();
        }
        if (new MailCmd().cmdSave(null,subject, TimeX.getToday(),files, Global.getDailySSEDir())==0) {
            LogX.d("JavaMail:", "Send Success!");
            try {
                fok.createNewFile();
                show_callback("mail send OK! " + g_url_date);
                hasDown=true;
                AService.sendBroadDownDone(0,"DailySSE succ");
            } catch (Exception e) {
                LogX.d("JavaMail:", "Create sendok fail!");
                show_callback("Error: mail send fail!");
                AService.sendBroadDownDone(0,"DailySSE fail");
            }
        }else{
            LogX.d("JavaMail:", "Send Fail! "+ g_url_date);
            AService.sendBroadDownDone(0,"DailySSE fail0");
        }
        /*{
            final String m1 = "111";
            final String m2 = "11a";
            final String m3 = "11";
            String subject = "title";
            String content = "content";
            final String mailserver = "smtp" + ".163.com";//"smtp.163.com";
            final String mailport = "25";
            final String mailac = "adat" + "ates" + "t@163.com";
            final String mailpa = m1 + m3 + m2;
            boolean validate = true;
            //
            List<String> to = new ArrayList<String>();
            to.add(mailac);
            ArrayList<File> attachment = new ArrayList<File>();
            attachment.add(new File(zipFilePathName));
            if (attachment != null) {
                StringBuilder sb = new StringBuilder("");//sb.setLength(0);//sb.delete( 0, sb.length() );
                for (File f : attachment) {
                    sb.append(f.getName());
                    if (attachment.size() > 1) {
                        sb.append(",");
                    }
                }
                content = sb.toString() + ",h";
                subject = attachment.get(0).getName();
            }
            boolean draft = false;
            //MailUtils senMail = new MailUtils(mailserver,mailport,mailac,mailpa,validate);//这个类用来发送邮件

            //if (senMail.sendMail(mailac,to,subject,content,attachment,draft)==0){
            //    LogX.d("JavaMail:","Send Success!");
            //    try {
            //        fok.createNewFile();
            //        show_callback("mail send OK! "+g_url_date);
            //    }catch (Exception e){
            //        LogX.d("JavaMail:","Create sendok fail!");
            //        show_callback("Error: mail send fail!");
            //    }
        }*/
    }
    private void show_callback(String comment){
        LogX.d(TAG,""+comment);
        //DataCallback.updateUI(mContext,DataCallback.TYPE_DAILYDOWN_START,comment, 0);
    }
    private void show_callback(String comment,int percent){
        LogX.d(TAG,""+comment);
        //DataCallback.updateUI(mContext,DataCallback.TYPE_DAILYDOWN_DO,comment, 0);
    }
    private void show_callback_finish(String comment){
        //LogX.d(TAG,"33: "+comment+"! "+ymdhms.format(new Date()));
        //DataCallback.updateUI(mContext,DataCallback.TYPE_DAILYDOWN_FINISH,comment+" "+ymdhms.format(new Date()), 0);
    }

    class cURLs {
        String type="";
        String url1="";
        String url2="";
        String url3="";
        String url4="";
        String url="";
        String encode="utf-8";//GB2312
        int nTry=0;
        int count=0;
        String filePathName="";
        JSONObject jsono;
        JSONArray jsona;
        int timeBegin=0;
        int timeEnd=0;
    }
    


}
