package com.whzl.mengbi.fragemengt.home;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.whzl.mengbi.R;
import com.whzl.mengbi.adapter.HomeLiveAdapter;
import com.whzl.mengbi.bean.ResultBean;
import com.whzl.mengbi.fragemengt.BaseFragement;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.RequestManager.ReqCallBack;
import com.whzl.mengbi.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * 全局获取fragment组件
     */
    private View mView;

    private RecyclerView.Adapter mAdapter;
    private List<ResultBean.LiveBean> arrayList;

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
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_layout,null);
        initView(mView);
        return mView;
    }

    private void initData(){
        //首页主播展示
        HashMap paramsMap = new HashMap();
        paramsMap.put("page","1");

        RequestManager.getInstance(getContext()).requestAsyn("v1/anchor/show-anchor", RequestManager.TYPE_POST_JSON, paramsMap,
                new ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        arrayList = new ArrayList<ResultBean.LiveBean>();
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e(errorMsg);
                    }
                });
    }

    private void initView(View mView) {
        wonderful_live_rv = (RecyclerView) mView.findViewById(R.id.fm_home_live_recycler_view);
        wonderful_live_rv.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new HomeLiveAdapter(arrayList);
        wonderful_live_rv.setAdapter(mAdapter);
    }
}
