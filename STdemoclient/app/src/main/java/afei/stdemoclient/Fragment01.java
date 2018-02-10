package afei.stdemoclient;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import afei.api.LogX;


public class Fragment01 extends android.support.v4.app.Fragment {
    private static final String TAG = "STdownnow:Fragment01";
    private View  mView;
    private WebView mWebView;
    private View mLoading;
    private Intent mIntent;
    private TextView mMessageView;
    private ProgressBar mProgressbar;
    private Button editFrom;
    private Button editTo;
    private Button btnDown;
    private String htmldata="";

    private int mYear=2018;
    private int mMonth=1;
    private int mDay=1;

    private boolean isInit=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment01_1, container, false);
        LogX.e(TAG,"fragmemt01  onCreateView()");
        init();
        return mView;
    }

    public void init(){

    }

}
