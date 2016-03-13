package dbtable.aap.tablebrowser;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewSwitcher;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

/**
 * The top lovel activity that dumps the content of the selected DB table. The {@link ContactsBrowserActivity#uris}
 * contains the list of table {@link Uri}. {@link ContactsBrowserActivity#uriArrayOffset} decides the current table
 * that is read and displayed.
 */
public class ContactsBrowserActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<Cursor>, ContactsAdapter.RowClickHandler {
    static final String EXTRA_SKIP_INIT_LOADER = "com.linkedin.contactsbrowser.skip_loader";
    private static List<Uri> uris;
    static {
        uris = new ArrayList<>();
        uris.add(ContactsContract.RawContacts.CONTENT_URI);
        uris.add(ContactsContract.Contacts.CONTENT_URI);
        uris.add(ContactsContract.Data.CONTENT_URI);
        uris.add(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        uris.add(ContactsContract.CommonDataKinds.Email.CONTENT_URI);
        uris.add(ContactsContract.Settings.CONTENT_URI);
        uris.add(ContactsContract.DisplayPhoto.CONTENT_URI);
    }

    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.recyclerview_single_row) RecyclerView recyclerViewSingleRow;
    @Bind(R.id.view_switcher) ViewSwitcher viewSwitcher;

    Uri uri;
    int uriArrayOffset;
    LoaderManager loaderManager;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        uri = uris.get(id);
        return new CursorLoader(this, uri, new String[] {}, null, new String[]{}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        recyclerView.setAdapter(new ContactsAdapter(uri, cursor, this));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerView.setAdapter(new ContactsAdapter(uri, null, this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        uriArrayOffset = 0;

        if (!BuildConfig.BUILD_TYPE.equals("debug") || !getIntent().getBooleanExtra(EXTRA_SKIP_INIT_LOADER, false)) {
            loaderManager = getSupportLoaderManager();
            callInitLoader();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArrayOffset++;
                if (uriArrayOffset >= uris.size()) {
                    uriArrayOffset = 0;
                }
                callInitLoader();

            }
        });
    }

    @VisibleForTesting
    void injectLoaderManager(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }

    @VisibleForTesting
    void callInitLoader() {
        loaderManager.restartLoader(uriArrayOffset, null, this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            recyclerViewSingleRow.setAdapter(null);
            viewSwitcher.setDisplayedChild(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDataRow(DBRowAdapter adapter) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewSwitcher.setDisplayedChild(1);
        recyclerViewSingleRow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSingleRow.setAdapter(adapter);
    }
}
