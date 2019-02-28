package com.whzl.mengbi.ui.fragment.me;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.eventbus.event.JumpMainActivityEvent;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.JumpRandomRoomBean;
import com.whzl.mengbi.model.entity.NewTaskBean;
import com.whzl.mengbi.presenter.WelfarePresenter;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.activity.NickNameModifyActivity;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class WelfareFragment extends BasePullListFragment<NewTaskBean.ListBean, WelfarePresenter> implements WelfareContract.View {
    private Map<Integer, Integer> map = new HashMap<>();
    private String[] strings;
    private String[] names;

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected boolean setShouldRefresh() {
        return false;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        ((FrgActivity) getMyActivity()).setTitle("新手任务");
        ((FrgActivity) getMyActivity()).setTitleColor(ContextCompat.getColor(getMyActivity(), R.color.comm_white));
        ((FrgActivity) getMyActivity()).setTitleBlack();
        mPresenter = new WelfarePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void init() {
        super.init();
        getPullView().setRefBackgroud(ContextCompat.getColor(getMyActivity(), R.color.comm_white));
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_walfare, getPullView(), false);
        getAdapter().addHeaderView(view);
        initIcon();
        strings = getResources().getStringArray(R.array.new_task);
        names = getResources().getStringArray(R.array.new_task_names);
    }

    private void initIcon() {
        map.put(0, R.drawable.ic_walfare_0);
        map.put(1, R.drawable.ic_walfare_1);
        map.put(2, R.drawable.ic_walfare_2);
        map.put(3, R.drawable.ic_walfare_3);
        map.put(4, R.drawable.ic_walfare_4);
        map.put(5, R.drawable.ic_walfare_5);
        map.put(6, R.drawable.ic_walfare_6);
        map.put(7, R.drawable.ic_walfare_7);
        map.put(8, R.drawable.ic_walfare_8);
        map.put(9, R.drawable.ic_walfare_9);

    }

    @Override
    protected void loadData(int action, int mPage) {
        mPresenter.newTask(SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L) + "");
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_walfare, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_state)
        TextView tvState;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            NewTaskBean.ListBean bean = mDatas.get(position);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), map.get(position), ivIcon);
            tvName.setText(names[position]);
            if (position < 6) {
                tvNum.setText("+8 萌币");
            } else {
                tvNum.setText("+500 萌币");
            }
            tvState.setText(strings[position]);
            tvProgress.setText("");
            tvProgress.append(String.valueOf(bean.completion));
            tvProgress.append("/");
            tvProgress.append(String.valueOf(bean.needCompletion));
            switch (bean.status) {
                case "INACTIVE":
                    tvState.setEnabled(true);
                    tvState.setBackgroundResource(R.drawable.btn_normal_walfare);
                    tvState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initEvent(position);
                        }
                    });
                    break;
                //可领取
                case "UNGRANT":
                    tvState.setEnabled(true);
                    tvState.setText("领取");
                    tvState.setBackgroundResource(R.drawable.btn_can_receive_walfare);
                    tvState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.receive(tvState, SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L) + "", bean.awardSn);
                        }
                    });
                    break;
                //已领取
                case "GRANT":
                    tvState.setText("已领取");
                    tvState.setBackgroundResource(R.drawable.btn_have_receive_walfare);
                    tvState.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    }

    private void initEvent(int position) {
        switch (position) {
            case 1:
                startActivity(new Intent(getMyActivity(), NickNameModifyActivity.class));
                break;
            case 2:
                startActivity(new Intent(getMyActivity(), MainActivity.class));
                EventBus.getDefault().postSticky(new JumpMainActivityEvent(0));
                break;
            case 5:
                startActivity(new Intent(getMyActivity(), MainActivity.class));
                EventBus.getDefault().postSticky(new JumpMainActivityEvent(0));
                break;
            case 6:
                startActivity(new Intent(getMyActivity(), WXPayEntryActivity.class));
                break;
            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
                jumpRandomLiveRoom();
                break;
            default:
                break;
        }
    }

    private void jumpRandomLiveRoom() {
        mPresenter.jump();
    }

    @Override
    public void onNewTask(NewTaskBean bean) {
        loadSuccess(bean.list);
    }

    @Override
    public void onReceiveSuccess(TextView tv, JsonElement jsonElement) {
        tv.setText("已领取");
        tv.setBackgroundResource(R.drawable.btn_have_receive_walfare);
        tv.setEnabled(false);
        EventBus.getDefault().post(new UserInfoUpdateEvent());
    }

    @Override
    public void onJumpRandom(JumpRandomRoomBean bean) {
        Intent intent = new Intent(getMyActivity(), LiveDisplayActivity.class);
        intent.putExtra(BundleConfig.PROGRAM_ID, bean.programId);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.newTask(SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L) + "");
    }

}
