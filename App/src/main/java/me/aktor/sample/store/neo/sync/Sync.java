package me.aktor.sample.store.neo.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by eto on 12/12/13.
 */
public class Sync extends AbstractThreadedSyncAdapter {
    final static String ISSUED_BY_GCM=Sync.class.getCanonicalName()+".ISSUED_BY_GCM";
    private ContentResolver mContentResolver;

    public Sync(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver= context.getContentResolver();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Sync(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {
        leggiStato();
        Log.d("TEST_SYNC","STARTED AT "+System.currentTimeMillis());
        aggiornaStato();
    }

    private void aggiornaStato() {

    }

    private void leggiStato() {

    }
}
