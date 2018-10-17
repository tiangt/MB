package com.whzl.mengbi.ui.fragment.me;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.AnchorFollowedDataBean;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/10/16
 */
public class TestFragment extends BasePullListFragment<AnchorFollowedDataBean.AnchorInfoBean> {
    @Override
    protected boolean setShouldLoadMore() {
        return true;
    }

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected void loadData(int action, int mPage) {
        if (action == PullRecycler.ACTION_LOAD_DATA || action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            HashMap hashMap = new HashMap();
            hashMap.put("userId", Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString()));
            hashMap.put("page", mPage);
            hashMap.put("pageSize", 10);
            RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ANCHOR_FOLLOWED, RequestManager.TYPE_POST_JSON, hashMap,
                    new RequestManager.ReqCallBack<Object>() {
                        @Override
                        public void onReqSuccess(Object result) {
                            String jsonStr = result.toString();
                            AnchorFollowedDataBean anchorFollowedDataBean = GsonUtils.GsonToBean(jsonStr, AnchorFollowedDataBean.class);
                            loadSuccess(anchorFollowedDataBean.data.list);
                        }

                        @Override
                        public void onReqFailed(String errorMsg) {
                            loadFail();
                        }
                    });
        }
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_anchor_followed, parent, false);
        return new AnchorViewHolder(itemView);
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
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mDatas.get(position);
            GlideImageLoader.getInstace().displayImage(getContext(), anchorInfoBean.avatar, ivAvatar);
            tvStatus.setVisibility("T".equals(anchorInfoBean.status) ? View.VISIBLE : View.GONE);
            tvRoomNum.setText(getString(R.string.room_num, anchorInfoBean.programId));
            tvAnchorName.setText(anchorInfoBean.anchorNickname);
            ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(anchorInfoBean.anchorLevelValue));
        }
    }
}
