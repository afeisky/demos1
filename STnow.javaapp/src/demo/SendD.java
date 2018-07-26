package demo;

import demo.api.*;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendD {
    private static String TAG = "SendD";


    public void send(SnA sna, SnBK snbk) {
        String cmdkey = "snnow,";

        String sna_lastfn1 = sna.getLastname(); //sndaily,180725_140502.json
        String snbk_lastfn2 = snbk.getLastname(); //snbk,180725_103056.json

        String sendfilelist = Global.getDailyDir() + "/" + cmdkey + "1," + ".send";
        String cmd_lastfn = FileX.readLastLine(sendfilelist, null);
        LogX.w(TAG, sna_lastfn1);
        LogX.w(TAG, snbk_lastfn2);
        LogX.w(TAG, cmd_lastfn);
        boolean needSendNow=true;
        if (cmd_lastfn.length() > 0) {
            if (cmd_lastfn.indexOf(sna_lastfn1) >= 0 && cmd_lastfn.indexOf(snbk_lastfn2) >= 0) {
                LogX.w(TAG, "exist , has send.");
                needSendNow=false;
            }
        }
        cmd_lastfn=sna_lastfn1+";"+snbk_lastfn2;
        if (needSendNow) {
            String subject = cmdkey + "1," + TimeX.get_yymmdd_hhmmss();
            String content = "";
            String allReport = Global.getDailyDir() + "/" + subject + ".json";
            JSONObject json = sna.getParseData();
            if (json == null) {
                LogX.w(TAG, "fail, no data!");
                return;
            }
            json.put("snbk", snbk.getParseJson());
            FileX.writeLines(allReport, json.toString(), null);

            String zipname=Global.getDailyDir() + "/" + subject + ".zip";
            File fzip = new File(zipname);
            if (fzip.exists()) {
                fzip.delete();
                LogX.w(TAG, "222.");
            }
            LogX.w(TAG, "333."+zipname);
            if (!fzip.isFile()) {
                LogX.w(TAG, "444.");
                List<File> files = new ArrayList<File>();
                files.add(new File(allReport));
                try {
                    new ZipUtils().zipFiles(files, fzip);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogX.e(TAG, e.getMessage());
                }
                LogX.w(TAG, "555." + fzip.getAbsolutePath());
            }
            if (fzip.exists()) {
                if (new MailCmd().cmdReponse(subject, content, MailCmd.getFileList(fzip.getAbsolutePath()), "AAnow", cmdkey) == 0) {
                    FileX.add2File(sendfilelist, cmd_lastfn + "\n");
                    LogX.d(TAG, "A send success," + fzip.getName());
                } else {
                    LogX.w(TAG, "A send fail,");
                }
            }else {
                LogX.w(TAG, "A send fail. ");
            }

        }else{
            LogX.w(TAG, "A exist,");
        }
        String hhmm=new SimpleDateFormat("HHmm").format(new Date());
        int hm=Integer.valueOf(hhmm);
        if (hm>1505) {
            cmdkey="sndaily,";
            String sdate=new File(Global.getDailyDir()).getName();
            sendfilelist = Global.getDailyDir() + "/" + cmdkey +sdate+ ".send";
            String filepathname = sendfilelist.replace(".send",".json");
            cmd_lastfn = FileX.readLastLine(sendfilelist, null);
            LogX.w(TAG, sna_lastfn1);
            LogX.w(TAG, snbk_lastfn2);
            LogX.w(TAG, cmd_lastfn);
            boolean needSendDaily=true;
            if (cmd_lastfn.length() > 0) {
                if (cmd_lastfn.indexOf(sna_lastfn1) >= 0 && cmd_lastfn.indexOf(snbk_lastfn2) >= 0) {
                    LogX.w(TAG, "exist , has send.");
                    needSendDaily=false;
                }
            }
            cmd_lastfn=sna_lastfn1+";"+snbk_lastfn2;
            if (needSendDaily) {
                LogX.w(TAG, "111.");
                String sname=sna_lastfn1.replace(SnA.namePRE,cmdkey);
                sname=sname.replace(".json",".zip");
                String zipname = Global.getDailyDir() + "/" + sname;

                String subject = sname ;  //TimeX.get_yyyymmdd();
                String content = "";

                String a_f=Global.getDailyDir() + "/" + sna_lastfn1;
                String bk_f=Global.getDailyDir() + "/" + snbk_lastfn2;

                File fzip = new File(zipname);
                if (fzip.exists()) {
                    fzip.delete();
                    LogX.w(TAG, "222.");
                }
                LogX.w(TAG, "333."+zipname);
                if (!fzip.isFile()) {
                    LogX.w(TAG, "444.");
                    List<File> files = new ArrayList<File>();
                    files.add(new File(a_f));
                    files.add(new File(bk_f));
                    try {
                        new ZipUtils().zipFiles(files, fzip);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogX.e(TAG, e.getMessage());
                    }
                    LogX.w(TAG, "555." + fzip.getAbsolutePath());
                }
                if (fzip.exists()) {
                    if (new MailCmd().cmdReponse(subject, content, MailCmd.getFileList(fzip.getAbsolutePath()), "AAdaily", cmdkey) == 0) {
                        FileX.add2File(sendfilelist, cmd_lastfn + "\n");
                        LogX.d(TAG, "daily send success," + fzip.getAbsolutePath());
                    } else {
                        LogX.w(TAG, "daily send fail,");
                    }
                }else {
                    LogX.w(TAG, "daily send fail. ");
                }
            }else{
                LogX.w(TAG, "daily exist,");
            }
        }
    }


    public void send000(){
        String cmdkey="snnow,";
        String sna_last=Global.getDailyDir()+"/"+ SnA.nameReport;

        String dailyreport=Global.getDailyDir()+"/"+ SnA.nameReport;
        String subject= cmdkey+"1,"+TimeX.get_yymmdd_hhmmss();
        String content="";
        String allReport=Global.getDailyDir()+"/"+subject+".json";
        String sendreport=Global.getDailyDir()+"/"+cmdkey+"1,"+".send";

        String filelist=Global.getDailyDir()+"/"+ SnA.nameFileList;
        String filelistlast=FileX.readLastLine(filelist,null);

        LogX.w(TAG,sendreport);
        File fs=new File(sendreport);
        if (fs.isFile()){
            // find lastfilename is in send list.
            String lastLine=FileX.readLastLine(sendreport,null);
            filelistlast=FileX.readLastLine(filelist,null);
            //str=lastLine.replace(cmdkey+"1,","").substring(7,7+4);
            LogX.w(TAG,filelistlast+" / "+lastLine);
            if (lastLine.length()>1 && filelistlast.length()>1){
                if (lastLine.equals(filelistlast)){
                    LogX.w(TAG,"exist , has send.");
                    return;
                }
                //int hm=Integer.valueOf(str);
                //if (hm<800 || hm>1510){
                //    LogX.w(TAG,"exist , return");
                //    return;
                //}
            }
        }
        JSONObject json=new SnA(Global.getDailyDir()).getParseData();
        if (json==null){
            LogX.w(TAG,"fail, no data!");
            return;
        }
        json.put("snbk",new SnBK(Global.getDailyDir()).getParseJson());
        FileX.writeLines(allReport,json.toString(),null);
        if (new MailCmd().cmdReponse(subject,content,MailCmd.getFileList(allReport),"AAnow",cmdkey)==0){
            FileX.add2File(sendreport,new File(allReport).getName()+"\n");
            LogX.d(TAG,"success , return"+fs.getAbsolutePath());
        }else{
            LogX.w(TAG,"fail , return");
        }

    }

    public void sendDailyReport(){
        String cmdkey="DRP,";

    }
}
