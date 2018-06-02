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
    private RecommendHandler mRecommendHandler;

    /**
     *精彩直播 消息处理
     */
    private Handler mWonderfulLiveHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecommendHandler = new RecommendHandler(HomeFragment.this);
        initData();
        initView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_layout, container, false);

    }


    private void initView() {

    }

    private void initData(){
        //首页主播展示
        HashMap paramsMap = new HashMap();
        paramsMap.put("page","1");

        RequestManager.getInstance(getContext()).requestAsyn("/v1/anchor/show-anchor", RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Message mMessage = mRecommendHandler.obtainMessage();
                        mMessage.arg1=1;
                        mMessage.obj=result;
                        mRecommendHandler.sendMessage(mMessage);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
    }

    private class RecommendHandler extends BaseHandler {
        public RecommendHandler(Fragment fragment) {
            super(fragment);
        }

        @Override
        public void handleMessage(Message msg, int what) {

        }
    }


}
