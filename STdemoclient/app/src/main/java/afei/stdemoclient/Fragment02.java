package afei.stdemoclient;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import afei.api.FileX;
import afei.api.Global;
import afei.api.LogX;


public class Fragment02 extends android.support.v4.app.Fragment {
    private static final String TAG = "STdownnow:Fragment02";
    private TextView mMessageView;
    private ListView listview_bk_now;
    private Button btnDown;
    //private List<Map<String, Object>> datalist=new ArrayList<Map<String, Object>>();
    private List<BK> list=new ArrayList<BK>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment2a, container, false);
        LogX.e(TAG, "fragmemt02  onCreateView()");
        init(mView);
        return mView;
    }

    public void init(View mView) {
        listview_bk_now = (ListView) mView.findViewById(R.id.listview_bk_now);
        mMessageView = (TextView) mView.findViewById(R.id.f02_sse_txt);
        mMessageView.setText("");
        btnDown = (Button) mView.findViewById(R.id.btnbknow);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        getDataBK();
        getDataSSE();

        //sa = new SimpleAdapter(getActivity(),
        //        datalist, R.layout.fragment2aitem, new String[]{"1", "2", "2"},
        //        new int[]{R.id.f02_a1, R.id.f02_a2, R.id.f02_a3});
        sa = new MyAdapter(getActivity().getApplicationContext());
        listview_bk_now.setAdapter(sa);
        listview_bk_now.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                switch (p3) {
                    case 0:
                        //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }


        });
        //mHandler.sendEmptyMessageDelayed(msg_id, msg_delay_secondes);
    }

    private MyAdapter sa;//private SimpleAdapter sa;

    private SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
    DecimalFormat decimalFormat=new DecimalFormat();

    private void getDataSSE(){

        JSONObject json=null;
        String filename=Global.workPath+"/ssenow.txt";
        File f=new File(filename);
        String data="";
        if (f.exists()) {
            data = FileX.readLines(filename, "gb2312");
            LogX.w(TAG, "getDataSSE=" + data);
        }else{
            return;
        }
        try {
            json = new JSONObject(data);
            //json = Global.getContentProvider().querySSE10();
            LogX.w(TAG, "=" + json.toString());
            String html = "";
            if (json != null && json.toString().length() > 10) {
                JSONArray js = json.getJSONArray("list");
                String sdate = json.getString("date");
                int count = json.getInt("count");
                html = sdate+" ["+count+"]<br>";
                decimalFormat.applyPattern("#0.0");
                for (int i = js.length() - 1; i >= 0; i--) {
                    JSONArray one = js.getJSONArray(i);
                    if (one.getInt(1) > 0) {
                        if (i < js.length() - 1) {
                            html += ";";
                        }
                        if (i == js.length() / 2 - 1) {
                            html += "<br>";
                        }
                        String read1 = "";
                        String read2 = "";
                        if (one.getInt(0) >= 0) {
                            read1 = "<font color='#FF0000'>";
                        } else {
                            read1 = "<font color='#00FF00'>";
                        }
                        String w1 = "<font color='#FFFFFF'>";
                        String w2 = "</font>";
                        int acount=one.getInt(1);
                        double dper=acount*100/count;
                        String per=decimalFormat.format(dper);
                        LogX.w(TAG, count+",acount="+acount+",dper="+dper+",per="+per);
                        w1=w2=read1="";
                        html += w1 + one.getString(0) + ":" + w2 + read1 + acount +"("+per+"%)" +w2;
                    }
                }
            }
            mMessageView.setText(Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY));
        }catch (Exception e){
            LogX.e(TAG, "Error: getDataSSE:" + e.getMessage());
        }
    }
    private void getDataBK() {
        try {
            JSONObject json=null;
            String filename=Global.workPath+"/snbk.txt";
            File f=new File(filename);
            String data="";
            if (f.exists()) {
                data = FileX.readLines(filename, "utf-8");
            }else{
                return;
            }
            json=new JSONObject(data);
            if (json!=null && json.toString().length()>10) {
                //LogX.w(TAG,json.toString());
                JSONObject js_gn = json.getJSONObject("gn_");
                JSONObject js_new = json.getJSONObject("new_");
                JSONObject js_hy = json.getJSONObject("hangye_");
                JSONArray ja_gn = js_gn.getJSONArray("data");
                JSONArray ja_new = js_new.getJSONArray("data");
                JSONArray ja_hy = js_hy.getJSONArray("data");
                String timeNow = js_gn.getString("time");
                int gn_len = ja_gn.length();
                int new_len = ja_new.length();
                int hy_len = ja_hy.length();
                LogX.e(TAG, "len=" + gn_len + "," + new_len + "," + hy_len);
                JSONArray ja = null;
                //LogX.e(TAG, "=" + js_gn.toString());
                BK bk = new BK();
                bk.a1 = js_gn.getString("time");
                bk.b1 = js_new.getString("time");
                bk.c1 = js_hy.getString("time");
                list.add(bk);
                if (false) {
                    for (int i = 0; i < gn_len; i++) {
                        bk = new BK();
                        bk.key = 1;
                        bk.a1 = ja.get(0).toString();
                        bk.a2 = ja.get(1).toString().trim();
                        bk.percent = Double.parseDouble(ja.get(1).toString().trim());
                        bk.count = Integer.parseInt(ja.get(2).toString().trim());
                        bk.number = Integer.parseInt(ja.get(3).toString().trim());
                        list.add(bk);
                    }
                    for (int i = 0; i < new_len; i++) {

                        bk = new BK();
                        bk.key = 2;
                        bk.a1 = ja.get(0).toString();
                        bk.a2 = ja.get(1).toString().trim();
                        bk.percent = Double.parseDouble(ja.get(1).toString().trim());
                        bk.count = Integer.parseInt(ja.get(2).toString().trim());
                        bk.number = Integer.parseInt(ja.get(3).toString().trim());
                        list.add(bk);
                    }
                    for (int i = 0; i < hy_len; i++) {
                        bk = new BK();
                        bk.key = 3;
                        bk.a1 = ja.get(0).toString();
                        bk.a2 = ja.get(1).toString().trim();
                        bk.percent = Double.parseDouble(ja.get(1).toString().trim());
                        bk.count = Integer.parseInt(ja.get(2).toString().trim());
                        bk.number = Integer.parseInt(ja.get(3).toString().trim());
                        list.add(bk);
                    }

                    //LogX.e(TAG, "list.size()= "+list.size());
                } else if (true) {
                    int max = gn_len > new_len ? gn_len : new_len;
                    max = max > hy_len ? max : hy_len;
                    for (int i = 0; i < max; i++) {
                        bk = new BK();
                        bk.key = 1;
                        if (ja_gn.length() > i) {
                            ja = ja_gn.getJSONArray(i);
                            bk.a1 = ja.get(0).toString();
                            bk.a2 = ja.get(1).toString().trim();
                            bk.a3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        if (ja_new.length() > i) {
                            ja = ja_new.getJSONArray(i);
                            bk.b1 = ja.get(0).toString();
                            bk.b2 = ja.get(1).toString().trim();
                            bk.b3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        if (ja_hy.length() > i) {
                            ja = ja_hy.getJSONArray(i);
                            bk.c1 = ja.get(0).toString();
                            bk.c2 = ja.get(1).toString().trim();
                            bk.c3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        list.add(bk);
                    }
                } else {
                    int max = gn_len > new_len ? gn_len : new_len;
                    max = max > hy_len ? max : hy_len;
                    int gn_countd1 = js_gn.getInt("countd1");
                    int mew_countd1 = js_new.getInt("countd1");
                    int hy_countd1 = js_hy.getInt("countd1");
                    int max_countd1 = gn_countd1 > mew_countd1 ? gn_countd1 : mew_countd1;
                    max_countd1 = max_countd1 > hy_countd1 ? max_countd1 : hy_countd1;
                    LogX.e(TAG, "gn_countd1= " + gn_countd1);
                    LogX.e(TAG, "max_countd1= " + max_countd1);
                    LogX.e(TAG, "max_countd1-hy_countd1= " + (max_countd1 - hy_countd1));
                    for (int i = 0; i < max_countd1; i++) {
                        bk = new BK();
                        bk.key = 1;
                        if (i >= max_countd1 - gn_countd1) {
                            ja = ja_gn.getJSONArray(i - (max_countd1 - gn_countd1));
                            bk.a1 = ja.get(0).toString();
                            bk.a2 = ja.get(1).toString().trim();
                            bk.a3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        if (i >= max_countd1 - mew_countd1) {
                            ja = ja_new.getJSONArray(i - (max_countd1 - mew_countd1));
                            bk.b1 = ja.get(0).toString();
                            bk.b2 = ja.get(1).toString().trim();
                            bk.b3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        if (i >= max_countd1 - hy_countd1) {
                            ja = ja_hy.getJSONArray(i - (max_countd1 - hy_countd1));
                            bk.c1 = ja.get(0).toString();
                            bk.c2 = ja.get(1).toString().trim();
                            bk.c3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        list.add(bk);
                    }
                    int gn_countd12 = gn_len - gn_countd1;
                    int mew_countd12 = new_len - mew_countd1;
                    int hy_countd12 = hy_len - hy_countd1;
                    max_countd1 = gn_countd12 > mew_countd12 ? gn_countd12 : mew_countd12;
                    max_countd1 = max_countd1 > hy_countd12 ? max_countd1 : hy_countd12;
                    LogX.e(TAG, "gn_countd12= " + gn_countd12);
                    LogX.e(TAG, "max_countd1= " + max_countd1);
                    for (int i = 0; i < max_countd1; i++) {
                        bk = new BK();
                        bk.key = 2;
                        if (i < gn_countd12) {
                            ja = ja_gn.getJSONArray(i + gn_countd1);
                            bk.a1 = ja.get(0).toString();
                            bk.a2 = ja.get(1).toString().trim();
                            bk.a3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        if (i < mew_countd12) {
                            ja = ja_new.getJSONArray(i + mew_countd1);
                            bk.b1 = ja.get(0).toString();
                            bk.b2 = ja.get(1).toString().trim();
                            bk.b3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        if (i < hy_countd12) {
                            ja = ja_hy.getJSONArray(i + hy_countd1);
                            bk.c1 = ja.get(0).toString();
                            bk.c2 = ja.get(1).toString().trim();
                            bk.c3 = Double.parseDouble(ja.get(1).toString().trim());
                        }
                        list.add(bk);
                    }
                }
            }
            return;
        } catch (Exception e) {
            LogX.e(TAG, "Error: getDataBK:" + e.getMessage());

        }
        return;


        //Map<String, Object> map1 = new HashMap<String, Object>();
        //map1.put("time", timeNow);
        //map1.put("code", "gn_afdfaaa");
    }

    private void refresh() {
        LogX.e(TAG, "--refresh--->");
        list.clear();
        getDataBK();
        getDataSSE();
        sa.notifyDataSetChanged();
    }

    private boolean isRunning = true;
    private final int msg_id = 1;
    private int msg_delay_secondes = 5000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msg_id:
                    refresh();
                    removeMessages(msg_id);
                    if (isRunning) {
                        sendEmptyMessageDelayed(msg_id, msg_delay_secondes);//this.sendMessageDelayed()
                        break;
                    }
            }
        }
    };

    public void stopUpdate() {
        mHandler.removeMessages(msg_id);
        isRunning = false;
    }

    public void startUpdate() {
        isRunning = true;
        refresh();
        mHandler.sendEmptyMessageDelayed(msg_id, 0);
    }

    public class BK {
        private int key;
        private String a1="";
        private String a2="";
        private Double a3=0.0;//percent
        private String b1="";
        private String b2="";
        private Double b3=0.0;//percent
        private String c1="";
        private String c2="";
        private Double c3=0.0;
        private Double percent=0.0;
        private int count;
        private int number;
    }
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        public MyAdapter(Context context){
            super();
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.fragment2aitem, null);
                holder.a1 = (TextView) convertView.findViewById(R.id.f02_a1);
                holder.a2 = (TextView) convertView.findViewById(R.id.f02_a2);

                holder.b1 = (TextView) convertView.findViewById(R.id.f02_b1);
                holder.b2 = (TextView) convertView.findViewById(R.id.f02_b2);
                holder.c1 = (TextView) convertView.findViewById(R.id.f02_c1);
                holder.c2 = (TextView) convertView.findViewById(R.id.f02_c2);
                //holder.pb = (ProgressBar) convertView.findViewById(R.id.f02_a14);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BK bk=list.get(position);
            //holder.img.setImageResource(R.drawable.ic_launcher);
            holder.a1.setText(bk.a1);
            holder.a2.setText(bk.a2);
            holder.b1.setText(bk.b1);
            holder.b2.setText(bk.b2);
            holder.c1.setText(bk.c1);
            holder.c2.setText(bk.c2);
            if (true) {
                holder.a2.setTextColor(bk.a3>0?Color.RED:Color.GREEN);//Color.rgb(255, 255, 255)); Color.parseColor("#FF0000"));
                holder.b2.setTextColor(bk.b3>0?Color.RED:Color.GREEN);
                holder.c2.setTextColor(bk.c3>0?Color.RED:Color.GREEN);
            }else{
                if (bk.key < 2) {
                    holder.a2.setTextColor(Color.RED);//Color.rgb(255, 255, 255)); Color.parseColor("#FF0000"));
                    holder.b2.setTextColor(Color.RED);
                    holder.c2.setTextColor(Color.RED);
                } else {
                    holder.a2.setTextColor(Color.GREEN);//Color.rgb(255, 255, 255));
                    holder.b2.setTextColor(Color.GREEN);
                    holder.c2.setTextColor(Color.GREEN);
                }
            }

            //holder.a3.setText(position+" , "+bk.count+"/"+bk.number);
            //holder.pb.setProgress(5);
            //holder.pb.setProgress(bk.count/3*100/bk.number);
            //holder.pb.setSecondaryProgress(1);
            //holder.speaker.setOnClickListener(new OnClickListener(){
            //    @Override
            //    public void onClick(View v) {
            //        System.out.println("Click on the speaker image on ListItem ");
            //    }
            //});

            return convertView;
        }

    }
    class ViewHolder {
        //public ImageView img;
        public TextView a1;
        public TextView a2;
        public TextView a3;
        public TextView b1;
        public TextView b2;
        public TextView c1;
        public TextView c2;
        public ProgressBar pb;
    }

}
