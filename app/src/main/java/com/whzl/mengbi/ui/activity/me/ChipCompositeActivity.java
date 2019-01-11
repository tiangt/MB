package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * 合成碎片
 *
 * @author cliang
 * @date 2019.1.10
 */
public class ChipCompositeActivity extends BaseActivity {

    @BindView(R.id.rv_parent_chip)
    RecyclerView recycler;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_chip_composite, "物品合成", "我的碎片", true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupView() {
        initRecycler();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        Intent intent = new Intent(ChipCompositeActivity.this, MyChipActivity.class);
        startActivity(intent);
    }

    private void initRecycler() {
        recycler.setNestedScrollingEnabled(false);
        recycler.setFocusableInTouchMode(false);
        recycler.setHasFixedSize(true);
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
