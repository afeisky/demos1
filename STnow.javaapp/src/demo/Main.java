package demo;

import demo.api.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static String TAG="Main";
    //private static String rootDir= System.getProperty("user.dir") + "";
    public static void main(String[] args) {
        System.out.println("args.length="+args.length );
        if (args.length >0) {
            System.out.println(args[0]);
            Global.workPath = args[0];
        }else {
            System.out.println("Error: not found Dir! : /A/");
            System.exit(0);
        }
        File f = new File(Global.workPath);
        if (Global.workPath.length()==0 || !f.exists()) {
            System.out.println("Error: not found Dir! : /A/");
            System.exit(0);
        }
        LogX.init(Global.workPath+"/log.log");
        Global.getJdbc().init(Global.workPath);
        //new SnA().parse("");
        //new SnA().start();//
        timer1(20);
        //new SnNowInput().start(Global.workPath,"2018-06-14");//timer1(200000);
        //new Sse2temp().start();//timer1(200000);
    }
    private static void test1(){
        File f=new File("/disk1/workspace/A/AA/20180613/sse,180613_151608.json");
        new NowParser().parserByFilename(f,f.getName());
    }
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static void timer1(int seconds) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                now_start();
            }
        }, 0,seconds*1000);
    }

    private static void now_start(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String timeNow = sdf.format(new Date());
                        String hhmm=new SimpleDateFormat("HHmm").format(new Date());
                        int hm=Integer.valueOf(hhmm);
                        if (hm<915){
                            LogX.w(TAG,"Exit time: "+hhmm);
                            return;
                        }else{
                            LogX.d(TAG,"START--> "+timeNow);
                        }
                        SnA sna=new SnA(Global.getDailyDir());
                        SnBK snbk=new SnBK(Global.getDailyDir());
                        sna.start();//new SnNowInput().start(Global.workPath,"");
                        snbk.start();//new SnNowInput().start(Global.workPath,"");

                        int seconds=20;
                        while (seconds>0){
                            seconds--;
                            if (sna.isDone() && snbk.isDone()){
                                seconds=0;
                                Thread.sleep(500);
                                new SendD().send(sna,snbk);
                            }
                            Thread.sleep(1000);
                        }
                        timeNow = sdf.format(new Date());
                        LogX.d(TAG,"DONE<-- "+timeNow);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }
    private static void timer2_once(int seconds) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                //new SendD().send();
            }
        }, seconds*1000);
    }
}