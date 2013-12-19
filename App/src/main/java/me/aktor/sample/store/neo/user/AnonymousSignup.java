package me.aktor.sample.store.neo.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.IOException;

/**
 * Created by eto on 12/12/13.
 */
public class AnonymousSignup {
    private static AnonymousSignup sAnonymousSignup;
    private final static Object LOCK = new Object();
    private final static String SCOPE = "audience:server:client_id:";

    private AccountManager mAccountManager;
    private Context mContext;
    private final String mClientId;

    private AnonymousSignup(Context context,String clientId){
        mClientId = clientId;
        mContext = context.getApplicationContext();
        mAccountManager = AccountManager.get(mContext);
    }

    public static AnonymousSignup get(Context context,String clientId){
        synchronized (LOCK){
            if(sAnonymousSignup==null){
                sAnonymousSignup= new AnonymousSignup(context,clientId);
            }
        }
        return sAnonymousSignup;
    }

    public String getAccount(){
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        if (accounts.length>0){
            return accounts[0].name;
        }else {
            return null;
        }
    }

    public String getToken(){
        try {
            return GoogleAuthUtil.getToken(mContext,getAccount(),SCOPE+mClientId);
        } catch (IOException e) {
            return null;
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        return null;
        }
    }


}
