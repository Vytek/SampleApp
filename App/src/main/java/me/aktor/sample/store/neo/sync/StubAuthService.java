package me.aktor.sample.store.neo.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by eto on 12/12/13.
 */
public class StubAuthService extends Service {
    public static String ACCOUNT_TYPE = "aktor.notes.neo.me";
    public static String ACCOUNT_NAME = "DUMMY";

    private StubAuthenticator mAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = new StubAuthenticator(this);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mAuth.getIBinder();
    }



    public static Account getOrCreateAccount(Context context) {
        Account account = new Account(ACCOUNT_NAME,ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(context);
        if(accountManager.addAccountExplicitly(account,null,null)){

        }else {
            //already exists
        }
        return account;
    }

    /**
     * Created by eto on 12/12/13.
     */
    static class StubAuthenticator extends AbstractAccountAuthenticator {

        public StubAuthenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }
    }
}
