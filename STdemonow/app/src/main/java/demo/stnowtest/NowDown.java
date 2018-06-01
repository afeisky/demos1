package demo.stnowtest;


import android.content.Context;

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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import demo.api.*;


import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Thread.sleep;

/**
 * Created by chaofei on 18-1-12.
 */

public class NowDown {

    private String TAG = "[NowDown]";
    public static String ACTION_ALARM = "afei.demo.ok";
    private ArrayList<String> listCode = new ArrayList<String>();
    private int nTryAgain = 5;
    private List<cURLs> listUrlx = new ArrayList<cURLs>();
    private int indexListUrls = 0;
    private int countConnect = 0; //
    private SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat yyMMdd_HHmmss = new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //String url1="http://q.stock.sohu.com/hisHq?code=cn_0001008&start=20170901&end=20171013&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
    public String urlSohu1 = "http://q.stock.sohu.com/hisHq?code=cn_";
    String sn_url = "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
    String sse_url = "http://yunhq.sse.com.cn:32041/v1/sh1/list/exchange/equity?select=code%2Cname%2Copen%2Chigh%2Clow%2Clast%2Cprev_close%2Cchg_rate%2Cvolume%2Camount%2Ctradephase%2Cchange%2Camp_rate&order=&begin=0&end=5000&_=1501664482391";
    //private DbContentProvider contentProvider;
    private String thisDir;
    private String downDir;
    private String temp;
    private String FILENAME_KEY="AA";
    private Context mContext;
    public NowDown(Context context) {
        this.mContext = context;
    }
    public void initVar(String rootDirPath) {
        //get listcode?

        //listCode
        String d="";
        try {
            d = yyyymmdd.format(new Date());
        } catch (Exception e) {
            LogX.e(TAG, "Error:  " + e.getStackTrace());
        }
        thisDir = rootDirPath+"/AA";// Global.workPath+"/"+d+ "/snbk_now"+yyyymmdd.format(new Date());
        File f = new File(thisDir);
        if (!f.exists()) {
            f.mkdir();
        }
        downDir = thisDir+"/"+d;
        f = new File(downDir);
        if (!f.exists()) {
            f.mkdir();
        }
        //init :clear 0;
        listUrlx.clear();
        indexListUrls=0;
        cURLs u = new cURLs();
        try {
            u = new cURLs();
            u.type = "bknode";
            u.filenamekey = downDir + "/gnbk";
            u.codekey = "gn_";
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
            u.filenamekey = downDir + "/bknode" ;
            u.codekey = "new_";
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
            u.filenamekey = downDir + "/bkshy" ;
            u.codekey = "hangye_";
            u.url1 = sn_url;
            u.url2 = "[[\"bkshy\",\"\",0]]";
            u.url3 = "shy";
            u.url = u.url1 + u.url2;
            u.count = 0;
            u.timeBegin = 1805;
            u.timeEnd = 800;
            u.nTry = 0;
            listUrlx.add(u);
            if (false) {
                u = new cURLs();
                u.type = "sse";
                u.filenamekey = downDir + "/sse";
                u.url1 = sse_url;
                u.url = u.url1;
                u.encode = "gb2312";
                u.count = 0;
                u.timeBegin = 1515;
                u.timeEnd = 800;
                u.nTry = 0;
                listUrlx.add(u);
            }
        } catch (Exception e) {
            LogX.e(TAG, "Error:  " + e.getStackTrace());
        }

    }
    private boolean saveBKdaily=false;
    private Date currTime;
    public boolean doDown() {
        boolean ret=false;
        int index = 0;
        LogX.d(TAG, "start2: 222---"+ TimeX.getToday());
        currTime=new Date();
        for (cURLs u : listUrlx) {
            //LogX.d(TAG, "start2:  " + index + "======" + index);
            downTask t = new downTask(index);
            t.start();
            index++;
            // break;
        }
        String lines = "";
        int timercount = 50;
        while (timercount > 0) {
            try {
                sleep(1000);
                timercount--;
                boolean finish = true;
                LogX.d(TAG, "timer:" + timercount);
                for (cURLs u : listUrlx) {
                    if (u.done > 0) {
                        finish = false;
                        break;
                    }
                    index++;
                }
                if (finish) {
                    timercount = -1;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogX.w(TAG, "start2: download done!");


        if (timercount==-1) {
            return true;
        }else {
            return false;
        }
        //index = 0;
        //for (cURLs u : listUrlx) {
        //LogX.d(TAG, "start2:  " + index + "======" + index);
        //    lines += u.time + "," + u.filePathName + "\n";
        //    index++;
        //}
        //save2db
    }
    private boolean save2db(){
        if (Global.getContentProvider()!=null) {
            for (cURLs item : listUrlx) {
                String s = item.filePathName;
                String destFolder = downDir;
                File f=new File(s);
                LogX.w(TAG, "start7!:" + TimeX.getToday());
                new NowParser().parserByFilename(f, f.getName());
            }
            LogX.w(TAG, "start8!:" + TimeX.getToday());
            LogX.w(TAG, "start9!:" + TimeX.getToday());
            return true;
        }

        //String timenow=HHmm.format(new Date());
        //int t= Integer.valueOf(timenow);
        if (false)  //( (t>1815 && t<2400)) {
        {
            String time =listUrlx.get(0).time;
            try {
                time = yyMMdd_HHmmss.format(yyyy_mm_dd_hhmmss.parse(time).getTime());
            } catch (Exception e) {
                LogX.e(TAG, "Error:" + e.getMessage());
            }
            int index = 0;
            String lines = "";
            //for (cURLs u : listUrlx) {
            //LogX.d(TAG, "start2:  " + index + "======" + index);
            //    lines += u.time + "," + u.filePathName + "\n";
            //    index++;
            //}
            LogX.w(TAG, downDir + "/files.txt");
            LogX.w(TAG, lines);
            FileX.writeLines(downDir + "/files.txt", lines, "utf-8");
            File fzip = new File(downDir + "/" + FILENAME_KEY + "," + time + ".zip");
            if (!fzip.exists()) {
                List<File> files = new ArrayList<File>();
                for (cURLs item : listUrlx) {
                    files.add(new File(item.filePathName));
                }
                files.add(new File(downDir + "/files.txt"));
                LogX.w(TAG, fzip.getAbsolutePath());
                try {
                    new ZipUtils().zipFiles(files, fzip);

                    //----
                    List<File> files1 = new ArrayList<File>();
                    files1.add(fzip);
                    new MailCmd().cmdSave(null, fzip.getName(), "", files1, "AA");


                    String filename = fzip.getName();
                    String dirname = filename.substring(0, filename.indexOf("."));
                    LogX.w(TAG, "filename:" + filename);
                    LogX.w(TAG, "dirname:" + dirname);

                    new NowParser().unzipFileAndInput(fzip.getAbsolutePath(), downDir);
                    //new NowBK().getBKdata("nowBK");
                } catch (Exception e) {
                    LogX.e(TAG, "Error:" + e.getStackTrace());
                }
            }
        }
        return false;
    }

    private boolean sendNowBK(){
        if (Global.getContentProvider()!=null) {
            String sendOKfilename="";
            new NowBK().getBKdata(Global.getNowBKDir(),"");
            LogX.w(TAG, "start9!:" + TimeX.getToday());
            return true;
        }
        return false;
    }
    private String sendOKfilename="";
    private boolean checkDialyBKsendok(){
        String dir = Global.workPath + "/dailyBK";
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdir();
        }
        String snow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String filename = "bk_" + snow + ".sendok";
        f = new File(dir + "/" + filename);
        sendOKfilename=filename;
        if (!f.exists()) {
            return false;
        }
        return true;
    }
    private boolean sendDailyBk(){
        if (Global.getContentProvider()!=null) {
            String sendOKfilename="";
            String timenow = HHmm.format(new Date());
            int t = Integer.valueOf(timenow);
            if (t > 1510 && !saveBKdaily) {
                if (!checkDialyBKsendok()) {
                    LogX.w(TAG, sendOKfilename + " sendOK!");
                } else {
                    LogX.w(TAG, sendOKfilename + " exist!");
                }
                new NowBK().getBKdata(Global.getNowBKDir(),sendOKfilename);
            }else if (t < 1510) {
                saveBKdaily = false;
            }
            LogX.w(TAG, "start9!:" + TimeX.getToday());
            return true;
        }
        return false;
    }

    private boolean deleteOldRecords(){
        String snow=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //Global.getJdbc().deleteSnbkOldData(snow);
        Global.getContentProvider().deleteSnbkOldData(snow);
        return true;
    }

    private boolean isRunning = true;

    public void stop() {
        isRunning = false;
    }
    public boolean isRunning() {
        return isRunning;
    }
    private SimpleDateFormat HHmm = new SimpleDateFormat("HHmm");
    private int period=60*1000;
    private boolean isopen=true;
    boolean isDeleteNotToday=false;
    private boolean isGoon = true;
    public void start(String rootDirPath){
        StartTask t = new StartTask(rootDirPath);
        t.start();
    }

    public boolean down(String rootDirPath){
        initVar(rootDirPath);
        if (doDown()){
            long startTime = System.currentTimeMillis();
            if (save2db()) {
                sendNowBK();
                sendDailyBk();
                long endTime = System.currentTimeMillis();
                long startend=(endTime-startTime)/1000;
                if (startend>15){
                    LogX.w(TAG, "db save&get time: "+startend+" s. (>15s)");
                }else{
                    LogX.w(TAG, "db save&get time: "+startend+" s.");
                }
                AService.sendBroadDownDone(0,"succ");
                return true;
            }
        }
        AService.sendBroadDownDone(0,"fail");
        return false;
    }
    public void start1(String rootDirPath){
        String timenow=HHmm.format(new Date());
        int t= Integer.valueOf(timenow);
        if ((nDailyDown==0 || nDailyDown==10)&& (t<830 ||t>1815)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new DailyDown(mContext).start1(Global.workPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            nDailyDown+=1;
        }
        //if ((t>(915-20) && t<1131) || (t>(1300-20) && t<1515)) {
        if ( isopen && ( (t>915 && t<1131) || (t>1300 && t<1520))) {
            if (!isDeleteNotToday){
                String snow=new SimpleDateFormat("yyMMdd_").format(new Date());
                //MailCmd.delteAllNotToday("AA",snow);
                deleteOldRecords();
                isDeleteNotToday=true;
            }
            LogX.w("", "BEGIN---->"+TimeX.getToday());
            //if (isGoon) {
            down(rootDirPath);
            //Thread.sleep(5*1000);
            //}
            LogX.w("", "END------>"+TimeX.getToday());
        }else{
            if (t > 1510 && !saveBKdaily) {
                if (!checkDialyBKsendok()) {
                    down(rootDirPath);
                }
            }


            if ( t<915 ||t>1915){
                isopen = true;
            }
            if (nDailyDown<10 && (t>1815 && t<1830)){
                nDailyDown=10;
            }
        }
        ///////////////isRunning=false;
    }
    public void start2(String rootDirPath){

        Task1 t = new Task1(rootDirPath);
        t.start();
    }

    class Task1 extends Thread {
        private String rootDirPath = "";

        public Task1(String rootDirPath) {
            this.rootDirPath=rootDirPath;
        }

        @Override
        public void run() {
            try {
                start1(rootDirPath);
            } catch (Exception e) {
                LogX.e(TAG, "Error: start ! " + e.getStackTrace());
                e.printStackTrace();
            }
        }
    }
    private boolean needDailyDown=false;
    private int nDailyDown=0;

    class StartTask extends Thread {
        private String rootDirPath = "";

        public StartTask(String rootDirPath) {
            this.rootDirPath=rootDirPath;
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    start1(rootDirPath);
                    String timenow=HHmm.format(new Date());
                    int t= Integer.valueOf(timenow);
                    if ( t<850 ||t>1850){
                        LogX.w("", "!isopen !!!!"+TimeX.getToday());
                        Thread.sleep(60 * 1000);
                    }else {
                        LogX.w("", "!isopen !!!!"+TimeX.getToday());
                        Thread.sleep(20 * 1000);
                    }
                    ///////////////isRunning=false;
                }

            } catch (Exception e) {
                LogX.e(TAG, "Error: start ! " + e.getStackTrace());
                e.printStackTrace();
            }
        }
    }
    private String getJsonString(String filepathname) {
        String data = "";
        //String filepathname=Global.workPath+"/snbk_now/gnbk_180125_174529.json";
        LogX.w(TAG, "get=" + filepathname);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepathname)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            bf.close();
            data = stringBuilder.toString();
            return data;
        } catch (Exception e) {
            LogX.e(TAG, "Error: getJsonString get ! " + e.getMessage());
        }
        return data;
    }

    private String getJsonString(String filepathname, String encode) {
        String data = "";
        //String filepathname=Global.workPath+"/snbk_now/gnbk_180125_174529.json";
        LogX.w(TAG, "get=" + filepathname);
        StringBuilder stringBuilder = new StringBuilder(encode);
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepathname)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            bf.close();
            data = stringBuilder.toString();
            return data;
        } catch (Exception e) {
            LogX.e(TAG, "Error: getJsonString ! " + e.getStackTrace());
        }
        return data;
    }

    class downTask extends Thread {
        private int blocSize = 248000;
        private int index = 0;

        public downTask(int index) {
            this.index = index;
        }


        @Override
        public void run() {
            if (listUrlx.get(index) == null) {
                return;
            }
            String timeNow = yyMMdd_HHmmss.format(currTime);
            //u.filePathName = u.filenamekey + "_" + timeNow + ".json";
            listUrlx.get(index).filePathName = listUrlx.get(index).filenamekey+"," + timeNow + ".json";
            listUrlx.get(index).time = yyyy_mm_dd_hhmmss.format(currTime);
            LogX.w(TAG, "run: filePathName=" + listUrlx.get(index).filePathName);
            LogX.w(TAG, "run: url=" + listUrlx.get(index).url);
            //updateUI(listUrlx.get(index).url, 0);
            try {
                //URL url = new URL("https://www.baidu.com/bd_logo1.png");
                //String url = URLEncoder.encode(url, "utf-8");//need convert if chinese char in URL
                URL url = new URL(listUrlx.get(index).url);
                HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
                conn1.setRequestMethod("GET");
                conn1.setInstanceFollowRedirects(false);
                int timeout = 20000;
                if (timeout > 0) {
                    conn1.setConnectTimeout(timeout);
                    conn1.setReadTimeout(timeout);
                }
                //Sets the flag indicating whether this URLConnection allows unzipFileAndInput. It cannot be set after the connection is established.
                conn1.setDoInput(true);
                InputStream in = null;
                int rspCode = conn1.getResponseCode();
                LogX.d(TAG, "+++++" + rspCode);
                if (rspCode == HttpURLConnection.HTTP_OK) {
                    //updateUI(listUrlx.get(index).url,10);
                    in = conn1.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] arr = new byte[1024];
                    int len = 0;
                    while ((len = in.read(arr)) != -1) {
                        bos.write(arr, 0, len);
                    }
                    byte[] b = bos.toByteArray();
                    String data = new String(b, listUrlx.get(index).encode);//"utf-8" "gb2312"
                    LogX.d(TAG, "==" + data.substring(0, (data.length() > 76) ? 76 : 0));
                    LogX.d(TAG, "==,filePathName=" + listUrlx.get(index).filePathName);
                    //updateUI(u.url,100);
                    //updateUI(listUrlx.get(index).filePathName,100);
                    //if (u.filePathName.length()>0){
                    OutputStreamWriter out = null;
                    out = new OutputStreamWriter(new FileOutputStream(listUrlx.get(index).filePathName), listUrlx.get(index).encode);
                    out.write(data);
                    out.close();
                    listUrlx.get(index).done = 0;
                    LogX.d(TAG, "==,done " + index);
                    //}

                    if (listUrlx.get(index).codekey.equals("sse_")){
                        if (data==null || data.length()==0){
                        }
                        JSONObject json;
                        if (data.indexOf("[")==0) {
                            JSONArray json1 = new JSONArray(data);
                            json = json1.getJSONObject(0);
                        }else{
                            json = new JSONObject(data);
                        }
                        int count = json.getInt("end");
                        int idate = json.getInt("date");
                        int itime = json.getInt("time");
                        Date time;
                        LogX.w(TAG,"idate="+idate+" ,count="+count);
                        String yymm=String.valueOf(idate);
                        String yyyymm = yyyymmdd.format(new Date());
                        if (!yymm.equals(yyyymm)){
                            isopen=false;
                        }
                    }
                } else {
                    //updateUI(listUrlx.get(index).url,0);
                }
                //关闭流
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                listUrlx.get(index).done = 0;
                LogX.e(TAG, "[run] Error1:" + e.getStackTrace());
                e.printStackTrace();
                updateUI("[run] Error1:" + e.getMessage(), 0);
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

        private void updateUI(String comment, int percent) {
            LogX.w(TAG, "11:" + comment);

        }

        private void updateUI(int type, String comment, int percent) {
            LogX.w(TAG, "22:" + comment + ",percent:" + percent);
        }

        class cURLs {
            String type = "";
            String url1 = "";
            String url2 = "";
            String url3 = "";
            String url4 = "";
            String url = "";
            String encode = "utf-8";//GB2312 //"utf-8"
            int nTry = 0;
            int count = 0;
            String filePathName = "";
            String time = "";
            String filenamekey = "";
            String codekey = "";
            JSONObject jsono;
            JSONArray jsona;
            int timeBegin = 0;
            int timeEnd = 0;
            int done = 1;
        }

}
