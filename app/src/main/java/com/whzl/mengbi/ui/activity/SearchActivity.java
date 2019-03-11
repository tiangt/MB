package com.whzl.mengbi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.SearchAnchorBean;
import com.whzl.mengbi.model.entity.TrendingAnchorInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.ConfirmDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.widget.view.FlowLayout;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/20
 */
public class SearchActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ib_clean)
    ImageButton ibClean;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.ll_find)
    LinearLayout llFind;
    @BindView(R.id.ll_list)
    LinearLayout llList;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    @BindView(R.id.srl_search)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.fl_history_search)
    FlowLayout flSearch;
    @BindView(R.id.rv_hot_anchor)
    RecyclerView rvHotAnchor;
    @BindView(R.id.scroll_search)
    ScrollView scrollView;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_hot_anchor)
    LinearLayout llHotAnchor;

    private BaseListAdapter anchorAdapter;
    private BaseListAdapter hotAdapter;
    private List<SearchAnchorBean.ListBean> mAnchorInfoList = new ArrayList();
    private ArrayList<TrendingAnchorInfo.DataBean.ListBean> mTrendingInfo = new ArrayList<>();
    private int mCurrentPager = 1;
    private String key;
    private SharedPreferences mPref;//使用SharedPreferences记录搜索历史
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "search_history";
    private List<String> mHistoryKeywords;
    private LayoutInflater layoutInflater;
    private BaseAwesomeDialog confirmDialog;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_search_record);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPref = getSharedPreferences("input", Activity.MODE_PRIVATE);
        layoutInflater = LayoutInflater.from(this);
    }

    @Override
    protected void setupView() {
        etSearch.addTextChangedListener(this);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (TextUtils.isEmpty(etSearch.getText().toString())) {
                    return false;
                }
                if (mAnchorInfoList.size() > 0) {
                    mAnchorInfoList.clear();
                }
                mCurrentPager = 1;
                key = etSearch.getText().toString().trim();
                search(key);
                KeyBoardUtil.hideInputMethod(this);
            }
            return false;
        });
        initRv();

        initSrl();

        initHotAnchor();
        initData();
    }

    private void initSrl() {
        refreshLayout.setOnLoadMoreListener(refreshLayout -> search(key));
    }

    private void search(String key) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("keyword", key);
        paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
        paramsMap.put("page", mCurrentPager++);
        ApiFactory.getInstance().getApi(Api.class)
                .anchorSearch(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SearchAnchorBean>(this) {

                    @Override
                    public void onSuccess(SearchAnchorBean bean) {
                        scrollView.setVisibility(View.GONE);
                        if (mCurrentPager == 2 && bean.list.size() == 0) {
                            llList.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            save(key);
                            llList.setVisibility(View.VISIBLE);
                            llEmpty.setVisibility(View.GONE);
                            SpannableStringBuilder string = StringUtils.coverStringColor("找到包含", Color.parseColor("#4b4b4b"));
                            SpannableStringBuilder string2 = StringUtils.coverStringColor(" " + etSearch.getText().toString().trim() + " ",
                                    Color.parseColor("#ff611b"));
                            SpannableStringBuilder string3 = StringUtils.coverStringColor("的主播共 ", Color.parseColor("#4b4b4b"));
                            SpannableStringBuilder string4 = StringUtils.coverStringColor(bean.total + "", Color.parseColor("#ff611b"));
                            SpannableStringBuilder string5 = StringUtils.coverStringColor(" 位", Color.parseColor("#4b4b4b"));
                            tvName.setText(string.append(string2).append(string3).append(string4).append(string5), TextView.BufferType.SPANNABLE);
//                            tvName.append(string2);
//                            tvName.append(string3);
//                            tvName.append(string4);
//                            tvName.append(string5);
                        }
                        mAnchorInfoList.addAll(bean.list);
                        anchorAdapter.notifyDataSetChanged();

                        if (bean.list.size() < NetConfig.DEFAULT_PAGER_SIZE) {
                            refreshLayout.finishLoadMore(500, true, true);
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(int code) {
                    }
                });
    }

    private void initRv() {
        rvSearch.setNestedScrollingEnabled(false);
        rvSearch.setFocusableInTouchMode(false);
        rvSearch.setHasFixedSize(true);
        rvSearch.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        anchorAdapter = new BaseListAdapter() {

            @Override
            protected int getDataCount() {
                return mAnchorInfoList == null ? 0 : mAnchorInfoList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getBaseActivity()).inflate(R.layout.item_search_record, parent, false);
                return new SearchViewHolder(itemView);
            }
        };
        rvSearch.setAdapter(anchorAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String strSearch = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(strSearch)) {
            ibClean.setVisibility(View.VISIBLE);
        } else {
            ibClean.setVisibility(View.GONE);
        }
    }

    class SearchViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_room)
        TextView tvRoom;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvName.setText(mAnchorInfoList.get(position).anchorNickname);
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(SearchActivity.this, 5));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            GlideImageLoader.getInstace().displayImage(SearchActivity.this,
                    ResourceMap.getResourceMap().getAnchorLevelIcon(mAnchorInfoList.get(position).anchorLevelValue), ivLevel);
//            GlideImageLoader.getInstace().displayImage(SearchActivity.this, mAnchorInfoList.get(position).anchorAvatar, ivIcon);
            Glide.with(SearchActivity.this).load(mAnchorInfoList.get(position).anchorAvatar).apply(requestOptions).into(ivIcon);
            tvRoom.setText(getString(R.string.room_num, mAnchorInfoList.get(position).programId));
            if ("T".equals(mAnchorInfoList.get(position).status)) {
                tvStatus.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            //保存热搜主播
            saveAnchorSearch(mAnchorInfoList.get(position).anchorId);
            Intent intent = new Intent(SearchActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, mAnchorInfoList.get(position).programId);
            startActivity(intent);
        }
    }

    @Override
    protected void loadData() {
        //获取热搜列表
        trendingAnchor();
    }

    @OnClick({R.id.ib_clean, R.id.tv_cancel, R.id.ll_list, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_clean:
                etSearch.setText("");
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.ll_list:
                KeyBoardUtil.hideInputMethod(this);
                break;
            case R.id.iv_delete:
                confirmDialog = ConfirmDialog.newInstance(getString(R.string.delete_search_history))
                        .setListener(() -> cleanHistory())
                        .show(getSupportFragmentManager());
                break;
        }
    }

    /**
     * 热搜主播
     */
    private void initHotAnchor() {
        rvHotAnchor.setNestedScrollingEnabled(false);
        rvHotAnchor.setFocusableInTouchMode(false);
        rvHotAnchor.setHasFixedSize(true);
        rvHotAnchor.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvHotAnchor.setLayoutManager(new LinearLayoutManager(this));
        hotAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mTrendingInfo == null ? 0 : mTrendingInfo.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getBaseActivity()).inflate(R.layout.item_search_record, parent, false);
                return new HotAnchorViewHolder(itemView);
            }
        };
        rvHotAnchor.setAdapter(hotAdapter);
    }

    class HotAnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_room)
        TextView tvRoom;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        public HotAnchorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvName.setText(mTrendingInfo.get(position).anchorNickname);
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(SearchActivity.this, 5));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            GlideImageLoader.getInstace().displayImage(SearchActivity.this,
                    ResourceMap.getResourceMap().getAnchorLevelIcon(mTrendingInfo.get(position).anchorLevelValue), ivLevel);
            Glide.with(SearchActivity.this).load(mTrendingInfo.get(position).anchorAvatar).apply(requestOptions).into(ivIcon);
            tvRoom.setText(getString(R.string.room_num, mTrendingInfo.get(position).programId));
            if ("T".equals(mTrendingInfo.get(position).status)) {
                tvStatus.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            //保存热搜主播
            saveAnchorSearch(mTrendingInfo.get(position).anchorId);
            Intent intent = new Intent(SearchActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, mTrendingInfo.get(position).programId);
            startActivity(intent);
        }
    }

    /**
     * 保存记录
     */
    private void save(String key) {
        String oldText = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(key) && !(oldText.contains(key))) {
            if (mHistoryKeywords.size() > 20) {
                //最多保存条数
                return;
            }
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_SEARCH_HISTORY_KEYWORD, key + "," + oldText);
            editor.commit();
            mHistoryKeywords.add(0, key);
        }
    }

    /**
     * 清空历史纪录
     */
    public void cleanHistory() {
        llSearch.setVisibility(View.GONE);
        mPref = getSharedPreferences("input", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(KEY_SEARCH_HISTORY_KEYWORD).commit();
        mHistoryKeywords.clear();
    }

    /**
     * 搜索记录数据
     */
    private void initData() {
        mHistoryKeywords = new ArrayList<String>();
        String history = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(history)) {
            llSearch.setVisibility(View.VISIBLE);
            List<String> list = new ArrayList<String>();
            for (Object o : history.split(",")) {
                list.add((String) o);
            }
            mHistoryKeywords = list;
        }
        if (mHistoryKeywords != null) {
            for (int i = 0; i < mHistoryKeywords.size(); i++) {
                if (i < 8) {
                    TextView tv = (TextView) layoutInflater.inflate(R.layout.item_flow, flSearch, false);
                    tv.setText(mHistoryKeywords.get(i));
                    final String str = tv.getText().toString();
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etSearch.setText(str);
                            search(str);
                        }
                    });
                    flSearch.addView(tv);
                }
            }
        }
    }

    /**
     * 保存搜索热度
     *
     * @param anchorId
     */
    private void saveAnchorSearch(int anchorId) {
        HashMap hashMap = new HashMap();
        hashMap.put("anchorId", anchorId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SAVE_ANCHOR_SEARCH, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        ResponseInfo reportBean = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                        if (reportBean.getCode() != 200) {
                            showToast(reportBean.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        showToast(errorMsg);
                    }
                });
    }

    /**
     * 热搜主播列表
     */
    private void trendingAnchor() {
        HashMap hashMap = new HashMap();
        hashMap.put("count", 10);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.TRENDING_ANCHOR, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        TrendingAnchorInfo anchorInfo = GsonUtils.GsonToBean(result.toString(), TrendingAnchorInfo.class);
                        if (anchorInfo.getCode() == 200) {
                            if (anchorInfo != null && anchorInfo.data != null && anchorInfo.data.list != null) {
                                if (anchorInfo.data.list.size() > 0) {
                                    if (llHotAnchor != null) {
                                        llHotAnchor.setVisibility(View.VISIBLE);
                                    }
                                    mTrendingInfo.addAll(anchorInfo.data.list);
                                    hotAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        showToast(errorMsg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (confirmDialog != null && confirmDialog.isAdded()) {
            confirmDialog.dismiss();
        }
    }
}
