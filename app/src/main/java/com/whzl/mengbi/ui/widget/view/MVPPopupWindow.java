package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * @author cliang
 * @date 2018.11.12
 */
public class MVPPopupWindow extends PopupWindow implements View.OnClickListener {

    private View window;
    private ImageView ivClose;
    private RecyclerView rvPunishment;
    private Context mContext;
    private ArrayList<String> list;

    public MVPPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = inflater.inflate(R.layout.pop_mvp, null);
        ivClose = window.findViewById(R.id.iv_close_mvp);
        rvPunishment = window.findViewById(R.id.rv_punishment);
        this.setContentView(window);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(false);
        initRecy();
    }

    private void initRecy() {
        list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("第" + i + "种方式");
        }
        rvPunishment.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvPunishment.setLayoutManager(new GridLayoutManager(mContext, 3));
        BaseListAdapter mvpAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_punishment, parent, false);
                return new MVPViewHolder(itemView);
            }
        };
        rvPunishment.setAdapter(mvpAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_mvp:
                dismiss();
                break;
        }
    }

    class MVPViewHolder extends BaseViewHolder {
        TextView tvPunishment;

        public MVPViewHolder(View itemView) {
            super(itemView);
            tvPunishment = itemView.findViewById(R.id.tv_punishment);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvPunishment.setText(list.get(position));
        }
    }
}
