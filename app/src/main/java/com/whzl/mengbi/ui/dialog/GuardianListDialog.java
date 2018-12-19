package com.whzl.mengbi.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.BaseFullScreenDialog;
import com.whzl.mengbi.ui.dialog.base.GuardDetailDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 守护列表
 *
 * @author cliang
 * @date 2018.12.17
 */
public class GuardianListDialog extends BaseFullScreenDialog {

    @BindView(R.id.ib_back)
    ImageButton mIbBack;
    @BindView(R.id.tv_guard_title)
    TextView tvGuardTitle;
    @BindView(R.id.rv_guard)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_open_guard)
    Button mBtnGuard;
    @BindView(R.id.tv_empty_text)
    TextView tvEmpty;

    private int mProgramId;
    private BaseListAdapter mAdapter;
    private RoomInfoBean.DataBean.AnchorBean mAnchorBean;
    private ArrayList<GuardListBean.GuardDetailBean> mData = new ArrayList<>();

    public static GuardianListDialog newInstance(int programId, RoomInfoBean.DataBean.AnchorBean anchorBean) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        args.putParcelable("anchor", anchorBean);
        GuardianListDialog dialog = new GuardianListDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_guardian_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseFullScreenDialog dialog) {
        mProgramId = getArguments().getInt("programId", 0);
        mAnchorBean = getArguments().getParcelable("anchor");
        getData();
        initRecycler();
    }

    @OnClick({R.id.ib_back, R.id.btn_open_guard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                dismiss();
                break;
            case R.id.btn_open_guard:
                dismiss();
                if (ClickUtil.isFastClick()) {
                    GuardDetailDialog.newInstance(mProgramId, mAnchorBean)
                            .setShowBottom(true)
                            .setDimAmount(0)
                            .show(getFragmentManager());
                }
                break;
            default:
                break;
        }
    }

    private void getData() {
        HashMap paramsMap = new HashMap();
        paramsMap.put("programId", mProgramId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(paramsMap);
        ApiFactory.getInstance().getApi(Api.class)
                .getGuardList(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GuardListBean>() {
                    @Override
                    public void onSuccess(GuardListBean guardListBean) {
                        if (guardListBean != null) {
                            tvEmpty.setVisibility(View.GONE);
                            tvGuardTitle.setText(getString(R.string.guardian_count, guardListBean.list.size()));
                            mData.clear();
                            mData.addAll(guardListBean.list);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                            tvEmpty.setText(getString(R.string.empty_guard_list));
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void initRecycler() {
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.color.tran_white2));
        mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mRecyclerView.setFocusableInTouchMode(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(divider);
        mAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mData == null ? 0 : mData.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_guardian, parent, false);
                return new GuardianViewHolder(itemView);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    class GuardianViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.iv_level_icon)
        ImageView ivLevelIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_expire)
        TextView tvExpire;


        public GuardianViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GuardListBean.GuardDetailBean guardDetailBean = mData.get(position);
            ivAvatar.setAlpha(guardDetailBean.isOnline == 1 ? 1f : 0.5f);
            GlideImageLoader.getInstace().displayImage(getContext(), guardDetailBean.avatar, ivAvatar);
            int userLevelIcon = ResourceMap.getResourceMap().getUserLevelIcon(guardDetailBean.userLevel);
            ivLevelIcon.setImageResource(userLevelIcon);
            tvName.setText(guardDetailBean.nickName);
            tvName.setAlpha(guardDetailBean.isOnline == 1 ? 1f : 0.5f);
            tvExpire.setText("剩余 ");
            SpannableString spannableString = StringUtils.spannableStringColor(guardDetailBean.surplusDays + "", Color.parseColor("#f1275b"));
            tvExpire.append(spannableString);
            tvExpire.append(" 天");
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            GuardListBean.GuardDetailBean guardDetailBean = mData.get(position);
            if (ClickUtil.isFastClick()) {
                if (getActivity() != null) {
                    ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(guardDetailBean.userId, true);
                }
            }
        }
    }

    private OnClickListener listener;

    public interface OnClickListener {
        void onGuardianClick();
    }

    public BaseFullScreenDialog setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

}
