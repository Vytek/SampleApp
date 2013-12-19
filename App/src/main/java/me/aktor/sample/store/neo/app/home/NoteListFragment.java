package me.aktor.sample.store.neo.app.home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import me.aktor.sample.store.neo.NoteContract;
import me.aktor.sample.store.neo.R;
import me.aktor.sample.store.neo.common.BaseFragment;
import me.aktor.sample.store.neo.utils.LogicError;

/**
 * Created by eto on 11/12/13.
 */
public class NoteListFragment extends BaseFragment {
    private final Callbacks mCallbacks = new Callbacks();

    private AbsListView mListView;
    private NoteListAdapter mAdapter;
    private OnNoteSelectedListener mListener;



    public static interface OnNoteSelectedListener {
        public void onNoteSelected(long id);
        public void onAddNote();
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.fragment_notes);
        mAdapter = new NoteListAdapter(getActivity());

        mListView  = findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>)mListView).setAdapter(mAdapter);

        mListView.setOnItemClickListener(mCallbacks);
        View v  = findViewById(android.R.id.empty);
        v.setOnClickListener(mCallbacks);
        mListView.setEmptyView(findViewById(android.R.id.empty));

    }

    public void setOnNoteSelectedListener(OnNoteSelectedListener listener){
        mListener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(R.id.NOTES_LOADER,null,mCallbacks);
    }

    private class Callbacks implements AdapterView.OnItemClickListener,
            LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mListener!=null){
                mListener.onNoteSelected(id);
            }
        }

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            if (loaderId == R.id.NOTES_LOADER){
                return new CursorLoader(getActivity(), NoteContract.Note.CONTENT_URI, NoteContract.Note.DEFAULT_PROJECTION,null,null, NoteContract.Note.DEFFAULT_SORT_ORDER);
            }
            throw new LogicError("unexpected loaderId");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            if (cursorLoader.getId()==R.id.NOTES_LOADER){
                mAdapter.swapCursor(cursor);
            }else {
                throw new LogicError("unexpected loaderId");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            if (cursorLoader.getId()==R.id.NOTES_LOADER){
                mAdapter.swapCursor(null);
            } else {
               throw new LogicError("unexpected loaderId");
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==android.R.id.empty && mListener!=null){
                mListener.onAddNote();
            }
        }
    }

}
