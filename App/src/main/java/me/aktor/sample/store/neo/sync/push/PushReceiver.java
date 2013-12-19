package me.aktor.sample.store.neo.sync.push;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import me.aktor.sample.store.neo.R;
import me.aktor.sample.store.neo.app.home.NotesListActivity;
import me.aktor.sample.store.neo.sync.SyncService;

/**
 * Created by eto on 12/12/13.
 */
public class PushReceiver extends WakefulBroadcastReceiver {
    private final static String KEY_SYNC_REQUEST = "sync";
    private final static String KEY_EXTRA = "extra";

    private final static int NOTIFY = 4;

    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)&&
                intent.getBooleanExtra(KEY_SYNC_REQUEST,false)){

            SyncService.syncNow(context,true);
            //SyncService.syncEventually(context,true);

        }
        notify(context, intent.getStringExtra(KEY_EXTRA));

//        alternative 2
//        ComponentName target = new ComponentName(context.getPackageName(),
//                                                 PushService.class.getName());
//        WakefulBroadcastReceiver.startWakefulService(context, intent.setComponent(target));

        setResultCode(Activity.RESULT_OK);


    }

    private static void notify(Context context,String data){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent intent = new Intent(context, NotesListActivity.class);

        PendingIntent action  = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("Received")
               .setContentText(data)
               .setSmallIcon(R.drawable.ic_stat)
               .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher))
               .setWhen(System.currentTimeMillis())
               .setContentIntent(action)
               .setAutoCancel(true);
        manager.notify(NOTIFY,builder.build());
    }


    private static void notifyUser(Context context,String data){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFY,new NotificationCompat.Builder(context).setContentTitle(data).setContentText("Ping!").setSmallIcon(android.R.drawable.ic_notification_overlay).build());
    }
}
