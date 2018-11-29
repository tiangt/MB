package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.RoundImageView;
import com.whzl.mengbi.util.BitmapUtils;
import com.whzl.mengbi.util.QRCodeUtils;
import com.whzl.mengbi.util.ShareUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.11.28
 */
public class AnchorCardActivity extends BaseActivity {

    @BindView(R.id.btn_close)
    ImageButton btnClose;
    @BindView(R.id.iv_host_cover)
    RoundImageView ivHostCover; //主播封面
    @BindView(R.id.iv_host_avatar)
    CircleImageView ivHostAvatar;
    @BindView(R.id.tv_host_name)
    TextView tvHostName;
    @BindView(R.id.tv_room_num)
    TextView tvRoomNum;
    @BindView(R.id.iv_qr_code)
    RoundImageView ivQRCode;
    @BindView(R.id.tv_weixin_circle)
    TextView tvWeixinCircle;
    @BindView(R.id.tv_weixin_friend)
    TextView tvWeixin;
    @BindView(R.id.tv_weibo)
    TextView tvWeibo;
    @BindView(R.id.tv_qq)
    TextView tvQQ;
    @BindView(R.id.tv_qq_zone)
    TextView tvZone;
    @BindView(R.id.rl_share_card)
    RelativeLayout shareCardLayout;

    private int mProgramId;
    private String mHostName;
    private int mAnchorId;
    private String mHostAvatar;
    private String mAnchorCover;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_anchor_card);
    }

    @Override
    protected void setupView() {
        mProgramId = getIntent().getIntExtra("programId", 0);
        mHostName = getIntent().getStringExtra("hostName");
        mAnchorId = getIntent().getIntExtra("anchorId", 0);
        mAnchorCover = getIntent().getStringExtra("anchorCover");
        mHostAvatar = ImageUrl.getAvatarUrl(mAnchorId, "png", System.currentTimeMillis());
        tvHostName.setText(mHostName);
        tvRoomNum.setText(getString(R.string.search_room, mProgramId));
        //头像
        GlideImageLoader.getInstace().displayImage(this, mHostAvatar, ivHostAvatar);
        //背景
        GlideImageLoader.getInstace().displayImage(this, mAnchorCover, ivHostCover);
        setQRCode();
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_close, R.id.tv_weixin_circle, R.id.tv_weixin_friend
            , R.id.tv_weibo, R.id.tv_qq, R.id.tv_qq_zone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.tv_weixin_circle:
                getLayoutToBitmap(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.tv_weixin_friend:
                getLayoutToBitmap(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tv_weibo:
                getLayoutToBitmap(SHARE_MEDIA.SINA);
                break;
            case R.id.tv_qq:
                getLayoutToBitmap(SHARE_MEDIA.QQ);
                break;
            case R.id.tv_qq_zone:
                getLayoutToBitmap(SHARE_MEDIA.QZONE);
                break;
            default:
                break;
        }
    }

    /**
     * 二维码
     */
    private void setQRCode() {
        try {
            Bitmap bitmap = QRCodeUtils.create2DCode("12312fgfdgdgfggggggggggggggggggggggggggggggggggggggggggggggsdfsfdsfsdfsfs312312313123132");
            Glide.with(this).load(bitmap).into(ivQRCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转布局为Bitmap
     *
     * @param platform
     */
    private void getLayoutToBitmap(SHARE_MEDIA platform) {
        shareCardLayout.findViewById(R.id.btn_close).setVisibility(View.INVISIBLE);
        Bitmap bitmap = BitmapUtils.getViewBitmap(shareCardLayout);
        ShareUtils.shareBitmap(this, bitmap, platform);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnClose.setVisibility(View.VISIBLE);
    }
}
