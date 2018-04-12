package afei.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

        try {

            ArrayList<String> list=new ArrayList<>();
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
        return null;
    }

    public static String readLines(String filepathname,String encode) {
        String resultString="";
        File f=new File(filepathname);
        if (!f.exists()){
            return "";
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
    public static boolean writeLines(String filePathName,String lines,String encode){
        File f=new File(filePathName);
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePathName), encode);
            out.write(lines);
            out.close();
            return true;
        }catch (Exception e){
            LogX.e("FileX",e.getMessage());
            return false;
        }

    }
}
