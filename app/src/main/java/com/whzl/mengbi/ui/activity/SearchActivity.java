package com.whzl.mengbi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
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
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.model.entity.SearchAnchorBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.ConfirmDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.widget.view.FlowLayout;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
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
public class SearchActivity extends BaseActivity {
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
    private List<String> mHotAnchorList = new ArrayList<>();
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
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
        mPref = getSharedPreferences("input", Activity.MODE_PRIVATE);
        layoutInflater = LayoutInflater.from(this);
    }

    @Override
    protected void setupView() {
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
                            SpannableString string = StringUtils.spannableStringColor("找到包含", Color.parseColor("#4b4b4b"));
                            SpannableString string2 = StringUtils.spannableStringColor(" " + etSearch.getText().toString().trim() + " ",
                                    Color.parseColor("#ff611b"));
                            SpannableString string3 = StringUtils.spannableStringColor("的主播共 ", Color.parseColor("#4b4b4b"));
                            SpannableString string4 = StringUtils.spannableStringColor( bean.total+"", Color.parseColor("#ff611b"));
                            SpannableString string5 = StringUtils.spannableStringColor( " 位", Color.parseColor("#4b4b4b"));
                            tvName.setText(string);
                            tvName.append(string2);
                            tvName.append(string3);
                            tvName.append(string4);
                            tvName.append(string5);
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
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            Intent intent = new Intent(SearchActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, mAnchorInfoList.get(position).programId);
            startActivity(intent);
        }
    }

    @Override
    protected void loadData() {

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
                        .setListener(new ConfirmDialog.OnClickListener() {
                            @Override
                            public void onClickSuccess() {
                                cleanHistory();
                            }
                        })
                        .show(getSupportFragmentManager());
                break;
        }
    }

    /**
     * 热搜主播
     */
    private void initHotAnchor() {
        for (int i = 0; i < 20; i++) {
            mHotAnchorList.add(i + "");
        }
        rvHotAnchor.setNestedScrollingEnabled(false);
        rvHotAnchor.setFocusableInTouchMode(false);
        rvHotAnchor.setHasFixedSize(true);
        rvHotAnchor.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        rvHotAnchor.setLayoutManager(new LinearLayoutManager(this));
        hotAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mHotAnchorList == null ? 0 : mHotAnchorList.size();
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
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(SearchActivity.this, 5));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
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
                    tv.setOnClickListener((view) -> {
                        etSearch.setText(str);
                    });
                    flSearch.addView(tv);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (confirmDialog != null && confirmDialog.isAdded()) {
            confirmDialog.dismiss();
        }
    }
}
