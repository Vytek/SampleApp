package me.aktor.sample.store.neo.app.details;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.aktor.sample.store.neo.NoteContract;
import me.aktor.sample.store.neo.R;
import me.aktor.sample.store.neo.common.BaseFragment;
import me.aktor.sample.store.neo.utils.LogicError;

/**
 * Created by eto on 12/12/13.
 */
public abstract class NoteDetailBase extends BaseFragment {
    protected final static String INITIAL_URI ="uri";

    public static NoteDetailBase createEmpty(){
        return new NoteEditFragment();
    }


    public static NoteDetailBase createWithData(Uri data,boolean view){
        NoteDetailBase base=view?new NoteViewFragment():new NoteEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(INITIAL_URI,data);
        base.setArguments(bundle);
        return base;
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    if (i!= R.id.SINGLE_NOTE_LOADER) throw new LogicError("unsupported");
                    Uri uri = bundle.getParcelable(INITIAL_URI);
                    return new CursorLoader(getActivity(),uri,null,null,null,null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                    if (cursor!=null&& cursor.moveToFirst()){
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(NoteContract.Note.TITLE));
                        String content = cursor.getString(cursor.getColumnIndex(NoteContract.Note.CONTENT));
                        swapContent(title,content);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> cursorLoader) {
                    clearContent();
                }
            };



    protected abstract void swapContent(String title,String content);

    protected abstract void clearContent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args!=null && args.containsKey(INITIAL_URI)){
            getLoaderManager().initLoader(R.id.SINGLE_NOTE_LOADER,args,mCallbacks);
        }
    }

}
