package dbtable.aap.tablebrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsRowHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.row_text) TextView row;
    public ContactsAdapter.RowClickListener rowClickListener;

    public ContactsRowHolder(View itemView, ContactsAdapter.RowClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(listener);
        rowClickListener = listener;
    }


}
