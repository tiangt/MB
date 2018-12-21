package com.whzl.mengbi.ui.activity;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/12/21
 */
public class DrawLayoutControl {
    private Activity activity;
    private LinearLayout drawLayout;
    private List<GetActivityBean.ListBean> bannerInfoList;
    private RecyclerView rvActivity;
    private BaseListAdapter baseListAdapter;


    public DrawLayoutControl(Activity liveDisplayActivity, LinearLayout drawLayout) {
        this.activity = liveDisplayActivity;
        this.drawLayout = drawLayout;
    }

    public void init() {
        bannerInfoList = new ArrayList<>();
        Switch switchVoice = drawLayout.findViewById(R.id.switch_voice_draw_layout_live);
        rvActivity = drawLayout.findViewById(R.id.rv_activity_draw_layout);
        rvActivity.setLayoutManager(new GridLayoutManager(activity, 3));
        baseListAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return bannerInfoList == null ? 0 : bannerInfoList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(activity).inflate(R.layout.item_activity_draw_layout, parent, false);
                return new Holder(itemView);
            }
        };
        rvActivity.setAdapter(baseListAdapter);

        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtils.showToast("ss");
                }
            }
        });
    }


    public void notifyData(List<GetActivityBean.ListBean> mBannerInfoList) {
        this.bannerInfoList.clear();
        this.bannerInfoList.addAll(mBannerInfoList);
        baseListAdapter.notifyDataSetChanged();
    }

    class Holder extends BaseViewHolder {
        @BindView(R.id.iv_activity)
        ImageView ivActivity;
        @BindView(R.id.tv_activity)
        TextView tvActivity;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GetActivityBean.ListBean listBean = bannerInfoList.get(position);
            GlideImageLoader.getInstace().displayImage(activity, listBean.imageUrl, ivActivity);
            tvActivity.setText(listBean.name);
        }
    }

}
