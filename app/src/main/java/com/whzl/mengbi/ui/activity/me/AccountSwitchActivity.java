package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
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
    private List<String> accountList = new ArrayList();
    private boolean edit;

    @Override
    protected void setupContentView() {
        StatusBarUtil.setColor(this, Color.parseColor("#181818"));
        setContentView(R.layout.activity_account_switch, "切换账号", "编辑", true);
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));
    }

    @Override
    protected void setupView() {
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        initRecycler();
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
        };
        recycler.setAdapter(baseListAdapter);
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
            tvName.setText(accountList.get(position));
            GlideImageLoader.getInstace().displayImage(
                    AccountSwitchActivity.this, edit ? R.drawable.ic_delete_switch : null, ivState);

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
            if (!edit) {
                ToastUtils.showToast("s");
            }
        }
    }

    @Override
    protected void loadData() {
        accountList.add("1");
        accountList.add("1");
        accountList.add("1");
        baseListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        getTitleRightText().setText(edit ? "编辑" : "完成");
        baseListAdapter.notifyDataSetChanged();
        edit = !edit;
    }

}
