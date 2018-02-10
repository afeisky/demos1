package afei.stdemonow;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import afei.api.DbContentProvider;
import afei.api.Global;
import afei.api.LogX;


public class Fragment02 extends android.support.v4.app.Fragment {
    private static final String TAG = "STdownnow:Fragment02";
    private TextView mMessageView;
    private ListView listview_bk_now;
    private Button btnDown;
    private List<Map<String, Object>> datalist=new ArrayList<Map<String, Object>>();
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
        mMessageView = (TextView) mView.findViewById(R.id.txtbknow);
        listview_bk_now = (ListView) mView.findViewById(R.id.listview_bk_now);
        btnDown = (Button) mView.findViewById(R.id.btnbknow);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        getData();

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
                        Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }


        });
        //mHandler.sendEmptyMessageDelayed(msg_id, msg_delay_secondes);
    }

    private MyAdapter sa;//private SimpleAdapter sa;

    private SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");

    private void getData() {

        if (true) {
            try {
                JSONObject json = Global.getContentProvider().queryBK13();//2("gn_", "", 0, datalist);
                //LogX.w(TAG,json.toString());
                JSONObject js_gn = json.getJSONObject("gn_");
                JSONObject js_new = json.getJSONObject("new_");
                JSONObject js_hy = json.getJSONObject("hangye_");
                JSONArray ja_gn = js_gn.getJSONArray("data");
                JSONArray ja_new = js_new.getJSONArray("data");
                JSONArray ja_hy = js_hy.getJSONArray("data");
                String timeNow = js_gn.getString("time");
                int gn_len = ja_gn.length();
                int mew_len = ja_new.length();
                int hy_len = ja_hy.length();
                LogX.e(TAG, "len=" + gn_len + "," + mew_len + "," + hy_len);
                if (true) {
                    JSONArray ja = null;
                    Map<String, Object> m = new HashMap<String, Object>();
                    LogX.e(TAG, "=" + js_gn.toString());
                    for (int i = 0; i < gn_len; i++) {
                        m = new HashMap<String, Object>();
                        ja = ja_gn.getJSONArray(i);
                        m.put("1", ja.get(0));
                        m.put("2", ja.get(1));
                        datalist.add(m);
                        BK bk=new BK();
                        bk.key=1;
                        bk.a1=ja.get(0).toString();
                        bk.a2=ja.get(1).toString().trim();
                        bk.percent=Double.parseDouble(ja.get(1).toString().trim());
                        bk.count=Integer.parseInt(ja.get(2).toString().trim());
                        bk.number=Integer.parseInt(ja.get(3).toString().trim());
                        list.add(bk);
                    }
                    m.put("1", "-------");
                    m.put("2", "------");
                    for (int i = 0; i < mew_len; i++) {
                        m = new HashMap<String, Object>();
                        ja = ja_new.getJSONArray(i);
                        m.put("1", ja.get(0));
                        m.put("2", ja.get(1));
                        datalist.add(m);
                        BK bk=new BK();
                        bk.key=2;
                        bk.a1=ja.get(0).toString();
                        bk.a2=ja.get(1).toString().trim();
                        bk.percent=Double.parseDouble(ja.get(1).toString().trim());
                        bk.count=Integer.parseInt(ja.get(2).toString().trim());
                        bk.number=Integer.parseInt(ja.get(3).toString().trim());

                        list.add(bk);
                    }
                    m.put("1", "-------");
                    m.put("2", "------");
                    datalist.add(m);
                    for (int i = 0; i < hy_len; i++) {
                        m = new HashMap<String, Object>();
                        ja = ja_hy.getJSONArray(i);
                        m.put("1", ja.get(0));
                        m.put("2", ja.get(1));
                        datalist.add(m);
                        BK bk=new BK();
                        bk.key=3;
                        bk.a1=ja.get(0).toString();
                        bk.a2=ja.get(1).toString().trim();
                        bk.percent=Double.parseDouble(ja.get(1).toString().trim());
                        bk.count=Integer.parseInt(ja.get(2).toString().trim());
                        bk.number=Integer.parseInt(ja.get(3).toString().trim());

                        list.add(bk);
                    }

                    LogX.e(TAG, "list.size()= "+list.size());
                } else {
                    int max = gn_len > mew_len ? gn_len : mew_len;
                    max = max > hy_len ? max : hy_len;
                    JSONArray ja = null;
                    for (int i = 0; i < max; i++) {
                        Map<String, Object> m = new HashMap<String, Object>();
                        if (ja_gn.length() > i) {
                            ja = ja_gn.getJSONArray(i);
                            m.put("1", ja.get(0));
                            m.put("2", ja.get(1));
                        }
                        if (ja_new.length() > i) {
                            ja = ja_new.getJSONArray(i);
                            m.put("11", ja.get(0));
                            m.put("12", ja.get(1));
                        }
                        if (ja_hy.length() > i) {
                            ja = ja_hy.getJSONArray(i);
                            m.put("21", ja.get(0));
                            m.put("22", ja.get(1));
                        }
                        datalist.add(m);
                    }
                }
                return;
            } catch (Exception e) {
                LogX.e(TAG, "Error: " + e.getMessage());
            }

        }
        String timeNow = HHmmss.format(new Date());
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("time", timeNow);
        map1.put("code", "gn_afdfaaa");
        map1.put("percent", "5.343");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("time", timeNow);
        map2.put("code", "gn_afdf333");
        map2.put("percent", "4.343");
        Map<String, Object> map3 = new HashMap<String, Object>();

        map3.put("time", timeNow);
        map3.put("code", "gn_afdf%%tt");
        map3.put("percent", "2.343");
        Map<String, Object> map4 = new HashMap<String, Object>();
        //map4.put("image",R.mipmap.ic_launcher);
        map4.put("time", timeNow);
        map4.put("code", "gn_afdffdsfa");
        map4.put("percent", "-1.343");

        datalist.add(map1);
        datalist.add(map2);
        datalist.add(map3);
        datalist.add(map4);

        LogX.e(TAG, "--refresh--->");
    }

    private void refresh() {
        datalist.clear();
        list.clear();
        getData();
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
        private String a1;
        private String a2;
        private String a3;
        private Double percent;
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
                holder.a3 = (TextView) convertView.findViewById(R.id.f02_a3);
                holder.pb = (ProgressBar) convertView.findViewById(R.id.f02_a4);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            LogX.e(TAG,position+","+list.size());
            BK bk=list.get(position);
            //holder.img.setImageResource(R.drawable.ic_launcher);
            if (bk.key==1) {
                holder.a1.setTextColor(Color.WHITE);
            }else if (bk.key==2) {
                holder.a1.setTextColor(Color.CYAN);
            }else{
                holder.a1.setTextColor(Color.YELLOW);
            }
            holder.a1.setText(bk.a1);
            holder.a2.setText(bk.a2);
            if (bk.percent>0) {
                holder.a2.setTextColor(Color.RED);//Color.rgb(255, 255, 255)); Color.parseColor("#FF0000"));
            }else{
                holder.a2.setTextColor(Color.GREEN);//Color.rgb(255, 255, 255));
            }
            holder.a3.setText(position+" , "+bk.count+"/"+bk.number);
            holder.pb.setProgress(5);
            //holder.pb.setProgress(bk.count/3*100/bk.number);
            holder.pb.setSecondaryProgress(1);
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
        public ProgressBar pb;
    }

}
