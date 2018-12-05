package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.BuySuccubusActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author nobody
 * @date 2018/11/29
 */
public class FreeGiftDialog extends BaseAwesomeDialog {

    @BindView(R.id.tv_buy_card)
    TextView tvBuyCard;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ArrayList list = new ArrayList();

    public static BaseAwesomeDialog newInstance() {
        FreeGiftDialog freeGiftDialog = new FreeGiftDialog();
        return freeGiftDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_free_gift;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        tvBuyCard.getPaint().setAntiAlias(true);
        tvBuyCard.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        tvBuyCard.getPaint().setFakeBoldText(true);
        tvBuyCard.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initRecycler();
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recyclerView.setAdapter(new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list == null ? 0:5;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_free_gift, parent, false);
                return new Holder(itemView);
            }
        });
    }

    class Holder extends BaseViewHolder{

        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }
    }

    @OnClick({R.id.tv_buy_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_buy_card:
                getActivity().startActivity(new Intent(getActivity(), BuySuccubusActivity.class));
                break;
        }
    }
}
