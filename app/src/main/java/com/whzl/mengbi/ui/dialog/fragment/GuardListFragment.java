package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.GuardListDialog;
import com.whzl.mengbi.ui.fragment.base.BaseListFragment;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/8/3
 */
public class GuardListFragment extends BaseListFragment<GuardListBean.GuardDetailBean> {

    public static GuardListFragment newInstance(int programId) {
        GuardListFragment fragment = new GuardListFragment();
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_guard, parent, false);
        return new GuardViewHolder(itemView);
    }

    @Override
    protected void loadData() {
        int programId = getArguments().getInt("programId");
        HashMap paramsMap = new HashMap();
        paramsMap.put("programId", programId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(paramsMap);
        ApiFactory.getInstance().getApi(Api.class)
                .getGuardList(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GuardListBean>() {
                    @Override
                    public void onSuccess(GuardListBean guardListBean) {
                        setEmptyText(getString(R.string.empty_guard_list));
                        if (guardListBean != null) {
                            loadSuccess(guardListBean.list);
                            GuardListDialog guardListDialog = (GuardListDialog) getParentFragment();
                            if(guardListDialog != null && guardListDialog.isAdded() && guardListBean.list != null){
                                guardListDialog.setGuardTitle(guardListBean.list.size());
                            }
                        } else {
                            loadSuccess(null);
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    class GuardViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.iv_level_icon)
        ImageView ivLevelIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_expire)
        TextView tvExpire;

        GuardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GuardListBean.GuardDetailBean guardDetailBean = mData.get(position);
            ivAvatar.setAlpha(guardDetailBean.isOnline == 1 ? 1f : 0.5f);
            GlideImageLoader.getInstace().displayImage(getContext(), guardDetailBean.avatar, ivAvatar);
            int userLevelIcon = ResourceMap.getResourceMap().getUserLevelIcon(guardDetailBean.userLevel);
            ivLevelIcon.setImageResource(userLevelIcon);
            tvName.setText(guardDetailBean.nickName);
            tvExpire.setText("剩余 ");
            SpannableString spannableString = StringUtils.spannableStringColor(guardDetailBean.surplusDays + "", Color.parseColor("#f1275b"));
            tvExpire.append(spannableString);
            tvExpire.append(" 天");
        }
    }

}
