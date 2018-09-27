package com.whzl.mengbi.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private BaseListAdapter anchorAdapter;
    private List mAnchorInfoList = new ArrayList();

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void setupView() {
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ToastUtils.showToast(etSearch.getText().toString().trim());
                KeyBoardUtil.hideInputMethod(this);
            }
            return false;
        });
        initRv();
    }

    private void initRv() {
        mAnchorInfoList.add("");
        mAnchorInfoList.add("");
        mAnchorInfoList.add("");
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
            tvRoom.setText(getString(R.string.search_room, 621211));
            GlideImageLoader.getInstace().displayImage(getBaseActivity(), ResourceMap.getResourceMap().getAnchorLevelIcon(22), ivLevel);

        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            ToastUtils.showToast(position + "");
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
