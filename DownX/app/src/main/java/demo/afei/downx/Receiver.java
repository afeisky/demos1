package demo.afei.downx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Date;

/**
 * Created by chaofei on 18-3-21.
 */

public class Receiver extends BroadcastReceiver {
    private static String TAG = "test";
    public static final String ACTION_BEGIN="demo.afei.downx.start";
    public static final String ACTION_END="demo.afei.downx.stop";
    private SimpleDateFormat yyyymdhms = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
    private static  DownTask downTask;
    private static String url;
    private static String filename="";
    private static boolean isDown=false;
    private static String LABEL_ROOT_DIR="downx";
    private static String LABEL_FILENAME_IN="_in";
    private static String LABEL_FILENAME_OUT="_out";
    private static String labelPathFileRootDir="";
    public static String labelPathFileNameIn ="";
    public static String labelPathFileNameOut ="";
    @Override
    public void onReceive(Context context, Intent intent) {
        TAG=this.getClass().getPackage().getName();
        logw("DailyReceiver onReceive()!");
        Toast.makeText(context, "downx--start()"+ yyyymdhms.format(new Date()), Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(ACTION_BEGIN)) {
            String action = intent.getStringExtra("action");
            Toast.makeText(context, "downx start()"+ yyyymdhms.format(new Date()) + action, Toast.LENGTH_SHORT).show();
            logw( "start! " + yyyymdhms.format(new Date()) + action);
            start();
        } else if (intent.getAction().equals(ACTION_END)) {
            String action = intent.getStringExtra("action");
            Toast.makeText(context, "downx stop()"+ yyyymdhms.format(new Date()) + action, Toast.LENGTH_SHORT).show();
            logw("stop! " + yyyymdhms.format(new Date()) + action);
            //stopDailyService(context);
        }
    }
    public static boolean init(){
        String roorDir=Environment.getExternalStorageDirectory().getAbsolutePath();
        labelPathFileRootDir=roorDir+"/"+LABEL_ROOT_DIR;
        labelPathFileNameIn = labelPathFileRootDir + "/"+LABEL_FILENAME_IN;
        labelPathFileNameOut= labelPathFileRootDir + "/"+LABEL_FILENAME_OUT;
        logw("DIR [" + labelPathFileNameIn +"]");
        File f=new File(labelPathFileRootDir);
        if (!f.exists()){
            f.mkdir();
            File f1=new File(labelPathFileNameIn);
            try {
                OutputStreamWriter out = null;
                out = new OutputStreamWriter(new FileOutputStream(labelPathFileNameIn), "utf-8");
                out.write("[url]\n");
                out.write("[filename]\n");
                out.close();
            }catch (Exception e){
                logw("[init] Error:"+e.getMessage());
            }

        }
        if (f.exists()) {
            return true;
        }
        return false;
    }
    private boolean start(){
        if (init()) {
            if (readLines(labelPathFileNameIn, "utf-8")) {
                logw("start:---");
                String downPathFileName= labelPathFileRootDir + "/"+filename;
                logw(downPathFileName);
                DownTask d = new DownTask(url, downPathFileName);
                d.start();
            }
        }else{
            logw("[start] Error: create dir fail: "+labelPathFileRootDir);
        }
        return true;
    }

    private boolean stop(){
        return true;
    }

    public boolean readLines(String filepathname,String encode) {
        String resultString="";
        File f=new File(filepathname);
        logw(""+filepathname);
        if (!f.exists()){
            return false;
        }
        url="";
        filename="";
        InputStream is=null;
        try {
            ArrayList<String> lines=new ArrayList<>();
            is=new FileInputStream(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    lines.add(line.trim());
                    //logw("=--->"+line);
                }
            }
            for (int i=0;i<lines.size();i++){
                line=lines.get(i);
                logw("=--->"+line);
                if (line.indexOf("[url]")==0){
                    url=line.substring("[url]".length());
                }else if (line.indexOf("[name]")==0){
                    filename=line.substring("[name]".length());
                }
            }
            logw("url:"+url);
            logw("name:"+filename);
            if (url.length()>0 && filename.length()>0) {
                return true;
            }
        } catch (IOException e) {
            logw("Error:"+e.getMessage());;
        }finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return false;
    }

    class DownTask extends Thread {
        private String urlD="";// 下载链接地址
        private String encode1="utf-8";// "utf-8"/"gb2312"
        private String filePathName="";// 保存文件路径地址
        private int blocSize=1024;//
        private int nTry=0;
        public DownTask(String downloadUrl,String filepath) {
            this.urlD = downloadUrl;
            this.filePathName = filepath;
            this.blocSize=248000;//
        }
        public DownTask(String downloadUrl, int threadNum, String filepath) {
            this.urlD = downloadUrl;
            this.filePathName = filepath;
            this.blocSize=248000;//
        }


        @Override
        public void run() {
            try {
                logw("[run] " +urlD);
                logw("[run] " +filePathName);
                if (urlD.length()==0){
                    logw("Error: downloadUrl is null! ");
                    return;
                }
                isDown=true;
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
                logw( "+++++" + rspCode);
                //conn1=reload(conn1);
                if (rspCode == HttpURLConnection.HTTP_OK) {
                    in = conn1.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] arr = new byte[1024];
                    int len = 0;
                    while ((len = in.read(arr)) != -1) {
                        bos.write(arr, 0, len);
                    }
                    byte[] b = bos.toByteArray();
                    String ss = new String(b, encode1);//"utf-8" "gb2312"
                    OutputStreamWriter out = null;
                    out = new OutputStreamWriter(new FileOutputStream(filePathName), encode1);
                    out.write(ss);
                    out.close();
                    logw("success!");
                    outResult(0);
                }else{
                    logw("fail!");
                }
                //关闭流
                if (in != null) {
                    in.close();
                }
                outResult(1);
                isDown=false;
            }  catch (Exception e) {
                isDown=false;
                outResult(1);
                logw("[run] Error:"+e.getMessage());
            }

        }
    }

    private static void outResult(int flag){
        try {
            OutputStreamWriter out = null;
            out = new OutputStreamWriter(new FileOutputStream(labelPathFileNameOut), "utf-8");
            out.write("" + flag);
            out.close();
        }catch (Exception e){
            logw("[outResult] Error:"+e.getMessage());
        }
    }

    private static void logw(String text){

        Log.w(TAG, text);
        try {
            String filename=labelPathFileRootDir + "/_down.log";;
            OutputStreamWriter out = null;
            out = new OutputStreamWriter(new FileOutputStream(filename), "utf-8");
            out.write("" + text);
            out.close();
        }catch (Exception e){
            logw("[outResult] Error:"+e.getMessage());
        }
    }

}
