package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.FollowRefreshEvent;
import com.whzl.mengbi.ui.activity.FindRankActivity;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.fragment.RankFragment;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.fragment.me.WelfareFragment;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2019.2.21
 */
public class FindFragment extends BaseFragment {

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void init() {

    }

    @OnClick({R.id.tv_rank_star, R.id.tv_rank_regal, R.id.tv_rank_pop,
            R.id.rl_find_task, R.id.rl_find_prop, R.id.rl_find_recharge, R.id.rl_sign_find})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_rank_star:
                Intent intent = new Intent(getMyActivity(), FindRankActivity.class);
                intent.putExtra("rank", 0);
                startActivity(intent);
                break;

            case R.id.tv_rank_regal:
                Intent intent1 = new Intent(getMyActivity(), FindRankActivity.class);
                intent1.putExtra("rank", 1);
                startActivity(intent1);
                break;

            case R.id.tv_rank_pop:
                Intent intent2 = new Intent(getMyActivity(), FindRankActivity.class);
                intent2.putExtra("rank", 2);
                startActivity(intent2);
                break;

            case R.id.rl_find_task:
                if (((MainActivity) getActivity()).checkLogin()) {
                    startActivity(new Intent(getMyActivity(), FrgActivity.class)
                            .putExtra(FrgActivity.FRAGMENT_CLASS, WelfareFragment.class));
                    return;
                }
                ((MainActivity) getActivity()).login();
                break;

            case R.id.rl_find_prop:
                if (((MainActivity) getActivity()).checkLogin()) {
                    startActivity(new Intent(getMyActivity(), ShopActivity.class));
                    return;
                }
                ((MainActivity) getActivity()).login();
                break;

            case R.id.rl_find_recharge:
                if (((MainActivity) getActivity()).checkLogin()) {
                    startActivity(new Intent(getMyActivity(), WXPayEntryActivity.class));
                    return;
                }
                ((MainActivity) getActivity()).login();
                break;
            case R.id.rl_sign_find:
                if (((MainActivity) getActivity()).checkLogin()) {
                    ((MainActivity) getActivity()).showSignDialog();
                    return;
                }
                ((MainActivity) getActivity()).login();
                break;
            default:
                break;
        }
    }
}
