package afei.stdemoclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import afei.api.Global;
import afei.api.LogX;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "STdownnow:MainActivity";
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdpter;
    private List<Fragment> mFragments = new ArrayList<>();
  
  
    //四个Tab每个Tab都有一个按钮  
    private LinearLayout id_tab_b_1;
    private LinearLayout id_tab_b_2;
    private LinearLayout id_tab_b_3;
    private LinearLayout id_tab_b_4;
    private LinearLayout id_tab_b_5;
    //四个按钮  
    private ImageButton id_tab1_page1;
    private ImageButton id_tab1_page2;
    private ImageButton id_tab1_page3;
    private ImageButton id_tab1_page4;

    private ImageButton id_tab_img1;
    private ImageButton id_tab_img2;
    private ImageButton id_tab_img3;
    private ImageButton id_tab_img4;
    private ImageButton id_tab_img5;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Global.init();
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

        //初始化四个按钮
        id_tab1_page1 = (ImageButton) findViewById(R.id.id_tab1_page1);
        id_tab1_page2 = (ImageButton) findViewById(R.id.id_tab1_page2);
        id_tab1_page3 = (ImageButton) findViewById(R.id.id_tab1_page3);
        id_tab1_page4 = (ImageButton) findViewById(R.id.id_tab1_page4);

        //初始化四个布局
        //id_tab_b_1 = (LinearLayout) findViewById(R.id.id_tab_b_1);
        //id_tab_b_2 = (LinearLayout) findViewById(R.id.id_tab_b_2);
        //id_tab_b_3 = (LinearLayout) findViewById(R.id.id_tab_b_3);
        //id_tab_b_4 = (LinearLayout) findViewById(R.id.id_tab_b_4);
        //id_tab_b_5 = (LinearLayout) findViewById(R.id.id_tab_b_5);
        //id_tab_img1 = (ImageButton) findViewById(R.id.id_tab_img1);
        //id_tab_img2 = (ImageButton) findViewById(R.id.id_tab_img2);
        //id_tab_img3 = (ImageButton) findViewById(R.id.id_tab_img3);
        //id_tab_img4 = (ImageButton) findViewById(R.id.id_tab_img4);
        //id_tab_img5 = (ImageButton) findViewById(R.id.id_tab_img5);
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

        //selectButtonTab(0);////
        id_tab1_page1.setImageResource(R.mipmap.maintab_icon_1_selected);
    }  
  
  
    private void initEvent() {

        //id_tab_b_1.setOnClickListener(this);
        //id_tab_b_2.setOnClickListener(this);
        //id_tab_b_3.setOnClickListener(this);
        //id_tab_b_4.setOnClickListener(this);
        //id_tab_b_5.setOnClickListener(this);

        //id_tab_img1.setOnClickListener(this);
        //id_tab_img2.setOnClickListener(this);
        //id_tab_img3.setOnClickListener(this);
        //id_tab_img4.setOnClickListener(this);
        //id_tab_img5.setOnClickListener(this);
        //设置监听器  
        id_tab1_page1.setOnClickListener(this);
        id_tab1_page2.setOnClickListener(this);
        id_tab1_page3.setOnClickListener(this);
        id_tab1_page4.setOnClickListener(this);

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
                        LogX.w(TAG,"onPageSelected(): " +currentPage);
                        selectTab1Fragment(0);
                        break;  
                    case 1:
                        LogX.w(TAG,"onPageSelected(): " +currentPage);
                        selectTab1Fragment(1);
                        break;  
                    case 2:
                        selectTab1Fragment(2);
                        break;  
                    case 3:
                        selectTab1Fragment(3);
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
    private void resetTab1PageImg() {
        id_tab1_page1.setImageResource(R.mipmap.maintab_icon_1_normal);
        id_tab1_page2.setImageResource(R.mipmap.maintab_icon_2_normal);
        id_tab1_page3.setImageResource(R.mipmap.maintab_icon_3_normal);
        id_tab1_page4.setImageResource(R.mipmap.maintab_icon_4_normal);
    }

    private void resetBottomTabImg() {
        //id_tab_img1.setImageResource(R.mipmap.maintab_icon_1_normal);
        //id_tab_img2.setImageResource(R.mipmap.maintab_icon_2_normal);
        //id_tab_img3.setImageResource(R.mipmap.maintab_icon_3_normal);
        //id_tab_img4.setImageResource(R.mipmap.maintab_icon_4_normal);
        //id_tab_img5.setImageResource(R.mipmap.maintab_icon_5_normal);
    }  
  
  
    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {
            //--bottom tab button
            /*
            case R.id.id_tab_b_1:
                selectButtonTab(0);
                break;
            case R.id.id_tab_b_2                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            :
                selectButtonTab(1);
                break;
            case R.id.id_tab_b_3:
                selectButtonTab(2);
                break;
            case R.id.id_tab_b_4:
                selectButtonTab(3);
                break;
            case R.id.id_tab_b_5:
                selectButtonTab(4);
                break;
                */
            case R.id.id_tab_img1:
                selectButtonTab(0);
                break;
            case R.id.id_tab_img2                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            :
                selectButtonTab(1);
                break;
            case R.id.id_tab_img3:
                selectButtonTab(2);
                break;
            case R.id.id_tab_img4:
                selectButtonTab(3);
                break;
            case R.id.id_tab_img5:
                selectButtonTab(4);
                break;
            //
            case R.id.id_tab1_page1:
                LogX.w(TAG,"id_tab1_btn1(): " );
                mViewPager.setCurrentItem(0);
                break;
            case R.id.id_tab1_page2:
                LogX.w(TAG,"id_tab1_btn2(): " );
                mViewPager.setCurrentItem(1);
                break;
            case R.id.id_tab1_page3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.id_tab1_page4:
                finish();
                //mViewPager.setCurrentItem(3);
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
    private void selectTab1Fragment(int id){
        LogX.w(TAG,"selectTab1Fragment(): " + id);
        resetTab1PageImg();
        selectpage(id);
        switch (id) {
            case 0:
                id_tab1_page1.setImageResource(R.mipmap.maintab_icon_1_selected);
                LogX.w(TAG,"fragmemt01");
                break;
            case 1:
                id_tab1_page2.setImageResource(R.mipmap.maintab_icon_2_selected);
                break;
            case 2:
                id_tab1_page3.setImageResource(R.mipmap.maintab_icon_3_selected);
                break;
            case 3:
                id_tab1_page4.setImageResource(R.mipmap.maintab_icon_4_selected);
                break;
            default:
                break;
        }
    }
    private void selectButtonTab(int id){
        LogX.w(TAG,"selectButtonTab(): " + id);
        resetBottomTabImg();
        switch (id) {
            case 0:
                mViewPager.setCurrentItem(0);
                selectpage(0);
                //id_tab_img1.setImageResource(R.mipmap.maintab_icon_1_selected);
                break;
            case 1:
                mViewPager.setCurrentItem(0);
                selectpage(0);
                //id_tab_img2.setImageResource(R.mipmap.maintab_icon_2_selected);
                break;
            case 2:
                mViewPager.setCurrentItem(0);
                selectpage(0);
                //id_tab_img3.setImageResource(R.mipmap.maintab_icon_3_selected);
                break;
            case 3:
                mViewPager.setCurrentItem(3);
                selectpage(3);
                //id_tab_img4.setImageResource(R.mipmap.maintab_icon_4_selected);
                break;
            case 4:
                //mViewPager.setCurrentItem(4);
                //id_tab_img5.setImageResource(R.mipmap.maintab_icon_5_selected);
                finish();
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