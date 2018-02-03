package afei.demo.stdemohistory.Api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by chaofei on 18-1-16.
 */

public class DataParse {
    private static final String TAG="DataParse";
    public int parseSiJson(String jsonpathname){
        return 1;

    }
    private String shstr = "historySearchHandler([{\"status\":0,\"hq\":[[\"2017-10-13\",\"3.67\",\"3.76\",\"0.08\",\"2.17%\",\"3.66\",\"3.76\",\"1805027\",\"67246.64\",\"2.01%\"],[\"2017-10-12\",\"3.68\",\"3.68\",\"-0.01\",\"-0.27%\",\"3.66\",\"3.74\",\"999578\",\"36981.35\",\"1.11%\"],[\"2017-10-11\",\"3.74\",\"3.69\",\"-0.05\",\"-1.34%\",\"3.67\",\"3.75\",\"1117259\",\"41345.07\",\"1.24%\"],[\"2017-10-10\",\"3.70\",\"3.74\",\"0.02\",\"0.54%\",\"3.66\",\"3.74\",\"1564838\",\"58006.54\",\"1.74%\"],[\"2017-10-09\",\"3.59\",\"3.72\",\"0.16\",\"4.49%\",\"3.58\",\"3.82\",\"2709271\",\"100011.91\",\"3.01%\"],[\"2017-09-29\",\"3.58\",\"3.56\",\"-0.05\",\"-1.39%\",\"3.55\",\"3.62\",\"601476\",\"21524.42\",\"0.67%\"],[\"2017-09-28\",\"3.54\",\"3.61\",\"0.07\",\"1.98%\",\"3.51\",\"3.63\",\"1120169\",\"40101.49\",\"1.24%\"],[\"2017-09-27\",\"3.54\",\"3.54\",\"-0.01\",\"-0.28%\",\"3.51\",\"3.56\",\"498337\",\"17592.50\",\"0.55%\"],[\"2017-09-26\",\"3.51\",\"3.55\",\"0.04\",\"1.14%\",\"3.51\",\"3.57\",\"512325\",\"18136.35\",\"0.57%\"],[\"2017-09-25\",\"3.57\",\"3.51\",\"-0.06\",\"-1.68%\",\"3.50\",\"3.58\",\"434498\",\"15353.24\",\"0.48%\"],[\"2017-09-22\",\"3.62\",\"3.57\",\"-0.07\",\"-1.92%\",\"3.55\",\"3.63\",\"756904\",\"27090.33\",\"0.84%\"],[\"2017-09-06\",\"3.58\",\"3.58\",\"-0.01\",\"-0.28%\",\"3.55\",\"3.59\",\"572535\",\"20440.65\",\"0.64%\"],[\"2017-09-05\",\"3.57\",\"3.59\",\"0.02\",\"0.56%\",\"3.56\",\"3.60\",\"569494\",\"20414.99\",\"0.63%\"],[\"2017-09-04\",\"3.59\",\"3.57\",\"-0.02\",\"-0.56%\",\"3.55\",\"3.60\",\"661823\",\"23665.42\",\"0.74%\"],[\"2017-09-01\",\"3.58\",\"3.59\",\"0.02\",\"0.56%\",\"3.57\",\"3.60\",\"506614\",\"18154.54\",\"0.56%\"]],\"code\":\"cn_000100\",\"stat\":[\"¨¤???:\",\"2017-09-01?¨¢2017-10-13\",\"0.19\",\"5.32%\",3.5,3.82,14430148,526065.44,\"16.03%\"]}])";
    private String aa = "{\"status\":0,\"hq\":[[\"2017-09-29\",\"16.60\",\"16.66\",\"0.06\",\"0.36%\",\"16.33\",\"17.10\",\"23525\",\"3923.57\",\"0.76%\"],[\"2017-09-28\",\"16.62\",\"16.60\",\"-0.02\",\"-0.12%\",\"16.46\",\"17.25\",\"48409\",\"8167.93\",\"1.57%\"],[\"2017-09-27\",\"16.60\",\"16.62\",\"0.36\",\"2.21%\",\"16.20\",\"16.65\",\"43616\",\"7197.72\",\"1.41%\"],[\"2017-09-26\",\"15.20\",\"16.26\",\"0.96\",\"6.27%\",\"15.20\",\"16.53\",\"49781\",\"7911.11\",\"1.61%\"],[\"2017-09-25\",\"15.51\",\"15.30\",\"-0.21\",\"-1.35%\",\"15.20\",\"15.52\",\"15152\",\"2328.05\",\"0.49%\"],[\"2017-09-22\",\"15.62\",\"15.51\",\"0.02\",\"0.13%\",\"15.43\",\"15.79\",\"16834\",\"2617.19\",\"0.54%\"],[\"2017-09-20\",\"15.80\",\"15.72\",\"-0.10\",\"-0.63%\",\"15.66\",\"15.89\",\"15106\",\"2379.38\",\"0.49%\"],[\"2017-09-19\",\"15.60\",\"15.82\",\"0.24\",\"1.54%\",\"15.44\",\"16.19\",\"25538\",\"4032.76\",\"0.83%\"],[\"2017-09-18\",\"15.43\",\"15.58\",\"0.16\",\"1.04%\",\"15.32\",\"15.69\",\"12772\",\"1981.19\",\"0.41%\"],[\"2017-09-15\",\"15.45\",\"15.42\",\"-0.15\",\"-0.96%\",\"15.35\",\"15.55\",\"13111\",\"2021.55\",\"0.42%\"],[\"2017-09-14\",\"15.59\",\"15.57\",\"-0.02\",\"-0.13%\",\"15.39\",\"15.59\",\"16734\",\"2590.09\",\"0.54%\"],[\"2017-09-13\",\"15.55\",\"15.59\",\"-0.07\",\"-0.45%\",\"15.44\",\"15.62\",\"14134\",\"2194.76\",\"0.46%\"],[\"2017-09-12\",\"15.30\",\"15.66\",\"0.36\",\"2.35%\",\"15.20\",\"15.70\",\"34324\",\"5330.63\",\"1.11%\"],[\"2017-09-11\",\"15.44\",\"15.30\",\"-0.18\",\"-1.16%\",\"15.20\",\"15.47\",\"15589\",\"2388.73\",\"0.50%\"],[\"2017-09-08\",\"15.17\",\"15.48\",\"0.26\",\"1.71%\",\"14.97\",\"15.53\",\"27679\",\"4189.81\",\"0.90%\"],[\"2017-09-07\",\"15.48\",\"15.22\",\"-0.33\",\"-2.12%\",\"15.20\",\"15.55\",\"26471\",\"4060.91\",\"0.86%\"],[\"2017-09-06\",\"15.70\",\"15.55\",\"-0.08\",\"-0.51%\",\"15.43\",\"15.70\",\"11723\",\"1820.39\",\"0.38%\"],[\"2017-09-05\",\"15.69\",\"15.63\",\"-0.02\",\"-0.13%\",\"15.46\",\"15.78\",\"16405\",\"2563.70\",\"0.53%\"],[\"2017-09-04\",\"15.65\",\"15.65\",\"0.15\",\"0.97%\",\"15.41\",\"15.80\",\"19164\",\"2993.76\",\"0.62%\"],[\"2017-09-01\",\"15.62\",\"15.50\",\"-0.22\",\"-1.40%\",\"15.44\",\"15.84\",\"22263\",\"3457.62\",\"0.72%\"],[\"2017-08-31\",\"15.75\",\"15.72\",\"-0.23\",\"-1.44%\",\"15.62\",\"15.89\",\"20795\",\"3266.71\",\"0.67%\"],[\"2017-08-30\",\"15.72\",\"15.95\",\"0.23\",\"1.46%\",\"15.53\",\"16.10\",\"27209\",\"4304.51\",\"0.88%\"],[\"2017-08-29\",\"15.57\",\"15.72\",\"0.14\",\"0.90%\",\"15.01\",\"15.91\",\"32900\",\"5116.34\",\"1.06%\"],[\"2017-08-28\",\"16.00\",\"15.58\",\"-0.36\",\"-2.26%\",\"15.47\",\"16.00\",\"40968\",\"6427.03\",\"1.33%\"],[\"2017-08-25\",\"16.12\",\"15.94\",\"-0.18\",\"-1.12%\",\"15.77\",\"16.30\",\"18378\",\"2932.26\",\"0.59%\"],[\"2017-08-24\",\"15.82\",\"16.12\",\"0.13\",\"0.81%\",\"15.81\",\"17.00\",\"26612\",\"4324.49\",\"0.86%\"],[\"2017-08-23\",\"16.00\",\"15.99\",\"-0.01\",\"-0.06%\",\"15.81\",\"16.18\",\"12074\",\"1929.67\",\"0.39%\"],[\"2017-08-22\",\"15.87\",\"16.00\",\"0.13\",\"0.82%\",\"15.80\",\"16.20\",\"18948\",\"3033.51\",\"0.61%\"],[\"2017-08-21\",\"15.80\",\"15.87\",\"0.05\",\"0.32%\",\"15.80\",\"16.08\",\"17737\",\"2817.68\",\"0.57%\"],[\"2017-08-18\",\"16.01\",\"15.82\",\"-0.23\",\"-1.43%\",\"15.60\",\"16.01\",\"19433\",\"3064.47\",\"0.63%\"],[\"2017-08-17\",\"16.54\",\"16.05\",\"-0.09\",\"-0.56%\",\"15.80\",\"16.54\",\"24821\",\"3971.46\",\"0.80%\"],[\"2017-08-16\",\"16.65\",\"16.14\",\"-0.51\",\"-3.06%\",\"15.88\",\"16.78\",\"26465\",\"4285.67\",\"0.86%\"],[\"2017-08-15\",\"16.69\",\"16.65\",\"-0.05\",\"-0.30%\",\"16.59\",\"16.88\",\"10619\",\"1772.08\",\"0.34%\"],[\"2017-08-14\",\"16.69\",\"16.70\",\"0.00\",\"0.00%\",\"16.59\",\"16.99\",\"17165\",\"2874.52\",\"0.56%\"],[\"2017-08-11\",\"16.50\",\"16.70\",\"0.08\",\"0.48%\",\"16.50\",\"16.96\",\"19487\",\"3268.44\",\"0.63%\"],[\"2017-08-10\",\"16.78\",\"16.62\",\"-0.29\",\"-1.71%\",\"16.50\",\"17.00\",\"26922\",\"4485.10\",\"0.87%\"],[\"2017-08-09\",\"17.24\",\"16.91\",\"-0.27\",\"-1.57%\",\"16.83\",\"17.29\",\"19149\",\"3249.94\",\"0.62%\"],[\"2017-08-08\",\"16.87\",\"17.18\",\"0.30\",\"1.78%\",\"16.70\",\"17.21\",\"27273\",\"4654.92\",\"0.88%\"],[\"2017-08-07\",\"17.00\",\"16.88\",\"-0.27\",\"-1.57%\",\"16.84\",\"17.20\",\"15526\",\"2629.13\",\"0.50%\"],[\"2017-08-04\",\"17.00\",\"17.15\",\"-0.01\",\"-0.06%\",\"16.88\",\"17.18\",\"27039\",\"4607.31\",\"0.88%\"],[\"2017-08-03\",\"16.93\",\"17.16\",\"0.23\",\"1.36%\",\"16.75\",\"17.35\",\"40551\",\"6921.96\",\"1.31%\"],[\"2017-08-02\",\"17.42\",\"16.93\",\"-0.44\",\"-2.53%\",\"16.90\",\"17.60\",\"53136\",\"9094.21\",\"1.72%\"],[\"2017-08-01\",\"16.77\",\"17.37\",\"0.60\",\"3.58%\",\"16.77\",\"17.53\",\"100044\",\"17278.46\",\"3.24%\"],[\"2017-07-31\",\"16.58\",\"16.77\",\"0.58\",\"3.58%\",\"16.49\",\"17.17\",\"54586\",\"9197.85\",\"1.77%\"],[\"2017-07-28\",\"16.51\",\"16.19\",\"-0.50\",\"-3.00%\",\"16.07\",\"16.80\",\"58217\",\"9587.32\",\"1.88%\"],[\"2017-07-27\",\"16.80\",\"16.69\",\"-0.28\",\"-1.65%\",\"16.69\",\"17.41\",\"71277\",\"12087.33\",\"2.31%\"],[\"2017-07-26\",\"16.49\",\"16.97\",\"0.35\",\"2.11%\",\"16.49\",\"17.33\",\"100040\",\"16966.48\",\"3.24%\"],[\"2017-07-25\",\"16.67\",\"16.62\",\"0.02\",\"0.12%\",\"16.31\",\"16.95\",\"63792\",\"10557.19\",\"2.06%\"],[\"2017-07-24\",\"16.43\",\"16.60\",\"0.55\",\"3.43%\",\"16.31\",\"17.27\",\"124240\",\"20836.50\",\"4.02%\"],[\"2017-07-21\",\"14.34\",\"16.05\",\"1.46\",\"10.01%\",\"14.34\",\"16.05\",\"136019\",\"21386.35\",\"4.40%\"],[\"2017-07-20\",\"14.34\",\"14.59\",\"0.08\",\"0.55%\",\"14.34\",\"14.72\",\"32302\",\"4705.07\",\"1.05%\"],[\"2017-07-19\",\"14.40\",\"14.51\",\"-0.09\",\"-0.62%\",\"14.05\",\"14.77\",\"61569\",\"8902.10\",\"1.99%\"],[\"2017-07-18\",\"12.85\",\"14.60\",\"0.32\",\"2.24%\",\"12.85\",\"15.45\",\"137786\",\"19106.08\",\"4.46%\"],[\"2017-07-17\",\"14.28\",\"14.28\",\"-1.59\",\"-10.02%\",\"14.28\",\"14.28\",\"35691\",\"5096.67\",\"1.16%\"]],\"code\":\"cn_000007\",\"stat\":[\"ÀÛ¼Æ:\",\"2017-07-17ÖÁ2017-09-29\",\"-7.15\",\"-30.03%\",12.85,17.6,1987100,318889.66,\"64.29%\"]}]";

    public static int parseShJsonstring(String jsonStr){
        try {
            String sHead = "historySearchHandler([";
            if (jsonStr.indexOf(sHead) == 0) {
                jsonStr = jsonStr.substring(sHead.length(), jsonStr.length() - 2);//historySearchHandler([{...}])
            } else {
                Log.e(TAG,"Error: error data! not found "+sHead);
                return 2;
            }
            //if (jsonStr.length()>100){
            //    Log.d(TAG,jsonStr.substring(0,50));
            //    Log.d(TAG,jsonStr.substring(jsonStr.length()-100,jsonStr.length()));
            //}

            JSONObject json =new JSONObject(jsonStr);
            JSONArray jsonStat = json.getJSONArray("stat");
            JSONArray jsonHq = json.getJSONArray("hq");
            String code = json.get("code").toString().replace("cn_", "");
            Log.w(TAG,jsonStat.toString());
            Log.w(TAG,jsonHq.toString());
            Log.w(TAG,code.toString());
                String name = "";
                int n = 0;

                //
            File f = new File("/storage/emulated/0/DownloadData/sohu_2014-01-01_2018-12-31/a.txt");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf-8")));

            out.write("[");
                for (int i = 0; i < jsonHq.length(); i++) {
                    JSONArray jo = jsonHq.getJSONArray(i);
                    //Global.logi(jo.toString());
                    //date,begin,high, low, gap, percent,price, volue,money,hand
                    String sdate = jo.getString(0).toString();
                    String sbegin = jo.getString(1).toString();
                    String sprice = jo.getString(2).toString();
                    String sgap = jo.getString(3).toString();
                    String spercent = jo.getString(4).toString().replace("%", "");
                    String slow = jo.getString(5).toString();
                    String shigh = jo.getString(6).toString();
                    String svolume = jo.getString(7).toString();
                    String smoney = jo.getString(8).toString();
                    String shand = jo.getString(9).toString().replace("%", "");
                    //Global.logi(code+name+sdate + "," + smoney + "," +spercent+","+ shand);
                    //Log.d(TAG,sbegin + "," + sprice + "," + sgap + "," + spercent + "," + slow + "," + shigh + "," + svolume + "," + smoney + "," + shand);
                    double begin = Double.parseDouble(sbegin);
                    double price = Double.parseDouble(sprice);
                    double gap = Double.parseDouble(sgap);
                    double percent = Double.parseDouble(spercent);
                    double low = Double.parseDouble(slow);
                    double high = Double.parseDouble(shigh);
                    long volume = Long.parseLong(svolume);
                    double money = Double.parseDouble(smoney);
                    double hand = Double.parseDouble(shand);
                    String sql = "(";
                    sql += "\"" + sdate + "\",";
                    sql += "\"" + code + "\",";
                    sql += "\"" + name + "\",";
                    sql += begin + ",";
                    sql += price + ",";
                    sql += high + ",";
                    sql += low + ",";
                    sql += gap + ",";
                    sql += percent + ",";
                    sql += volume + ",";
                    sql += money + ",";
                    sql += hand;
                    sql += ")";
                    Log.w(TAG,i+": "+sql);
                    sql = "{";
                    sql += "\"dt\":"+"\"" + sdate + "\",";
                    sql += "\"co\":"+"\"" + code + "\",";
                    sql += "\"na\":"+"\"" + name + "\",";
                    sql += "\"be\":"+"\"" +begin + "\",";
                    sql += "\"pr\":"+"\"" +price + "\",";
                    sql += "\"hi\":"+"\"" +high + "\",";
                    sql += "\"lo\":"+"\"" +low + "\",";
                    sql += "\"ga\":"+"\"" +gap + "\",";
                    sql += "\"pe\":"+"\"" +percent + "\",";
                    sql += "\"vo\":"+"\"" +volume + "\",";
                    sql += "\"mo\":"+"\"" +money + "\",";
                    sql += "\"ha\":"+"\"" +hand+ "\"";
                    sql += "}";
                    out.write(sql+"\n");
                }

            out.write("]");
            out.flush();
            out.close();

            return 0;
        }catch (Exception e){
            Log.e(TAG,"_parseShJsonstring_ Error:"+e.getMessage());
        }
        return 1;
    }
    public int parseSiJsonstring(String jsonStr){
        try {
            String sHead = "historySearchHandler([";
            if (jsonStr.indexOf(sHead) == 0) {
                jsonStr = jsonStr.substring(sHead.length() - 1, jsonStr.length() - 2);//historySearchHandler([{...}])
            } else {
                Log.e(TAG,"Error: error data! not found "+sHead);
                return 2;
            }
            if (jsonStr.length()>50){
                Log.d(TAG,jsonStr.substring(0,50));
            }

            if (jsonStr.indexOf("{\"code\":") == 0) {
                Log.d(TAG,jsonStr);
                JSONObject js =new JSONObject(jsonStr);
                //int code = json.getInt("code");
                //String m = json.getString("msg");
                //Logw("Error:" + "[" + code + "] " + m);
            }


            return 0;
        }catch (Exception e){
            Log.e(TAG,"Error:"+e.getMessage());
        }
        return 1;
    }
}
