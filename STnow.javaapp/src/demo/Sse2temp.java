package demo;

import demo.api.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Sse2temp {
    private static String TAG="Sse2temp";
    private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void start(){
        String sql="select id,code,time,last,begin,new,low,high,money,volume,ctime,url,note from ssen where DATE_FORMAT(time,'%Y-%m-%d')='"+"2018-06-14"+"' and code='600000' order by time;";
        LogX.w(TAG,sql);
        ResultSet rs=null;
        try {
            rs = Global.getJdbc().query(sql);
            if (rs ==null) {
            }
            int nn=0;
            SimpleDateFormat MMdd = new SimpleDateFormat("MMdd");
            while (rs.next()) {
                String code0=rs.getString(2).toString();
                String time0=rs.getString(3).toString().substring(0,"2018-06-21 09:25:03".length());
                double last0 = rs.getDouble(4);
                double begin0 = rs.getDouble(5);
                double new0 = rs.getDouble(6);
                double low0 = rs.getDouble(7);
                double high0 = rs.getDouble(8);
                double money0 = rs.getDouble(9);
                double volume0 = rs.getDouble(10);
                String ctime0= rs.getString(11).toString().substring(0,"2018-06-21 09:25:03".length());
                String url0 = rs.getString(12).toString();
                String note0 = rs.getString(13).toString();
                LogX.w(TAG,time0+" --->");
                boolean is1500=false;
                boolean is0915=false;
                boolean needInsert=false;
                boolean needUpdate=false;
                ResultSet rs1 =null;

                String s1=time0.substring(11,13)+time0.substring(14,16);
                int sn=0;
                try {
                    sn=Integer.valueOf(s1);
                }catch (Exception e){

                }
                if (sn>915 && sn<930){
                    is0915=true;
                }

                String sqlA="";//" from ssen_in_temp where code='" + code0 + "' and DATE_FORMAT(time,'%Y-%m-%d %H:%i')= DATE_FORMAT('" + time0 + "','%Y-%m-%d %H:%i')";
                if (time0.substring(11,13).equals("15")) {
                    LogX.w(TAG, "===15");
                    is1500=true;
                    //sqlA=" from ssen_in_temp where code='" + code0 + "' and DATE_FORMAT(time,'%Y-%m-%d %H')= DATE_FORMAT('" + time0 + "','%Y-%m-%d %H')";
                    sqlA=" from ssen_in_temp where code='" + code0 + "' and DATE_FORMAT(time,'%Y-%m-%d')= DATE_FORMAT('" + time0 + "','%Y-%m-%d') and DATE_FORMAT(time,'%Y-%m-%d %H:%i')<= DATE_FORMAT('" + time0 + "','%Y-%m-%d %H:%i') order by time DESC ";
                }
                //if (is0915)
                {
                    sqlA=" from ssen_in_temp where code='" + code0 + "' and DATE_FORMAT(time,'%Y-%m-%d')= DATE_FORMAT('" + time0 + "','%Y-%m-%d') and DATE_FORMAT(time,'%Y-%m-%d %H:%i')<= DATE_FORMAT('" + time0 + "','%Y-%m-%d %H:%i') order by time DESC ";
                }
                if (true) {
                    sql="select count(*) "+sqlA;
                    LogX.w(TAG, sql);
                    rs1 = Global.getJdbc().query(sql);
                    while (rs1.next()) {
                        LogX.w(TAG, "=" + rs1.getInt(1));
                        int n = rs1.getInt(1);
                        if (n == 0) {
                            needInsert = true;
                        }
                        break;
                    }
                }

                if (false){
                    String sqlB=" from ssen_in_temp where code='" + code0 + "' and DATE_FORMAT(time,'%Y-%m-%d %H:%i')<= DATE_FORMAT('" + time0 + "','%Y-%m-%d %H:%i') order by time DESC ";
                    sql = "select id,code,time,last,begin,new,low,high,money,volume "+sqlB+" limit 1;";
                    LogX.w(TAG, sql);
                    rs1 = Global.getJdbc().query(sql);
                    LogX.w(TAG, "1111");
                    if (rs1.next()) {
                        LogX.w(TAG, "111------11");
                        LogX.w(TAG, "=" + rs1.getInt(1));
                        String code1=rs1.getString(2).toString();
                        String time1=rs1.getString(3).toString().substring(0,"2018-06-21 09:25:03".length());
                        double last1 = rs1.getDouble(4);
                        double begin1 = rs1.getDouble(5);
                        double new1 = rs1.getDouble(6);
                        double low1 = rs1.getDouble(7);
                        double high1 = rs1.getDouble(8);
                        double money1 = rs1.getDouble(9);
                        double volume1 = rs1.getDouble(10);
                        LogX.w(TAG, "needUpdate??  "+code1+","+time1);
                        if (code1==code0 && last1==last0 && begin1==begin0 && new1==new0 && low1==low0 && high1==high0 && money1==money0 && volume1==volume0){
                            needInsert=false;
                        }else{
                            needInsert=true;
                            LogX.w(TAG, "needUpdate=  "+code1+","+time1);
                        }
                    }
                }
                if (true) {//(!needInsert) {
                    sql = "select id,code,time,last,begin,new,low,high,money,volume,ctime,url "+sqlA+" limit 1;";
                    LogX.w(TAG, sql);
                    rs1 = Global.getJdbc().query(sql);
                    LogX.w(TAG, "2222");
                    if (rs1.next()) {
                        LogX.w(TAG, "222------11");
                        LogX.w(TAG, "=" + rs1.getInt(1));
                        String code1=rs1.getString(2).toString();
                        String time1=rs1.getString(3).toString().substring(0,"2018-06-21 09:25:03".length());
                        double last1 = rs1.getDouble(4);
                        double begin1 = rs1.getDouble(5);
                        double new1 = rs1.getDouble(6);
                        double low1 = rs1.getDouble(7);
                        double high1 = rs1.getDouble(8);
                        double money1 = rs1.getDouble(9);
                        double volume1 = rs1.getDouble(10);
                        LogX.w(TAG, "needUpdate??  ,"+code1+","+time1);

                        LogX.w(TAG, "need-1- "+code0+time0+last0+begin0+new0+low0+high0+money0+volume0);
                        LogX.w(TAG, "need-2- "+code1+time1+last1+begin1+new1+low1+high1+money1+volume1);
                        if (code1.equalsIgnoreCase(code0)){
                            LogX.d(TAG, "code1,"+code1+",");
                        }
                        if (last1==last0){
                            LogX.d(TAG, "last1");
                        }
                        if (begin1==begin0){
                            LogX.d(TAG, "begin1");
                        }
                        if (new1==new0){
                            LogX.d(TAG, "new1");
                        }
                        if (low1==low0){
                            LogX.d(TAG, "low1");
                        }
                        if (high1==high0){
                            LogX.d(TAG, "high1");
                        }
                        if (money1==money0){
                            LogX.d(TAG, "money1");
                        }
                        if (volume1==volume0){
                            LogX.d(TAG, "volume1");
                        }
                        if (code1.equalsIgnoreCase(code0) && last1==last0 && begin1==begin0 && new1==new0 && low1==low0 && high1==high0 && money1==money0 && volume1==volume0){
                            LogX.w(TAG, "needUpdate=11  "+code1+","+time1);
                        }else{
                            needInsert=true;
                            LogX.w(TAG, "needUpdate=22  "+code1+","+time1);
                        }
                        ////break;
                    }
                }
                if (needInsert) {
                    if (!is0915 && !is1500) {
                        sqlA = "from ssen_in_temp where code='" + code0 + "' and DATE_FORMAT(time,'%Y-%m-%d %H:%i')= DATE_FORMAT('" + time0 + "','%Y-%m-%d %H:%i')";
                        sql = "select count(*) " + sqlA;
                        LogX.w(TAG, sql);
                        rs1 = Global.getJdbc().query(sql);
                        while (rs1.next()) {
                            LogX.w(TAG, "=" + rs1.getInt(1));
                            int n = rs1.getInt(1);
                            if (n ==0) {
                                needInsert =true;
                            }else{
                                needInsert =false;
                            }
                            break;
                        }
                        if (!needInsert) {
                            sql = "select id,code,time,last,begin,new,low,high,money,volume,ctime,url " + sqlA + " limit 1;";
                            LogX.w(TAG, sql);
                            rs1 = Global.getJdbc().query(sql);
                            LogX.w(TAG, "2222");
                            if (rs1.next()) {
                                LogX.w(TAG, "222------11");
                                String code1 = rs1.getString(2).toString();
                                String time1 = rs1.getString(3).toString().substring(0, "2018-06-21 09:25:03".length());
                                double last1 = rs1.getDouble(4);
                                double begin1 = rs1.getDouble(5);
                                double new1 = rs1.getDouble(6);
                                double low1 = rs1.getDouble(7);
                                double high1 = rs1.getDouble(8);
                                double money1 = rs1.getDouble(9);
                                double volume1 = rs1.getDouble(10);

                                LogX.w(TAG, "need-1- " + code0 + time0 + last0 + begin0 + new0 + low0 + high0 + money0 + volume0);
                                LogX.w(TAG, "need-2- " + code1 + time1 + last1 + begin1 + new1 + low1 + high1 + money1 + volume1);
                                if (code1.equalsIgnoreCase(code0) && last1 == last0 && begin1 == begin0 && new1 == new0 && low1 == low0 && high1 == high0 && money1 == money0 && volume1 == volume0) {
                                    LogX.w(TAG, "needUpdate=11  " + code1 + "," + time1);
                                } else {
                                    needUpdate = true;
                                    LogX.w(TAG, "needUpdate=22  " + code1 + "," + time1);
                                }
                                ////break;
                            }
                        }
                    }
                }
                if (needInsert){
                    sql = "insert into ssen_in_temp (code,time,last,begin,new,low,high,money,volume,ctime,url,note) values (";
                    sql += "'" + code0 + "'";
                    sql += ",'" + time0 + "'";
                    sql += "," + last0 + "";
                    sql += "," + begin0 + "";
                    sql += "," + new0 + "";
                    sql += "," + low0 + "";
                    sql += "," + high0 + "";
                    sql += "," + money0 + "";
                    sql += "," + volume0 + "";
                    sql += ",'" + ctime0 + "'";
                    sql += ",'" + url0 + "'";
                    sql += ",'" + note0 + "'";
                    sql += ")";
                    LogX.w(TAG,sql);
                    Global.getJdbc().update(sql);
                }
                //break;////
            }
        }catch (SQLException e) {
            LogX.e(TAG,"Error: database has error！"+e.getStackTrace());
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }



}
