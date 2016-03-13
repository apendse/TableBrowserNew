package dbtable.aap.tablebrowser;

import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * The adapter that lists rows of a given table. Each row corresponds to a row of DB table.
 * The row gives a text description of the row, things like type of each column, name of each column and the value
 * is displayed.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsRowHolder> {
    static final int ITEM_TYPE_HEADER = 10;
    static final int ITEM_TYPE_ROW = 11;
    static final int ITEM_EMPTY_LIST = 12;

    Cursor cursor;
    boolean cursorValid;
    Uri uri;
    RowClickHandler rowClickHandler;

    public ContactsAdapter(Uri uri, Cursor cursor, RowClickHandler rowClickHandler) {
        cursorValid = false;
        this.cursor = cursor;
        this.uri = uri;
        if (cursor == null || cursor.isClosed()) {
            cursorValid = false;
            return;
        }
        cursorValid = cursor.moveToFirst();
        this.rowClickHandler = rowClickHandler;
    }

    @Override
    public ContactsRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowClickListener rowClickListener = new RowClickListener(rowClickHandler);
        rowClickListener.cursor = cursor;
        return new ContactsRowHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false), rowClickListener);
    }

    @Override
    public void onBindViewHolder(ContactsRowHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == ITEM_TYPE_HEADER) {
            renderHeader(holder, cursor);
            return;
        } else if (itemType == ITEM_EMPTY_LIST) {
            renderEmptyList(holder);
            return;
        } else {
            if (cursor.moveToPosition(position - 1)) {
                outputCursorData(holder, cursor);

            }
        }
    }

    private void renderEmptyList(ContactsRowHolder holder) {
        holder.row.setText(R.string.empty_table);
        holder.row.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
    }

    private void outputCursorData(ContactsRowHolder holder, Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnCount; i++) {
            outputColumn(sb, cursor, i, i == columnCount);
        }
        holder.row.setText(sb.toString());
    }

    private void outputColumn(StringBuilder sb, Cursor cursor, int column, boolean isLast) {
        int type = cursor.getType(column);
        sb.append(cursor.getColumnName(column));
        switch(type) {
            case Cursor.FIELD_TYPE_FLOAT:
                sb.append("(float): ");
                sb.append(cursor.getDouble(column));
                break;
            case Cursor.FIELD_TYPE_BLOB:
                byte[] bytes = cursor.getBlob(column);
                sb.append("(blob ").append(bytes.length).append(")");
                int length = Math.min(bytes.length, 16);
                for (int i = 0; i < length; i++) {
                    byte b = bytes[i];
                    sb.append(b);
                    if (i != length-1) {
                        sb.append(", ");
                    }
                }
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                sb.append("(integer): ");
                long val = cursor.getLong(column);
                sb.append(val).append(" (").append(Long.toHexString(val)).append(')');
                break;
            case Cursor.FIELD_TYPE_STRING:
                sb.append(("(string): "));
                String str = cursor.getString(column);
                if (TextUtils.isEmpty(str)) {
                    str = "NULL";
                }
                sb.append((str));
                break;
            case Cursor.FIELD_TYPE_NULL:
                sb.append(": null");
                break;
            default:
                sb.append("Unknown ").append(type).append(cursor.getString(column));
                break;
        }
        if (!isLast) {
            sb.append(",");
        }
        sb.append("\n");
    }


    private void renderHeader(ContactsRowHolder holder, Cursor cursor) {
        holder.row.setTypeface(Typeface.DEFAULT_BOLD);
        holder.row.setText(String.format("Uri : %s Table has %d rows %d columns", uri.toString(), cursor.getCount(), cursor.getColumnCount()));
    }

    @Override
    public int getItemCount() {
        return cursorValid ? 1 + cursor.getCount() : 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (!cursorValid) {
            return ITEM_EMPTY_LIST;
        }
        return ITEM_TYPE_ROW;
    }
    public interface RowClickHandler {
        void showDataRow(DBRowAdapter adapter);
    }

    static class RowClickListener implements View.OnClickListener {

        @Nullable
        Cursor cursor;

        final RowClickHandler rowListener;

        RowClickListener(@NonNull RowClickHandler rowListener) {
            this.rowListener = rowListener;
        }

        @Override
        public void onClick(View v) {
            if (cursor != null) {
                rowListener.showDataRow(new DBRowAdapter(cursor));
            }
        }
    }
}
