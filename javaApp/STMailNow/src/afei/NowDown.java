package afei;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import afei.api.*;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by chaofei on 18-1-12.
 */

public class NowDown {

    private String TAG = "[NowDown]";
    public static String ACTION_ALARM = "afei.demo.ok";
    private ArrayList<String> listCode = new ArrayList<String>();
    private int nTryAgain = 5;
    private  List<cURLs> listUrlx = new ArrayList<cURLs>();
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
    private void init(String rootDirPath) {
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
            u = new cURLs();
            u.type = "sse";
            u.filenamekey = downDir + "/sse" ;
            u.url1 = sse_url;
            u.url = u.url1;
            u.encode = "gb2312";
            u.count = 0;
            u.timeBegin = 1515;
            u.timeEnd = 800;
            u.nTry = 0;
            listUrlx.add(u);

        } catch (Exception e) {
            LogX.e(TAG, "Error:  " + e.getStackTrace());
        }

    }

    public void start2() {
        int index = 0;
        for (cURLs u : listUrlx) {
            LogX.e(TAG, "start2:  " + index + "======" + index);
            downTask t = new downTask(index);
            t.start();
            index++;
            // break;
        }
        LogX.w(TAG, "start2: 222---");
        String lines = "";
        int timercount = 60;
        while (timercount > 0) {
            try {
                Thread.sleep(1000);
                timercount--;
                boolean finish = true;
                LogX.w(TAG, "start2: 333---" + timercount);
                for (cURLs u : listUrlx) {
                    if (u.finish > 0) {
                        finish = false;
                        break;
                    }
                    index++;
                }
                if (finish) {
                    timercount = 0;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogX.e(TAG, "start2: 888---");
        index = 0;
        for (cURLs u : listUrlx) {
            LogX.e(TAG, "start2:  " + index + "======" + index);
            lines += u.time + "," + u.filePathName + "\n";
            index++;
        }
        LogX.w(TAG, downDir + "/files.txt");
        LogX.w(TAG, lines);
        FileX.writeLines(downDir + "/files.txt", lines, "utf-8");
        String time = listUrlx.get(0).time;
        try {
            time = yyMMdd_HHmmss.format(yyyy_mm_dd_hhmmss.parse(time).getTime());
        } catch (Exception e) {
            LogX.e(TAG, "Error:" + e.getMessage());
        }
        File fzip = new File(downDir + "/"+FILENAME_KEY+"," + time + ".zip");
        if (!fzip.exists()) {
            List<File> files = new ArrayList<File>();
            for (cURLs item : listUrlx) {
                files.add(new File(item.filePathName));
            }
            files.add(new File(downDir + "/files.txt"));
            LogX.w(TAG, fzip.getAbsolutePath());
            try {
                new ZipUtils().zipFiles(files, fzip);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //----
                            List<File> files=new ArrayList<File>();
                            files.add(fzip);
                            new MailX().saveNew(null,fzip.getName(),"",files,"AA");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                //new MailX().saveOne(fzip);
                String filename=fzip.getName();
                String dirname=filename.substring(0,filename.indexOf("."));
                LogX.e(TAG, "filename:" +filename);
                LogX.e(TAG, "dirname:" +dirname);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new NowParser().input(fzip.getAbsolutePath(),downDir);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (Exception e) {
                LogX.e(TAG, "Error:" + e.getStackTrace());
            }
        }
        //Global.getContentProvider().queryBK10();
    }


    private boolean isRunning = false;

    public void stop() {
        isRunning = false;
    }

    public void start(String rootDirPath) {
        init(rootDirPath);
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isRunning) {
                        start2();
                    }
                } catch (Exception e) {
                    LogX.e(TAG, "Error: start2 ! " + e.getStackTrace());
                }
            }
        }).start();
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

        public void run2() {

            if (listUrlx.get(index) == null) {
                return;
            }
            String timeNow = yyMMdd_HHmmss.format(new Date());
            //u.filePathName = u.filenamekey + "_" + timeNow + ".json";
            listUrlx.get(index).filePathName = listUrlx.get(index).filenamekey+","+timeNow + ".json";
            listUrlx.get(index).time = yyyy_mm_dd_hhmmss.format(new Date());
            LogX.w(TAG, "run: filePathName=" + listUrlx.get(index).filePathName);
            LogX.w(TAG, "run: url=" + listUrlx.get(index).url);
            LogX.w(TAG, "run: encode=" + listUrlx.get(index).encode);
            updateUI(listUrlx.get(index).url, 0);
            try {
                //URL url = new URL("https://www.baidu.com/bd_logo1.png");
                //String url = URLEncoder.encode(url, "utf-8");//need convert if chinese char in URL
                URL url = new URL(listUrlx.get(index).url);
                HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
                conn1.setRequestMethod("GET");
                conn1.setInstanceFollowRedirects(false);
                //conn1.setRequestProperty("Content-type", "text/html");
                //conn1.setRequestProperty("Accept-Charset", "utf-8");
                conn1.setRequestProperty("contentType", "utf-8");
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
                if (rspCode == HttpURLConnection.HTTP_OK) {
                    //updateUI(listUrlx.get(index).url,10);

                    in = conn1.getInputStream();
                    File file = new File(listUrlx.get(index).filePathName);
                    FileWriter fWriter = new FileWriter(file);
                    int temp = 0;
                    while ((temp = in.read()) != -1) {
                        fWriter.write(temp);
                    }
                    fWriter.close();
                    listUrlx.get(index).finish = 0;
                    LogX.d(TAG, "==,finish " + index);
                    //}
                } else {
                    //updateUI(listUrlx.get(index).url,0);
                }
                //关闭流
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                LogX.e(TAG, "[run] Error1:" + e.getStackTrace());
            }

        }

        @Override
        public void run() {
            if (listUrlx.get(index) == null) {
                return;
            }
            String timeNow = yyMMdd_HHmmss.format(new Date());
            //u.filePathName = u.filenamekey + "_" + timeNow + ".json";
            listUrlx.get(index).filePathName = listUrlx.get(index).filenamekey+"," + timeNow + ".json";
            listUrlx.get(index).time = yyyy_mm_dd_hhmmss.format(new Date());
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
                //Sets the flag indicating whether this URLConnection allows input. It cannot be set after the connection is established.
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
                    String ss = new String(b, listUrlx.get(index).encode);//"utf-8" "gb2312"
                    LogX.d(TAG, "==" + ss.substring(0, (ss.length() > 100) ? 100 : 0));
                    LogX.d(TAG, "==,filePathName=" + listUrlx.get(index).filePathName);
                    //updateUI(u.url,100);
                    //updateUI(listUrlx.get(index).filePathName,100);
                    //if (u.filePathName.length()>0){
                    OutputStreamWriter out = null;
                    out = new OutputStreamWriter(new FileOutputStream(listUrlx.get(index).filePathName), listUrlx.get(index).encode);
                    out.write(ss);
                    out.close();
                    listUrlx.get(index).finish = 0;
                    LogX.d(TAG, "==,finish " + index);
                    //}

                } else {
                    //updateUI(listUrlx.get(index).url,0);
                }
                //关闭流
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                LogX.e(TAG, "[run] Error1:" + e.getStackTrace());
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
            int finish = 1;
        }

}
