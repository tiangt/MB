package com.whzl.mengbi.ui.activity.me;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.GetVipPriceBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class SendVipActivity extends BaseActivity {
    @BindView(R.id.et_id_search)
    EditText etIdSearch;
    @BindView(R.id.iv_search_success)
    ImageView ivSearchSuccess;
    @BindView(R.id.tv_search_fail)
    TextView tvSearchFail;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_buy_vip)
    Button btnBuyVip;
    private GetVipPriceBean priceBean;
    private boolean idIsOk = false;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_send_vip, "赠送VIP", true);
    }

    @Override
    protected void setupView() {
//        etIdSearch.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                tvSearchFail.setVisibility(View.GONE);
//                if (TextUtils.isEmpty(etIdSearch.getText().toString())) {
//                    return false;
//                }
//                BusinessUtils.getUserInfo(this, etIdSearch.getText().toString().trim(), new BusinessUtils.UserInfoListener() {
//                    @Override
//                    public void onSuccess(UserInfo.DataBean bean) {
//                        ivSearchSuccess.setVisibility(View.VISIBLE);
//                        tvNick.setText(bean.getNickname());
//                        idIsOk = true;
//                    }
//
//                    @Override
//                    public void onError(int code) {
//                        tvSearchFail.setVisibility(View.VISIBLE);
//                        idIsOk = false;
//                    }
//                });
//            }
//            return false;
//        });

        etIdSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                tvSearchFail.setVisibility(View.GONE);
//                ivSearchSuccess.setVisibility(View.GONE);
//                tvNick.setText("");
                if (s.toString().length() == 0) {
                    tvSearchFail.setVisibility(View.GONE);
                    return;
                }
                BusinessUtils.getUserInfo(SendVipActivity.this, etIdSearch.getText().toString().trim(), new BusinessUtils.UserInfoListener() {

                    @Override
                    public void onSuccess(UserInfo.DataBean bean) {
                        tvSearchFail.setVisibility(View.GONE);
                        ivSearchSuccess.setVisibility(View.VISIBLE);
                        tvNick.setText(bean.getNickname());
                        idIsOk = true;
                    }

                    @Override
                    public void onError(int code) {
                        tvSearchFail.setVisibility(View.VISIBLE);
                        tvNick.setText("");
                        ivSearchSuccess.setVisibility(View.GONE);
                        idIsOk = false;
                    }
                });
            }
        });
    }

    @Override
    protected void loadData() {
        HashMap paramsMap = new HashMap();
        ApiFactory.getInstance().getApi(Api.class)
                .getVipprices(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetVipPriceBean>(SendVipActivity.this) {

                    @Override
                    public void onSuccess(GetVipPriceBean bean) {
                        priceBean = bean;
                        tvPrice.setText(String.valueOf(bean.prices.month.rent));
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }


    @OnClick({R.id.btn_buy_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buy_vip:
//                if (idIsOk) {
//                    sendVip();
//                } else {
//                    tvSearchFail.setVisibility(View.VISIBLE);
//                }
                if (TextUtils.isEmpty(etIdSearch.getText())) {
                    return;
                }
                if (!idIsOk) {
                    return;
                }
                sendVip();
                break;
        }
    }

    private void sendVip() {
        if (TextUtils.isEmpty(etIdSearch.getText().toString())) {
            return;
        }
        BusinessUtils.mallBuy(this, String.valueOf(SPUtils.get(this, SpConfig.KEY_USER_ID, 0L)), String.valueOf(priceBean.goodsId)
                , String.valueOf(priceBean.prices.month.priceId), "1", etIdSearch.getText().toString()
                , "", new BusinessUtils.MallBuyListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.toastMessage(SendVipActivity.this, "赠送成功");
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN &&
//                getCurrentFocus() != null &&
//                getCurrentFocus().getWindowToken() != null) {
//
//            View v = getCurrentFocus();
//            if (isShouldHideKeyboard(v, event)) {
//                hideKeyboard(v.getWindowToken());
//                tvSearchFail.setVisibility(View.GONE);
//                if (TextUtils.isEmpty(etIdSearch.getText().toString())) {
//                    return false;
//                }
//                BusinessUtils.getUserInfo(this, etIdSearch.getText().toString().trim(), new BusinessUtils.UserInfoListener() {
//                    @Override
//                    public void onSuccess(UserInfo.DataBean bean) {
//                        ivSearchSuccess.setVisibility(View.VISIBLE);
//                        tvNick.setText(bean.getNickname());
//                        idIsOk = true;
//                    }
//
//                    @Override
//                    public void onError(int code) {
//                        tvSearchFail.setVisibility(View.VISIBLE);
//                        idIsOk = false;
//                    }
//                });
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }
//
//    /**
//     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
//     */
//    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] l = {0, 0};
//            v.getLocationOnScreen(l);
//            int left = l[0],
//                    top = l[1],
//                    bottom = top + v.getHeight(),
//                    right = left + v.getWidth();
//            return !(event.getRawX() > left && event.getRawX() < right
//                    && event.getRawY() > top && event.getRawY() < bottom);
//        }
//        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
//        return false;
//    }
//
//    /**
//     * 获取InputMethodManager，隐藏软键盘
//     */
//    private void hideKeyboard(IBinder token) {
//        if (token != null) {
//            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            mInputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
}
