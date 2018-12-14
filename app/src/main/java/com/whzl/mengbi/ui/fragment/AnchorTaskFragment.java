package com.whzl.mengbi.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.AnchorWeekTaskEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnchorWeekTaskJson;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

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
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            case "DIV":
                tv.setText(bean.completion / bean.number >= bean.needCompletion / bean.number ?
                        bean.needCompletion / bean.number + "" : bean.completion / bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion / bean.number + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            case "ADD":
                tv.setText(bean.completion + bean.number >= bean.needCompletion + bean.number ?
                        bean.needCompletion + bean.number + "" : bean.completion + bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion + bean.number + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            case "SUB":
                tv.setText(bean.completion - bean.number >= bean.needCompletion - bean.number ?
                        bean.needCompletion - bean.number + "" : bean.completion - bean.number + "");
                tv.append(" / ");
                tv.append(bean.needCompletion - bean.number + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
            default:
                tv.setText(bean.completion >= bean.needCompletion ?
                        bean.needCompletion + "" : bean.completion + "");
                tv.append(" / ");
                tv.append(bean.needCompletion + "");
                if (!TextUtils.isEmpty(bean.unit)) {
                    tv.append(bean.unit);
                }
                break;
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
