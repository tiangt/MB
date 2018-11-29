package com.whzl.mengbi.ui.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.RoundImageView;
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
    ImageView ivQRCode;
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

    private int mProgramId;
    private String mHostName;
    private int mAnchorId;
    private String mHostAvatar;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_anchor_card);
    }

    @Override
    protected void setupView() {
        mProgramId = getIntent().getIntExtra("programId", 0);
        mHostName = getIntent().getStringExtra("hostName");
        mAnchorId = getIntent().getIntExtra("anchorId", 0);
        mHostAvatar = ImageUrl.getAvatarUrl(mAnchorId, "png", System.currentTimeMillis());
        tvHostName.setText(mHostName);
        tvRoomNum.setText(getString(R.string.search_room, mProgramId));
        //头像
        GlideImageLoader.getInstace().displayImage(this, mHostAvatar, ivHostAvatar);
        //背景
        GlideImageLoader.getInstace().displayImage(this, mHostAvatar, ivHostCover);
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
                break;
            case R.id.tv_weixin_friend:
                break;
            case R.id.tv_weibo:
                break;
            case R.id.tv_qq:
                break;
            case R.id.tv_qq_zone:
                break;
            default:
                break;
        }
    }
}
