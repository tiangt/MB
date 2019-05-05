package com.whzl.mengbi.ui.control;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.OpenRedBean;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.WeakHandler;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
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
    private WeakHandler handler = new WeakHandler();
    public BaseListAdapter redpackAdapter;
    private Long userId;
    private String sessionId;
    private BaseAwesomeDialog offDialog;
    private BaseAwesomeDialog okDialog;

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
            setIsRecyclable(false);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvSecond.setText("s");
            GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_red_pack_off_live, ivState);
            RoomRedpackList.ListBean listBean = redPackList.get(position);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (listBean.leftSeconds == -1) {
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
                    tvTime.setText((listBean.leftSeconds) == -1 ? "0" : (listBean.leftSeconds) + "");
                    listBean.leftSeconds = listBean.leftSeconds - 1;
                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(runnable);
            ivState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((LiveDisplayActivity) context).mUserId == 0) {
                        ((LiveDisplayActivity) context).login();
                        return;
                    }
                    if (!TextUtils.isEmpty(tvTime.getText())) {
                        return;
                    }
                    redPackList.remove(position);
                    handler.removeCallbacksAndMessages(null);
                    redpackAdapter.notifyDataSetChanged();
                    openRed(listBean);
                }
            });
        }

    }

    public void openRed(RoomRedpackList.ListBean listBean) {
        HashMap params = new HashMap();
        params.put("packed", listBean.redPacketId);
        params.put("userId", SPUtils.get(context, SpConfig.KEY_USER_ID, 0L).toString());
        params.put("token", sessionId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(SPUtils.get(context, SpConfig.REDPACKETURL, "").toString(), RequestManager.TYPE_POST_URL, params,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        OpenRedBean openRedBean = GsonUtils.GsonToBean(result.toString(), OpenRedBean.class);
                        if (openRedBean.code == 200) {
                            if (okDialog != null && okDialog.isAdded()) {
                                return;
                            }
                            okDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_redpack_ok)
                                    .setConvertListener(new ViewConvertListener() {
                                        @Override
                                        protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                                            holder.setText(R.id.tv_amount, AmountConversionUitls.amountConversionFormat(openRedBean.data.amount));
                                        }
                                    })
                                    .setOutCancel(true)
                                    .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                            EventBus.getDefault().post(new UserInfoUpdateEvent());
                        } else if (openRedBean.code == 1008) {
                            if (offDialog != null && offDialog.isAdded()) {
                                return;
                            }
                            offDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_redpack_off)
                                    .setConvertListener(new ViewConvertListener() {
                                        @Override
                                        protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

                                        }
                                    })
                                    .setOutCancel(true)
                                    .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        AwesomeDialog.init().setLayoutId(R.layout.dialog_redpack_off)
                                .setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

                                    }
                                })
                                .setOutCancel(true)
                                .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                    }
                });
    }

    public void destroy() {
        handler.removeCallbacksAndMessages(null);
    }
}
