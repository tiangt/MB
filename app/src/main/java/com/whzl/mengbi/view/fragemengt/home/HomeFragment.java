package com.whzl.mengbi.view.fragemengt.home;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.alibaba.fastjson.JSON;

import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.LiveDisplayActivity;
import com.whzl.mengbi.bean.BannerBean;
import com.whzl.mengbi.bean.LiveShowBean;
import com.whzl.mengbi.bean.LiveShowListBean;
import com.whzl.mengbi.bean.RecommendBean;
import com.whzl.mengbi.bean.RecommendListBean;
import com.whzl.mengbi.glide.GlideImageLoader;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.thread.HomeBannerThread;
import com.whzl.mengbi.thread.HomeLiveShowThread;
import com.whzl.mengbi.thread.HomeRecommendThread;
import com.whzl.mengbi.view.fragemengt.BaseFragement;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.RequestManager.ReqCallBack;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.widget.recyclerview.base.ViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends BaseFragement {

    /**
     * 首页轮番图组件
     */
    private Banner mBanner;
    /**
     * 首页轮番图集合
     */
    private List<BannerBean.DataBean.ListBean> bannerBeanList;

    private HomeBannerThread mHomeBannerThread;
    private HomeBannerHandler mHomeBannerHandler = new HomeBannerHandler(this);

    /**
     * 小编推荐
     */
    private TextView liveShowTv;
    private RecyclerView recommendLiveRv;
    private RecommendBean recommendBean;
    private CommonAdapter<RecommendListBean> mRecommendCommonAdapter;
    private HomeRecommendThread mHomeRecommendThread;
    private HomeRecommendHandler mHomeRecommendHandler = new HomeRecommendHandler(this);
    /**
     *精彩直播
     */
    private TextView recommend_tv;
    private RecyclerView wonderfulLiveRv;
    private LiveShowBean liveShowBean;
    private CommonAdapter<LiveShowListBean> mWonderfulCommonAdapter;
    private HomeLiveShowThread mHomeLiveShowThread;
    private HomeLiveShowHandler mHomeLiveShowHandler = new HomeLiveShowHandler(this);


    /**
     * 全局获取fragment组件
     */
    private View mView;
    //布局管理器
    private  RecyclerView.LayoutManager mWonderfulLayoutManager;
    private  RecyclerView.LayoutManager mRecommendLayoutManager;


    private List<LiveShowListBean> wonderfulList = new ArrayList<>();
    private List<RecommendListBean> recommendList = new ArrayList<>();


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
        HashMap hashMap = new HashMap();
        //首页轮番图
        mHomeBannerThread = new HomeBannerThread(mContext,hashMap,mHomeBannerHandler);
        mHomeBannerThread.start();

        //首页推荐主播
        HashMap recommendMap = new HashMap();
        mHomeRecommendThread = new HomeRecommendThread(mContext,recommendMap,mHomeRecommendHandler);
        mHomeRecommendThread.start();

        //首页主播展示
        HashMap liveMap = new HashMap();
        liveMap.put("page",1);
        mHomeLiveShowThread = new HomeLiveShowThread(mContext,liveMap,mHomeLiveShowHandler);
        mHomeLiveShowThread.start();
    }

     //绑定首页推荐主播数据
     public void initRecommendViewAndData(){
         // 设置布局管理器
         mRecommendLayoutManager = new GridLayoutManager(mContext,2);
         recommendLiveRv.setLayoutManager(mRecommendLayoutManager);
         recommendLiveRv.setAdapter(mRecommendCommonAdapter = new CommonAdapter<RecommendListBean>(mContext,R.layout.fragment_home_recommend_rvitem_layout,recommendList) {
             @Override
             protected void convert(ViewHolder holder, RecommendListBean mData, int position) {
                 if (mData.getStatus().equals("T")){
                     GlideImageLoader.getInstace().displayImage(mContext,R.drawable.ic_home_live_middle,holder.getView(R.id.home_recommend_rvitem_status));
                 }
                 GlideImageLoader.getInstace().displayImage(mContext,mData.getCover(),holder.getView(R.id.home_recommend_rvitem_cover));
                 holder.setText(R.id.home_recommend_rvitem_anchorNickname,mData.getAnchorLevelName());
                 holder.setText(R.id.home_recommend_rvitem_roomUserCount,mData.getRoomUserCount()+"");
             }
         });
        mRecommendCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent mIntent = new Intent(mContext, LiveDisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ProgramId",recommendList.get(position).getProgramId());
                bundle.putString("AnchorNickname",recommendList.get(position).getAnchorNickname());
                bundle.putInt("RoomUserCount",recommendList.get(position).getRoomUserCount());
                bundle.putString("Cover",recommendList.get(position).getCover());
                if(recommendList.get(position).getShowStreamData()!=null){
                    bundle.putString("Stream",recommendList.get(position).getShowStreamData().getFlv());
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
         wonderfulLiveRv.setAdapter(mWonderfulCommonAdapter = new CommonAdapter<LiveShowListBean>(mContext,R.layout.fragment_home_show_rvitem_layout,wonderfulList) {
             @Override
             protected void convert(ViewHolder holder, LiveShowListBean mData, int position) {
                 if (mData.getStatus().equals("T")){
                     GlideImageLoader.getInstace().displayImage(mContext,R.drawable.ic_home_live_middle,holder.getView(R.id.home_rvitem_status));
                 }
                 GlideImageLoader.getInstace().displayImage(mContext,mData.getCover(),holder.getView(R.id.home_rvitem_cover));
                 holder.setText(R.id.home_rvitem_anchorNickname,mData.getAnchorLevelName());
                 holder.setText(R.id.home_rvitem_roomUserCount,mData.getRoomUserCount()+"");
             }
         });

         mWonderfulCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                 Intent mIntent = new Intent(mContext, LiveDisplayActivity.class);
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



    /**
     * 首页轮番图Handler
     为避免handler造成的内存泄漏
     1、使用静态的handler，对外部类不保持对象的引用
     2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
     private static class HomeBannerHandler extends Handler{
        private final WeakReference<Fragment> mFragmentWeakReference;
         HomeBannerHandler(Fragment fragment){
            this.mFragmentWeakReference = new WeakReference<Fragment>(fragment);
        }

         @Override
         public void handleMessage(Message msg) {
             HomeFragment homeFragment = (HomeFragment)mFragmentWeakReference.get();
             super.handleMessage(msg);
             switch (msg.what){
                 case 1:
                     //绑定首页轮番图数据
                     BannerBean bannerBean =(BannerBean) msg.obj;
                     //首页轮番图图片集合
                     List bannerImages=new ArrayList();
                     //首页轮番图标题集合
                     List  bannerTitles=new ArrayList();
                     for (BannerBean.DataBean.ListBean listBean:bannerBean.getData().getList()){
                         bannerImages.add(listBean.getPiclink());
                         bannerTitles.add(listBean.getSubject());
                     }
                     //设置图片集合
                     homeFragment.mBanner.setImages(bannerImages);
                     //设置标题集合（当banner样式有显示title时）
                     homeFragment.mBanner.setBannerTitles(bannerTitles);
                     //banner设置方法全部调用完毕时最后调用
                     homeFragment.mBanner.start();
                     break;
             }
         }
     }

    /**
     * 主播推荐Handler
     为避免handler造成的内存泄漏
     1、使用静态的handler，对外部类不保持对象的引用
     2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
     public static class HomeRecommendHandler extends Handler{
         private final WeakReference<Fragment> mFragmentWeakReference;
         HomeRecommendHandler(Fragment fragment){
             this.mFragmentWeakReference = new WeakReference<Fragment>(fragment);
         }

         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             HomeFragment homeFragment = (HomeFragment)mFragmentWeakReference.get();
             switch (msg.what){
                 case 1:
                     RecommendBean recommendBean = (RecommendBean) msg.obj;
                     homeFragment.recommendList.addAll(recommendBean.getData().getList());
                     homeFragment.initRecommendViewAndData();
                     break;
             }
         }



     }

    /**
     * 精彩主播Handler
     为避免handler造成的内存泄漏
     1、使用静态的handler，对外部类不保持对象的引用
     2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
    public static class HomeLiveShowHandler extends Handler{
        private final WeakReference<Fragment> mFragmentWeakReference;
        HomeLiveShowHandler(Fragment fragment){
            this.mFragmentWeakReference = new WeakReference<Fragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HomeFragment homeFragment = (HomeFragment)mFragmentWeakReference.get();
            switch (msg.what){
                case 1:
                    LiveShowBean liveShowBean = (LiveShowBean) msg.obj;
                    homeFragment.wonderfulList.addAll(liveShowBean.getData().getList());
                    homeFragment.initLiveViewAndData();
                    break;
            }
        }
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
    }
}
