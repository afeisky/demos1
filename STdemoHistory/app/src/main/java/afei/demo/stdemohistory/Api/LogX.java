package afei.demo.stdemohistory.Api;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * Created by chaofei on 18-1-17.
 */

public class LogX {

    private static File fileSave=null;
    private static final String filePathName="logSoh.txt";
    public LogX(){

    }
    public static void init(){
        File f = Environment.getExternalStorageDirectory();
        String workDir = f.getAbsolutePath() + Global.WORK_Dir;
        f = new File(workDir);
        if (!f.exists()) {
            f.mkdir();
        }
        fileSave = new File(f.getAbsolutePath() +"/"+ filePathName);
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
        Log.e(TAG, str);
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
