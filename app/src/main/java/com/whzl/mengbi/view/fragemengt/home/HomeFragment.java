package com.whzl.mengbi.view.fragemengt.home;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
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

import com.alibaba.fastjson.JSON;

import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.LiveDisplayActivity;
import com.whzl.mengbi.bean.BannerBean;
import com.whzl.mengbi.bean.LiveShowBean;
import com.whzl.mengbi.bean.RecommendBean;
import com.whzl.mengbi.bean.RecommendListBean;
import com.whzl.mengbi.glide.GlideImageLoader;
import com.whzl.mengbi.network.URLContentUtils;
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

    /**
     * 首页轮番图图片集合
     */
    private List bannerImages=new ArrayList();

    /**
     * 首页轮番图标题集合
     */
    private List  bannerTitles=new ArrayList();


    /**
     * 小编推荐
     */
    private TextView liveShowTv;
    private RecyclerView recommendLiveRv;
    private RecommendBean recommendBean;
    private CommonAdapter<RecommendListBean> mRecommendCommonAdapter;
    /**
     *精彩直播
     */
    private TextView recommend_tv;
    private RecyclerView wonderfulLiveRv;
    private LiveShowBean liveShowBean;
    private CommonAdapter<LiveShowBean.DataBean.ListBean> mLiveCommonAdapter;


    /**
     * 全局获取fragment组件
     */
    private View mView;
    //布局管理器
    private  RecyclerView.LayoutManager mLayoutManager;
    private  RecyclerView.LayoutManager mRecommendLayoutManager;


    private List<LiveShowBean.DataBean.ListBean> arrayList = new ArrayList<>();
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
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
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
        SpannableString spanString = new SpannableString("小编推荐");
        StyleSpan span = new StyleSpan(Typeface.BOLD_ITALIC);//加粗
        spanString.setSpan(span, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        recommend_tv.setText(spanString);

        liveShowTv = mView.findViewById(R.id.fm_home_show_live_tv);
        SpannableString spanString_ls = new SpannableString("精彩直播");
        StyleSpan span_ls = new StyleSpan(Typeface.BOLD_ITALIC);//加粗
        spanString.setSpan(span_ls, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        liveShowTv.setText(spanString_ls);
        recommendLiveRv = mView.findViewById(R.id.fm_home_recommend_recycler_view);
        wonderfulLiveRv =  mView.findViewById(R.id.fm_home_show_live_recycler_view);


    }

    public void initData() {
        //首页轮番图
        HashMap hashMap = new HashMap();
        RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.INDEX_ADV, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                bannerBeanList = new ArrayList();
                BannerBean bannerBean = new Gson().fromJson(result.toString(),BannerBean.class);
                if(bannerBean.getCode()==200){
                    bannerBeanList.addAll(bannerBean.getData().getList());
                    for (int i=0;i<bannerBeanList.size();i++){
                        bannerTitles.add(bannerBeanList.get(i).getSubject());
                        bannerImages.add(bannerBeanList.get(i).getPiclink());
                    }
                    initViewAndData();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d("onReqFailed"+errorMsg.toString());
            }
        });

        //首页推荐主播
        HashMap recommendParamsMap = new HashMap();
        RequestManager.getInstance(getContext()).requestAsyn(URLContentUtils.RECOMMEND_ANCHOR, RequestManager.TYPE_POST_JSON, recommendParamsMap,
                new ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        recommendBean = JSON.parseObject(jsonStr,RecommendBean.class);
                        if (recommendBean.getCode()==200){
                            recommendList.addAll(recommendBean.getData().getList());
                            initViewAndData();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg);
                    }
                });

        //首页主播展示
        HashMap showParamsMap = new HashMap();
        showParamsMap.put("page","1");
        RequestManager.getInstance(getContext()).requestAsyn(URLContentUtils.SHOW_ANCHOR, RequestManager.TYPE_POST_JSON, showParamsMap,
                new ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        liveShowBean =   new Gson().fromJson(result.toString(), LiveShowBean.class);
                        if(liveShowBean.getCode()==200){
                            arrayList.addAll(liveShowBean.getData().getList());
                            initViewAndData();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg);
                    }
                });
        }

     //初始化View并绑定Data
     public void initViewAndData(){
        //绑定首页轮番图数据
         //设置图片集合
         mBanner.setImages(bannerImages);
         //设置标题集合（当banner样式有显示title时）
         mBanner.setBannerTitles(bannerTitles);
         //banner设置方法全部调用完毕时最后调用
         mBanner.start();

         //绑定首页推荐主播数据
         mRecommendLayoutManager = new GridLayoutManager(mContext,2);
         recommendLiveRv.setLayoutManager(mRecommendLayoutManager);
         recommendLiveRv.setAdapter(mRecommendCommonAdapter = new CommonAdapter<RecommendListBean>(mContext,R.layout.fragment_home_recommend_rvitem_layout,recommendList) {
             @Override
             protected void convert(ViewHolder holder, RecommendListBean mData, int position) {

                 if (mData.getStatus().equals("T")){
                     GlideImageLoader.getInstace().displayImage(mContext,R.mipmap.ic_home_live_middle,holder.getView(R.id.home_recommend_rvitem_status));
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


         //绑定首页精彩主播数据
         // 设置布局管理器
         mLayoutManager = new GridLayoutManager(mContext,2);
         wonderfulLiveRv.setLayoutManager(mLayoutManager);
         // 设置adapter
         wonderfulLiveRv.setAdapter(mLiveCommonAdapter = new CommonAdapter<LiveShowBean.DataBean.ListBean>(mContext,R.layout.fragment_home_show_rvitem_layout,arrayList) {
             @Override
             protected void convert(ViewHolder holder, LiveShowBean.DataBean.ListBean mData, int position) {
                 if (mData.getStatus().equals("T")){
                     GlideImageLoader.getInstace().displayImage(mContext,R.mipmap.ic_home_live_middle,holder.getView(R.id.home_rvitem_status));
                 }
                 GlideImageLoader.getInstace().displayImage(mContext,mData.getCover(),holder.getView(R.id.home_rvitem_cover));
                 holder.setText(R.id.home_rvitem_anchorNickname,mData.getAnchorLevelName());
                 holder.setText(R.id.home_rvitem_roomUserCount,mData.getRoomUserCount()+"");
             }
         });

         mLiveCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
}
