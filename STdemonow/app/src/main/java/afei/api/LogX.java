package afei.api;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by chaofei on 18-1-17.
 */

public class LogX {

    private static File fileSave=null;
    private static final String filePathName="_logSoh1.txt";
    public LogX(){

    }
    public static void init(){
        init(Global.WORK_DIR,filePathName);
    }
    public static void init(String workdir,String filePathName){
        File f = Environment.getExternalStorageDirectory();
        Global.workPath = f.getAbsolutePath() + Global.WORK_DIR;
        f = new File(Global.workPath);
        if (!f.exists()) {
            f.mkdir();
        }
        //fileSave = new File(f.getAbsolutePath() +"/"+ filePathName);
        String snbk_now = Global.workPath + "/snbk_now";
        f = new File(snbk_now);
        if (!f.exists()) {
            f.mkdir();
        }
        fileSave = new File(f.getAbsolutePath() +"/"+ filePathName);
        LogX.d("LogX",fileSave.getAbsolutePath());
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

    private static void write2File(String TAG,String str){
        try {
            if (fileSave==null){
                init();
            }
            if (!fileSave.exists()) {
                fileSave.createNewFile();
            }
            if (fileSave.exists()){
                FileWriter writer = new FileWriter(fileSave.getAbsolutePath(), true);
                writer.write(TAG+" "+str+"\n");
                writer.close();
            }
        }catch (Exception e){
            Log.e("ERROR"," Fail to save log!"+e.getStackTrace());
        }
    }
    public static void d(String TAG, String str){
        Log.w(TAG, str);
        write2File(TAG,str);
    }
    public static void w(String TAG, String str){
        Log.w(TAG, str);
        write2File(TAG,str);
    }
    public static void e(String TAG, String str){
        Log.e(TAG, str);
        write2File(TAG,str);
    }

}
