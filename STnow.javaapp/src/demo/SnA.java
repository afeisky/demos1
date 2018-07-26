package demo;

import demo.api.FileX;
import demo.api.Global;
import demo.api.LogX;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class SnA extends Thread{
    private static String TAG="SnA";
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat yymmdd_hhmmss = new SimpleDateFormat("yyMMdd_HHmmss");
    private static SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    private JSONObject json=new JSONObject();
    private JSONArray jarrA =new JSONArray();
    private JSONArray jarrGC =new JSONArray();
    private JSONArray jarrJJ =new JSONArray();
    private JSONArray jarrZS =new JSONArray();
    private int nurls=0;
    private int nurlext=0;
    public static String URL_LIST="http://quote.eastmoney.com/stocklist.html";
    public static String URL_HQ_LIST="http://hq.sinajs.cn/list=";//http://hq.sinajs.cn/list=sh600000,sh600001

    public static String namePRE ="sna,";
    private String nameEndFile = namePRE +"180720_150000.json";
    public static String nameFileList = namePRE +"filelist.txt";
    public static String nameReport = namePRE +"report.json";

    private String PathDir ="";
    private boolean needDownload=true;
    private String nowFilepathname ="";
    private int runStep=0; // 0: init;  1: init fail; 2: timeout; 9: success;
    public boolean isDone(){
        LogX.w(TAG, "isDone="+runStep);
        return (runStep>0);
    }

    private void setDone(int step){
        runStep=step;
    }
    public SnA(String Dir){
        PathDir =Dir;
        String stime="";
        try {
            stime=yymmdd_hhmmss.format(new Date());
        }catch (Exception e){
        }
        String filename= PathDir +"/"+ nameEndFile.replace("180720",stime.substring(0,6));
        LogX.w(TAG, filename);
        File f=new File(filename);
        if (f.isFile()){
            LogX.w(TAG, "exits 15:00 data");
            needDownload=false;
            return ;
        }
        return;
    }
    private boolean needDown(){
        String filepathname = Global.getDailyDir() + "/" + nameFileList;
        String lastname=FileX.readLastLine(filepathname,null);
        if (lastname.length()>10) {
            lastname = lastname.replace(namePRE, "");
            try {
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
            }catch (Exception e){
                e.printStackTrace();
                LogX.e(TAG,"lastname=["+lastname+"]");
            }
        }
        return true;
    }
    public void run(){
        setDone(0);
        if (!needDownload){
            setDone(1);
            return;
        }
        if (!needDown()){
            setDone(1);
            return;
        }
        File codelistfile=new File(PathDir +"/"+"codelist_es.json");
        if (!codelistfile.isFile()) {
            new DownCodelist().download(URL_LIST, codelistfile.getAbsolutePath(), "GB2312", new DownCb() {
                public void done(int retFlag, String retData) {
                    LogX.w(TAG, retFlag + "," + retData.substring(0, 100));
                    retData="";
                }
            });
        }
        if (codelistfile.isFile()) {
            ArrayList<String> codes=new DownCodelist().getCodes(codelistfile.getAbsolutePath());
            ArrayList<String> codenew=new ArrayList<String>();

            String str="";
            for (int i=0;i<codes.size();i++){
                //LogX.w(TAG,codes.get(i));
                String code=codes.get(i);
                String pre="";
                if (code.length()==6){
                    String ch=code.substring(0,1);
                    String ch3=code.substring(0,3);
                    if (ch.equals("0") || ch.equals("1") || ch.equals("3") ){
                        pre="sz";
                    }else if (ch.equals("2") || ch.equals("6") ){
                        if (ch3.equals("200")){
                            continue;
                        }
                        pre="sh";
                    }else { // 500001
                        continue;
                    }
                    codenew.add(pre+code);
                }
            }
            ArrayList<String> urls=new ArrayList<>();
            for (int i=0;i<codenew.size();i++){
                String code=codenew.get(i);
                if (i>0 && (i%500==0 || i==(codenew.size()-1) )) {
                    urls.add(URL_HQ_LIST + str);
                    str="";
                }
                if (str.length()>0){
                    str=str+",";
                }
                str=str+code;
            }
            codes.clear();
            ArrayList<String> clist=new ArrayList<String>();
            nurls=urls.size();
            json.put("time",yyyy_mm_dd_hhmmss.format(new Date()));
            String filepathname= PathDir +"/"+ namePRE +yyyymmdd.format(new Date())+".json";
            for (int i=0;i<urls.size();i++) {
                new DownTrd(urls.get(i), PathDir +"/"+"_sn"+i+".txt", "GB2312", new DownCb() {
                    public void done(int retFlag, String retData) {
                        //LogX.w(TAG, retFlag + "," + retData);
                        convertSN(retData);
                        retData="";
                        nurls--;
                        LogX.w(TAG, "nurls="+nurls);
                        finish();
                    }
                }).start();
            }
            nurlext=1;
            new DownTrd(getSnSstr(), PathDir +"/"+"_snZS.txt", "GB2312", new DownCb() {
                public void done(int retFlag, String retData) {
                    //LogX.w(TAG, retFlag + "," + retData);
                    convertSnZS(retData);
                    retData="";
                    nurlext--;
                    LogX.w(TAG, "nurlext="+nurlext);
                    finish();
                }
            }).start();
            start_finish();
            LogX.w(TAG, "Done");
        }
    }
    private void finish(){
        if (timeout){
            LogX.w(TAG, "timeout");
            setDone(2);
            return;
        }
        if (nurls==0 && nurlext==0){
            //json.put("count", jarrSHA.length());
            json.put("a", jarrA);
            json.put("gc", jarrGC);
            json.put("jj", jarrJJ);
            json.put("zs", jarrZS);
            //LogX.w(TAG, "==" + json.toString());
            String stime=json.getString("time");
            try {
                stime=yymmdd_hhmmss.format(yyyy_mm_dd_hhmmss.parse(stime));
            }catch (Exception e){
                e.printStackTrace();
            }
            nowFilepathname = PathDir +"/"+ namePRE +stime+".json";
            LogX.w(TAG, "nowFilepathname==" + nowFilepathname);
            FileX.writeLines(nowFilepathname,json.toString(),"");
            String filepathname= PathDir +"/"+ nameFileList;
            FileX.add2File(filepathname, namePRE +stime+".json\n");
            LogX.w(TAG, "finish");
            setDone(9);
        }
    }
    private boolean timeout=false;
    private void start_finish(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                timeout=true;
            }
        }, 20*1000);
    }
    private void convertSN(String retData){
        for (int i=0;i<1;){
            if (retData.length()<10){
                break;
            }
            int pos=retData.indexOf("\n");
            String line=retData.substring(0,pos);
            retData=retData.substring(pos+1,retData.length());
            String key="var hq_str_";
            //LogX.w(TAG, "line=" + line + ";" + line.length());
            if (line.length()<key.length()){
                continue;
            }
            boolean has=line.substring(0,key.length()).equals(key);
            if (line.length()>key.length()+"sh600000=\"\";".length() && has){
                String[] lines= line.split("=");
                if (lines.length==2) {
                    line = lines[0].replace(key, "");
                    String shsz = line.substring(0, 2);
                    String code = line.substring(2, line.length());
                    //
                    line = lines[1].replace(";", "").replace("\"", "");

                    String[] vs = line.split(",");
                    String name = vs[0];
                    //LogX.d(TAG, "-->" + shsz + ";" + code + ";" + name+";"+vs.toString());
                    JSONArray ja=new JSONArray();

                    int n=0;
                    ja.put(n++,code);
                    ja.put(n++,name);
                    for (int k=1;k<vs.length;k++) {
                        ja.put(n++, vs[k]);
                    }
                    String ch=code.substring(0,1);
                    String ch2=code.substring(0,2);
                    if (ch.equals("6") || ch.equals("0") || ch.equals("3")){
                        jarrA.put(ja);
                        if (code.equals("601988")){
                            json.put("time",vs[30]+" "+vs[31]);
                        }
                    }else if(ch2.equals("20") || ch2.equals("13")) {
                        jarrGC.put(ja);
                    }else{
                        jarrJJ.put(ja);
                    }

                }
            }
            ////break;
        }
    }

    private void convertSnZS(String retData){
        for (int i=0;i<1;){
            if (retData.length()<10){
                break;
            }
            int pos=retData.indexOf("\n");
            String line=retData.substring(0,pos);
            retData=retData.substring(pos+1,retData.length());
            String key="var hq_str_s_";
            //LogX.w(TAG, "line=" + line + ";" + line.length());
            if (line.length()<key.length()){
                continue;
            }
            boolean has=line.substring(0,key.length()).equals(key);
            if (line.length()>key.length()+"sh000088=\"\";".length() && has){
                String[] lines= line.split("=");
                if (lines.length==2) {
                    line = lines[0].replace(key, "");
                    String shsz = line.substring(0, 2);
                    String code = line.substring(2, line.length());
                    //
                    line = lines[1].replace(";", "").replace("\"", "");

                    String[] vs = line.split(",");
                    String name = vs[0];
                    //LogX.d(TAG, "-->" + shsz + ";" + code + ";" + name+";"+vs.toString());
                    JSONArray ja=new JSONArray();

                    int n=0;
                    ja.put(n++,code);
                    ja.put(n++,name);
                    for (int k=1;k<vs.length;k++) {
                        ja.put(n++, vs[k]);
                    }
                    jarrZS.put(ja);
                }
            }
            ////break;
        }
    }
    /*
    public void json2single(){
        //
        String filepathname= PathDir+"/"+dailyLastName;
        filepathname=PathDir+"/"+FileX.getJsonString(filepathname,"utf-8");
        if (filepathname.length()==0){
            return;
        }
        if (!new File(filepathname).isFile()){
            return;
        }
        String jsondate=FileX.getJsonString(filepathname,"utf-8");
        JSONObject json=new JSONObject(jsondate);
        JSONArray jarrA=json.getJSONArray("list");
        for (int i=0;i<jarrA.length();i++){
            JSONArray ja=jarrA.getJSONArray(i);
            JSONArray jan=new JSONArray();
            if (ja.length()>10) {
                for (int k = 0; k < 11; k++) {
                    jan.put(k, ja.getString(k));
                }
            }
            jarrA.put(i,jan);
        }
        filepathname= PathDir+"/"+dailyLastName;
        FileX.writeLines(filepathname,json.toString(),"");
        jsondate="";
        json=null;
    }
    */
    public String getSnSstr(){
        String cs="";
        DecimalFormat df = new DecimalFormat ("00");
        for (int i=1;i<100;i++){
            if (i>1){
                cs+=",";
            }
            cs+="s_sh0000"+df.format(i);
        }
        for (int i=1;i<100;i++){
            cs+=",";
            cs+="s_sz3990"+df.format(i);
        }
        cs=URL_HQ_LIST+cs;
        //LogX.d(TAG,"-> "+cs);
        return cs;
    }
    public boolean start3(){
        String cs="";
        DecimalFormat df = new DecimalFormat ("00");
        for (int i=1;i<100;i++){
            if (i>1){
                cs+=",";
            }
            cs+="s_sh0000"+df.format(i);
        }
        for (int i=1;i<100;i++){
            cs+=",";
            cs+="s_sz3990"+df.format(i);
        }
        //LogX.d(TAG,"-> "+cs);
        new DownTrd(URL_HQ_LIST+cs, PathDir +"/"+"_snzs"+".txt", "GB2312", new DownCb() {
            public void done(int retFlag, String retData) {
                //LogX.w(TAG, retFlag + "," + retData);
                retData="";
            }
        }).run();
        return true;
    }

/*
    public boolean start1(){
        System.out.println("--->>");
        String url = URL_LIST;
        String fileName=Global.workPath+"/aa.txt";
        try {
            Document document = Jsoup.connect(url).get();

            if (false) {
                document = Jsoup.connect(url)
                        .data("query", "Java")
                        .userAgent("Mozilla")
                        .cookie("auth", "token")
                        .timeout(3000)
                        .post();
            }
            Elements ListDiv = document.getElementsByAttributeValue("class","quotebody");
            //System.out.println(ListDiv);
            if (ListDiv == null) {
                return false;
            }

            JSONArray list = new JSONArray();
            String url1="";
            for (Element element :ListDiv) {
                Elements links = element.getElementsByTag("a");
                for (Element link : links) {
                    //String linkHref = link.attr("href");
                    if (url1.length()<20){
                        url1= link.attr("href");
                    }
                    String linkText = link.text().trim();
                    //System.out.println(linkHref);
                    //System.out.println(linkText);
                    int n=linkText.indexOf("(");
                    int k=linkText.indexOf(")");
                    JSONArray array = new JSONArray();
                    if (n>0){
                        array.put(linkText.substring(n+1,k));
                        array.put(linkText.substring(0,n));
                        //System.out.println(linkText.substring(0,n)+","+linkText.substring(n+1,k));
                        list.put(array);
                    }
                }
            }
            JSONObject json = new JSONObject();
            json.put("url",url1);
            json.put("time",yyyy_mm_dd_hhmmss.format(new Date()));
            json.put("list",list);
            //System.out.println(json);
            OutputStreamWriter out = null;
            boolean save2File = false;
            save2File = true;
            if (save2File) {
                out = new OutputStreamWriter(new FileOutputStream(fileName));
                out.write(json.toString());
            }
            if (out != null) {
                out.close();
            }
            return false;

        } catch (Exception e) {
            LogX.e(TAG,  e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
*/
    JSONObject jsonDayp = new JSONObject();
    String temp="";
    String jsondate="";
    public JSONObject getParseData() {
        String ffilelist = PathDir + "/" + nameFileList;
        ArrayList<String> sfilelist = FileX.readLineList(ffilelist, null);
        int startline = 0;
        int endline = 0;
        if (sfilelist != null) {
            if (sfilelist.size() > 0) {
                endline = sfilelist.size();
            }
        }
        for (int i=startline;i<endline;i++){
            //LogX.w(TAG, sfilelist.get(i));
            //getper10u()
            //getper44()
            //getper10d()
            //dayp1(PathDir + "/" + sfilelist.get(i));
            if (i==endline-1){
                String filepathname= PathDir + "/" + sfilelist.get(i);
                if (filepathname.length()>0){
                    if (new File(filepathname).isFile()){
                        String[] zs={"000001","399001","399006","399007","399008"};
                        String jsondate=FileX.getJsonString(filepathname,"utf-8");
                        JSONObject json=new JSONObject(jsondate);
                        if (json.getJSONArray("a").length()>0) {
                            parseSNa(json);
                            parseSNgc(json);
                            parseZS(json);
                        }
                    }
                }
            }
        }
        //LogX.d(TAG, jsonDayp.toString());
        //String filepathname= PathDir+"/"+nameReport;
        //FileX.writeLines(filepathname,jsonDayp.toString(),"");
        return jsonDayp;
    }

    private int parseZS(JSONObject json){
        String[] zs={"000001","399001","399006","399007","399008"};
        JSONArray jarr=json.getJSONArray("zs");
        JSONArray jarrN=new JSONArray();
        for (int i=0;i<jarr.length();i++){
            JSONArray ja=jarr.getJSONArray(i);
            //JSONArray jan=new JSONArray();
            for (int k=0;k<zs.length;k++){
                if (zs[k].equals(ja.get(0))){
                    jarrN.put(jarrN.length(),ja);
                }
            }
        }
        jsonDayp.put("zs",jarrN);
        return 0;
    }
    private int parseSNgc(JSONObject json){
        String[] zs={"204001","131810"};
        JSONArray jarr=json.getJSONArray("gc");
        JSONArray jarrN=new JSONArray();
        for (int i=0;i<jarr.length();i++){
            JSONArray ja=jarr.getJSONArray(i);
            JSONArray jan=new JSONArray();
            for (int k=0;k<zs.length;k++){
                if (zs[k].equals(ja.get(0))){
                    for (int j=0;j<ja.length() &&j<11 ; j++){
                        jan.put(j,ja.getString(j));
                    }
                    jarrN.put(jarrN.length(),jan);
                }
            }
        }
        jsonDayp.put("gc",jarrN);
        return 0;
    }
    private int parseSNa(JSONObject json){

        JSONArray jarrA=json.getJSONArray("a");
        //u10
        JSONArray jarrN=new JSONArray();
        int nU10=0;
        JSONArray jarrD=new JSONArray();
        int nD10=0;
        int i=0;
        try {
            LogX.e(TAG, "++++++++++  len= "+jarrA.length());
            for (i = 0; i < jarrA.length(); i++) {
                JSONArray ja = jarrA.getJSONArray(i);
                JSONArray jan = new JSONArray();
                if (ja!=null){
                    if (ja.length() > 10) {
                        String code = ja.getString(0);
                        double last = ja.getDouble(3);
                        double now = ja.getDouble(4);
                        double now1 = (last * 1.1 * 100) / 100;
                        now1 = (double) Math.round(now1 * 100) / 100;
                        //LogX.w(TAG, i+": "+ja.getString(0)+"  ; "+last+"  ? "+now+";  "+now1);
                        if (last > 0 && now > 0) {
                            if (now >= now1) {
                                //LogX.w(TAG, last+"  =10");
                                for (int j = 0; j < ja.length() && j < 11; j++) {
                                    jan.put(j, ja.getString(j));
                                }
                                jarrN.put(nU10++, jan);
                                //LogX.w(TAG, "="+jan.toString());
                            } else {
                                now1 = (last * 0.9 * 100) / 100;
                                now1 = (double) Math.round(now1 * 100) / 100;
                                //LogX.w(TAG, i+": "+ja.getString(0)+"  ; "+last+"  ? "+now+";  "+now1);
                                if (now <= now1) {
                                    for (int j = 0; j < ja.length() && j < 11; j++) {
                                        jan.put(j, ja.getString(j));
                                    }
                                    jarrN.put(jarrN.length(), jan);
                                    jarrD.put(nD10++, jan);
                                }
                            }
                        }
                    }
                }
                jsonDayp.put("time", json.getString("time"));
            }
        }catch (Exception e){
            e.printStackTrace();
            LogX.e(TAG, "Error: "+e.getMessage());
            //LogX.e(TAG, "++++++++++  "+i+"  , "+json.toString());
            //FileX.add2File(temp+"_temp1.json",jsondate);

        }
        jsonDayp.put("u10",jarrN);
        jsonDayp.put("d10",jarrD);
        return 0;
    }

    public String getLastname(){
        return  FileX.readLastLine(PathDir +"/"+ nameFileList,null);
    }
}
