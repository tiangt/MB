package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.LoginSuccussEvent;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.gen.UsualGiftDao;
import com.whzl.mengbi.greendao.User;
import com.whzl.mengbi.greendao.UsualGift;
import com.whzl.mengbi.model.entity.CheckLoginResultBean;
import com.whzl.mengbi.ui.activity.LoginActivity;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2019/1/11
 */
public class AccountSwitchActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private BaseListAdapter baseListAdapter;
    private List<User> accountList = new ArrayList();
    private boolean edit;
    private Long currentUserId;
    private int REQUEST_LOGIN = 120;
    private UserDao userDao;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_account_switch, "切换账号", "编辑", true);
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));
    }

    @Override
    protected void setupView() {
        userDao = BaseApplication.getInstance().getDaoSession().getUserDao();
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        initRecycler();
        currentUserId = (Long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        baseListAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return accountList == null ? 0 : accountList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View inflate = LayoutInflater.from(AccountSwitchActivity.this).inflate(R.layout.item_account_switch, null);
                return new AccountSwitchHolder(inflate);
            }

            @Override
            protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_account_switch, parent, false);
                return new LoadMoreFooterViewHolder(view);
            }
        };
        baseListAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
        recycler.setAdapter(baseListAdapter);
    }

    private class LoadMoreFooterViewHolder extends BaseViewHolder {

        public LoadMoreFooterViewHolder(View view) {
            super(view);
        }

        @Override
        public void onBindViewHolder(int position) {
            // 强制关闭复用，刷新动画
            this.setIsRecyclable(false);
            // 设置自定义加载中和到底了效果
        }

        @Override
        public void onItemClick(View view, int position) {
            // 设置点击效果，比如加载失败，点击重试
            jumpToLogin();
        }
    }

    private void jumpToLogin() {
        startActivityForResult(new Intent(AccountSwitchActivity.this, LoginActivity.class).putExtra("from", "account"), REQUEST_LOGIN);
    }


    class AccountSwitchHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.iv_state)
        ImageView ivState;

        public AccountSwitchHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            User user = accountList.get(position);
            GlideImageLoader.getInstace().displayCircleAvatar(AccountSwitchActivity.this, user.getAvatar(), ivAvatar);
            tvName.setText(user.getNickname());
            tvNum.setText("萌号：" + user.getUserId());
            GlideImageLoader.getInstace().displayImage(
                    AccountSwitchActivity.this, edit ? R.drawable.ic_delete_switch : null, ivState);
            if (user.getUserId().equals(currentUserId)) {
                GlideImageLoader.getInstace().displayImage(
                        AccountSwitchActivity.this, edit ? R.drawable.ic_delete_switch : R.drawable.ic_right_switch, ivState);
            }
            ivState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edit) {
                        AwesomeDialog.init().setLayoutId(R.layout.dialog_delete_account)
                                .setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });

                                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (currentUserId.equals(user.getUserId())) {
                                                    ToastUtils.showToastUnify(AccountSwitchActivity.this, "您不能删除正在使用账号");
                                                    dialog.dismiss();
                                                    return;
                                                }
                                                dialog.dismiss();
                                                removeGreenDao(user);
                                            }
                                        });
                                    }
                                })
                                .setOutCancel(false)
                                .show(getSupportFragmentManager());
                    }
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (edit) {
                return;
            }
            if (accountList.get(position).getUserId().equals(currentUserId)) {
                return;
            }
            checkLogin(accountList.get(position));
        }
    }

    private void removeGreenDao(User commonGift) {
        userDao.deleteByKey(commonGift.getUserId());
        UsualGiftDao usualGiftDao = BaseApplication.getInstance().getDaoSession().getUsualGiftDao();
        List<UsualGift> list = usualGiftDao.queryBuilder().where(UsualGiftDao.Properties.UserId.eq(commonGift.getUserId())).list();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                usualGiftDao.deleteByKey(list.get(i).getId());
            }
        }
        loadData();
    }

    private void checkLogin(User user) {
        showLoading("切换中");
        if (user.getSeesionId() != null) {
            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, user.getSeesionId());
        }
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", user.getUserId());
        if (PushServiceFactory.getCloudPushService().getDeviceId()!=null) {
            paramsMap.put("deviceNumber",PushServiceFactory.getCloudPushService().getDeviceId() );
        }
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CHECK_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object object) {
                        dismissLoading();
                        CheckLoginResultBean resultBean = GsonUtils.GsonToBean(object.toString(), CheckLoginResultBean.class);
                        if (resultBean.code == RequestManager.RESPONSE_CODE) {
                            if (resultBean.data.isLogin) {
                                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, user.getUserId());
                                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, user.getSeesionId());
                                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, user.getNickname());
                                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, user.getRecharged());
                                currentUserId = user.getUserId();
                                EventBus.getDefault().post(new LoginSuccussEvent());
                                baseListAdapter.notifyDataSetChanged();
                            } else {
                                jumpToLogin();
                            }
                        } else {
                            jumpToLogin();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        dismissLoading();
                        jumpToLogin();
                    }
                });
    }

    @Override
    protected void loadData() {
        accountList.clear();
        List<User> list = userDao.queryBuilder().list();
        if (list != null && !list.isEmpty()) {
            accountList.addAll(list);
            baseListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                loadData();
                currentUserId = (Long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
            }
        }
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        getTitleRightText().setText(edit ? "编辑" : "完成");
        baseListAdapter.notifyDataSetChanged();
        edit = !edit;
    }

}
