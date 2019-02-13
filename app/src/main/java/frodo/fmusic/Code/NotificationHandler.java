package frodo.fmusic.Code;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import frodo.fmusic.Activities.MainActivity;

public class NotificationHandler extends ContextWrapper {

    private NotificationManager manager;

    public static final String CHANNEL_HIGH_ID = "1";
    private final String CHANNEL_HIGH_NAME ="HIGH CHANNEL";
    public static final String CHANNEL_LOW_ID ="2";
    private final String CHANNEL_LOW_NAME ="LOW CHANNEL";

    public NotificationHandler(Context context) {
        super(context);
        createChannel();
    }

    public NotificationManager getManager() {
        if(manager == null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT >= 26){
            //craendo el high channel
            NotificationChannel highChannel = new NotificationChannel(
                    CHANNEL_HIGH_ID,
                    CHANNEL_HIGH_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            //configuraciones extras
            highChannel.enableLights(true); //habilita led de notificaciones
            highChannel.setLightColor(Color.YELLOW); //dar color a led
            highChannel.setShowBadge(true); //agrega botoncito en el icono en el launcher
            highChannel.enableVibration(true); //habilitamos la vibracion
            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);



            NotificationChannel lowChannel = new NotificationChannel(
                    CHANNEL_LOW_ID,
                    CHANNEL_LOW_NAME,
                    NotificationManager.IMPORTANCE_LOW);

            getManager().createNotificationChannel(lowChannel);
        }
    }

    public Notification.Builder createNotification(String title, String message){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            return createNotificationWithChannel(title,message,CHANNEL_HIGH_NAME);
        }
        return createNotificationWithoutChannel(title,message);
    }

    //PARA VERSIONES SUPERIORES A OREO
    private Notification.Builder createNotificationWithChannel(String title, String message,String channelID){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Intent intent =  new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            return new Notification.Builder(getApplicationContext(),channelID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    .setAutoCancel(true);
        }
        return null;
    }

    //PARA VERSIONES INFERIORES A OREO
    private Notification.Builder createNotificationWithoutChannel(String title, String message){
        Intent intent =  new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .setAutoCancel(true);
    }

}
