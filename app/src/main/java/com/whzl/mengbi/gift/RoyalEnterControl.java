package com.whzl.mengbi.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.util.RxTimerUtil;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2018/11/5
 */
public class RoyalEnterControl {
    LinearLayout llEnter;
    TextView tvEnter;
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLlEnter(LinearLayout llEnter) {
        this.llEnter = llEnter;
    }

    public void setTvEnter(TextView tvEnter) {
        this.tvEnter = tvEnter;
    }

    private List<String> list = new ArrayList<>();

    private boolean isPlay = false;

    public void showEnter(String string) {
        list.add(string);
        if (!isPlay && list.size() != 0) {
            startAnimal(llEnter);
        }

    }

    private void startAnimal(final LinearLayout ll) {
        isPlay = true;
        ll.setVisibility(View.VISIBLE);
        tvEnter.setText(list.get(0));
        tvEnter.setSelected(false);

        ObjectAnimator translationX = new ObjectAnimator().ofFloat(ll, "translationX",
                -UIUtil.dip2px(context, 200), 0);
        translationX.setDuration(1000);  //设置动画时间
        translationX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvEnter.setSelected(true);
                RxTimerUtil.timer(3000, new RxTimerUtil.IRxNext() {
                    @Override
                    public void doNext(long number) {
                        outAnim(ll);
                    }
                });
            }
        });
        translationX.start();
    }

    private void outAnim(final LinearLayout ll) {
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(ll, "translationX",
                0, -UIUtil.dip2px(context, 200));
        translationX.setDuration(1000);
        translationX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                list.remove(0);
                if (list.size() > 0) {
                    startAnimal(ll);
                } else {
                    isPlay = false;
                    tvEnter.setSelected(false);
                    ll.setVisibility(View.GONE);
                }
            }
        });
        translationX.start();
    }
}
