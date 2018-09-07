package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.gift.LuckGiftControl;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.recyclerview.DividerGridItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Time;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * @author shaw
 * @date 2018/8/27
 */
public class TreasureBoxDialog extends BaseAwesomeDialog {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    private int[] boxes;

    public void setmTime(long mTime) {
        this.mTime = mTime;
        mAdapter.notifyDataSetChanged();
    }

    private long mTime;
    private BaseListAdapter mAdapter;

    public void setTreasureStatusMap(HashMap<Integer, Integer> treasureStatusMap) {
        this.treasureStatusMap = treasureStatusMap;
        mAdapter.notifyDataSetChanged();
    }

    private HashMap<Integer, Integer> treasureStatusMap;

    public static TreasureBoxDialog newInstance(HashMap<Integer, Integer> treasureStatusMap, Long time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("statusMap", treasureStatusMap);
        bundle.putLong("time", time == null ? 0 : time);
        TreasureBoxDialog treasureBoxDialog = new TreasureBoxDialog();
        treasureBoxDialog.setArguments(bundle);
        return treasureBoxDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_treasure_box;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        treasureStatusMap = (HashMap<Integer, Integer>) getArguments().getSerializable("statusMap");
        mTime = getArguments().getLong("time");
        boxes = new int[]{R.drawable.treasure_box_wood_icon, R.drawable.treasure_box_gold_1_icon, R.drawable.treasure_box_gold_2};
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recycler.addItemDecoration(new DividerGridItemDecoration(getContext(), 1, Color.parseColor("#ded4d4")));
        recycler.setOverScrollMode(OVER_SCROLL_NEVER);
        mAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return 6;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_treasure_box, parent, false);
                return new TreasureViewHolder(itemView);
            }
        };
        recycler.setAdapter(mAdapter);
    }

    class TreasureViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_box)
        ImageView ivBox;
        @BindView(R.id.tv_timeCount)
        TextView tvTimeCount;

        public TreasureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            itemView.setOnClickListener(null);
            if (position < 3) {
                tvTimeCount.setTextColor(Color.WHITE);
                ivBox.setImageResource(boxes[position]);
                tvTimeCount.setVisibility(View.VISIBLE);
                if (treasureStatusMap.get(6 + position) == 0) {
                    if (treasureStatusMap.get(6 + position - 1) == 3) {
                        tvTimeCount.setBackgroundResource(R.drawable.shape_treasure_status_timing);
                        if (mTime > 0) {
                            tvTimeCount.setText(getString(R.string.two, (599 - mTime) / 60, (599 - mTime) % 60));
                        }
                    } else {
                        tvTimeCount.setBackgroundResource(R.drawable.shape_treasure_status_waiting);
                        tvTimeCount.setTextColor(Color.parseColor("#db7d03"));
                        tvTimeCount.setText("等待");
                    }

                } else if (treasureStatusMap.get(6 + position) == 1) {
                    itemView.setEnabled(true);
                    tvTimeCount.setText("领取");
                    tvTimeCount.setBackgroundResource(R.drawable.shape_treasure_status_receive);
                    itemView.setOnClickListener(v -> {
                        if (onReceiveClick != null) {
                            itemView.setEnabled(false);
                            onReceiveClick.onReceive(6 + position);
                        }
                    });
                } else if (treasureStatusMap.get(6 + position) == 3) {
                    tvTimeCount.setText("已领取");
                    tvTimeCount.setBackgroundResource(R.drawable.shape_treasure_status_received);
                }
            } else {
                ivBox.setImageResource(R.drawable.treasure_box_not_open_icon);
                tvTimeCount.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public interface onReceiveClick {
        void onReceive(int id);

    }

    public TreasureBoxDialog setOnReceiveClick(TreasureBoxDialog.onReceiveClick onReceiveClick) {
        this.onReceiveClick = onReceiveClick;
        return this;
    }

    private onReceiveClick onReceiveClick;
}