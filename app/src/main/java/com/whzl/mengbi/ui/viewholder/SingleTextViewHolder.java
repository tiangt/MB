package com.whzl.mengbi.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.R;

public class SingleTextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public SingleTextViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.chat_text);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }
}
