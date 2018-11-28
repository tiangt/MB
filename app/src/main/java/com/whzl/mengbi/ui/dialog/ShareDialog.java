package com.whzl.mengbi.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.AnchorCardActivity;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.ShareUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.11.28
 */
public class ShareDialog extends BaseAwesomeDialog implements View.OnClickListener {

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

    public static BaseAwesomeDialog newInstance() {
        Bundle bundle = new Bundle();
        ShareDialog dialog = new ShareDialog();
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

    }

    @OnClick({R.id.tv_host_card, R.id.tv_weixin_circle, R.id.tv_weixin_friend,
            R.id.tv_weibo, R.id.tv_qq, R.id.tv_qq_zone, R.id.tv_copy, R.id.tv_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_host_card:
                Intent intent = new Intent(getActivity(), AnchorCardActivity.class);
                startActivity(intent);
                dismiss();
                break;
            case R.id.tv_weixin_circle:
                ShareUtils.shareWeb(getActivity(), "https://www.mengbitv.com", "http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480", "我正在偷偷观看 主播昵称 的直播！", "主播昵称 正在性感热舞中，快去瞧瞧吧！"
                        , SHARE_MEDIA.WEIXIN_CIRCLE);
                dismiss();
                break;
            case R.id.tv_weixin_friend:
                ShareUtils.shareWeb(getActivity(), "https://www.mengbitv.com", "http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480", "我正在偷偷观看 主播昵称 的直播！", "主播昵称 正在性感热舞中，快去瞧瞧吧！"
                        , SHARE_MEDIA.WEIXIN);
                dismiss();
                break;
            case R.id.tv_weibo:
                ShareUtils.shareWeb(getActivity(), "https://www.mengbitv.com", "http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480", "我正在偷偷观看 主播昵称 的直播！", "主播昵称 正在性感热舞中，快去瞧瞧吧！"
                        , SHARE_MEDIA.SINA);
                dismiss();
                break;
            case R.id.tv_qq:
                ShareUtils.shareWeb(getActivity(), "https://www.mengbitv.com", "http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480", "我正在偷偷观看 主播昵称 的直播！", "主播昵称 正在性感热舞中，快去瞧瞧吧！"
                        , SHARE_MEDIA.QQ);
                dismiss();
                break;
            case R.id.tv_qq_zone:
                ShareUtils.shareWeb(getActivity(), "https://www.mengbitv.com", "http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480", "我正在偷偷观看 主播昵称 的直播！", "主播昵称 正在性感热舞中，快去瞧瞧吧！"
                        , SHARE_MEDIA.QZONE);
                dismiss();
                break;
            case R.id.tv_copy:
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
