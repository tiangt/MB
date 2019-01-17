package com.whzl.mengbi.ui.control;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2019/1/17
 */
public class RedPacketControl {
    private Context context;
    private RecyclerView rvRedPack;
    public List<RoomRedpackList.ListBean> redPackList = new ArrayList();
    private Handler handler = new Handler();
    public BaseListAdapter redpackAdapter;

    public RedPacketControl(Context context, RecyclerView rvRedPack) {
        this.context = context;
        this.rvRedPack = rvRedPack;
    }

    public void init() {
        rvRedPack.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        redpackAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return redPackList == null ? 0 : redPackList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_redpack_live, null);
                return new RedPackViewHolder(itemView);
            }
        };
        rvRedPack.setAdapter(redpackAdapter);
    }

    class RedPackViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_state)
        ImageView ivState;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl_red_bag)
        RelativeLayout rlRedBag;

        public RedPackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_red_pack_off_live, ivState);
            RoomRedpackList.ListBean listBean = redPackList.get(position);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (listBean.leftSeconds == 0) {
                        GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_red_pack_on_live, ivState);
                        tvTime.setText("");
                        handler.removeCallbacks(this);
                        Runnable runnableWait = new Runnable() {
                            @Override
                            public void run() {
                                if (listBean.expDate <= listBean.effDate) {
                                    redPackList.remove(position);
                                    handler.removeCallbacksAndMessages(null);
                                    redpackAdapter.notifyDataSetChanged();
                                    return;
                                }
                                listBean.expDate = listBean.expDate - 1000;
                                handler.postDelayed(this, 1000);
                            }
                        };
                        handler.post(runnableWait);
                        return;
                    }
                    listBean.leftSeconds = listBean.leftSeconds - 1;
                    tvTime.setText((listBean.leftSeconds) + "");
                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(runnable);
            ivState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(tvTime.getText())) {
                        return;
                    }
                    redPackList.remove(position);
                    handler.removeCallbacksAndMessages(null);
                    redpackAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    public void destroy() {
        handler.removeCallbacksAndMessages(null);
    }
}
