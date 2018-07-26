package demo.api;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by chaofei on 18-1-16.
 */

public class FileX {
    private static final String TAG="FileX";
    public static String readLines1(InputStream is){
        try {
            StringBuilder sb2 = new StringBuilder();
            String succeedStr = new String(sb2.toString().getBytes("iso8859-1"), "UTF-8");
            return succeedStr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static StringBuilder readLines(String filepathname){

        //BufferedReader reader = new BufferedReader(new InputStreamReader(is),"UTF-8");

        String line = null;

        StringBuilder sb2 = new StringBuilder();
        //String succeedStr = new String(sb2.toString().getBytes("iso8859-1"),"UTF-8");
        InputStream is=null;
        StringBuilder sb=new StringBuilder();
        File f=new File(filepathname);
        if (!f.exists()){
            return sb;
        }
        try {
            is=new FileInputStream(f.getAbsolutePath());
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(isr);
            int n=0,count=0;
            while ((line = bf.readLine()) != null) {
                sb.append(line); //sb.append(line+ "/n");
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb;
    }
    public static ArrayList<String> readLines(InputStream is,String encode) {
        ArrayList<String> list=new ArrayList<>();
        try {
            if (encode==null){
                encode="utf-8";
            }
            String lines;
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    list.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static ArrayList<String> readLineList(String filepathname,String encode) {
        String resultString="";
        File f=new File(filepathname);
        if (!f.exists()){
            return null;
        }
        if (encode==null){
            encode="utf-8";
        }
        ArrayList<String> list=new ArrayList<>();
        InputStream is=null;
        try {

            String lines;
            is=new FileInputStream(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    list.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.getMessage();
            }
            return list;
        }
    }

    public static String readLines(String filepathname,String encode) {
        String resultString="";
        File f=new File(filepathname);
        if (!f.exists()){
            return "";
        }
        InputStream is=null;
        try {
            if (encode==null){
                encode="utf-8";
            }
            if (encode.trim().length()==0){
                encode="utf-8";
            }
            ArrayList<String> list=new ArrayList<>();
            String lines;
            is=new FileInputStream(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    resultString+=line;
                }
            }
            return resultString;
        } catch (IOException e) {
            e.getMessage();
        }finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return "";
    }
    public static String readOneLines(String filepathname,String encode) {
        String resultString="";
        File f=new File(filepathname);
        if (!f.exists()){
            return "";
        }
        if (encode==null){
            encode="utf-8";
        }
        if (encode.trim().length()==0){
            encode="utf-8";
        }
        InputStream is=null;
        try {
            ArrayList<String> list=new ArrayList<>();
            String lines;
            is=new FileInputStream(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    resultString+=line;
                    break;///only first line
                }
            }
            return resultString;
        } catch (IOException e) {
            e.getMessage();
        }finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return "";
    }
    public static String readLastLine(String filepathname,String encode) {
        String resultString="";
        File f=new File(filepathname);
        if (!f.exists()){
            return "";
        }
        if (encode==null){
            encode="utf-8";
        }
        if (encode.length()==0){
            encode="utf-8";
        }
        InputStream is=null;
        try {
            ArrayList<String> list=new ArrayList<>();
            String lines;
            is=new FileInputStream(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    resultString=line;//only last line
                }
            }
            return resultString;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return "";
    }
    public static boolean writeLines(String filePathName,String lines,String encode){
        File f=new File(filePathName);
        try {
            if (encode==null){
                encode="utf-8";
            }
            if (encode.length()==0){
                encode="utf-8";
            }
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePathName), encode);
            out.write(lines);
            out.close();
            return true;
        }catch (Exception e){
            LogX.e("FileX",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void add2File(String filePathName, String content) {
        try {
            FileWriter writer = new FileWriter(filePathName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJsonString(String filepathname,String encode){
        String data="";
        //String filepathname=Global.workPath+"/snbk_now/gnbk_180125_174529.json";
        LogX.w(TAG,"get="+filepathname);
        if (encode==null){
            encode="utf-8";
        }else if (encode.length()==0){
            encode="utf-8";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepathname),encode));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            bf.close();
            data=stringBuilder.toString();
            return data;
        }catch (Exception e){
            LogX.e(TAG,"Error: getJsonString() "+e.getStackTrace());
        }
        return data;
    }

    public static boolean createNewFile(File f){
        try {
            f.createNewFile();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}