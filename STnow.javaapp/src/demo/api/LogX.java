package demo.api;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by chaofei on 18-1-17.
 */

public class LogX {

    private static File fileSave=null;
    private static String filePathName="";
    public LogX(){
    }
    public static void init(String _filePathName){
        if (_filePathName!=null) {
            filePathName = _filePathName;
        }
        File f = null ;// Environment.getExternalStorageDirectory();//System.getProperty("user.dir")
        f = new File(filePathName);
        if (f.exists()) {
            f.delete();
        }
        if (!f.exists()) {
            if (f.getTotalSpace()>10000000){
                f.delete();
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("ERROR: Fail to save log!!"+e.getStackTrace());
                    }
                }
            }
            try {
                f.createNewFile();
            }catch (IOException e){
                System.out.println("ERROR: not create log file!");
            }
        }
        if (f.exists()) {
            fileSave = f;
            System.out.println("log file: "+fileSave.getAbsolutePath());
        }else{
            fileSave=null;
        }
    }


    private static void write2File_with_seek(String fileName, String content) {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);// move to
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write2File(String str){
        try {
            if (fileSave==null){
                init(null);
                return;
            }
            if (!fileSave.exists()){
                init(null);
            }
            if (fileSave.exists()){
                FileWriter writer = new FileWriter(fileSave.getAbsolutePath(), true);
                writer.write(str+"\n");
                writer.close();
            }
        }catch (Exception e){
            System.out.println("ERROR:Fail to save log!"+e.getMessage());
        }
    }
    public static void d(String TAG,String str){
        System.out.println("[D] "+TAG+" "+str);
        write2File("[D] "+TAG+" "+str+"\n");
    }
    public static void w( String TAG,String str){
        System.out.println("[W] "+TAG+" "+str);
        write2File("[W] "+TAG+" "+str+"\n");
    }
    public static void e(String TAG,String str){
        System.out.println("[E] "+TAG+" "+str);
        write2File("[E] "+TAG+" "+str+"\n");
    }

}
