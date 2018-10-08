package com.whzl.mengbi.ui.activity;

import android.content.Intent;
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
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.model.entity.SearchAnchorBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
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
    private BaseListAdapter anchorAdapter;
    private List<SearchAnchorBean.ListBean> mAnchorInfoList = new ArrayList();
    private int mCurrentPager = 1;
    private String key;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_search);
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
    }

    private void initSrl() {
        refreshLayout.setOnLoadMoreListener(refreshLayout -> search(key));
    }

    private void search(String key) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("keyword", key);
        paramsMap.put("pageSize", 20);
        paramsMap.put("page", mCurrentPager++);
        ApiFactory.getInstance().getApi(Api.class)
                .anchorSearch(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SearchAnchorBean>(this) {

                    @Override
                    public void onSuccess(SearchAnchorBean bean) {

                        if (bean.list == null || bean.list.size() == 0) {
                            llList.setVisibility(View.GONE);
                            return;
                        } else {
                            llList.setVisibility(View.VISIBLE);
                            SpannableString string = StringUtils.spannableStringColor("找到包含", Color.parseColor("#4b4b4b"));
                            SpannableString string2 = StringUtils.spannableStringColor("\"" + etSearch.getText().toString().trim() + "\"",
                                    Color.parseColor("#ff611b"));
                            SpannableString string3 = StringUtils.spannableStringColor("的主播共" + bean.list.size() + "位",
                                    Color.parseColor("#4b4b4b"));
                            tvName.setText(string);
                            tvName.append(string2);
                            tvName.append(string3);
                        }
                        mAnchorInfoList.addAll(bean.list);
                        anchorAdapter.notifyDataSetChanged();
                        refreshLayout.finishLoadMore();
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
                View itemView = LayoutInflater.from(getBaseActivity()).inflate(R.layout.item_search_home, parent, false);
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

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvName.setText(mAnchorInfoList.get(position).anchorNickname);
            GlideImageLoader.getInstace().displayImage(SearchActivity.this,
                    ResourceMap.getResourceMap().getAnchorLevelIcon(mAnchorInfoList.get(position).anchorLevelValue), ivLevel);
            GlideImageLoader.getInstace().displayImage(SearchActivity.this, mAnchorInfoList.get(position).anchorAvatar, ivIcon);
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


    @OnClick({R.id.ib_clean, R.id.tv_cancel, R.id.ll_list})
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
        }
    }

}
