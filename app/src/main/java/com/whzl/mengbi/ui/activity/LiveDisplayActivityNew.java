package com.whzl.mengbi.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ksyun.media.player.KSYMediaPlayer;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.dialog.GiftDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseAudienceListDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseChatDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseRankDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
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
import java.util.List;

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
    ImageButton btnFollow;
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
    ImageButton btnChat;
    @BindView(R.id.btn_send_gift)
    ImageButton btnSendGift;
    @BindView(R.id.gift_anim_view)
    RelativeLayout giftAnimView;
    @BindView(R.id.btn_contribute)
    Button btnContribute;
    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.iv_gift_gif)
    ImageView ivGiftGif;

    private Unbinder mBind;
    private LivePresenterImpl mLivePresenter;
    private int mProgramId;
    private KSYMediaPlayer mMasterPlayer;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private GiftInfo mGiftData;
    private ArrayList<FillHolderMessage> chatList = new ArrayList<>();
    private RecyclerView.Adapter chatAdapter;
    private boolean isRecyclerScrolling;
    private int userId;
    private BaseAwesomeDialog mGiftDialog;
    private BaseAwesomeDialog mLiveHouseChatDialog;
    private final static String TAG_DIALOG_GIFT = "tagGift";
    private final static String TAG_DIALOG_CHAT = "tagChat";
    private final static String TAG_DIALOG_AUDIENCE = "tagAudience";
    private final static String TAG_DIALOG_CONTRIBUTE = "tagContribute";
    private ArrayList<AnimJson> mTotalAnimtList = new ArrayList<>();
    private boolean flagIsAnimating;
    private ArrayList<AnimEvent> mGifAnimList = new ArrayList<>();
    private boolean flagIsGifAnimating;

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
        mLivePresenter.getRoomUserInfo(userId, mProgramId
        );
    }

    private void initEnv() {
        mLivePresenter = new LivePresenterImpl(this);
        chatRoomPresenter = new ChatRoomPresenterImpl();
        if (getIntent() != null) {
            mProgramId = getIntent().getIntExtra("ProgramId", -1);
            SPUtils.put(this, "programId", mProgramId);
        }
        userId = (Integer) SPUtils.get(this, "userId", 0);
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
                View item = getLayoutInflater().inflate(R.layout.chat_text, null);
                return new SingleTextViewHolder(item);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                FillHolderMessage message = chatList.get(position);
                message.fillHolder(holder);
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
        String sessionId = SPUtils.get(this, "sessionId", "0").toString();
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("programId", mProgramId);
        map.put("sessionId", sessionId);
        mLivePresenter.getLiveToken(map);
    }

    private void initPlayers(String stream) {
        mMasterPlayer = new KSYMediaPlayer.Builder(this).build();
        mMasterPlayer.setOnPreparedListener(mp -> {
            if (mMasterPlayer != null)
                mMasterPlayer.start();
        });
        try {
            mMasterPlayer.shouldAutoPlay(false);
            mMasterPlayer.setDataSource(stream);
            mMasterPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_follow, R.id.btn_close, R.id.btn_chat, R.id.btn_send_gift, R.id.tv_popularity, R.id.btn_contribute})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_follow:
                mLivePresenter.followHost(userId, mProgramId);
                break;
            case R.id.btn_close:
                break;
            case R.id.btn_chat:
                LiveHouseChatDialog.newInstance()
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_send_gift:
                if (mGiftData == null) {
                    mLivePresenter.getLiveGift();
                    return;
                }
                mGiftDialog = GiftDialog.newInstance(mGiftData)
                        .setShowBottom(true)
                        .setDimAmount(0)
                        .show(getSupportFragmentManager());
                break;

            case R.id.tv_popularity:
                LiveHouseAudienceListDialog.newInstance(mProgramId)
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;

            case R.id.btn_contribute:
                LiveHouseRankDialog.newInstance(mProgramId).
                        setShowBottom(true)
                        .show(getSupportFragmentManager());
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
        mLiveHouseChatDialog = null;
        mGiftDialog = null;
    }


    @Override
    public void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo) {
        chatRoomPresenter.setupConnection(liveRoomTokenInfo, LiveDisplayActivityNew.this);
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
    public void onMessageEvent(AnimEvent animEvent) {
        if ("TOTAl".equals(animEvent.getAnimJson().getAnimType())) {
            AnimJson animJson = animEvent.getAnimJson();
            animJson.setGiftUrl(animEvent.getAnimUrl());
            mTotalAnimtList.add(animJson);
            if (!flagIsAnimating) {
                animGift(mTotalAnimtList.get(0));
            }
        } else {
            long seconds = 0;
            if ("MOBILE_GIFT_GIF".equals(animEvent.getAnimJson().getAnimType())) {
                seconds = animEvent.getAnimJson().getContext().getSeconds();
                ivGiftGif.setVisibility(View.VISIBLE);
            } else if ("MOBILE_CAR_GIF".equals(animEvent.getAnimJson().getAnimType())) {
                List<AnimJson.ResourcesEntity> resources = animEvent.getAnimJson().getResources();
                for (int i = 0; i < resources.size(); i++) {
                    AnimJson.ResourcesEntity resourcesEntity = resources.get(i);
                    if ("PARAMS".equals(resourcesEntity.getResType())) {
                        String resValue = resourcesEntity.getResValue();
                    }
                }
            }
            if (seconds > 0) {
                animEvent.setSeconds(seconds);
                mGifAnimList.add(animEvent);
                if(!flagIsGifAnimating){
                    animGif(mGifAnimList.get(0));
                }
            }
        }

    }

    private void animGif(AnimEvent animEvent) {
        flagIsGifAnimating = true;
        GlideImageLoader.getInstace().loadGif(getApplicationContext(), animEvent.getAnimUrl(), ivGiftGif, new GlideImageLoader.GifListener() {
            @Override
            public void onResourceReady() {
                ivGiftGif.setVisibility(View.VISIBLE);
                ivGiftGif.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivGiftGif.setVisibility(View.GONE);
                        mGifAnimList.remove(0);
                        if(mGifAnimList.size() > 0){
                            animGif(mGifAnimList.get(0));
                        }else {
                            flagIsGifAnimating = false;
                        }
                    }
                }, animEvent.getSeconds() * 1000);
            }

            @Override
            public void onFail() {
                ivGiftGif.setVisibility(View.GONE);
                mGifAnimList.remove(0);
                if(mGifAnimList.size() > 0){
                    animGif(mGifAnimList.get(0));
                }else {
                    flagIsAnimating = true;
                }
            }
        });
    }

    @BindView(R.id.iv_anim_gift_avatar)
    CircleImageView ivAnimGiftAvatar;
    @BindView(R.id.tv_anim_gift_form)
    TextView tvAnimGiftForm;
    @BindView(R.id.tv_anim_gift_name)
    TextView tvAnimGiftName;
    @BindView(R.id.iv_anim_gift_icon)
    ImageView ivAnimGiftIcon;
    @BindView(R.id.tv_anim_gift_count)
    TextView tvAnimGiftCount;

    private void animGift(AnimJson animJson) {
        AnimJson.ContextEntity context = animJson.getContext();
        giftAnimView.setVisibility(View.VISIBLE);
        String avatarUrl = ImageUrl.getImageUrl(context.getUserId(), "jpg", context.getLastUpdateTime());
        GlideImageLoader.getInstace().displayImage(this, avatarUrl, ivAnimGiftAvatar);
        GlideImageLoader.getInstace().displayImage(this, animJson.getGiftUrl(), ivAnimGiftIcon);
        tvAnimGiftForm.setText(context.getNickname());
        SpannableString span = new SpannableString("送 " + context.getGoodsName());
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#fe4b22")), 2, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAnimGiftName.setText(span);
        tvAnimGiftCount.setText("x " + context.getGiftTotalCount());
        giftAnimView.post(() -> {
            int animGiftWidth = giftAnimView.getWidth();
            int animX = UIUtil.dip2px(LiveDisplayActivityNew.this, 13.5f) + animGiftWidth;
            giftAnimView.animate().translationX(animX).setInterpolator(new DecelerateInterpolator())
                    .setDuration(500)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            flagIsAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            scaleAnim(tvAnimGiftCount);
                            giftAnimView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    giftAnimView.setVisibility(View.GONE);
                                    giftAnimView.setTranslationX(0);
                                    mTotalAnimtList.remove(0);
                                    if (mTotalAnimtList.size() > 0) {
                                        animGift(mTotalAnimtList.get(0));
                                    } else {
                                        flagIsAnimating = false;
                                    }
                                }
                            }, 2500);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        });
    }

    private void scaleAnim(View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 2f, 1);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                view.setScaleX(animatedValue);
                view.setScaleY(animatedValue);
            }
        });
        animator.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdatePubChatEvent updatePubChatEvent) {
        FillHolderMessage message = updatePubChatEvent.getMessage();
        chatList.add(message);
        if (!isRecyclerScrolling) {
            chatAdapter.notifyDataSetChanged();
            recycler.smoothScrollToPosition(chatList.size() - 1);
        }
    }


    @Override
    public void onRoomInfoSuccess(RoomInfoBean roomInfoBean) {
        if (roomInfoBean.getData() != null) {
            tvFansCount.setText("粉丝：" + roomInfoBean.getData().getSubscriptionNum() + "");
            ChatRoomInfo.getInstance().setRoomInfoBean(roomInfoBean);
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

    private void setupPlayerSize(int height, int width) {
        if (height == 0 || width == 0) {
            return;
        }
        ratioLayout.setPicRatio(width / (float) height);
        ratioLayout.post(new Runnable() {
            @Override
            public void run() {
                ratioLayout.requestLayout();
            }
        });
    }


    @Override
    public void onAudienceSuccess(long count) {
        tvPopularity.setText("人气\n" + count);
    }

    @Override
    public void onFollowHostSuccess() {
        btnFollow.setVisibility(View.GONE);
    }

    @Override
    public void onGetRoomUserInFoSuccess(RoomUserInfo.DataBean data) {
        btnFollow.setVisibility(data.isIsSubs() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onLiveFaceSuccess(EmjoyInfo emjoyInfo) {

    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        mGiftData = giftInfo;
    }

    public void sendMeeage(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatRoomPresenter.sendMessage(message);
            }
        }).start();
    }
}
