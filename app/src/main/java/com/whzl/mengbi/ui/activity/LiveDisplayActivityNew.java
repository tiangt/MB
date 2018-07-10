package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.dialog.GiftDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseChatDialog;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.RatioRelativeLayout;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

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
    @BindView(R.id.rl_host_info)
    RelativeLayout rlHostInfo;
    @BindView(R.id.ratio_layout)
    RatioRelativeLayout ratioLayout;
    @BindView(R.id.btn_chat)
    Button btnChat;
    @BindView(R.id.btn_send_gift)
    ImageButton btnSendGift;
    private Unbinder mBind;
    private LivePresenterImpl mLivePresenter;
    private int mProgramId;
    private KSYMediaPlayer mMasterPlayer;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private GiftInfo mGiftInfo;
    private ArrayList<ChatMessage> chatList = new ArrayList<>();
    private RecyclerView.Adapter chatAdapter;
    private boolean isRecyclerScrolling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEnv();
        setContentView(R.layout.activity_live_display_new);
        StatusBarUtil.setColor(this, Color.parseColor("#181818"));
        mBind = ButterKnife.bind(this);
        initView();
        getDatas();
    }

    private void getDatas() {
        getRoomToken();
        mLivePresenter.getRoomInfo(mProgramId);
        mLivePresenter.getAudienceAccount(mProgramId);
    }

    private void initEnv() {
        mLivePresenter = new LivePresenterImpl(this);
        chatRoomPresenter = new ChatRoomPresenterImpl();
        if (getIntent() != null) {
            mProgramId = getIntent().getIntExtra("ProgramId", -1);
            SPUtils.put(this, "programId", mProgramId);
//            mAnchorNickName = getIntent().getStringExtra("AnchorNickname");
//            mRoomUserCount = getIntent().getIntExtra("RoomUserCount", 0);
//            mCover = getIntent().getStringExtra("Cover");
//            mStream = getIntent().getStringExtra("Stream");
//            mDisplayWidth = getIntent().getIntExtra("displayWidth", 4);
//            mDisplayHeight = getIntent().getIntExtra("displayHeight", 3);
//            mStream = getIntent().getStringExtra("Stream");
        }
        mLivePresenter.getLiveGift();
    }

    private void initView() {
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
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView textView = new TextView(LiveDisplayActivityNew.this);
                textView.setId(R.id.chat_text);
                int paddingTop = UIUtil.dip2px(LiveDisplayActivityNew.this, 10);
                int paddingLeft = UIUtil.dip2px(LiveDisplayActivityNew.this, 15);
                int paddingRight = UIUtil.dip2px(LiveDisplayActivityNew.this, 60);
                textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);
                textView.setTextSize(UIUtil.sp2px(LiveDisplayActivityNew.this, 15));
                return new SingleTextViewHolder(textView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ChatMessage chatMessage = chatList.get(position);
                chatMessage.fillHolder(holder);
            }

            @Override
            public int getItemCount() {
                return chatList == null ? 0 : chatList.size();
            }
        };
        recycler.setAdapter(chatAdapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        isRecyclerScrolling = false;
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case SCROLL_STATE_DRAGGING:
                        isRecyclerScrolling = true;
                        break;
                    case SCROLL_STATE_SETTLING:
                        isRecyclerScrolling = true;
                        break;
                }
            }
        });
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

    private void initPlayers(String stream) {
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
            mMasterPlayer.setDataSource(stream);
            mMasterPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_follow, R.id.btn_contribution_dismiss, R.id.btn_chat, R.id.btn_send_gift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_follow:
                int userId = (int) SPUtils.get(this, "userId", -1);
                mLivePresenter.fellowHost(userId, mProgramId);
                break;
            case R.id.btn_contribution_dismiss:
                break;
            case R.id.btn_chat:
                LiveHouseChatDialog.newInstance()
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_send_gift:
                if (mGiftInfo != null) {
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
        new Thread(() -> chatRoomPresenter.setupConnection(liveRoomTokenInfo, LiveDisplayActivityNew.this)).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KickoutEvent kickoutEvent) {
        // TODO: 2018/7/10
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LuckGiftEvent luckGiftEvent) {
        // TODO: 2018/7/10  
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdatePubChatEvent updatePubChatEvent) {
        FillHolderMessage message = updatePubChatEvent.getMessage();
        chatList.add(((ChatMessage) message));
        if (!isRecyclerScrolling) {
            chatAdapter.notifyDataSetChanged();
            recycler.smoothScrollToPosition(chatList.size() - 1);
        }
    }


    @Override
    public void onRoomInfoSuccess(RoomInfoBean roomInfoBean) {
        if (roomInfoBean.getData() != null) {
            tvFansCount.setText("粉丝：" + roomInfoBean.getData().getSubscriptionNum() + "");
            if (roomInfoBean.getData().getAnchor() != null) {
                GlideImageLoader.getInstace().circleCropImage(this, roomInfoBean.getData().getAnchor().getAvatar(), ivHostAvatar);
                tvHostName.setText(roomInfoBean.getData().getAnchor().getName());
            }
            if (roomInfoBean.getData().getStream() != null) {
                setupPlayerSize(roomInfoBean.getData().getStream().getHeight(), roomInfoBean.getData().getStream().getWidth());
                if (roomInfoBean.getData().getStream().getStreamAddress() != null) {
                    if (roomInfoBean.getData().getStream().getStreamAddress().getFlv() != null) {
                        initPlayers(roomInfoBean.getData().getStream().getStreamAddress().getFlv());
                    } else if (roomInfoBean.getData().getStream().getStreamAddress().getHls() != null) {
                        initPlayers(roomInfoBean.getData().getStream().getStreamAddress().getFlv());
                    } else if (roomInfoBean.getData().getStream().getStreamAddress().getRtmp() != null) {
                        initPlayers(roomInfoBean.getData().getStream().getStreamAddress().getFlv());
                    }
                }
            }
        }
    }

    @Override
    public void onAudienceSuccess(long count) {
        tvPopularity.setText("人气\n" + count);
    }

    @Override
    public void onFellowHostSuccess() {
        btnFollow.setVisibility(View.GONE);
    }

    private void setupPlayerSize(int height, int width) {
        ratioLayout.setPicRatio(width / (float) height);
        ratioLayout.post(new Runnable() {
            @Override
            public void run() {
                ratioLayout.requestLayout();
            }
        });
    }

    @Override
    public void onLiveFaceSuccess(EmjoyInfo emjoyInfo) {

    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        mGiftInfo = giftInfo;
    }
}