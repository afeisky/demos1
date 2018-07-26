package demo;

import demo.api.LogX;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownTrd extends Thread {
    private static String TAG="DownTrd";
    private int blocSize = 248000;
    private String strUrl = "";
    private String filePathName = "";
    private String encode = "utf-8";
    private SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private SimpleDateFormat yyMMdd_HHmmss = new SimpleDateFormat("yyMMdd_HHmmss");
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DownCb callInterface;
    public DownTrd(String strUrl,String filePathName,String encode,DownCb cb) {
        if (strUrl!=null) {
            if (strUrl.length() > 0) {
                this.strUrl=strUrl;
            }
        }
        if (filePathName!=null) {
            if (filePathName.length() > 0) {
                this.filePathName = filePathName;
            }
        }
        if (encode!=null) {
            if (encode.length() > 0) {
                this.encode = encode;
            }
        }
        this.callInterface=cb;
    }
    public void run() {
        int retFlag=0;
        String retData="";
        try {
            if (strUrl.length()<5){
                retFlag=2;
                return;
            }
            //LogX.w(TAG, "strUrl=" + strUrl);
            //LogX.w(TAG, "filePathName=" + filePathName);
            String timeNow = yyMMdd_HHmmss.format(new Date());
            //URL url = new URL("https://www.baidu.com/bd_logo1.png");
            //String url = URLEncoder.encode(url, "utf-8");//need convert if chinese char in URL
            URL url = new URL(strUrl);
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
                retData = new String(b, encode);//"utf-8" "gb2312"
                LogX.d(TAG, "==" + retData.substring(0, (retData.length() > 20) ? 20 : 0));

                //updateUI(u.url,100);
                //updateUI(listUrlx.get(index).filePathName,100);
                if (filePathName!=null) {
                    if (filePathName.length() > 0) {
                        OutputStreamWriter out = null;
                        out = new OutputStreamWriter(new FileOutputStream(filePathName), encode);
                        out.write(retData);
                        out.close();
                    }
                }
                retFlag=0;
            } else {
                retFlag=1;
            }
            //关闭流
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            LogX.e(TAG, "[run] Error1:" + e.getStackTrace());
            e.printStackTrace();
        }finally {
            callInterface.done(retFlag,retData);
        }
    }


}
