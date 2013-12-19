package me.aktor.sample.store.neo.sync.push;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import me.aktor.sample.store.neo.utils.L;
import me.aktor.sample.store.neo.utils.LogicError;

/**
 * Created by eto on 12/12/13.
 */
public class PushRegister {
    public static final int RESOLVE_PLAY_ERROR_REQUEST = 10000;

    private static PushRegister sPushRegister;
    private static final Object SLOCK = new Object();

    //project id grounded-plane-427
    //project number 844255035643

    private static final String SENDER_ID = "844255035643";
    private static final String PUSH_PREFS = "pushprefs";
    private static final String REG_ID_KEY = "reg_id_key";
    private static final String APP_VERSION_KEY = "app_ver";

    private SharedPreferences mPrefs;
    private Context mContext;
    private GoogleCloudMessaging mGCM;

    private PushRegister(Context context){
        mContext = context.getApplicationContext();
        mGCM = GoogleCloudMessaging.getInstance(mContext);
        mPrefs = mContext.getSharedPreferences(PUSH_PREFS,Context.MODE_PRIVATE);
    }


    public static PushRegister getInstance(Context context){
        synchronized (SLOCK) {
            if(sPushRegister==null){
                sPushRegister = new PushRegister(context);
            }
            return sPushRegister;
        }
    }



    public String getRegistrationId(){
        String regId = mPrefs.getString(REG_ID_KEY,"");
        if (regId.isEmpty()){
            return "";
        }

        int regVersion = mPrefs.getInt(APP_VERSION_KEY,Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (regVersion!=currentVersion){
            return "";
        }
        return regId;
    }

    public String register(){
        try {
            String regId = mGCM.register(SENDER_ID);

            propagateRegistration(regId);
            storeRegistration(regId);
            return regId;
        } catch (IOException e) {
            Log.e("ERROR","Error",e);
            return null;
        }
    }


    private void clearRegistration() {
        mPrefs.edit().clear().commit();
    }

    private void storeRegistration(String regId) {
        mPrefs.edit().putString(REG_ID_KEY,regId)
                     .putInt(APP_VERSION_KEY, getAppVersion())
                     .commit();
    }

    public void propagateRegistration(String registrationId){
        L.d("REGISTRATION: "+registrationId);
    }

    public void unregister(){
        try {
            String reg = getRegistrationId();
            if (reg.isEmpty()){
                return;
            }
            mGCM.unregister();
            propagateUnregister(reg);
            clearRegistration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void propagateUnregister(String reg) {
        L.d("UNREG: "+reg);
    }

    private int getAppVersion(){
        try {
            PackageInfo pkg = mContext.getPackageManager()
                            .getPackageInfo(mContext.getPackageName(),0);
            return pkg.versionCode;
        } catch (PackageManager.NameNotFoundException e){
            // could never happen
            throw new LogicError("Cannot get here");
        }
    }

    public static boolean isPlayAvailable(Activity context){
        int response = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (response!= ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(response)){
                GooglePlayServicesUtil.getErrorDialog(response,context,RESOLVE_PLAY_ERROR_REQUEST).show();
            } else {
                //there was an error
               context.finish();
            }
            return false;
        }
        return true;
    }
}
