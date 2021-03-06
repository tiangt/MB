package com.whzl.mengbi.ui.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.PkRecordContract;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;
import com.whzl.mengbi.model.entity.QueryBagByGoodsTypeBean;
import com.whzl.mengbi.presenter.PkRecordPresenter;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/12/28
 */
public class PkRecordActivity extends BaseActivity<PkRecordPresenter> implements PkRecordContract.View {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.iv_lever)
    ImageView ivLever;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.tv_victory_time)
    TextView tvVictoryTime;
    @BindView(R.id.tv_rate_time)
    TextView tvRateTime;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.tv_num_record)
    TextView tvNumRecord;
    @BindView(R.id.tv_time_black_room)
    TextView tvTimeBlack;
    @BindView(R.id.tv_save_black_room)
    TextView tvSaveBlack;
    @BindView(R.id.tv_card_black_room)
    TextView tvCardBlack;
    @BindView(R.id.tv_state_black)
    TextView tvStateBlack;
    @BindView(R.id.ll_state_black)
    LinearLayout llStateBlack;
    @BindView(R.id.ll_state)
    LinearLayout llState;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int anchorLever;
    private int anchorId;
    private String anchorName;
    private String anchorAvatar;
    private List<PkRecordListBean.ListBean> list = new ArrayList<>();
    private BaseListAdapter baseListAdapter;
    private int totalHours = 0;
    private boolean isAll = false;
    private int editHours = 0;
    private Long userId;
    private List<QueryBagByGoodsTypeBean.ListBean> mDatas = new ArrayList<>();
    private QueryBagByGoodsTypeBean.ListBean selectCard;

    @Override
    protected void initEnv() {
        super.initEnv();
        anchorLever = getIntent().getIntExtra("anchorLever", 0);
        anchorId = getIntent().getIntExtra("anchorId", 0);
        anchorName = getIntent().getStringExtra("anchorName");
        anchorAvatar = getIntent().getStringExtra("anchorAvatar");
        mPresenter = new PkRecordPresenter();
        mPresenter.attachView(this);
        userId = (Long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_pkrecord, "PK战绩", "说明", true);
    }

    @Override
    protected void setupView() {
        GlideImageLoader.getInstace().displayImage(this, anchorAvatar, ivAvatar);
        ivAvatar.setBorderWidth(UIUtil.dip2px(this, 1.5f));
        ivAvatar.setBorderColor(Color.parseColor("#ffda69"));
        tvName.setText(anchorName);
        GlideImageLoader.getInstace().displayImage(this, ResourceMap.getResourceMap().getAnchorLevelIcon(anchorLever), ivLever);
        initRv();
        initEvent();
    }

    private void initEvent() {
        showSaveDialog();
        showCardDialog();
    }

    private void showCardDialog() {
        tvCardBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AwesomeDialog.init().setLayoutId(R.layout.dialog_card_black_room)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                                if (mDatas.isEmpty()) {
                                    holder.setText(R.id.tv_card_black_room, "您暂无解救卡");
                                } else {
                                    holder.setText(R.id.tv_card_black_room, "请选择解救卡");
                                }
                                holder.setOnClickListener(R.id.tv_cancel_card_black_room, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismissDialog();
                                    }
                                });

                                holder.setOnClickListener(R.id.tv_card_black_room, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mDatas.isEmpty()) {
                                            return;
                                        }
                                        showCardList(holder.getView(R.id.tv_card_black_room));
                                    }
                                });

                                holder.setOnClickListener(R.id.tv_save_card_black_room, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mDatas.isEmpty() || selectCard == null) {
                                            return;
                                        }
                                        mPresenter.rescure(userId, anchorId, -1, selectCard.goodsId);
                                        dialog.dismissDialog();
                                    }
                                });
                            }
                        })
                        .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                        .show(getSupportFragmentManager());
            }
        });
    }

    private void showCardList(TextView view) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = mDatas.get(options1).goodsName;
            view.setText(tx);
            selectCard = mDatas.get(options1);
        })
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(13)
                .setSubmitText("完成")//确认按钮文字
                .setTitleSize(15)//标题文字大小
                .setTitleText("解救卡")//标题文字
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setTitleBgColor(Color.WHITE)
                .setLineSpacingMultiplier(2)
                .setTitleColor(Color.parseColor("#70000000"))//标题文字颜色
                .setSubmitColor(Color.parseColor("#ff2b3f"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#70000000"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(15)
                .setTextColorCenter(Color.BLACK)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvOptions.setPicker(mDatas);
        Dialog mDialog = pvOptions.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    UIUtil.getScreenWidthPixels(this),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvOptions.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.5f);
            }
        }
        pvOptions.show();

    }

    private void showSaveDialog() {
        tvSaveBlack.setOnClickListener(v -> AwesomeDialog.init().setLayoutId(R.layout.dialog_black_room)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                        TextView tvAll = holder.getView(R.id.tv_all);
                        LinearLayout llHours = holder.getView(R.id.ll_hours);
                        EditText etHours = holder.getView(R.id.et_hours);
                        etHours.setText(totalHours + "");
                        tvAll.setSelected(true);

                        tvAll.setOnClickListener(v1 -> {
                            KeyBoardUtil.closeKeybord(etHours, PkRecordActivity.this);
                            tvAll.setSelected(true);
                            llHours.setSelected(false);

                        });
                        llHours.setOnClickListener(v12 -> {
                            tvAll.setSelected(false);
                            llHours.setSelected(true);
                        });
                        etHours.setOnClickListener(v13 -> {
                            tvAll.setSelected(false);
                            llHours.setSelected(true);
                        });

                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.tv_save, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(etHours.getText())) {
                                    return;
                                }
                                String trim = etHours.getText().toString().trim();
                                editHours = Integer.parseInt(trim);
                                if (editHours == 0 || editHours > totalHours) {
                                    return;
                                }
                                if (tvAll.isSelected()) {
                                    isAll = true;
                                    mPresenter.rescure((long) SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L),
                                            anchorId, totalHours, -1);
                                    KeyBoardUtil.closeKeybord(etHours, PkRecordActivity.this);
                                    dialog.dismiss();
                                    return;
                                }
                                isAll = false;
                                mPresenter.rescure((long) SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L),
                                        anchorId, editHours, -1);
                                KeyBoardUtil.closeKeybord(etHours, PkRecordActivity.this);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .show(getSupportFragmentManager()));
    }

    private void initRv() {
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        baseListAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(PkRecordActivity.this).inflate(R.layout.item_history_pk_record, parent, false);
                return new Holder(itemView);
            }
        };
        rvHistory.setAdapter(baseListAdapter);
    }

    class Holder extends BaseViewHolder {
        @BindView(R.id.iv_result)
        ImageView ivResult;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_avatar_pk)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_lever)
        ImageView ivLever;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            PkRecordListBean.ListBean bean = list.get(position);
            switch (bean.result) {
                case "T":
                    GlideImageLoader.getInstace().displayImage(PkRecordActivity.this, R.drawable.ic_tie_pk_record, ivResult);
                    break;
                case "V":
                    GlideImageLoader.getInstace().displayImage(PkRecordActivity.this, R.drawable.ic_win_pk_record, ivResult);
                    break;
                case "F":
                    GlideImageLoader.getInstace().displayImage(PkRecordActivity.this, R.drawable.ic_lose_pk_record, ivResult);
                    break;
            }
            tvTime.setText(bean.date);
            tvName.setText(bean.nickname);
            GlideImageLoader.getInstace().displayImage(PkRecordActivity.this,
                    ResourceMap.getResourceMap().getAnchorLevelIcon(bean.anchorLevel), ivLever);
            long millis = DateUtils.dateStrToMillis(bean.lastUpdateTime, "yyyy-MM-dd HH:mm:ss");
            String png = ImageUrl.getAvatarUrl(bean.userId, "png", millis);
            GlideImageLoader.getInstace().displayImage(PkRecordActivity.this, png, ivAvatar);
        }
    }

    @Override
    protected void loadData() {
        mPresenter.getPkTime(anchorId);
        mPresenter.getPkRecordList(anchorId);
        mPresenter.getRoomTime(anchorId);
        mPresenter.getCardList(userId, "BLACK_CARD");
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        showPopWindow();
    }


    private void showPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_pk_record, null);
        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(toolbar, 0, -UIUtil.dip2px(PkRecordActivity.this, 8));
    }

    @Override
    public void onGetPkTimes(PkTimeBean bean) {
        double total = (bean.victoryTimes + bean.tieTimes + bean.failureTimes);
        tvTotalTime.setText(String.valueOf((int) total));
        tvVictoryTime.setText((int) bean.victoryTimes + "");
        if (total == 0) {
            tvRateTime.setText("0%");
        } else {
            DecimalFormat df = new DecimalFormat("0.0");//格式化小数
            String s = df.format(bean.victoryTimes / total * 100);//返回的是String类型
            tvRateTime.setText(s + "%");
        }
    }

    @Override
    public void getPkList(PkRecordListBean bean) {
        if (bean == null || bean.list == null) {
            return;
        }
        tvNumRecord.setText(getString(R.string.history_record, bean.list.size()));
        list.addAll(bean.list);
        baseListAdapter.notifyDataSetChanged();
    }

    @Override
    public void getRoomTime(BlackRoomTimeBean bean) {
        double l = bean.time / 60 / 60;
        totalHours = (int) Math.ceil(l);
        if (totalHours == 0) {
            llStateBlack.setVisibility(View.GONE);
            llState.setBackgroundResource(R.drawable.bg_black_room_normal);
            tvStateBlack.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tvStateBlack.setTextColor(Color.parseColor("#f59362"));
            tvStateBlack.setText("当前战绩良好，请继续加油!");
            return;
        }
        llStateBlack.setVisibility(View.VISIBLE);
        llState.setBackgroundResource(R.drawable.bg_black_room);
        tvTimeBlack.setText(totalHours + "小时");
        tvStateBlack.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tvStateBlack.setTextColor(Color.parseColor("#aaaaaa"));
        tvStateBlack.setText("战绩不佳,已被关小黑屋，无法继续PK");
    }

    @Override
    public void onRescure() {
        if (selectCard == null) {
            ToastUtils.showRedToast(this, isAll ? "恭喜您成功解救出主播" : "成功解救" + editHours + "小时");
            mPresenter.getRoomTime(anchorId);
        } else {
            ToastUtils.showRedToast(this, "成功解救" + selectCard.effSecond / 3600 + "小时");
            mPresenter.getCardList(userId, "BLACK_CARD");
            mPresenter.getRoomTime(anchorId);
            selectCard = null;
        }
    }

    @Override
    public void onGetCardList(QueryBagByGoodsTypeBean bean) {
        mDatas.clear();
        mDatas.addAll(bean.list);
    }

}
