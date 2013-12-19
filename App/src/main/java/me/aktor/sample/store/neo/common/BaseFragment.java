package me.aktor.sample.store.neo.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eto on 11/12/13.
 */
public class BaseFragment extends Fragment {

    private LayoutInflater mTempInflater;
    private ViewGroup mContainer;
    private View mRootView;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTempInflater = inflater;
        mContainer = container;
        onCreateView(savedInstanceState);
        mTempInflater = null;
        mContainer = null;
        return mRootView;
    }

    protected final void setContentView(int layout,boolean attach){
        if (mTempInflater == null) throw new IllegalStateException("View already attached");
        mRootView = mTempInflater.inflate(layout,mContainer,attach);
        mContainer = null;
        mTempInflater= null;
    }

    protected final void setContentView(int layout){
        setContentView(layout,true);
    }

    protected void onCreateView(Bundle savedInstanceState){

    }


    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int id){
        T v =(T)mRootView.findViewById(id);
        if (v==null)throw new NullPointerException("View not present in layout");
        return v;
    }

    public final void setOnClickListener(int id,View.OnClickListener listener){
        findViewById(id).setOnClickListener(listener);
    }

    public final ActionBar getActionBar(){
        Activity a = getActivity();
        if (a instanceof ActionBarActivity){
            return ((ActionBarActivity) a).getSupportActionBar();
        }
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Activity a  = getActivity();
        if (a instanceof BaseActivity){
            ((BaseActivity) a).onDetachFragment(this);
        }
    }
}

