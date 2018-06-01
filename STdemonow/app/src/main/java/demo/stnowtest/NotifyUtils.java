package demo.stnowtest;

/**
 * Created by chaofei on 18-5-18.
 */


    import android.app.Notification;
            import android.app.NotificationChannel;
            import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.Context;
            import android.content.ContextWrapper;
    import android.content.Intent;
    import android.os.Build;
            import android.support.v4.app.NotificationCompat;
    import android.widget.RemoteViews;

    import demo.api.LogX;

/**
 * Created by chaofei on 2018/05/21.
 */

public class NotifyUtils extends ContextWrapper {
    private static String TAG="NofityUtils";

    private NotificationManager manager;
    private Notification.Builder mBuilder;
    private NotificationCompat.Builder mBuilder25;

    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotifyUtils(Context context){
        super(context);
    }

    public void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public Notification.Builder getChannelNotification(String title, String content){
        Notification.Builder mNotifyBuilder=new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(false) //donnot cancel.
                .setOngoing(true)//donnot delete
                .setStyle(new Notification.BigTextStyle().bigText(content))
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);
        return mNotifyBuilder;
    }
    public NotificationCompat.Builder getNotification_25(String title, String content){
        NotificationCompat.Builder mNotifyBuilder=new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(false)
                .setOngoing(true)//donnot delete
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);
        return mNotifyBuilder;

    }
    public void sendNotification(String title, String content,PendingIntent nofityIntent){
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            mBuilder = getChannelNotification(title, content);
            mBuilder.setFullScreenIntent(nofityIntent,false);
            //mBuilder.setContentIntent(nofityIntent);
            getManager().notify(1,mBuilder.build());
        }else{
            mBuilder25 = getNotification_25(title, content);
            mBuilder25.setFullScreenIntent(nofityIntent,false);

            getManager().notify(1,mBuilder25.build());
        }
    }
    public void changeTitleContent(String title,String text){
        if (Build.VERSION.SDK_INT>=26) {
            if (title != null) {
                LogX.w(TAG,"changeTitleContent");
                mBuilder.setContentTitle(title);
            }
            if (text != null){
                mBuilder.setContentText(text);
            }
            getManager().notify(1, mBuilder.build());
        }else{
            if (title!=null) {
                mBuilder25.setContentTitle(title);
            }
            if (text!=null) {
                mBuilder25.setContentText(text);
            }
            getManager().notify(1, mBuilder25.build());
        }
    }
    public void changeTitleContent(PendingIntent intent){
        if (Build.VERSION.SDK_INT>=26) {
            if (intent != null) {
                mBuilder.setFullScreenIntent(intent,false);
            }
            getManager().notify(1, mBuilder.build());
        }else{
            if (intent!=null) {
                mBuilder25.setFullScreenIntent(intent,false);
            }
            getManager().notify(1, mBuilder25.build());
        }
    }

}
