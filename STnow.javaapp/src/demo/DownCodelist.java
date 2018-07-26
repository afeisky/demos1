package demo;

import demo.api.FileX;
import demo.api.Global;
import demo.api.LogX;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DownCodelist {
    private static String TAG="codelist";
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int index = 0;
    private String strUrl = "";
    private String filePathName = "";
    private String encode = "utf-8";
    private DownCb callInterface;
    public void download(String strUrl,String filePathName,String encode,DownCb cb) {
        this.strUrl = strUrl;
        this.filePathName = filePathName;
        if (encode != null) {
            if (encode.length() > 0) {
                this.encode = encode;
            }
        }
        this.callInterface = cb;
        int retFlag = 1;
        String retData = "";
        System.out.println(strUrl);
        for (int try_times = 10; try_times > 0; try_times--){
            try {
                Document document = null;
                if (try_times == 10) {
                    document = Jsoup.connect(strUrl).get();

                } else {
                    document = Jsoup.connect(strUrl)
                            .data("query", "Java")
                            .userAgent("Mozilla")
                            .cookie("auth", "token")
                            .timeout(20000)
                            .post();
                }
                JSONObject json = new JSONObject();
                Elements ListDiv = document.getElementsByAttributeValue("class", "quotebody");
                //System.out.println(ListDiv);
                if (ListDiv == null) {
                    retData = "No found data!";
                    return;
                }
                JSONArray list = new JSONArray();
                String url1 = "";
                for (Element element : ListDiv) {
                    Elements links = element.getElementsByTag("a");
                    for (Element link : links) {
                        //String linkHref = link.attr("href");
                        if (url1.length() < 20) {
                            url1 = link.attr("href");
                        }
                        String linkText = link.text().trim();
                        //System.out.println(linkHref);
                        //System.out.println(linkText);
                        int n = linkText.indexOf("(");
                        int k = linkText.indexOf(")");
                        JSONArray array = new JSONArray();
                        if (n > 0) {
                            array.put(linkText.substring(n + 1, k));
                            array.put(linkText.substring(0, n));
                            //System.out.println(linkText.substring(0,n)+","+linkText.substring(n+1,k));
                            list.put(array);
                        }
                    }
                }
                json.put("url", url1);
                json.put("time", yyyy_mm_dd_hhmmss.format(new Date()));
                json.put("list", list);
                //System.out.println(json);
                OutputStreamWriter out = null;
                boolean save2File = false;
                save2File = true;
                if (save2File) {
                    out = new OutputStreamWriter(new FileOutputStream(filePathName), "utf-8");
                    out.write(json.toString());
                }
                if (out != null) {
                    out.close();
                }
                retFlag = 0;
                retData = json.toString();
                if (retFlag == 0) {
                    callInterface.done(retFlag, retData);
                    break;
                }
            } catch (Exception e) {
                LogX.e(TAG, e.getMessage());
                e.printStackTrace();
                retData = e.getMessage();
                retFlag = 2;
            }//try
        }//for

        callInterface.done(retFlag, retData);

    }

    public ArrayList<String> getCodes(String filePathName) {
        ArrayList<String> retlist=new ArrayList<>();
        String strJson=FileX.getJsonString(filePathName,"");
        LogX.w(TAG,strJson.substring(0,100));
        JSONObject json = new JSONObject(strJson);
        JSONArray list = json.getJSONArray("list");
        if (strJson.length()>0){
            for (int i=0;i<list.length();i++){
                JSONArray a=list.getJSONArray(i);
                retlist.add(a.getString(0));
            }
        }
        return retlist;
    }
}
