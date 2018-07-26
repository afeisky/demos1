package demo;

import demo.api.FileX;
import demo.api.LogX;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SnNowInput {
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

	public void start(String dir,String sdate)
	{
		String rootDirPath=dir;
		//get listcode?
		//listCode


		String d="";
		try {
			if (sdate.length()==0) {
				d = yyyymmdd.format(new Date());
			}else{
				Date atime=yyyy_mm_dd.parse(sdate);
				d = yyyymmdd.format(atime);
			}
		} catch (Exception e) {
			LogX.e(TAG, "Error:  " + e.getStackTrace());
			return;
		}
		thisDir = rootDirPath+"/AA";// Global.workPath+"/"+d+ "/snbk_now"+yyyymmdd.format(new Date());
		File f = new File(thisDir);
		if (!f.exists()) {
			f.mkdir();
		}
		downDir = thisDir+"/"+d;//.replace("14","13");
		f = new File(downDir);
		if (!f.exists()) {
			f.mkdir();
		}
		//init :clear 0;
		listUrlx.clear();
		indexListUrls=0;
		cURLs u = new cURLs();
		listfilename = downDir + "/_list.txt" ;
		try {
			u = new cURLs();
			u.type = "bknode";
			u.filenamekey = downDir + "/gnbk";
			u.listfilename = u.filenamekey + "_list.txt";
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
			u.listfilename = u.filenamekey + "_list.txt";
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
			u.listfilename = u.filenamekey + "_list.txt";
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
				u.listfilename = u.filenamekey + "_list.txt";
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

		int index=0;
		for (cURLs c : listUrlx) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						parseFile(c.listfilename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			index++;
			////break;
		}
	}


	public boolean parseFile(String listfilename) {
		// only support HTTP:
		LogX.e(TAG,"Error: "+listfilename.indexOf("sse_list"));
		if (listfilename.indexOf("sse_list")>0){

		}else{
			return false;
		}
		File f=new File(listfilename);
		String okfilename=f.getAbsolutePath().replace(".txt","_ok.txt");
		ArrayList<String> listok=FileX.readLineList(okfilename,null);
		ArrayList<String> list=FileX.readLineList(listfilename,null);
		String okline="";
		String dirname=f.getParent();
		boolean find=false;
		if (list==null){
			LogX.e(TAG,"Error: list==null");
			return false;
		}
		if (listok==null) {
			find = true;
		}else{
			if (listok.size() > 0) {
				okline = listok.get(0);
				find = false;
			}
		}
		find = true;//////temp add.
		String lastFilename="";
		for (int i=0;i<list.size();i++){
			if (!find){
				if (okline.length()>0) {
					if (okline.equalsIgnoreCase(list.get(i))) {
						find = true;
					}
				}
				lastFilename=Paths.get(dirname,list.get(i)).toString();
				continue;
			}else{
				String filename= Paths.get(dirname,list.get(i)).toString();
				File fi=new File(filename);

				LogX.w(TAG,"+"+dirname);
				LogX.w(TAG,"->"+fi.getAbsolutePath());
				boolean md5=outMd5sum(filename,lastFilename);
                boolean issame=false;
				if (md5) {
					LogX.e(TAG, "=same= " + fi.getName() + lastFilename);

                    boolean diff=outDiff(filename,lastFilename);
                    if (md5!=diff){
                        LogX.e(TAG," ==== same???? ==== md5:"+md5+","+diff+" -->"+filename+" : "+lastFilename);
                        issame=false;
                    }else
                        issame=true;

                    if (issame){
						//moveFile2same(filename);
					}
				}
				if (!issame){
					lastFilename=filename;
					new NowParser().parserByFilename(fi,fi.getName());
					LogX.w(TAG,fi.getName());
					LogX.w(TAG,"="+list.get(i));
					FileX.writeLines(okfilename,list.get(i)+"\n",null);
				}
				////break;
			}
		}


		//new NowParser().parserByFilename(f,f.getName());
		return true;

	}
	public static boolean outDiff(String filename1,String filename2) {
		if (filename1.length()==0)
			return false;
		if (filename2.length()==0)
			return false;
		String str1=cmdLinux("diff "+filename1+" "+filename2,10);
		if (str1.length()>0){
			return false;
		}
		return true;
	}
	public static boolean outMd5sum(String filename1,String filename2) {
		if (filename1.length()==0)
			return false;
		if (filename2.length()==0)
			return false;
		String str1=cmdLinux("md5sum "+filename1,200);
		String str2=cmdLinux("md5sum "+filename2,200);
		int n1=str1.indexOf(" ");
		int n2=str2.indexOf(" ");
		if (n1>=0 && n2>=0) {
			if (str1.substring(0, n1).equalsIgnoreCase(str2.substring(0, n2))) {
				return true;
			}
		}
		return false;
	}
	public static boolean moveFile2same(String filename1) {
		File f=new File(filename1);
		String dir=f.getParent();
		String filename=f.getName();
		f=new File(dir+"/out");
		LogX.e(TAG,"mv ->"+f.getAbsolutePath());
		if (!f.exists()){
			f.mkdir();
		}
		if (!f.exists()){
			return false;
		}
		LogX.e(TAG,"mv "+filename1+" "+dir+"/same/"+filename);
		String str1=cmdLinux("mv "+filename1+" "+dir+"/same/"+filename,200);
		LogX.e(TAG,str1);
		return false;
	}
	public static String cmdLinux(String strCmd,int maxSize) {
		//strCmd = "chdir";
		String outline = "";
		int size=0;
		LogX.w(TAG,"[" + strCmd + "]");
		try {
			Process process = Runtime.getRuntime().exec(strCmd);
			BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = strCon.readLine()) != null) {
				//LogX.d(TAG,"CMDLOG: " + line);
				outline += line;
				if (maxSize>0) {
					size += line.length();
					if (size > maxSize) {
						break;
					}
				}
			}
			//Global.logd("CMDLOG: "+outline);
			return outline;
		} catch (Exception e) {
			LogX.e(TAG,e.getMessage());
			return "";
		}
	}

}
