package me.aktor.sample.store.neo.app.details;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import me.aktor.sample.store.neo.R;

/**
 * Created by eto on 11/12/13.
 */
public class NoteEditFragment extends NoteDetailBase {

    @Override
    protected void swapContent(String title, String content) {
        mTitleInput.setText(title);
        mContentInput.setText(content);
    }

    @Override
    protected void clearContent() {
        swapContent(null,null);
    }


    public static interface OnEditNoteListener {
        public void onNewNoteContent(String title,String content);

        void discard();
    }


    private EditText mTitleInput;
    private EditText mContentInput;
    private OnEditNoteListener mEditNoteListener;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        prepareActionBar(fClick);
        setContentView(R.layout.fragment_edit,false);
        mTitleInput = findViewById(R.id.edt_title);
        mContentInput = findViewById(R.id.edt_content);
    }


    private void prepareActionBar(View.OnClickListener listener) {
        ActionBar ab = getActionBar();
        final LayoutInflater inflater = (LayoutInflater)ab.getThemedContext()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View customView = inflater.inflate(R.layout.actionbar_done_discard,null);

        customView.findViewById(R.id.action_done).setOnClickListener(listener);
        customView.findViewById(R.id.action_discard).setOnClickListener(listener);

        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
          ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);

        ab.setCustomView(customView,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                               ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setOnEditNoteListener(OnEditNoteListener listener){
        mEditNoteListener = listener;
    }

    private final View.OnClickListener fClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mEditNoteListener==null)return;
            switch (v.getId()){
                case R.id.action_done:
                String title = mTitleInput.getText().toString();
                String content = mContentInput.getText().toString();
                mEditNoteListener.onNewNoteContent(title,content);
                break;
                case R.id.action_discard:
                    mEditNoteListener.discard();
            }
        }
    };
}
