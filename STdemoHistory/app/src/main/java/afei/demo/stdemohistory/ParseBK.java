package afei.demo.stdemohistory;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by chaofei on 18-1-18.
 */

public class ParseBK {
    private String TAG = "[ParseBK]";

    public void start(Context context){

        File f = Environment.getExternalStorageDirectory();
        String workDir = f.getAbsolutePath() + "/DownloadData";
        f = new File(workDir);
        if (!f.exists()) {
            f.mkdir();
        }
        String sohuDir = f.getAbsolutePath() + "";
        Logw("dir="+sohuDir);
        f = new File(sohuDir);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    private void Logd(String str){
        Log.e(TAG, str);
    }
    private void Logw(String str){
        Log.w(TAG, str);
    }
    private void Loge(String str){
        Log.e(TAG, str);
    }

}
