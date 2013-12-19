package me.aktor.sample.store.neo.app;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;

import me.aktor.sample.store.neo.NoteContract;

/**
 * Created by eto on 12/12/13.
 */
public class WorkerFragment extends Fragment {
    public final static String INSERT_URI = "insert_uri";
    public final static String ACTION_TOKEN = "action_token";



    public interface OnActionCompletedListener {
        public void onActionComplete(int actionId,Bundle data);
    }

    private ContentHandler mContentHandler;
    private OnActionCompletedListener mActionListener;
    private boolean mReady;
    private Integer mLastAction;
    private Bundle mLastData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContentHandler = new ContentHandler(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mReady = true;
        deliverPending();
    }

    @Override
    public void onPause() {
        super.onPause();
        mReady = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActionListener=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentHandler.detach();
        mContentHandler = null;
    }

    private void deliverPending(){
        if (mLastAction!=null){
            if(mActionListener!=null){
                mActionListener.onActionComplete(mLastAction,mLastData);
            }
            mLastAction= null;
            mLastData = null;
        }
    }
    public void setActionListener(OnActionCompletedListener listener){
        mActionListener = listener;
    }


    private void completeAction(int token, Uri uri) {
        Bundle data = new Bundle();
        data.putParcelable(INSERT_URI, uri);
        if (mReady&& mActionListener!=null){
            mActionListener.onActionComplete(token,data);
        } else {
            mLastAction = token;
            mLastData = data;
        }
    }

    public void createNote(int opId,String title,String content){
        ContentValues values = prepareValues(title, content);
        mContentHandler.startInsert(opId,null, NoteContract.Note.CONTENT_URI,values);
    }

    private ContentValues prepareValues(String title, String content) {
        ContentValues values = new ContentValues();
        values.put(NoteContract.Note.TITLE,title);
        values.put(NoteContract.Note.CONTENT,content);
        Time now = new Time();
        now.setToNow();
        values.put(NoteContract.Note.DATE, now.format3339(true));
        return values;
    }


    public void updateNote(int editNote,Uri uri, String title, String content) {
        ContentValues values = prepareValues(title,content);
        mContentHandler.startUpdate(editNote,uri,uri,values,null,null);
    }

    private static class ContentHandler extends AsyncQueryHandler{

        private WorkerFragment fragment;
        public ContentHandler(WorkerFragment f) {
            super(f.getActivity().getApplicationContext().getContentResolver());
            fragment= f;
        }


        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
            if (fragment!=null){
                fragment.completeAction(token,(Uri)cookie);
            }
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            if (fragment!=null){
                fragment.completeAction(token, uri);
            }
        }

        private void detach(){
            fragment = null;
        }
    }


}
