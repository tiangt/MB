package com.whzl.mengbi.fragemengt.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.fragemengt.BaseFragement;
import com.whzl.mengbi.handler.BaseHandler;
import com.whzl.mengbi.network.RequestManager;

import java.util.HashMap;
import java.util.List;
import com.alibaba.fastjson.JSON;

public class HomeFragment extends BaseFragement {

    /**
     * 小编推荐
     */
    private RecyclerView recommend_rv;

    /**
     *精彩直播
     */
    private RecyclerView wonderful_live_rv;


    /**
     *小编推荐 消息处理
     */
    //private RecommendHandler mRecommendHandler;

    /**
     *精彩直播 消息处理
     */
    private WonderfulLiveHandler mWonderfulLiveHandler;

    /**
     * 全局获取fragment组件
     */
    private View mView;

    public static HomeFragment newInstance(String info) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWonderfulLiveHandler = new WonderfulLiveHandler(HomeFragment.this);
        initData();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_layout,null);
        initView(mView);
        return mView;
    }


    private void initView(View mView) {
        wonderful_live_rv = (RecyclerView) mView.findViewById(R.id.fm_home_live_recycler_view);
    }

    private void initData(){
        //首页主播展示
        HashMap paramsMap = new HashMap();
        paramsMap.put("page","1");

        RequestManager.getInstance(getContext()).requestAsyn("/v1/anchor/show-anchor", RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Message mMessage = mWonderfulLiveHandler.obtainMessage();
                        mMessage.arg1=1;
                        mMessage.obj=result;
                        mWonderfulLiveHandler.sendMessage(mMessage);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
    }

    /**
    *精彩直播handler消息处理，更新UI组件
    */
    private class WonderfulLiveHandler extends BaseHandler {
        public WonderfulLiveHandler(Fragment fragment) {
            super(fragment);
        }

        @Override
        public void handleMessage(Message msg, int what) {
            List liveList = JSON.parseArray(msg+"");
            
        }
    }


}
