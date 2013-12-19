package me.aktor.sample.store.neo.app.home;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.aktor.sample.store.neo.NoteContract;
import me.aktor.sample.store.neo.R;
import me.aktor.sample.store.neo.utils.Text;

/**
 * Created by eto on 11/12/13.
 */
class NoteListAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;
    private IndexHolder mIndexHolder;

    public NoteListAdapter(Context context) {
        super(context, null,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder h = new ViewHolder();
        View v  = mInflater.inflate(R.layout.item_row,viewGroup,false);
        h.title = (TextView) v.findViewById(R.id.tv_row_title);
        h.date = (TextView) v.findViewById(R.id.tv_row_date);
        v.setTag(h);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder h = (ViewHolder) view.getTag();
        if (mIndexHolder==null){
            mIndexHolder = loadColumnIndex(cursor);
        }
        h.title.setText(cursor.getString(mIndexHolder.titleIndex));
        String date = cursor.getString(mIndexHolder.dateIndex);
        h.date.setText(Text.formatDate(date));
    }

    private IndexHolder loadColumnIndex(Cursor cursor) {
        IndexHolder h = new IndexHolder();
        h.titleIndex = cursor.getColumnIndexOrThrow(NoteContract.Note.TITLE);
        h.dateIndex = cursor.getColumnIndexOrThrow(NoteContract.Note.DATE);
        h.id = cursor.getColumnIndexOrThrow(NoteContract.Note._ID);
        return h;
    }


    @Override
    public Cursor swapCursor(Cursor newCursor) {
        mIndexHolder = null;
        return super.swapCursor(newCursor);
    }


    private static class IndexHolder{
        int id;
        int titleIndex;
        int dateIndex;
    }


    private static class ViewHolder {
        TextView title;
        TextView date;
    }
}
