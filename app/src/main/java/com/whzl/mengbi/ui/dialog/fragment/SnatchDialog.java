package com.whzl.mengbi.ui.dialog.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2019/3/6
 */
public class SnatchDialog extends BaseAwesomeDialog {

    private TextView tvHisPrize;
    private ImageButton ibReduce;
    private ImageButton ibAdd;
    private TextView tvWant;
    private List list = new ArrayList();

    @Override
    public int intLayoutId() {
        return R.layout.dialog_snatch;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        tvHisPrize = holder.getView(R.id.tv_his_prize);
        tvHisPrize.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvWant = holder.getView(R.id.tv_want_join);
        ibReduce = holder.getView(R.id.ib_reduce_want);
        ibAdd = holder.getView(R.id.ib_add_want);
        ibReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(tvWant.getText().toString()) <= 1) {
                    return;
                }
                tvWant.setText(String.valueOf(Integer.parseInt(tvWant.getText().toString()) - 1));
            }
        });
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(tvWant.getText().toString()) >= 10) {
                    return;
                }
                tvWant.setText(String.valueOf(Integer.parseInt(tvWant.getText().toString()) + 1));
            }
        });
        holder.setOnClickListener(R.id.tv_five, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWant.setText("5");
            }
        });
        holder.setOnClickListener(R.id.tv_ten, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWant.setText("10");
            }
        });
        holder.setOnClickListener(R.id.iv_mengdou_snatch, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopActivity.class));
            }
        });
    }
}
