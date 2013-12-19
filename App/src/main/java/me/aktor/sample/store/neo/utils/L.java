package me.aktor.sample.store.neo.utils;

import android.util.Log;

/**
 * Created by eto on 12/12/13.
 */
public class L {
    private L(){}
    private final static String APP_TAG = "SAMPLE";

    public final static void d(String message){
        Log.d(APP_TAG,message);
    }
}
