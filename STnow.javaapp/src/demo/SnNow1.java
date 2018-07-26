package demo;

import demo.api.FileX;
import demo.api.LogX;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SnNow1 {
	private static String TAG="SnBK";

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
	private String listfilename;
	private String FILENAME_KEY="AA";

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public boolean saveUrlAs(String photoUrl, String fileName,String listfilename) {
		// only support HTTP:
		boolean succ=false;
		try {
			URL url = new URL(photoUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			DataInputStream in = new DataInputStream(
					connection.getInputStream());
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					fileName));
			LogX.d(TAG,"fileName: " + fileName);
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
			succ=true;
		} catch (Exception e) {
			succ=false;
		}finally {
			if (succ){
				File f=new File(fileName);
				FileX.add2File(listfilename,f.getName()+"\n");
				String timeNow = sdf.format(new Date());
				LogX.d(TAG,"--> "+timeNow);
				return true;
			}
			return false;
		}
	}
    private SimpleDateFormat HHmm = new SimpleDateFormat("HHmm");
	private boolean check(){
        String timenow=HHmm.format(new Date());
        int t= Integer.valueOf(timenow);
        if ((t>915 && t<1131) || (t>1300 && t<1520)){
            return true;
        }else{

        }
        return false;
    }
	private String timeNow = yyMMdd_HHmmss.format(new Date());
    private void init(String dir){
		String rootDirPath=dir;
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
        if (!check()){
            return;
        }
		f = new File(downDir);
		if (!f.exists()) {
			f.mkdir();
		}
		//init :clear 0;
		listUrlx.clear();
		indexListUrls=0;
		cURLs u = new cURLs();
		listfilename = downDir + "/_list.txt" ;
		timeNow = yyMMdd_HHmmss.format(new Date());
		try {
			u = new cURLs();
			u.type = "gnbk";
			u.filenamekey = downDir + "/gnbk";
			u.listfilename = u.filenamekey + ",filelist.txt";
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
			u.listfilename = u.filenamekey + ",filelist.txt";
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
			u.listfilename = u.filenamekey + ",filelist.txt";
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
			if (true) {
				u = new cURLs();
				u.type = "sse";
				u.filenamekey = downDir + "/sse";
				u.listfilename = u.filenamekey + ",filelist.txt";
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
	public void start(String dir)
	{
		init(dir);
		int index=0;
		for (cURLs c : listUrlx) {
			c.filePathName = c.filenamekey+"," + timeNow + ".json";
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						saveUrlAs(c.url,c.filePathName,c.listfilename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			index++;
			//break;
		}
	}

	public JSONObject parseBK(String filepathname) {
        LogX.w(TAG,"[parseBK]"+filepathname);
		File f=new File(filepathname);
        JSONObject json=new JSONObject();
		if (f.isFile()) {
			LogX.w(TAG, "parserFileBK:" + f.getAbsolutePath());
			Date time;
			String data = FileX.getJsonString(f.getAbsolutePath(), "gb2312");
			if (data == null || data.length() == 0) {
				return null;
			}
            //LogX.w(TAG, "" + data);
            JSONArray jarr = new JSONArray(data);
			if (data.substring(0,1).equals("[")){
                LogX.w(TAG, "=");
                json=jarr.getJSONObject(0);
            }else{
                LogX.w(TAG, "====");
                json=new JSONObject(data);
            }
			return json;
		}
		return null;
	}
	public JSONObject getParseJson(String dir){
        LogX.w(TAG,dir);
		init(dir);
		JSONObject json=new JSONObject();
		int index=0;
		for (cURLs c : listUrlx) {
            LogX.w(TAG,c.listfilename);
			if ( c.type.equals("gnbk")|| c.type.equals("bknode")||c.type.equals("bkshy")) {
			    String pathname=FileX.readLastLine(c.listfilename,"");
                pathname=new File(c.listfilename).getParent()+"/"+pathname;
				json.put(c.type,parseBK(pathname));
			}
			index++;
		}
		FileX.writeLines(downDir+"/snbk,report.json",json.toString(),"");
        //LogX.w(TAG,json.toString());
    	return json;
	}
}
