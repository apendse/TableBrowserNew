package dbtable.aap.tablebrowser;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.LinearLayout;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DBRowAdapterTest {

    @Test
    public void testNumberOfRows() {
        MyMockCursor myMockCursor = new MyMockCursor(10, 3);
        DBRowAdapter dbRowAdapter = new DBRowAdapter(myMockCursor);
        assertEquals(myMockCursor.getColumnCount(), dbRowAdapter.getItemCount());
    }

    @Test
    public void testRows() {
        MyMockCursor myMockCursor = new MyMockCursor(10, 3);
        DBRowAdapter dbRowAdapter = new DBRowAdapter(myMockCursor);
        LinearLayout linearLayout = new LinearLayout(InstrumentationRegistry.getTargetContext());
        DBRowAdapter.DBRowHolder dbRowHolder = dbRowAdapter.onCreateViewHolder(linearLayout, 0);
        for (int i = 0; i < myMockCursor.getColumnCount(); i++) {
            dbRowAdapter.onBindViewHolder(dbRowHolder, i);
            String title = dbRowHolder.title.getText().toString();
            assertTrue(title.indexOf(myMockCursor.getColumnName(i)) >= 0);
            assertTrue(title.indexOf(myMockCursor.getColumnName(i)) >= 0);
            String data = dbRowHolder.data.getText().toString();
            testData(myMockCursor, data, i);
        }
    }

    private void testData(MyMockCursor myMockCursor, String string, int offset) {
        switch(myMockCursor.getType(offset)) {
            case Cursor.FIELD_TYPE_STRING:
                assertTrue(string.indexOf(myMockCursor.getString(offset)) >= 0);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                assertTrue(string.indexOf(String.valueOf(myMockCursor.getLong(offset))) >= 0);
                break;
            default:
                assertTrue(string.indexOf(String.valueOf(myMockCursor.getDouble(offset))) >= 0);
                break;
        }
    }

}
