package dbtable.aap.tablebrowser;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ContactsAdapterTest {
    ContactsAdapter.RowClickHandler rowClickHandler = new ContactsAdapter.RowClickHandler() {
        @Override
        public void showDataRow(DBRowAdapter adapter) {
            //NOOP
        }
    };

    public ContactsAdapterTest() {
    }

    @Test
    public void testNumberOfRows() {
        final int size = 10;
        ContactsAdapter adapter = new ContactsAdapter(ContactsContract.RawContacts.CONTENT_URI, new MyMockCursor(size, 3), rowClickHandler);
        assertEquals(size + 1, adapter.getItemCount());
    }

    @Test
    public void testGetHeader() {
        int size = 20;
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        ContactsAdapter adapter = new ContactsAdapter(uri, new MyMockCursor(size, 3), rowClickHandler);
        ViewGroup parent = new LinearLayout(InstrumentationRegistry.getTargetContext());
        ContactsRowHolder contactsRowHolder = adapter.onCreateViewHolder(parent, ContactsAdapter.ITEM_TYPE_HEADER);

        assertNotNull(contactsRowHolder.row);
        adapter.onBindViewHolder(contactsRowHolder, 0);
        assertTrue("Content URI not found in the header",
                   contactsRowHolder.row.getText().toString().indexOf(uri.toString()) >= 0);
        assertTrue("Size not mentioned in the header",
                   contactsRowHolder.row.getText().toString().indexOf(String.valueOf(size)) >= 0);
    }

    @Test
    public void testRow() {
        int size = 20;
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        ContactsAdapter adapter = new ContactsAdapter(uri, new MyMockCursor(size, 3), rowClickHandler);
        ViewGroup parent = new LinearLayout(InstrumentationRegistry.getTargetContext());
        ContactsRowHolder contactsRowHolder = adapter.onCreateViewHolder(parent, ContactsAdapter.ITEM_TYPE_ROW);
        assertNotNull(contactsRowHolder.row);
        adapter.onBindViewHolder(contactsRowHolder, 1);
        String content = contactsRowHolder.row.getText().toString();
        assertTrue("String data missing",
                   content.indexOf(MyMockCursor.DUMMY_STRING) >= 0);
        assertTrue("String column name missing",
                   content.indexOf(MyMockCursor.STRING_COLUMN) >= 0);
        assertTrue("Int data missing",
                   content.indexOf(String.valueOf(MyMockCursor.DUMMY_INT)) >= 0);
        assertTrue("Int column name missing",
                   content.indexOf(MyMockCursor.INT_COLUMN) >= 0);
        assertTrue("Float data missing",
                   content.indexOf(String.valueOf((float)Math.PI)) >= 0);
        assertTrue("Float column name missing",
                   content.indexOf(MyMockCursor.FLOAT_COLUMN) >= 0);
    }
}
