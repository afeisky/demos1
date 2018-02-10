package afei.stdemodaily.Data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import afei.api.LogX;

/**
 * Created by chaofei on 17-12-18.
 */

public class DataCallback  implements Serializable {
    private static String TAG="STdemodaily:DataCallback";
    public static int TYPE_TIMERSERVICE_START=1;
    public static int TYPE_DAILYDOWN_START=10;
    public static int TYPE_DAILYDOWN_DO=11;
    public static int TYPE_DAILYDOWN_FINISH=12;
    public static int TYPE_HISTDOWN_START=21;
    public static int TYPE_HISTDOWN_FINISH=22;
    public int type=0;
    public String comment="";
    public int percent=0;

    public DataCallback(int _type, String _comment, int _percent){
        type=_type;
        comment=_comment;
        percent=_percent;
    }

    public static void updateUI(Context mContext,int type, String comment,int percent){
        LogX.w(TAG,"555:"+comment+",percent:"+percent);
        Intent intent = new Intent(Global.ACTION_UPDATE_UI);
        Bundle mBundle = new Bundle();
        DataCallback  dcb=new DataCallback(type, comment, percent);
        mBundle.putSerializable("data",dcb);
        intent.putExtras(mBundle);
        mContext.sendBroadcast(intent);
    }
    public static void updateUI(Context mContext,String comment){
        LogX.w(TAG,"666:"+comment);
        Intent intent = new Intent(Global.ACTION_UPDATE_UI);
        Bundle mBundle = new Bundle();
        DataCallback  dcb=new DataCallback(5, comment, 0);
        mBundle.putSerializable("data",dcb);
        intent.putExtras(mBundle);
        mContext.sendBroadcast(intent);
    }
}
