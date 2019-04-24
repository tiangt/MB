package com.whzl.mengbi.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.AnchorWeekTaskEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnchorWeekTaskJson;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/11/29
 */
public class AnchorTaskFragment extends BaseFragment {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv)
    TextView tv;
    private AnchorTaskBean bean;
    private PopupWindow popupWindow;

    public static AnchorTaskFragment newInstance(AnchorTaskBean dataBean) {
        AnchorTaskFragment anchorTaskFragment = new AnchorTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", dataBean);
        anchorTaskFragment.setArguments(bundle);
        return anchorTaskFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_anchor_task;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        EventBus.getDefault().register(this);
        bean = getArguments().getParcelable("bean");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void init() {
        Glide.with(this).load(bean.pic).into(iv);
        switch (bean.operation) {
            case "MUL":
                tv.setText(bean.completion * bean.number >= bean.needCompletion * bean.number ?
                        bean.needCompletion * bean.number + "" : bean.completion * bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion * bean.number + "");
//                if (!TextUtils.isEmpty(bean.unit)) {
//                    tv.append(bean.unit);
//                }
                break;
            case "DIV":
                tv.setText(bean.completion / bean.number >= bean.needCompletion / bean.number ?
                        bean.needCompletion / bean.number + "" : bean.completion / bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion / bean.number + "");
//                if (!TextUtils.isEmpty(bean.unit)) {
//                    tv.append(bean.unit);
//                }
                break;
            case "ADD":
                tv.setText(bean.completion + bean.number >= bean.needCompletion + bean.number ?
                        bean.needCompletion + bean.number + "" : bean.completion + bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion + bean.number + "");
//                if (!TextUtils.isEmpty(bean.unit)) {
//                    tv.append(bean.unit);
//                }
                break;
            case "SUB":
                tv.setText(bean.completion - bean.number >= bean.needCompletion - bean.number ?
                        bean.needCompletion - bean.number + "" : bean.completion - bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion - bean.number + "");
//                if (!TextUtils.isEmpty(bean.unit)) {
//                    tv.append(bean.unit);
//                }
                break;
            default:
                tv.setText(bean.completion >= bean.needCompletion ?
                        bean.needCompletion + "" : bean.completion + "");
                tv.append(" / ");
                tv.append(bean.needCompletion + "");
//                if (!TextUtils.isEmpty(bean.unit)) {
//                    tv.append(bean.unit);
//                }
                break;
        }

        initPop();

        iv.setOnClickListener(v -> {
            if (popupWindow != null && !popupWindow.isShowing()) {
                popupWindow.showAsDropDown(iv, -UIUtil.dip2px(getContext(), 250f), -UIUtil.dip2px(getContext(), 124f));
            }
        });
    }

    private void initPop() {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_anchor_task, null);
        ImageView ivCover = popView.findViewById(R.id.iv_cover_anchor_task);

        popupWindow = new PopupWindow(popView, UIUtil.dip2px(getContext(), 242f),
                UIUtil.dip2px(getContext(), 124f));

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        GlideImageLoader.getInstace().displayImage(getContext(), bean.detailPic, ivCover);
        popView.findViewById(R.id.iv_close_pop_anchor_wish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        TextView tvName = popView.findViewById(R.id.tv_name_pop_anchor_task);
        TextView tvTime = popView.findViewById(R.id.tv_time_pop_anchor_task);
        TextView tv1 = popView.findViewById(R.id.tv_1_pop_anchor_task);
        TextView tv2 = popView.findViewById(R.id.tv_2_pop_anchor_task);
        ImageView iv1 = popView.findViewById(R.id.iv_1_pop_anchor_task);
        ImageView iv2 = popView.findViewById(R.id.iv_2_pop_anchor_task);
        LinearLayout ll1 = popView.findViewById(R.id.ll_1_pop_anchor_task);
        LinearLayout ll2 = popView.findViewById(R.id.ll_2_pop_anchor_task);
        tvName.setText(bean.name);
        tvTime.setText(bean.time);
        if (bean.awardList == null || bean.awardList.isEmpty()) {
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
        } else if (bean.awardList.size() == 1) {
            ll2.setVisibility(View.GONE);
            tv1.setText("x" + bean.awardList.get(0).awardNum);
            GlideImageLoader.getInstace().displayImage(getContext(), bean.awardList.get(0).awardPic, iv1);
        } else if (bean.awardList.size() == 2) {
            tv1.setText("x" + bean.awardList.get(0).awardNum);
            GlideImageLoader.getInstace().displayImage(getContext(), bean.awardList.get(0).awardPic, iv1);

            tv2.setText("x" + bean.awardList.get(1).awardNum + "");
            GlideImageLoader.getInstace().displayImage(getContext(), bean.awardList.get(1).awardPic, iv2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AnchorWeekTaskEvent event) {
        AnchorWeekTaskJson anchorWeekTaskJson = event.anchorWeekTaskJson;
        if (anchorWeekTaskJson.context.actionValue >= anchorWeekTaskJson.context.actionNeedValue) {
            bean.completion = anchorWeekTaskJson.context.actionNeedValue;
        } else {
            bean.completion = anchorWeekTaskJson.context.actionValue;
        }
        init();
    }
}
