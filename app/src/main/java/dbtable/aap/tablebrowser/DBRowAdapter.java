package dbtable.aap.tablebrowser;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DBRowAdapter extends RecyclerView.Adapter<DBRowAdapter.DBRowHolder> {

    private static final String FLOAT = "float";
    private static final String STRING = "string";
    private static final String INT = "int";
    private static final String BLOB = "blob";
    private static final String NULL = "null";

    Cursor cursor;
    public DBRowAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public DBRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DBRowHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.column_details, parent, false));
    }

    @Override
    public void onBindViewHolder(DBRowHolder holder, int position) {
        int type = cursor.getType(position);
        switch(type) {
            case Cursor.FIELD_TYPE_FLOAT:
                populateFloat(holder, position);
                break;
            case Cursor.FIELD_TYPE_STRING:
                populateString(holder, position);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                populateInt(holder, position);
                break;
            case Cursor.FIELD_TYPE_BLOB:
                populateBlob(holder, position);
                break;
            case Cursor.FIELD_TYPE_NULL:
                populateNull(holder, position);
                break;
        }
    }

    private void populateHeader(DBRowHolder holder, String columnName, String type) {
        holder.title.setText(String.format("%s (%s)", columnName, type));
    }

    private void populateFloat(DBRowHolder holder, int position) {
        populateHeader(holder, cursor.getColumnName(position), FLOAT);
        holder.data.setText(String.valueOf(cursor.getDouble(position)));
    }

    private void populateString(DBRowHolder holder, int position) {
        populateHeader(holder, cursor.getColumnName(position), STRING);
        holder.data.setText(cursor.getString(position));
    }

    private void populateInt(DBRowHolder holder, int position) {
        populateHeader(holder, cursor.getColumnName(position), INT);
        holder.data.setText(String.valueOf(cursor.getLong(position)));
    }

    private void populateBlob(DBRowHolder holder, int position) {
        byte[] bytes = cursor.getBlob(position);
        populateHeader(holder, cursor.getColumnName(position), String.format("%s (%d bytes)", BLOB, bytes.length));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            sb.append(String.format("%d (%x)", b, b));
            if (i != bytes.length - 1) {
                sb.append(",");
            }
        }
        holder.data.setText(sb.toString());
    }

    private void populateNull(DBRowHolder holder, int position) {
        populateHeader(holder, cursor.getColumnName(position), NULL);
        holder.data.setText("");
    }

    @Override
    public int getItemCount() {
        return cursor == null || cursor.isClosed() || cursor.getCount() == 0  ? 0 : cursor.getColumnCount();
    }

    static class DBRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.column_data) TextView data;
        @Bind(R.id.column_title) TextView title;

        public DBRowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
