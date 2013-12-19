package me.aktor.sample.store.neo;

import android.app.Application;

import me.aktor.sample.store.neo.sync.StubAuthService;

/**
 * Created by eto on 12/12/13.
 */
public class NotesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StubAuthService.getOrCreateAccount(this);
    }

}
