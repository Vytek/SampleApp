package me.aktor.sample.store.neo.sync.push;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by eto on 12/12/13.
 */
public class PushService extends IntentService {
    public PushService(){
        super("GCM");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle data = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!data.isEmpty()){
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(data)){
                //do some work
            }
        }

        PushReceiver.completeWakefulIntent(intent);

    }

}
