package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.ui.activity.AnchorCardActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.ClipboardUtils;
import com.whzl.mengbi.util.ShareUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.11.28
 */
public class ShareDialog extends BaseAwesomeDialog {

    @BindView(R.id.tv_host_card)
    TextView mHostCard;
    @BindView(R.id.tv_weixin_circle)
    TextView mWeixinCircle;
    @BindView(R.id.tv_weixin_friend)
    TextView mWeixinFriend;
    @BindView(R.id.tv_weibo)
    TextView mWeibo;
    @BindView(R.id.tv_qq)
    TextView mQQ;
    @BindView(R.id.tv_qq_zone)
    TextView mZone;
    @BindView(R.id.tv_copy)
    TextView mCopyLink;
    @BindView(R.id.tv_cancel)
    TextView mCancel;

    private int mProgramId;
    private int anchorId;
    private RoomInfoBean.DataBean.AnchorBean mAnchorBean;
    private String strHostName;
    private String mAnchorCover;
    private String mShareUrl;

    public static BaseAwesomeDialog newInstance(int programId, RoomInfoBean.DataBean.AnchorBean anchorBean, String anchorCover, String shareUrl) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        args.putParcelable("anchor", anchorBean);
        args.putString("anchorCover", anchorCover);
        args.putString("shareUrl", shareUrl);
        ShareDialog dialog = new ShareDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mProgramId = getArguments().getInt("programId");
        mAnchorBean = getArguments().getParcelable("anchor");
        mAnchorCover = getArguments().getString("anchorCover", "");
        mShareUrl = getArguments().getString("shareUrl", "");
        strHostName = mAnchorBean.getName();
        anchorId = mAnchorBean.getId();
        if (TextUtils.isEmpty(mAnchorCover)) {
            mAnchorCover = ImageUrl.getAvatarUrl(anchorId, "png", System.currentTimeMillis());
        }
    }

    @OnClick({R.id.tv_host_card, R.id.tv_weixin_circle, R.id.tv_weixin_friend,
            R.id.tv_weibo, R.id.tv_qq, R.id.tv_qq_zone, R.id.tv_copy, R.id.tv_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_host_card:
                ShareCardDialog.newInstance(mProgramId, strHostName, anchorId, mAnchorCover, mShareUrl)
                        .show(getActivity().getSupportFragmentManager());
                dismiss();
                break;
            case R.id.tv_weixin_circle:
                ShareUtils.shareWeb(getActivity(), mShareUrl, mAnchorCover
                        , getString(R.string.share_outside_title, strHostName)
                        , getString(R.string.share_outside_decs, strHostName)
                        , SHARE_MEDIA.WEIXIN_CIRCLE);
                dismiss();
                break;
            case R.id.tv_weixin_friend:
                ShareUtils.shareWeb(getActivity(), mShareUrl, mAnchorCover
                        , getString(R.string.share_outside_title, strHostName)
                        , getString(R.string.share_outside_decs, strHostName)
                        , SHARE_MEDIA.WEIXIN);
                dismiss();
                break;
            case R.id.tv_weibo:
                ShareUtils.shareWeb(getActivity(), mShareUrl, mAnchorCover
                        , getString(R.string.share_outside_title, strHostName)
                        , getString(R.string.share_outside_decs, strHostName)
                        , SHARE_MEDIA.SINA);
                dismiss();
                break;
            case R.id.tv_qq:
                ShareUtils.shareWeb(getActivity(), mShareUrl, mAnchorCover
                        , getString(R.string.share_outside_title, strHostName)
                        , getString(R.string.share_outside_decs, strHostName)
                        , SHARE_MEDIA.QQ);
                dismiss();
                break;
            case R.id.tv_qq_zone:
                ShareUtils.shareWeb(getActivity(), mShareUrl, mAnchorCover
                        , getString(R.string.share_outside_title, strHostName)
                        , getString(R.string.share_outside_decs, strHostName)
                        , SHARE_MEDIA.QZONE);
                dismiss();
                break;
            case R.id.tv_copy:
                ClipboardUtils.putTextIntoClip(getActivity(), mShareUrl);
                //复制到剪贴板
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}
