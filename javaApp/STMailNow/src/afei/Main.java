package afei;

import afei.api.Global;
import afei.api.LogX;

import java.beans.IntrospectionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class Main {
    private static String rootDir= System.getProperty("user.dir") + "";
    public static void main(String[] args) {
        LogX.init();
        Global.getJdbc().init();
        //MailCmd.run(args);
        nowdown();
        //new NowParser().input(rootDir+"/A/AA/20180411/AA,180411_172943.zip",rootDir+"/temp");
    }

    private static SimpleDateFormat HHmm = new SimpleDateFormat("HHmm");
    private static int period=60*1000;
    private static void nowdown(){
        Timer timer = new Timer();
        timer.schedule(
                new java.util.TimerTask() {
                    public void run() {
                        String timenow=HHmm.format(new Date());
                        int t= Integer.valueOf(timenow);
                        //if ((t>(915-20) && t<1131) || (t>(1300-20) && t<1515)) {
                        if ( (t>915 && t<1131) || (t>1300 && t<1515)) {
                            new NowDown().start(Global.workPath);//
                            LogX.w("", "gogogog---->");
                        }
                    }
                }, 0, period);
    }

}