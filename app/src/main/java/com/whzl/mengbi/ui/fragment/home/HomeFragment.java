package com.whzl.mengbi.ui.fragment.home;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.presenter.HomePresenter;
import com.whzl.mengbi.presenter.impl.HomePresenterImpl;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.LiveShowListInfo;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.model.entity.RecommendListInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivityNew;
import com.whzl.mengbi.ui.adapter.HomeLiveAdapter;
import com.whzl.mengbi.ui.adapter.RecommendAdapter;
import com.whzl.mengbi.ui.view.HomeView;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.ui.fragment.BaseFragement;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragement implements HomeView{

    /**
     * 首页轮番图组件
     */
    private Banner mBanner;
    /**
     * 首页轮番图集合
     */
    private List<BannerInfo.DataBean.ListBean> bannerBeanList;

    /**
     * 小编推荐
     */
    private TextView liveShowTv;
    private RecyclerView recommendLiveRv;
    private RecommendAdapter<RecommendListInfo> mRecommendAdapter;
    /**
     *精彩直播
     */
    private TextView recommend_tv;
    private RecyclerView wonderfulLiveRv;
    private HomeLiveAdapter<LiveShowListInfo> mWonderfulAdapter;


    /**
     * 全局获取fragment组件
     */
    private View mView;
    //布局管理器
    private  RecyclerView.LayoutManager mWonderfulLayoutManager;
    private  RecyclerView.LayoutManager mRecommendLayoutManager;


    private List<LiveShowListInfo> wonderfulList = new ArrayList<>();
    private List<RecommendListInfo> recommendList = new ArrayList<>();

    private HomePresenter homePresenter;

    public static HomeFragment newInstance(String info) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_home_layout,null);
        Toolbar mToolbar = (Toolbar)mView.findViewById(R.id.fragment_home_toolbar);
        mToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        homePresenter = new HomePresenterImpl(this);
        initData();
        initView();
        return mView;
    }

    public void initView() {
        mBanner = (Banner) mView.findViewById(R.id.home_banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(GlideImageLoader.getInstace());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);


        recommend_tv = mView.findViewById(R.id.fm_home_recommend_tv);
        liveShowTv = mView.findViewById(R.id.fm_home_show_live_tv);
        recommendLiveRv = mView.findViewById(R.id.fm_home_recommend_recycler_view);
        wonderfulLiveRv =  mView.findViewById(R.id.fm_home_show_live_recycler_view);

        //首页小编推荐字体加粗
        SpannableString spanString = new SpannableString("小编推荐");
        StyleSpan span = new StyleSpan(Typeface.BOLD_ITALIC);//加粗
        spanString.setSpan(span, 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        recommend_tv.setText(spanString);

        //首页精彩直播字体加粗
        SpannableString liveSpanTV = new SpannableString("精彩直播");
        StyleSpan liveStyleTV = new StyleSpan(Typeface.BOLD_ITALIC);//加粗
        spanString.setSpan(liveStyleTV, 0, liveSpanTV.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        liveShowTv.setText(liveSpanTV);
    }

    public void initData() {
        homePresenter.getBanner();
        homePresenter.getRecommend();
        homePresenter.getLiveShow();
    }

     //绑定首页推荐主播数据
     public void initRecommendViewAndData(){
         // 设置布局管理器
         mRecommendLayoutManager = new GridLayoutManager(mContext,2);
         recommendLiveRv.setLayoutManager(mRecommendLayoutManager);
         //设置Adapter
         mRecommendAdapter = new RecommendAdapter<RecommendListInfo>(mContext,R.layout.fragment_home_recommend_rvitem_layout,recommendList);
         recommendLiveRv.setAdapter(mRecommendAdapter);
         mRecommendAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Intent mIntent = new Intent(mContext, LiveDisplayActivity.class);
                Intent mIntent = new Intent(mContext, LiveDisplayActivityNew.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ProgramId",recommendList.get(position).getProgramId());
                bundle.putString("AnchorNickname",recommendList.get(position).getAnchorNickname());
                bundle.putInt("RoomUserCount",recommendList.get(position).getRoomUserCount());
                bundle.putString("Cover",recommendList.get(position).getCover());
                if(recommendList.get(position).getShowStreamData()!=null){
                    bundle.putString("Stream",recommendList.get(position).getShowStreamData().getFlv());
                    bundle.putInt("displayWidth", recommendList.get(position).getShowStreamData().getWidth());
                    bundle.putInt("displayHeight", recommendList.get(position).getShowStreamData().getHeight());
                }
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
     }

     //绑定首页精彩主播数据
     public void initLiveViewAndData(){
         // 设置布局管理器
         mWonderfulLayoutManager = new GridLayoutManager(mContext,2);
         wonderfulLiveRv.setLayoutManager(mWonderfulLayoutManager);
         // 设置adapter
         mWonderfulAdapter = new HomeLiveAdapter(mContext,R.layout.fragment_home_show_rvitem_layout,wonderfulList);
         wonderfulLiveRv.setAdapter(mWonderfulAdapter);
         mWonderfulAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                 Intent mIntent = new Intent(mContext, LiveDisplayActivity.class);
                 Intent mIntent = new Intent(mContext, LiveDisplayActivityNew.class);
                 Bundle bundle = new Bundle();
                 bundle.putInt("ProgramId",wonderfulList.get(position).getProgramId());
                 bundle.putString("AnchorNickname",wonderfulList.get(position).getAnchorNickname());
                 bundle.putInt("RoomUserCount",wonderfulList.get(position).getRoomUserCount());
                 bundle.putString("Cover",wonderfulList.get(position).getCover());
                 if(wonderfulList.get(position).getShowStreamData()!=null){
                     bundle.putString("Stream",wonderfulList.get(position).getShowStreamData().getFlv());
                 }
                 mIntent.putExtras(bundle);
                 startActivity(mIntent);
             }

             @Override
             public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                 return false;
             }
         });
     }

    @Override
    public void showBanner(BannerInfo bannerInfo) {
        //首页轮番图图片集合
        List bannerImages=new ArrayList();
        //首页轮番图标题集合
        List  bannerTitles=new ArrayList();
        for (BannerInfo.DataBean.ListBean listBean: bannerInfo.getData().getList()){
            bannerImages.add(listBean.getPiclink());
            bannerTitles.add(listBean.getSubject());
        }
        //设置图片集合
        mBanner.setImages(bannerImages);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(bannerTitles);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    @Override
    public void showRecommend(RecommendInfo recommendInfo) {
        recommendList.addAll(recommendInfo.getData().getList());
        initRecommendViewAndData();
    }

    @Override
    public void showLiveShow(LiveShowInfo liveShowInfo) {
        wonderfulList.addAll(liveShowInfo.getData().getList());
        initLiveViewAndData();
    }

    @Override
    public void onStart() {
        //开始轮番
        mBanner.startAutoPlay();
        super.onStart();
    }

    @Override
    public void onStop() {
        //结束轮番
        mBanner.stopAutoPlay();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.onDestroy();
    }
}
