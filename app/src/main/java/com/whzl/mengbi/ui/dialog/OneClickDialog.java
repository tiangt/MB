package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 一键赠送
 *
 * @author cliang
 * @date 2018.12.25
 */
public class OneClickDialog extends BaseAwesomeDialog {

    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.tv_gift_count)
    TextView tvGiftCount;
    @BindView(R.id.tv_gift_amount)
    TextView tvGiftAmount;

    private int mProgramId;
    private int mTargetId;
    private int mGoodsId;
    private int mCount;
    private long mUserId;
    private int mGoodsRent;
    private OnClickListener listener;

    public static OneClickDialog newInstance(int programId, int targetId, int goodsId, int count, Long userId, int goodsRent, String goodsName) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        args.putInt("targetId", targetId);
        args.putInt("goodsId", goodsId);
        args.putInt("count", count);
        args.putLong("userId", userId);
        args.putInt("goodsRent", goodsRent);
        args.putString("goodsName", goodsName);
        OneClickDialog dialog = new OneClickDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_one_click;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mProgramId = getArguments().getInt("programId");
        mTargetId = getArguments().getInt("targetId");
        mGoodsId = getArguments().getInt("goodsId");
        mCount = getArguments().getInt("count");
        mUserId = getArguments().getLong("userId");
        mGoodsRent = getArguments().getInt("goodsRent");
        String goodsName = getArguments().getString("goodsName");
        tvGiftCount.setText(getString(R.string.need_goods, mCount, goodsName));
        long amount = mCount * mGoodsRent;
        tvGiftAmount.setText(getString(R.string.need_mengbi, StringUtils.formatNumber(amount)));
    }

    @OnClick({R.id.btn_confirm, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                sendGift();
                break;

            case R.id.btn_cancel:
                dismiss();
                break;

            default:
                break;
        }
    }

    private void sendGift() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("roomId", mProgramId + "");
        paramsMap.put("programId", mProgramId + "");
        paramsMap.put("targetId", mTargetId + "");
        paramsMap.put("goodsId", mGoodsId + "");
        paramsMap.put("count", mCount + "");
        paramsMap.put("userId", mUserId + "");
        paramsMap.put("runwayShowMark", "T");
        paramsMap.put("runwayAppend", "");
        paramsMap.put("useBag", false + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_GIFT, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    if (listener != null) {
                        listener.onSendSuccess();
                    }
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), responseInfo.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.e(errorMsg);
            }
        });
    }


    public interface OnClickListener {
        void onSendSuccess();
    }

    public BaseAwesomeDialog setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }
}
