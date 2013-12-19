package me.aktor.sample.store.neo.sync.push;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by eto on 12/12/13.
 */
public class PushRegistrationService extends IntentService {
    private final static String ACTION_REGISTER= "reg_action";

    private PushRegister mPushRegister;

    public PushRegistrationService(){
        super("PRS");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPushRegister = PushRegister.getInstance(this);
    }

    public static void register(Context context){
        start(context,true);
    }

    public static void unregister(Context context){
        start(context,false);
    }

    private static void start(Context context,boolean register){
        Intent intent = new Intent(context,PushRegistrationService.class);
        intent.putExtra(ACTION_REGISTER,register);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getBooleanExtra(ACTION_REGISTER,true)){
            String r =mPushRegister.register();
            Log.e("XXXNNXXX",r);
        } else {
            mPushRegister.unregister();
        }
    }
}
