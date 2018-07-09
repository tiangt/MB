package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.dialog.GiftDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseChatDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class LiveDisplayActivityNew extends BaseAtivity implements LiveView {
    @BindView(R.id.iv_host_avatar)
    CircleImageView ivHostAvatar;
    @BindView(R.id.tv_host_name)
    TextView tvHostName;
    @BindView(R.id.btn_follow)
    Button btnFollow;
    @BindView(R.id.rl_contribution_container)
    RelativeLayout rlContributionContainer;
    @BindView(R.id.player_master)
    SurfaceView playerMaster;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;
    @BindView(R.id.tv_popularity)
    TextView tvPopularity;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    private Unbinder mBind;
    private LivePresenterImpl mLivePresenter;
    private int mProgramId;
    private String mAnchorNickName;
    private int mRoomUserCount;
    private String mCover;
    private String mStream;
    private KSYMediaPlayer mMasterPlayer;
    private int mDisplayWidth;
    private int mDisplayHeight;
    private BaseAwesomeDialog mLiveHouseChatDialog;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private GiftInfo mGiftInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEnv();
        setContentView(R.layout.activity_live_display_new);
        StatusBarUtil.setColor(this, Color.parseColor("#181818"));
        mBind = ButterKnife.bind(this);
        initView();
    }

    private void initEnv() {
        mLivePresenter = new LivePresenterImpl(this);
        chatRoomPresenter = new ChatRoomPresenterImpl();
        if (getIntent() != null) {
            mProgramId = getIntent().getIntExtra("ProgramId", -1);
            SPUtils.put(this, "programId", mProgramId);
            mAnchorNickName = getIntent().getStringExtra("AnchorNickname");
            mRoomUserCount = getIntent().getIntExtra("RoomUserCount", 0);
            mCover = getIntent().getStringExtra("Cover");
            mStream = getIntent().getStringExtra("Stream");
            mDisplayWidth = getIntent().getIntExtra("displayWidth", 4);
            mDisplayHeight = getIntent().getIntExtra("displayHeight", 3);
            mStream = getIntent().getStringExtra("Stream");
        }
        mLivePresenter.getLiveGift();
    }

    private void initView() {
        GlideImageLoader.getInstace().circleCropImage(this, mCover, ivHostAvatar);
        tvHostName.setText(mAnchorNickName);
        tvFansCount.setText("粉丝：" + "");
        tvPopularity.setText("人气\n" + mRoomUserCount);
        initPlayers();
        playerMaster.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                if (mMasterPlayer != null)
                    mMasterPlayer.setSurface(surfaceHolder.getSurface());
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if (mMasterPlayer != null)
                    mMasterPlayer.setSurface(null);
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        getRoomToken();
    }

    private void getRoomToken() {
        int userId = (Integer) SPUtils.get(this, "userId", 0);
        String sessionId = SPUtils.get(this, "sessionId", "0").toString();
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("programId", mProgramId);
        map.put("sessionId", sessionId);
        mLivePresenter.getLiveToken(map);
    }

    private void initPlayers() {
        mMasterPlayer = new KSYMediaPlayer.Builder(this).build();
        mMasterPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (mMasterPlayer != null)
                    mMasterPlayer.start();
            }
        });
        try {
            mMasterPlayer.shouldAutoPlay(false);
            mMasterPlayer.setDataSource(mStream);
            mMasterPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_follow, R.id.btn_contribution_dismiss, R.id.btn_chat, R.id.btn_send_gift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_follow:
                break;
            case R.id.btn_contribution_dismiss:
                break;
            case R.id.btn_chat:
                mLiveHouseChatDialog = LiveHouseChatDialog.newInstance()
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_send_gift:
                if(mGiftInfo != null){
                    GiftDialog.newInstance(mGiftInfo)
                            .setShowBottom(true)
                            .setDimAmount(0)
                            .show(getSupportFragmentManager());
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        mLivePresenter.onDestory();
        if (chatRoomPresenter != null) {
            chatRoomPresenter.disconnectChat();
        }
        if (mMasterPlayer != null)
            mMasterPlayer.release();
        mMasterPlayer = null;
    }


    @Override
    public void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatRoomPresenter.setupConnection(liveRoomTokenInfo);
            }
        }).start();
    }

    @Override
    public void onLiveFaceSuccess(EmjoyInfo emjoyInfo) {

    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        mGiftInfo = giftInfo;
    }
}
