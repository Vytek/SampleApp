package me.aktor.sample.store.neo.app.home;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.aktor.sample.store.neo.NoteContract;
import me.aktor.sample.store.neo.R;
import me.aktor.sample.store.neo.app.details.NotesDetailActivity;
import me.aktor.sample.store.neo.common.BaseActivity;
import me.aktor.sample.store.neo.sync.SyncService;
import me.aktor.sample.store.neo.sync.push.PushRegister;
import me.aktor.sample.store.neo.sync.push.PushRegistrationService;
import me.aktor.sample.store.neo.utils.L;

/**
 * Created by eto on 11/12/13.
 */
public class NotesListActivity extends BaseActivity {
    private static final int INSERT_NEW = 1;
    private static final int EDIT_ITEM = 2;
    private final Listeners mListeners = new Listeners();
    private PushRegister mGCMReg;
    private String mRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        handleGCMRegistration();
    }

    private void handleGCMRegistration() {
        mGCMReg = PushRegister.getInstance(this);
        if (PushRegister.isPlayAvailable(this)){
            mRegistration = mGCMReg.getRegistrationId();
            Log.e("XXXNNXXX", "REGISTERED: " + mRegistration);

            if (mRegistration.isEmpty()){
                PushRegistrationService.register(this);

            } else {
                Log.e("XXXNNXXX","REGISTERED: "+mRegistration);

            }
        } else {
            Log.e("XXXNNXXX","ALT");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushRegister.isPlayAvailable(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        switch (item.getItemId()){
            case R.id.action_add_note:
                showDetails(NoteContract.Note.CONTENT_URI,true);
                break;
            case R.id.action_settings:
                break;
            case R.id.action_refresh:
                SyncService.syncNow(this);
                break;
            default:
                handled = false;
        }
        return handled || super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof NoteListFragment){
            ((NoteListFragment) fragment).setOnNoteSelectedListener(mListeners);
        }
    }


    private void showDetails(Uri uri,boolean edit){
        Intent intent = new Intent(this, NotesDetailActivity.class);
        L.d("uri is "+uri);
        String type = getContentResolver().getType(uri);
        L.d("type is " + type);
        if (type.equals(NoteContract.Note.DIR_MIME_TYPE)){
            intent.setAction(Intent.ACTION_INSERT);
            intent.setData(uri);
            startActivityForResult(intent,INSERT_NEW);
        } else if (edit){
            intent.setAction(Intent.ACTION_EDIT);
            intent.setData(uri);
            startActivityForResult(intent,EDIT_ITEM);
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }

    }

    private class Listeners implements NoteListFragment.OnNoteSelectedListener{

        @Override
        public void onNoteSelected(long id) {
            Uri uri = ContentUris.withAppendedId(NoteContract.Note.CONTENT_URI,id);
            L.d("Clicked on uri "+uri);
            showDetails(uri, false);
        }

        @Override
        public void onAddNote() {
            showDetails(NoteContract.Note.CONTENT_URI,true);
        }
    }
}
