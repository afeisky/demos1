package afei.stdemonow;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import afei.api.LogX;


public class MainActivity1 extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "STdownnow:MainActivity";
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdpter;
    private List<Fragment> mFragments = new ArrayList<>();
  
  
    //四个Tab每个Tab都有一个按钮  
    private LinearLayout mTabMyCircle;
    private LinearLayout mTabMyDiscovery;  
    private LinearLayout mTabMyMsg;  
    private LinearLayout mTabMyCenter;  
    //四个按钮  
    private ImageButton myCircleImg;
    private ImageButton myDiscoveryImg;  
    private ImageButton myMsgImg;  
    private ImageButton myCenterImg;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (hasGrantExternalRW(this)) {
            init();
        }
    }

    private void init(){
        if (hasGrantExternalRW(this)) {
            initViews();//初始化控件
            initEvent();//监听逻辑事件
            initViewPages();//初始化viewpager
        }
    }
    private void initViews() {  
  
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);  
        //初始化四个布局  
        mTabMyCircle = (LinearLayout) findViewById(R.id.id_tab_mycircle);  
        mTabMyDiscovery = (LinearLayout) findViewById(R.id.id_tab_discovery);  
        mTabMyMsg = (LinearLayout) findViewById(R.id.id_tab_message);  
        mTabMyCenter = (LinearLayout) findViewById(R.id.id_tab_setting);  
        //初始化四个按钮  
        myCircleImg = (ImageButton) findViewById(R.id.id_tab_mycirlceImg);  
        myDiscoveryImg = (ImageButton) findViewById(R.id.id_tab_discovery_img);  
        myMsgImg = (ImageButton) findViewById(R.id.id_tab_message_img);  
        myCenterImg = (ImageButton) findViewById(R.id.id_tab_setting_img);  
    }
    private Fragment01 tab01 = new Fragment01();
    private Fragment02 tab02 = new Fragment02();
    private void initViewPages() {  
        //初始化四个布局  

        Fragment03 tab03 = new Fragment03();  
        Fragment04 tab04 = new Fragment04();  
        mFragments.add(tab01);  
        mFragments.add(tab02);  
        mFragments.add(tab03);  
        mFragments.add(tab04);  
        //初始化Adapter这里使用FragmentPagerAdapter  
        mAdpter = new FragmentPagerAdapter(getSupportFragmentManager()) {  
            @Override  
            public Fragment getItem(int position) {  
  
                return mFragments.get(position);  
            }  
  
            @Override  
            public int getCount() {  
                return mFragments.size();  
            }  
  
  
        };  
        mViewPager.setAdapter(mAdpter);
    }  
  
  
    private void initEvent() {  
        //设置监听器  
        myCircleImg.setOnClickListener(this);  
        myDiscoveryImg.setOnClickListener(this);  
        myMsgImg.setOnClickListener(this);  
        myCenterImg.setOnClickListener(this);  
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override  
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {  
  
            }  
  
            @Override  
            public void onPageSelected(int position) {  
                //当viewPager滑动的时候  
                int currentPage = mViewPager.getCurrentItem();  
                switch (currentPage) {  
                    case 0:
                        selectFragment(0);
                        break;  
                    case 1:
                        selectFragment(1);
                        break;  
                    case 2:
                        selectFragment(2);
                        break;  
                    case 3:
                        selectFragment(3);
                        break;  
                    default:  
                        break;  
                }  
  
            }  
  
            @Override  
            public void onPageScrollStateChanged(int state) {  
  
            }  
        });  
  
  
    }  
  
    private void reSetImg() {  
        myCircleImg.setImageResource(R.drawable.mainpage_tab_mycircle_normal);  
        myDiscoveryImg.setImageResource(R.drawable.mainpage_tab_discovery_normal);  
        myMsgImg.setImageResource(R.drawable.mainpage_tab_message_normal);  
        myCenterImg.setImageResource(R.drawable.mainpage_tab_setting_normal);  
  
    }  
  
  
    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {  
            case R.id.id_tab_mycirlceImg:  
                mViewPager.setCurrentItem(0);
                selectFragment(0);
                break;  
            case R.id.id_tab_discovery_img:  
                mViewPager.setCurrentItem(1);
                selectFragment(1);
                break;  
            case R.id.id_tab_message_img:  
                mViewPager.setCurrentItem(2);
                selectFragment(2);
                break;  
            case R.id.id_tab_setting_img:  
                mViewPager.setCurrentItem(3);
                selectFragment(3);
                break;  
            default:  
                break;
        }
    }

    private void selectpage(int id){
        LogX.w(TAG,"selectpage id="+id);
        if (id==0){

        }else{

        }

        if (id==1){
            tab02.startUpdate();
        }else{
            tab02.stopUpdate();
        }

    }
    private void selectFragment(int id){
        selectpage(id);
        switch (id) {
            case 0:
                reSetImg();
                myCircleImg.setImageResource(R.drawable.mainpage_tab_mycircle_selected);
                LogX.w(TAG,"fragmemt01");
                break;
            case 1:
                reSetImg();
                myDiscoveryImg.setImageResource(R.drawable.mainpage_tab_discovery_selected);
                break;
            case 2:
                reSetImg();
                myMsgImg.setImageResource(R.drawable.mainpage_tab_message_selected);
                break;
            case 3:
                reSetImg();
                myCenterImg.setImageResource(R.drawable.mainpage_tab_setting_selected);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogX.w(TAG,"onSaveInstanceState()");
        tab02.stopUpdate();
        super.onSaveInstanceState(outState);
    }


    private int PERMISSIONS_CODE = 1;

    public boolean hasGrantExternalRW(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            LogX.w(TAG, "hasGrantExternalRW--11-");
            return false;
        }
        LogX.w(TAG, "hasGrantExternalRW--22-");
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogX.w(TAG, "onRequestPermissionsResult--1-");
        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                LogX.w(TAG, "READ_EXTERNAL_STORAGE--2-");
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    LogX.d(TAG, "READ_EXTERNAL_STORAGE--3-");
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        LogX.w(TAG, "READ_EXTERNAL_STORAGE--4-");
                        init();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
                        LogX.w(TAG, "READ_EXTERNAL_STORAGE--5-");
                        //mWebView.loadData("Please set app right!", "text/html", "utf8");
                    }
                }
            }
        }
    }

}  