/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.*;

/**
 *
 * @author chaofei.wu
 */
public class Jdbc {
    //add lib : mysql-connector-java-5.1.44-bin.jar
    private static String TAG="JDBC";
    private boolean hasJdbc = true;
    private Connection conn;
    private static boolean isSqlite=true;
    public Jdbc(boolean _isSqlite) {
        isSqlite=_isSqlite;
        try {
            if (isSqlite) {
                Class.forName("org.sqlite.JDBC");
            }else{
                Class.forName("com.mysql.jdbc.Driver");
            }
        } catch (ClassNotFoundException e) {
            LogX.e(TAG,"no found JDBC driver！ ="+isSqlite+",\n" + e.getMessage());
            e.printStackTrace();
            hasJdbc = false;
        }
    }

    public boolean getJdbc() {
        return hasJdbc;
    }
    public Connection getConn() {
        return conn;
    }
    private boolean init_sqlite3(String rootDir){
        boolean ret=false;
        try {
            conn= DriverManager.getConnection ( "jdbc:sqlite:aaaa.db" );
            DatabaseMetaData meta=conn.getMetaData();
            ResultSet rs = meta.getTables(null,null,TBK.TABLE_NAME,null);
            boolean errorDb=false;
            if(rs.next()){
            }else{
                errorDb=true;
            }
            rs.close();
            if (errorDb){
                LogX.e(TAG,"no found table SNBK！");
                File dest=new File(rootDir+"/aaaa.db");
                File source=new File(rootDir+"/a");
                if (source.exists()){
                    FileChannel inputChannel = null;
                    FileChannel outputChannel = null;
                    try {
                        inputChannel = new FileInputStream(source).getChannel();
                        outputChannel = new FileOutputStream(dest).getChannel();
                        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                        ret=true;
                    } finally {
                        inputChannel.close();
                        outputChannel.close();
                    }
                }
            }else{
                ret=true;
            }
            //Statement stat = conn.createStatement();
            //ResultSet rs1=stat.executeQuery ( "select * from snbk limit 1" ) ;

        } catch (SQLException e) {
            LogX.e(TAG,"Error: database connect fail！"+e.getStackTrace());
            e.printStackTrace();
        }finally {
            return ret;
        }

    }

    public boolean init(String rootDir) {
        if (isSqlite) {
            return init_sqlite3(rootDir);
        }
        String url = "jdbc:mysql://localhost:3306/aaaa?characterEncoding=utf8&useSSL=true";
        String username = "root";
        String password = "chaofei1";//"a123456";//
        try {
            conn= DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            LogX.e(TAG,"Error: database connect fail！"+e.getStackTrace());
            e.printStackTrace();
        }
        return true;
    }

    public ResultSet query(String sql) {
        ResultSet rs=null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);// int result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            if (rs ==null) {
                LogX.e(TAG,"Error:NULL");
            }
            return rs;
        }catch (SQLException e) {
            LogX.e(TAG,"Error: database has error！"+e.getStackTrace());
            e.printStackTrace();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return rs;
        }   
    }
    public int update(String sql){
        int result=-1;
        try {
            Statement stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            if (result==-1) {
                LogX.e(TAG, "Fail update = " + result);
            }
            return result;
        }catch (SQLException e) {
            LogX.e(TAG,"Error: database has error！ "+e.getStackTrace());
            e.printStackTrace();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }         
    }
    public int getRs(ResultSet rs){
        try {
                while (rs.next()) {
                    System.out.println(rs.getString(1).toString() + "\t" + rs.getString(2).toString());// 入如果返回的是int类型可以用getInt()
                }
        } catch (SQLException e) {
                LogX.e(TAG,"Error: database has error！"+e.getStackTrace());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
                return 1;
    }

    public void closeDb(){
        try{
            conn.close();
        }catch (SQLException e) {
            LogX.e(TAG,"Error: database has error！"+e.getStackTrace());
            e.printStackTrace();
        }  
    }
    public int deleteSnbkOldData(String str_time){
        String sql="";
        if (isSqlite) {
            //select * from snbk where SUBSTR(time,1,10)="2018-04-23"
            sql="delete from "+TBK.TABLE_NAME+" where SUBSTR("+TBK.COLUMN_TIME+",1,10)!=\""+str_time+"\"";
        }else{
            //select * from snbk where SUBSTRING(time,1,10)="2018-04-23"
            sql="delete from "+TBK.TABLE_NAME+" where SUBSTRING("+TBK.COLUMN_TIME+",1,10)!=\""+str_time+"\"";
        }
        update(sql);
        return 0;
    }

    public String getBKsql(){
        String sql="";//select a.time,code,substring(trade,1,6),substring(chgprice,1,6),substring(chgpercent,1,6) from snbk as a,(select time from snbk order by time DESC limit 1) b where a.time=b.time
        sql="select ";
        sql+= "a."+TBK.COLUMN_TIME+",";
        sql+= TBK.COLUMN_TYPE+",";  //TBK.COLUMN_NAME
        sql+= TBK.COLUMN_NAME+",";  //TBK.COLUMN_CODE
        sql+= "substring("+TBK.COLUMN_CHANGEPERCENT+",1,6),";
        sql+= TBK.COLUMN_COUNT+",";
        sql+= TBK.COLUMN_NUMBER+",";
        sql += "substring(" + TBK.COLUMN_TRADE + ",1,6),";
        sql += "substring(" + TBK.COLUMN_CHANGEPRICE + ",1,6)";//;
        if (isSqlite)  {
            sql = sql.replace("substring(", "substr(");
        }
        sql+= "from "+TBK.TABLE_NAME+" as a,(select "+TBK.COLUMN_TIME+" from "+TBK.TABLE_NAME+" order by "+TBK.COLUMN_TIME+" DESC limit 1) b ";
        sql+=" where a."+TBK.COLUMN_TIME+"=b."+TBK.COLUMN_TIME + " order by "+TBK.COLUMN_TYPE+","+TBK.COLUMN_CHANGEPERCENT+" DESC";
        return sql;
    }

}
