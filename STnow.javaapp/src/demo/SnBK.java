package demo;

import demo.api.FileX;
import demo.api.Global;
import demo.api.LogX;
import demo.api.TimeX;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class SnBK extends Thread{
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
	private String temp;
	private String listfilename;
	private String FILENAME_KEY="AA";
	public static String snbk_filelist="snbk,filelist.txt";

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat HHmm = new SimpleDateFormat("HHmm");
	private String PathDir ="";
	private int runStep=0; // 0: init;  1: init fail; 2: timeout; 9: success;
	public boolean isDone(){
		LogX.w(TAG, "isDone="+runStep);
		return (runStep>0);
	}
	private void setDone(int step){
		runStep=step;
	}
	private boolean check(){
        String timenow=HHmm.format(new Date());
        int t= Integer.valueOf(timenow);
        if ((t>915 && t<1131) || (t>1300 && t<1520)){
            return true;
        }else{

        }
        return false;
    }
	public SnBK(String dir){
		PathDir =dir;
		//get listcode?
		//listCode
		//init :clear 0;
		listUrlx.clear();
		indexListUrls=0;
		cURLs u = new cURLs();
		listfilename = PathDir + "/_list.txt";
		try {
			u = new cURLs();
			u.type = "gnbk";
			u.filenamekey = PathDir + "/gnbk";
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
			u.filenamekey = PathDir + "/bknode" ;
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
			u.filenamekey = PathDir + "/bkshy" ;
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
			if (false) {
				u = new cURLs();
				u.type = "sse";
				u.filenamekey = PathDir + "/sse";
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
			e.printStackTrace();
		}
	}
	private int countDownUrl=3;
	private JSONObject jsonBK=new JSONObject();
	private boolean needDown(){
		String filepathname = Global.getDailyDir() + "/" + snbk_filelist;
		String lastname=FileX.readLastLine(filepathname,null);
		//filepathname=Global.getDailyDir() + "/" + SnA.nameFileList;
		//String dailylast=FileX.readLastLine(filepathname,null);
		LogX.w(TAG,lastname);
		if ( lastname.length()>1) {
			lastname=lastname.replace("snbk,","");
			String hhmm = lastname.substring(7, 7 + 4);
			int lastHM = Integer.valueOf(hhmm);
			LogX.w(TAG, hhmm);
			String shhmm = new SimpleDateFormat("HHmm").format(new Date());
			int nowHM = Integer.valueOf(shhmm);
			if (nowHM >= 1130 && nowHM < 1300 && lastHM >= 1130) {
				LogX.w(TAG, "pause, 11:30");
				return false;
			} else if (nowHM > 1510 && lastHM > 1501) {
				LogX.w(TAG, "exist 15:00 data,");
				return false;
			}
		}
		return true;
	}
	public void run()
	{		
		setDone(0);
		LogX.w(TAG, PathDir);
		if (!needDown()){
			setDone(1);
			return;
		}
		int index=0;
		jsonBK=new JSONObject();
		countDownUrl=listUrlx.size();
		LogX.w(TAG, "urls count="+countDownUrl);
		timeout=false;
		String timeNow = TimeX.get_yymmdd_hhmmss();
		for (cURLs c : listUrlx) {
			c.filePathName = c.filenamekey+"," + timeNow + ".json";
			LogX.w(TAG, c.filePathName);
			//new DownTrd(c.url, c.filePathName, null, new DownCb() { //not save filename
			new DownTrd(c.url, "", null, new DownCb() {
				public void done(int retFlag, String retData) {
					//LogX.w(TAG, retFlag + "," + retData);
					if (retFlag==0) {
						JSONObject json=new JSONObject();
						JSONArray jarr = new JSONArray(retData);
						if (retData.substring(0,1).equals("[")){
							LogX.w(TAG, "=1"+c.type);
							json=jarr.getJSONObject(0);
						}else{
							LogX.w(TAG, "=2"+c.type);
							json=new JSONObject(retData);
						}
						jsonBK.put(c.type,json);
						retData = "";
					}
					countDownUrl--;
					LogX.w(TAG, "countDownUrl=="+countDownUrl);
					if (timeout){
						LogX.w(TAG, "timeout");
						setDone(2);
						return;
					}
					if (countDownUrl==0) {
						String stime1 = "";
						stime1 = TimeX.get_yyyy_mm_dd_hhmmss();
						jsonBK.put("time", stime1);
						String filename = "snbk," + timeNow + ".json";
						String filepathname = Global.getDailyDir() + "/" + filename;
						FileX.writeLines(filepathname, jsonBK.toString(), "");
						filepathname = Global.getDailyDir() + "/" + snbk_filelist;
						FileX.add2File(filepathname, filename + "\n");
						LogX.w(TAG, "finish");
						setDone(9);
					}
				}
			}).start();
			index++;
			//break;
		}
		start_finish();
		LogX.w(TAG, "done");
	}
	private boolean timeout=false;
	private void start_finish(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				timeout=true;
			}
		}, 30*1000);
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
	public JSONObject getParseJson1(){
        LogX.w(TAG, PathDir);
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
		FileX.writeLines(PathDir +"/snbk,report.json",json.toString(),"");
        //LogX.w(TAG,json.toString());
    	return json;
	}
	public JSONObject getParseJson(){
		String filepathname = Global.getDailyDir() + "/" + snbk_filelist;
		String lastname=FileX.readLastLine(filepathname,null);
		filepathname=Global.getDailyDir() + "/" + lastname;
		return new JSONObject(FileX.getJsonString(filepathname,null));
	}

	public String getLastname(){
		return  FileX.readLastLine(Global.getDailyDir()+"/"+snbk_filelist,null);
	}
}
