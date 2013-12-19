package me.aktor.sample.store.neo.app.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import me.aktor.sample.store.neo.NoteContract;
import me.aktor.sample.store.neo.R;
import me.aktor.sample.store.neo.app.WorkerFragment;
import me.aktor.sample.store.neo.common.BaseActivity;
import me.aktor.sample.store.neo.utils.LogicError;

/**
 * Created by eto on 11/12/13.
 */
public class NotesDetailActivity extends BaseActivity {
    private final static String FRAGMENT_TAG = "fragment_tag";
    private final static String WORKER = "worker_tag";

    private final static int NEW_NOTE = 1;
    private static final int EDIT_NOTE = 2;

    private final Callbacks mCallbacks = new Callbacks();
    private WorkerFragment mWorker;
    private Uri mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (savedInstanceState==null){
            Fragment fragment =processIntent(intent);
            mFragmentManager.beginTransaction()
                            .add(R.id.host,fragment,FRAGMENT_TAG)
                            .add(new WorkerFragment(),WORKER)
                            .commit();
        } else {
            mData = savedInstanceState.getParcelable("data");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data",mData);
    }

    private Fragment processIntent(Intent intent) {
        mData = intent.getData();
        if (intent.getAction().equals(Intent.ACTION_VIEW)){

            return NoteDetailBase.createWithData(mData,true);
        } else if (intent.getAction().equals(Intent.ACTION_INSERT)){

            return NoteDetailBase.createEmpty();
        } else if(intent.getAction().equals(Intent.ACTION_EDIT)){

            return NoteDetailBase.createWithData(mData,false);
        } else {
            throw new LogicError("not implemented");
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof WorkerFragment) {
            mWorker = (WorkerFragment) fragment;
            mWorker.setActionListener(mCallbacks);
        } else if (fragment instanceof NoteEditFragment) {
            ((NoteEditFragment) fragment).setOnEditNoteListener(mCallbacks);
        } else if (fragment instanceof NoteViewFragment){
            ((NoteViewFragment) fragment).setSwithToEditModeListener(mCallbacks);
        }
    }


    private class Callbacks  implements WorkerFragment.OnActionCompletedListener, NoteEditFragment.OnEditNoteListener, NoteViewFragment.SwitchToEditModeListener {

        @Override
        public void onActionComplete(int actionId, Bundle data) {

            if (actionId==NEW_NOTE){
                Uri uri = data.getParcelable(WorkerFragment.INSERT_URI);
                setResult(RESULT_OK,new Intent().setData(uri));
                finish();

            } else if (actionId==EDIT_NOTE){
                if (!getSupportFragmentManager().popBackStackImmediate()){
                    Uri uri = data.getParcelable(WorkerFragment.INSERT_URI);
                    setResult(RESULT_OK,new Intent().setData(uri));
                    finish();
                }
            }
        }

        @Override
        public void onNewNoteContent(String title, String content) {

            if (getContentResolver().getType(mData).equals(NoteContract.Note.DIR_MIME_TYPE)){
                mWorker.createNote(NEW_NOTE,title,content);
            } else {
                mWorker.updateNote(EDIT_NOTE,mData,title,content);
            }
        }

        @Override
        public void discard() {
           if (!getSupportFragmentManager().popBackStackImmediate())
                finish();
        }

        @Override
        public void onSwitchToEditMode() {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,NoteDetailBase.createWithData(mData,false),FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
