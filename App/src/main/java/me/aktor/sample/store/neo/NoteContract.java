package me.aktor.sample.store.neo;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eto on 11/12/13.
 */
public final class NoteContract {
    private NoteContract(){}

    public final static String AUTHORITY = "me.aktor.sample.store.neo.provider";

    public final static Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);


    private final static String BASE_MIME_TYPE ="/vnd.store.neo.provider.";


    public final static class Note {

        private Note(){}

        public final static String PATH = "notes";

        public final static String DIR_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+BASE_MIME_TYPE+PATH;
        public final static String ITEM_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+BASE_MIME_TYPE+PATH;

        public final static Uri CONTENT_URI = NoteContract.CONTENT_URI.buildUpon().appendPath(PATH).build();

        public final static String _ID = BaseColumns._ID; // BaseColumns._ID = "_id"
        public final static String TITLE = "title";
        public final static String CONTENT = "content";
        public final static String DATE = "date";
        public final static String SYNC_STATE = "_sync_state";

        public final static int SYNC_STATE_LOCAL = 0;

        public final static String DEFFAULT_SORT_ORDER = TITLE+ " ASC";
        public final static String DATE_ORDER = DATE+" ASC";
        public final static String DATE_INVERTED_OREDER = DATE+" DESC";

        public static final String[] DEFAULT_PROJECTION = {
                _ID,TITLE,CONTENT,DATE,SYNC_STATE
        };


    }
}
