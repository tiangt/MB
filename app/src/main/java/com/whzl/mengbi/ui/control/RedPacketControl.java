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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.WeakHandler;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author nobody
 * @date 2019/1/17
 */
public class RedPacketControl {
    private Context context;
    private RecyclerView rvRedPack;
    public List<RoomRedpackList.ListBean> redPackList = new ArrayList();
    private WeakHandler handler = new WeakHandler();
    public BaseListAdapter redpackAdapter;
    private Long userId;
    private String sessionId;

    public RedPacketControl(Context context, RecyclerView rvRedPack) {
        this.context = context;
        this.rvRedPack = rvRedPack;
    }

    public void init() {
        userId = (Long) SPUtils.get(context, SpConfig.KEY_USER_ID, 0L);
        sessionId = (String) SPUtils.get(context, SpConfig.KEY_SESSION_ID, "");
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
        @BindView(R.id.tv_second)
        TextView tvSecond;

        public RedPackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvSecond.setText("s");
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_red_pack_off_live, ivState);
            RoomRedpackList.ListBean listBean = redPackList.get(position);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (listBean.leftSeconds == 0) {
                        GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_red_pack_on_live, ivState);
                        tvTime.setText("");
                        tvSecond.setText("");
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
                    openRed(listBean);
                    redPackList.remove(position);
                    handler.removeCallbacksAndMessages(null);
                    redpackAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    public void openRed(RoomRedpackList.ListBean listBean) {
        HashMap params = new HashMap();
        params.put("packed", listBean.redPacketId);
        params.put("userId", userId+"");
        params.put("token", sessionId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn("red_packed", RequestManager.TYPE_POST_URL, params,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        LogUtils.e("sssssssssss   "+result.toString());
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }

    public void destroy() {
        handler.removeCallbacksAndMessages(null);
    }
}
