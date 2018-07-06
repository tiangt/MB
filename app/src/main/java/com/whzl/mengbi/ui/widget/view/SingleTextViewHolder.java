package com.whzl.mengbi.ui.widget.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.R;

public class SingleTextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public SingleTextViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.chat_content);
    }
}
