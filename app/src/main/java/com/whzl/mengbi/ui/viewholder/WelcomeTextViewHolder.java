package com.whzl.mengbi.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.R;

public class WelcomeTextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public WelcomeTextViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.chat_text);
    }
}
