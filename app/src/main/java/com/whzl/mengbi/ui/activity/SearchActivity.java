package com.whzl.mengbi.ui.activity;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ToastUtils;

import butterknife.BindView;
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
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.ib_clean, R.id.tv_cancel,R.id.ll_list})
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
