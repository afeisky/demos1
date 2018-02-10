package afei.stdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import afei.api.Global;
import afei.api.LogX;

/**
 * Created by chaofei on 18-1-12.
adb shell am broadcast  -a afei.stdemo.download_temp --es "filename" "temp1.txt" --es "url" "http://www.baidu.com/aa.jpeg" --es "encode" "gb2312" 

 */

public class DownOne {
    private String TAG = "[DownOne]";
    private SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat yyMMdd_HHmmss=new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat yyyyMMdd_HHmmss=new SimpleDateFormat("yyyyMMdd_HHmmss");
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String dirlhb="";
    public DownOne(Context context){

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
        dirlhb = Global.workPath + "/download_temp";
        File f = new File(dirlhb);
        if (!f.exists()) {
            f.mkdir();
        }

        return true;
    }

    private String filename;
    private String url;
    private String encode;
    public void start(Intent intent){
        filename = intent.getStringExtra("filename");
        url  = intent.getStringExtra("url");
        try {
            encode = intent.getStringExtra("encode");
        }catch (Exception e){

        }
        if (encode.length()==0) {
            encode = "utf-8";
        }
        LogX.w(TAG,"filename="+filename);
        LogX.w(TAG,"url="+url);

        if (url.length()>0 && filename.length()>0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                            if (init_dir()) {
                                downTask t = new downTask(dirlhb+"/"+filename,url,encode);
                                t.start();
                            }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            LogX.e(TAG,"url.length()="+url.length()+",filename.length()="+url.length());
        }
    }

    class downTask extends Thread {
        private int blocSize = 248000;
        private String filePathName;
        private String url;
        private String encode;

        public downTask(String filename,String url,String encode) {
            this.filePathName=filename;
            this.url=url;
            this.encode=encode;
        }

        @Override
        public void run() {
            try {
                //URL url = new URL("https://www.baidu.com/bd_logo1.png");
                URL url = new URL(this.url);
                HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
                conn1.setRequestMethod("GET");
                conn1.setInstanceFollowRedirects(false);
                int timeout = 20000;
                if (timeout > 0) {
                    conn1.setConnectTimeout(timeout);
                    conn1.setReadTimeout(timeout);
                }
                LogX.d(TAG, "" + url);
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
                    String ss = new String(b, encode);//"utf-8" "gb2312"
                    LogX.d(TAG, "==" + ss.substring(0, (ss.length() > 100) ? 100 : 0));
                    LogX.d(TAG, "filePathName=" + filePathName);
                    OutputStreamWriter out = null;
                    out = new OutputStreamWriter(new FileOutputStream(filePathName), encode);
                    out.write(ss);
                    out.close();

                } else {
                }
                //关闭流
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                LogX.e(TAG, "[run] Error1:" + e.getMessage());
            }
        }

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
