package com.whzl.mengbi.ui.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
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
 * @date 2018.12.4
 */
public class ShareCardDialog extends BaseAwesomeDialog {

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
    private String mShareUrl;

    public static BaseAwesomeDialog newInstance(int programId, String hostName, int anchorId, String anchorCover, String shareUrl) {
        Bundle args = new Bundle();
        args.putInt("programId",programId);
        args.putString("hostName", hostName);
        args.putInt("anchorId",anchorId);
        args.putString("anchorCover",anchorCover);
        args.putString("shareUrl",shareUrl);
        ShareCardDialog dialog = new ShareCardDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_share_card;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mProgramId = getArguments().getInt("programId", 0);
        mHostName = getArguments().getString("hostName");
        mAnchorId = getArguments().getInt("anchorId", 0);
        mAnchorCover = getArguments().getString("anchorCover");
        mShareUrl = getArguments().getString("shareUrl");
        mHostAvatar = ImageUrl.getAvatarUrl(mAnchorId, "png", System.currentTimeMillis());
        tvHostName.setText(mHostName);
        tvRoomNum.setText(getString(R.string.search_room, mProgramId));
        //头像
        GlideImageLoader.getInstace().displayImage(getActivity(), mHostAvatar, ivHostAvatar);
        //背景
        GlideImageLoader.getInstace().displayImage(getActivity(), mAnchorCover, ivHostCover);
        setQRCode();
    }

    @OnClick({R.id.btn_close, R.id.tv_weixin_circle, R.id.tv_weixin_friend
            , R.id.tv_weibo, R.id.tv_qq, R.id.tv_qq_zone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
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
            if (!TextUtils.isEmpty(mShareUrl)) {
                Bitmap bitmap = QRCodeUtils.create2DCode(mShareUrl);
                Glide.with(this).load(bitmap).into(ivQRCode);
            }
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
        ShareUtils.shareBitmap(getActivity(), bitmap, platform);
        dismiss();
    }
}
