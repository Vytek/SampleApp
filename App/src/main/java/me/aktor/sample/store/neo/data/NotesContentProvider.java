package me.aktor.sample.store.neo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.database.DatabaseUtilsCompat;
import android.text.TextUtils;

import java.util.Locale;

import static me.aktor.sample.store.neo.NoteContract.*;

public class NotesContentProvider extends ContentProvider {

    private SQLiteOpenHelper mDB;
    public NotesContentProvider() {
    }
    @Override
    public boolean onCreate() {
        mDB = new NotesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)){
            case MATCH_NOTES: return Note.DIR_MIME_TYPE;
            case MATCH_SINGLE_NOTE: return Note.ITEM_MIME_TYPE;
            default: throw unsupportedUri("type",uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (MATCHER.match(uri) == MATCH_NOTES){
            if (values.containsKey(Note._ID)){
                values.remove(Note._ID);
            }
            SQLiteDatabase db = mDB.getWritableDatabase();
            long id = db.insert(Note.PATH, null, values);
            if (id==-1){
                return null;
            }
            getContext().getContentResolver().notifyChange(uri,null,true);
            return ContentUris.withAppendedId(uri,id);
        }
        throw unsupportedUri("insert",uri);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)){
            case MATCH_SINGLE_NOTE:
                selection = contacWhereNoteIdUri(selection,uri);
                break;
            case MATCH_NOTES:
                break;
            default:throw  unsupportedUri("delete",uri);
        }
        SQLiteDatabase db = mDB.getWritableDatabase();
        int deleted = db.delete(Note.PATH, selection, selectionArgs);
        if (deleted>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return deleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (MATCHER.match(uri)){
            case MATCH_NOTES:
                if (values.containsKey(Note._ID)){
                    selection = concatWhereNoteId(selection,values.getAsLong(Note._ID));
                    values.remove(Note._ID);
                }
                break;
            case MATCH_SINGLE_NOTE:
                if (values.containsKey(Note._ID)){
                    values.remove(Note._ID);
                }
                selection = contacWhereNoteIdUri(selection,uri);
                break;
            default: throw unsupportedUri("update",uri);

        }
        SQLiteDatabase db = mDB.getWritableDatabase();
        int updated = db.update(Note.PATH, values, selection, selectionArgs);
        if (updated>0){
            getContext().getContentResolver().notifyChange(uri,null,true);
        }
        return updated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        switch (MATCHER.match(uri)){
            case MATCH_NOTES:

                break;
            case MATCH_SINGLE_NOTE:
                selection = contacWhereNoteIdUri(selection,uri);
                break;
            default: unsupportedUri("query",uri);
        }

        if (projection==null){
            projection = Note.DEFAULT_PROJECTION;
        }

        if (TextUtils.isEmpty(sortOrder)){
            sortOrder = Note.DEFFAULT_SORT_ORDER;
        }

        SQLiteDatabase db = mDB.getReadableDatabase();
        Cursor cur = db.query(true, Note.PATH, projection, selection, selectionArgs, null, null, sortOrder, null);
        if (cur!=null){
            cur.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cur;
    }

    private static String contacWhereNoteIdUri(String selection,Uri uri){
        return DatabaseUtilsCompat.concatenateWhere(selection,Note._ID+" = "+ContentUris.parseId(uri));
    }

    private static String concatWhereNoteId(String selection,long id){
        return DatabaseUtilsCompat.concatenateWhere(selection,Note._ID+" = "+id);
    }

    private static UnsupportedOperationException unsupportedUri(String action,Uri uri){
        return new UnsupportedOperationException(String.format(Locale.US,
                "Uri not supported (%s): %s",action,uri));
    }

    /*
        URI MATCHER
     */

    private final static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int MATCH_NOTES = UriMatcher.NO_MATCH+1;
    private final static int MATCH_SINGLE_NOTE = MATCH_NOTES+1;
    static {
        MATCHER.addURI(AUTHORITY,Note.PATH,MATCH_NOTES);
        MATCHER.addURI(AUTHORITY,Note.PATH+"/#",MATCH_SINGLE_NOTE);
    }


    /*
        Database constants
     */
    private final static String DB_NAME = "notes.db";
    private final static int DB_VERSION= 1;
    private final static SQLiteDatabase.CursorFactory CURSOR_FACTORY = null;
    private final static String CREATE_NOTES =
            "CREATE TABLE IF NOT EXISTS "+Note.PATH+"("+
                    Note._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Note.TITLE+ " TEXT NOT NULL, "+
                    Note.CONTENT+ " TEXT DEFAULT \"\", "+
                    Note.DATE + " TEXT NOT NULL, "+
                    Note.SYNC_STATE+ " INTEGER NOT NULL DEFAULT "+Note.SYNC_STATE_LOCAL+")";

    private final static String DROP_NOTES =
            "DROP TABLE IF EXISTS "+Note.PATH;

    private static class NotesDbHelper extends SQLiteOpenHelper{

        public NotesDbHelper(Context context) {
            super(context, DB_NAME, CURSOR_FACTORY, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NOTES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_NOTES);
        }
    }
}
