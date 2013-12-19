package me.aktor.sample.store.neo.app.details;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import me.aktor.sample.store.neo.R;

/**
 * Created by eto on 12/12/13.
 */
public class NoteViewFragment extends NoteDetailBase {

    private TextView mTitle;
    private TextView mContent;
    private SwitchToEditModeListener mListener;
    public static interface SwitchToEditModeListener {
        public void onSwitchToEditMode();
    }


    public void setSwithToEditModeListener(SwitchToEditModeListener listener){
        mListener =listener;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.fragment_details,false);
        mTitle = findViewById(R.id.tv_title);
        mContent = findViewById(R.id.tv_content);

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                                        ActionBar.DISPLAY_SHOW_TITLE
                ,
                                        ActionBar.DISPLAY_SHOW_CUSTOM |
                                        ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

    }


    @Override
    protected void swapContent(String title, String content) {
        mTitle.setText(title);
        mContent.setText(content);
    }

    @Override
    protected void clearContent() {
        swapContent(null,null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.view,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_edit_note){
            if (mListener!=null){
                mListener.onSwitchToEditMode();
            }
            return true;
        }else {
           return super.onOptionsItemSelected(item);
        }
    }
}
