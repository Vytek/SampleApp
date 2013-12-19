package me.aktor.sample.store.neo.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by eto on 11/12/13.
 */
public abstract class BaseActivity extends ActionBarActivity {
    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
    }


    @SuppressWarnings("unchecked")
    protected <T extends Fragment> T findFragmentById(int id){
        return (T)mFragmentManager.findFragmentById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Fragment> T findFragmentByTag(String tag){
        if (tag == null) throw new NullPointerException("tag");
        return (T)mFragmentManager.findFragmentByTag(tag);
    }


    protected void onDetachFragment(Fragment fragment){

    }
}
