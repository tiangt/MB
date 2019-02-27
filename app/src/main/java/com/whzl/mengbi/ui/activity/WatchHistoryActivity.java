package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.WatchHistoryListBean;
import com.whzl.mengbi.ui.activity.base.BaseListActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/30
 */
public class WatchHistoryActivity extends BaseListActivity<WatchHistoryListBean.AnchorDetailBean> {

    @Override
    protected void initEnv() {
        super.initEnv();
    }

    @Override
    protected String getActivityTitle() {
        return "观看记录";
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(this).inflate(R.layout.item_anchor_followed, parent, false);
        return new AnchorViewHolder(item);
    }

    @Override
    protected void onLoadAction(int action) {
        switch (action) {
            case BaseListActivity.ACTION_FIRST_LOAD:
            case BaseListActivity.ACTION_PULL_REFRESH:
                mPager = 1;
                loadDataFromNet(mPager++);
                break;
            case BaseListActivity.ACTION_LOAD_MORE:
                loadDataFromNet(mPager++);
                break;
            default:
                break;
        }
    }

    class AnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_anchor_name)
        TextView tvAnchorName;
        @BindView(R.id.tv_room_num)
        TextView tvRoomNum;
        @BindView(R.id.iv_level_icon)
        ImageView ivLevelIcon;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        public AnchorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            WatchHistoryListBean.AnchorDetailBean anchorDetailBean = mDatas.get(position);
            GlideImageLoader.getInstace().displayImage(WatchHistoryActivity.this, anchorDetailBean.anchorAvatar, ivAvatar);
            tvStatus.setVisibility("T".equals(anchorDetailBean.status) ? View.VISIBLE : View.GONE);
            tvRoomNum.setText(getString(R.string.room_num, anchorDetailBean.programId));
            tvAnchorName.setText(anchorDetailBean.anchorNickname);
//            ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(anchorDetailBean.));
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            WatchHistoryListBean.AnchorDetailBean anchorDetailBean = mDatas.get(position);
            Intent intent = new Intent(WatchHistoryActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, anchorDetailBean.programId);
            startActivity(intent);
        }
    }

    private void loadDataFromNet(int pager) {
        long userId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        paramsMap.put("page", pager);
        paramsMap.put("pageSize", 20);
        Map signPramsMap = ParamsUtils.getSignPramsMap(paramsMap);
        ApiFactory.getInstance().getApi(Api.class)
                .getWatchHistory(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<WatchHistoryListBean>(this) {

                    @Override
                    public void onSuccess(WatchHistoryListBean watchHistoryListBean) {
                        if(watchHistoryListBean == null){
                            loadSuccess(null);
                        }else {
                            loadSuccess(watchHistoryListBean.list);
                        }
                    }

                    @Override
                    public void onError(int code) {
                        loadFail();
                    }
                });

    }

}
