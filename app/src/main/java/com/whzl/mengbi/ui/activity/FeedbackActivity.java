package com.whzl.mengbi.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.JsonElement;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.ImgUploadBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.Glide4Engine;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author shaw
 * @date 2018/8/7
 */
public class FeedbackActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 103;
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.tv_time_feed_back)
    TextView tvTimeFeedBack;
    @BindView(R.id.tv_limit_feed_back)
    TextView tvLimit;
    @BindView(R.id.rv_pic_feed_back)
    RecyclerView recyclerView;


    private TimePickerView pvTime;
    List<String> mSelected = new ArrayList<>();
    List<File> files = new ArrayList<>();
    private BaseListAdapter adapter;
    private int upImgCount = 0;
    private StringBuffer pictureIds = new StringBuffer("");

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_feed_back_layout, "意见反馈", "提交", true);
    }

    @Override
    protected void setupView() {
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));
        tvTimeFeedBack.setText(DateUtils.getStringDate());
        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLimit.setText(s.toString().length() + "/100");
            }
        });
        initRV();
    }

    private void initRV() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new BaseListAdapter() {

            @Override
            protected int getDataCount() {
                return files.size() >= 3 ? 3 : files.size() + 1;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(FeedbackActivity.this).inflate(R.layout.item_feed_back, parent, false);
                return new FootViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    class FootViewHolder extends BaseViewHolder {

        private final ImageView imageView;
        private final ImageView delete;

        public FootViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_feed_back);
            delete = itemView.findViewById(R.id.iv_del_item_feed_back);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (position == files.size()) {
                delete.setVisibility(View.GONE);
                GlideImageLoader.getInstace().displayImage(FeedbackActivity.this, R.drawable.ic_item_feed_back, imageView);
            } else {
                delete.setVisibility(View.VISIBLE);
                GlideImageLoader.getInstace().loadRoundImage(FeedbackActivity.this, files.get(position).getAbsolutePath(), imageView, 2);
                delete.setOnClickListener(v -> {
                    files.remove(position);
                    adapter.notifyDataSetChanged();
                });
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (position == files.size()) {
                selectPic();
            }
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        KeyBoardUtil.hideInputMethod(this);
        String content = etFeedback.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showToastUnify(this, "请输入反馈内容");
            return;
        }

        showLoading("请稍后...");
        imgUpoad(files);
    }

    private void imgUpoad(List<File> files) {
        if (files.size() == 0) {
            feedback("");
            return;
        }
        for (int i = 0; i < files.size(); i++) {
            upload(files.get(i));
        }
    }

    private void upload(File file) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(this, SpConfig.KEY_USER_ID, 0L).toString());
//        File file = new File(file2.getAbsolutePath()+".jpg");
        LogUtils.e("ssssssss    " + file.getAbsolutePath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("default", file.getName(), requestFile);
        ApiFactory.getInstance().getApi(Api.class)
                .imgUpload(ParamsUtils.getMultiParamsMap(paramsMap), part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ImgUploadBean>() {

                    @Override
                    public void onSuccess(ImgUploadBean bean) {
                        upImgCount += 1;
                        if (upImgCount == 1) {
                            pictureIds.append(String.valueOf(bean.filedId));
                        } else {
                            pictureIds.append(",").append(String.valueOf(bean.filedId));
                        }

                        if (upImgCount == files.size()) {
                            feedback(pictureIds.toString());
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void feedback(String s) {
        long userId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        paramsMap.put("content", etFeedback.getText().toString().trim());
        paramsMap.put("contact", etContact.getText().toString().trim());
        paramsMap.put("happenTime", tvTimeFeedBack.getText().toString().trim());
        paramsMap.put("pictureIds", s);
        ApiFactory.getInstance().getApi(Api.class)
                .feedback(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToastUnify(FeedbackActivity.this, "提交成功");
                        dismissLoading();
                        finish();
                    }


                    @Override
                    public void onError(ApiResult<JsonElement> body) {
                        upImgCount = 0;
                        ToastUtils.showToastUnify(FeedbackActivity.this, body.msg);
                        dismissLoading();
                    }
                });
    }


    @OnClick({R.id.rl_time_feed_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_time_feed_back:
                showDatePick();
                break;
        }
    }

    private void selectPic() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Matisse.from(FeedbackActivity.this)
                                    .choose(MimeType.ofImage())
                                    .theme(R.style.Matisse_Dracula)
                                    .countable(true)
                                    .maxSelectable(3 - files.size())
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new Glide4Engine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        } else {
                            ToastUtils.showToast(R.string.permission_storage_denid);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainPathResult(data);
            LogUtils.e("sssssssss   " + mSelected);
            RxPermissions rxPermissions = new RxPermissions(FeedbackActivity.this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                withLs(mSelected);
                            } else {
                                ToastUtils.showToast(R.string.permission_storage_denid);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private <T> void withLs(final List<T> photos) {
        Luban.with(this)
                .load(photos)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .setFocusAlpha(false)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
//                .setRenameListener(new OnRenameListener() {
//                    @Override
//                    public String rename(String filePath) {
//                        try {
//                            MessageDigest md = MessageDigest.getInstance("MD5");
//                            md.update(filePath.getBytes());
//                            return new BigInteger(1, md.digest()).toString(32);
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        }
//                        return "";
//                    }
//                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        files.add(file);
                        LogUtils.e("ssssssss   onSuccess" + file.getAbsolutePath());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    private void showDatePick() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvTimeFeedBack.setText(DateUtils.getTime2(date));
            }
        })

                .setDate(calendar)
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(13)
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(15)//标题文字大小
                .setTitleText("出生日期")//标题文字
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(2)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.parseColor("#70000000"))//标题文字颜色
                .setSubmitColor(Color.parseColor("#ff2b3f"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#70000000"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(15)
                .setTextColorCenter(Color.BLACK)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvTime.show();

    }
}
