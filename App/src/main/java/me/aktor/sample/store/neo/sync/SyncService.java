package me.aktor.sample.store.neo.sync;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import me.aktor.sample.store.neo.NoteContract;

/**
 * Created by eto on 12/12/13.
 */
public class SyncService extends Service {
    private static Sync sSync = null;
    private final static Object SLOCK = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (SLOCK){
            if (sSync==null){
                sSync = new Sync(getApplicationContext(),true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSync.getSyncAdapterBinder();
    }

    public static void setAutomaticSync(Context context,boolean state){
        ContentResolver.setSyncAutomatically(
                StubAuthService.getOrCreateAccount(context),
                NoteContract.AUTHORITY, state);
    }

    public static void syncNow(Context context){
       syncNow(context,false);
    }

    public static void setSyncPeriodic(){
        ContentResolver.addPeriodicSync(null,NoteContract.AUTHORITY,null, TimeUnit.DAYS.toMillis(1));
    }

    public static void syncNow(Context context,boolean byGcm){
        Bundle manualSync = new Bundle();
        manualSync.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        manualSync.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        manualSync.putBoolean(Sync.ISSUED_BY_GCM,byGcm);
        ContentResolver.requestSync(StubAuthService.getOrCreateAccount(context),NoteContract.AUTHORITY,manualSync);
    }

    public static void syncEventually(Context context, boolean fromGcm) {
        Bundle eventual = new Bundle();
        eventual.putBoolean(Sync.ISSUED_BY_GCM,fromGcm);

        ContentResolver.requestSync(StubAuthService.getOrCreateAccount(context),NoteContract.AUTHORITY,eventual);
    }
}
