package com.whzl.mengbi.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.fragment.WatchHistoryFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2019.2.20
 */
public class HistoryListActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Fragment[] fragments;
    private WatchHistoryFragment fragment;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_history_list);
    }

    @Override
    protected void setupView() {
        tvTitle.setText("观看记录");
        rlBack.setOnClickListener((v -> finish()));

        fragment = WatchHistoryFragment.newInstance();
        fragments = new Fragment[]{fragment};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragments[0]).commit();
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.ib_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_clear:
                fragment.clickIbClear();
                break;
        }
    }
}
