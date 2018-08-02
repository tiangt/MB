package com.whzl.mengbi.ui.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.chat.room.message.events.StartPlayEvent;
import com.whzl.mengbi.chat.room.message.events.StopPlayEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.message.messageJson.StartStopLiveJson;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.eventbus.event.LiveHouseUserInfoUpdateEvent;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.gift.GiftControl;
import com.whzl.mengbi.gift.RunWayGiftControl;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.receiver.NetStateChangeReceiver;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.dialog.GiftDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseAudienceListDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseChatDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseRankDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.RatioRelativeLayout;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.zxing.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class LiveDisplayActivity extends BaseActivity implements LiveView {
    @BindView(R.id.iv_host_avatar)
    CircleImageView ivHostAvatar;
    @BindView(R.id.tv_host_name)
    TextView tvHostName;
    @BindView(R.id.btn_follow)
    ImageButton btnFollow;
    @BindView(R.id.rl_contribution_container)
    RelativeLayout rlContributionContainer;
    //    @BindView(R.id.player_master)
//    SurfaceView surfaceView;
    @BindView(R.id.texture_view)
    KSYTextureView textureView;
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
    //    @BindView(R.id.gift_anim_view)
//    RelativeLayout giftAnimView;
    @BindView(R.id.ll_gift_container)
    LinearLayout llGiftContainer;
    @BindView(R.id.btn_contribute)
    Button btnContribute;
    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.iv_gift_gif)
    ImageView ivGiftGif;
    @BindView(R.id.tv_stop_tip)
    TextView tvStopTip;
    //    @BindView(R.id.tv_run_way_gift)
//    TextView tvRunWayGift;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tv_run_way_gift)
    AutoScrollTextView runWayText;
    //    @BindView(R.id.rl_info_container)
//    RelativeLayout rlInfoContainer;
    @BindView(R.id.tv_lucky_gift)
    TextView tvLuckyGift;
    private LivePresenterImpl mLivePresenter;
    private int mProgramId;
    //    private KSYMediaPlayer mMasterPlayer;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private GiftInfo mGiftData;
    private ArrayList<FillHolderMessage> chatList = new ArrayList<>();
    private static final int TOTAL_CHAT_MSG = 100;
    private RecyclerView.Adapter chatAdapter;
    private boolean isRecyclerScrolling;
    private long mUserId;
    private BaseAwesomeDialog mGiftDialog;
    //    private volatile ArrayList<AnimJson> mTotalAnimList = new ArrayList<>();
//    private volatile boolean flagIsTotalAnimating = false;
    private volatile ArrayList<AnimEvent> mGifAnimList = new ArrayList<>();
    private boolean flagIsGifAnimating = false;
    private int mAnchorId;
    //    private Runnable mTotalGiftAnimAction;
    private Runnable mGifAction;
    //    private Runnable mCacheComboAction;
    private int REQUEST_LOGIN = 120;
    private boolean flagRunwayRunning = false;
    private volatile ArrayList<RunWayEvent> mRunWayList = new ArrayList<>();
    private Runnable mRunWayAction;
    private boolean isLuckyGiftShow;
    private volatile ArrayList<LuckGiftEvent> mLuckGiftList = new ArrayList<>();
    private Runnable mLuckyGiftAction;
    private long coin;
    private GiftControl giftControl;
    private String mStream;
    private RunWayGiftControl mRunWayGiftControl;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_live_display_new);
    }

    @Override
    protected void initEnv() {
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#181818"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mLivePresenter = new LivePresenterImpl(this);
        if (getIntent() != null) {
            mProgramId = getIntent().getIntExtra(BundleConfig.PROGRAM_ID, -1);
            SPUtils.put(this, "programId", mProgramId);
        }
        chatRoomPresenter = new ChatRoomPresenterImpl(mProgramId + "");

        mUserId = Long.parseLong(SPUtils.get(this, "userId", 0L).toString());
        mLivePresenter.getLiveGift();
//        initPlayer();
        giftControl = new GiftControl(this);
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        NetStateChangeReceiver receiver = new NetStateChangeReceiver();
        receiver.setEvevt(netMobile -> {
            if (netMobile != NetUtils.NETWORK_NONE) {
                if (textureView != null && mStream != null) {
                    textureView.softReset();
                    try {
                        textureView.setDataSource(mStream);
                        textureView.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                showToast("网络连接断开，请检测网络设置");
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        registerReceiver(receiver, intentFilter);
    }

    private void initPlayer() {
//        mMasterPlayer = new KSYMediaPlayer.Builder(this).build();
//        mMasterPlayer.setOnPreparedListener(mp -> {
//            if (mMasterPlayer != null) {
//                mMasterPlayer.start();
//            } else {
//                Log.e("Player", "player is null at start");
//            }
//        });
//        mMasterPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(IMediaPlayer iMediaPlayer, int info, int i1) {
//                if (info == IMediaPlayer.MEDIA_INFO_RELOADED) {
//                    Log.d("LiveDisplayActivity", "Succeed to reload video.");
//                }
//                //Log.e("Player", "info=" + info);
//                return false;
//            }
//        });
        textureView.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        textureView.setOnPreparedListener(iMediaPlayer -> {
            progressBar.setVisibility(View.GONE);
            textureView.start();
        });
        textureView.setOnInfoListener((iMediaPlayer, i, i1) -> {
            if (i == IMediaPlayer.MEDIA_INFO_RELOADED) {
                Log.d("LiveDisplayActivity", "Succeed to reload video.");
            }
            //Log.e("Player", "info=" + info);
            switch (i) {
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            return false;
        });
        textureView.setOnCompletionListener(iMediaPlayer -> {
            textureView.stop();
            textureView.release();
        });
    }

    @Override
    protected void setupView() {
        initPlayer();
        giftControl.setGiftLayout(llGiftContainer, 3).setHideMode(false);
//        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                if (mMasterPlayer != null) {
//                    mMasterPlayer.setDisplay(surfaceHolder);
//                    mMasterPlayer.setScreenOnWhilePlaying(true);
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//                if (mMasterPlayer != null) {
//                    mMasterPlayer.setDisplay(null);
//                }
//            }
//        });
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(LiveDisplayActivity.this).inflate(R.layout.chat_text, null);
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
                    default:
                        break;
                }
            }
        });
        initAction();
    }

    @Override
    protected void loadData() {
        getRoomToken();
        mLivePresenter.getRoomInfo(mProgramId);
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
        Observable.interval(0, 10, TimeUnit.SECONDS)
                .subscribe(aLong -> mLivePresenter.getAudienceAccount(mProgramId));
    }

    private void initAction() {
//        mTotalGiftAnimAction = () -> {
//            giftAnimView.setTranslationX(0);
//            flagIsTotalAnimating = false;
//            if (mTotalAnimList.size() > 0) {
//                mTotalAnimList.remove(0);
//            }
//            if (mTotalAnimList.size() > 0) {
//                animGift(mTotalAnimList.get(0));
//            }
//        };
//        mCacheComboAction = this::comboCache;

//        mRunWayAction = () -> {
//            flagIsGifAnimating = false;
//            tvRunWayGift.setVisibility(View.GONE);
//            mRunWayList.remove(0);
//            if (mRunWayList.size() > 0) {
//                showRunWay(mRunWayList.get(0));
//            }
//        };

        mLuckyGiftAction = () -> {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = ((float) animation.getAnimatedValue());
                    tvLuckyGift.setTranslationX(-UIUtil.dip2px(LiveDisplayActivity.this, animatedValue));
                    if (animatedValue == 360) {
                        tvLuckyGift.setTranslationX(UIUtil.dip2px(LiveDisplayActivity.this, 360));
                        mLuckGiftList.remove(0);
                        if (mLuckGiftList.size() > 0) {
                            showLuckyGift(mLuckGiftList.get(0));
                        } else {
                            isLuckyGiftShow = false;
                        }
                    }
                }
            });
            valueAnimator.start();
        };

        mGifAction = () -> {
            ivGiftGif.setVisibility(View.GONE);
            mGifAnimList.remove(0);
            if (mGifAnimList.size() > 0) {
                animGif(mGifAnimList.get(0));
            } else {
                flagIsGifAnimating = false;
            }
        };
    }

    private void getRoomToken() {
        HashMap map = new HashMap();
        map.put("userId", mUserId);
        map.put("programId", mProgramId);
        mLivePresenter.getLiveToken(map);
    }

    private void setDateSourceForPlayer(String stream) {
        mStream = stream;
//        try {
//            mMasterPlayer.setDataSource(stream);
//            mMasterPlayer.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            textureView.setDataSource(stream);
            textureView.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_follow, R.id.btn_close, R.id.btn_chat, R.id.btn_send_gift, R.id.tv_popularity, R.id.btn_contribute})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_follow:
                if (mUserId == 0) {
                    login();
                    return;
                }
                mLivePresenter.followHost(mUserId, mProgramId);
                break;
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_chat:
                LiveHouseChatDialog.newInstance()
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_send_gift:
                if (mUserId == 0) {
                    login();
                    return;
                }
                if (mGiftData == null) {
                    mLivePresenter.getLiveGift();
                    return;
                }
                if (mGiftDialog != null && mGiftDialog.isAdded()) {
                    return;
                }
                mGiftDialog = GiftDialog.newInstance(mGiftData, coin)
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
                LiveHouseRankDialog.newInstance(mProgramId)
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
            default:
                break;
        }

    }

    private void login() {
        Intent intent = new Intent(this, LoginActivityNew.class);
        intent.putExtra("from", this.getClass().toString());
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    public void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo) {
        chatRoomPresenter.setupConnection(liveRoomTokenInfo, LiveDisplayActivity.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KickoutEvent kickoutEvent) {
        showToast(kickoutEvent.getNoChatMsg().getNochatType() == 8 ? "你已经被踢出踢出直播间" : "用多个手机打开同一个直播间，强制退出之前的直播间");
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LuckGiftEvent luckGiftEvent) {
        mLuckGiftList.add(luckGiftEvent);
        synchronized (this) {
            if (!isLuckyGiftShow) {
                showLuckyGift(mLuckGiftList.get(0));
            }
        }
    }

    private void showLuckyGift(LuckGiftEvent luckGiftEvent) {
        isLuckyGiftShow = true;
        tvLuckyGift.animate().translationX(0).setInterpolator(new DecelerateInterpolator()).setDuration(300).start();
        tvLuckyGift.setText("恭喜");
        SpannableString nickNameSpannable = new SpannableString(luckGiftEvent.getNickname());
        nickNameSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#f76667")), 0, nickNameSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLuckyGift.append(nickNameSpannable);
        tvLuckyGift.append("送幸运礼物喜中");
        SpannableString bonusCoinsSpannable = new SpannableString(luckGiftEvent.getTotalLuckMengBi() + "");
        bonusCoinsSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#f76667")), 0, bonusCoinsSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLuckyGift.append(bonusCoinsSpannable);
        tvLuckyGift.append("萌币");
        tvLuckyGift.postDelayed(mLuckyGiftAction, 2800);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AnimEvent animEvent) {
        if ("TOTAl".equals(animEvent.getAnimJson().getAnimType())) {
            AnimJson animJson = animEvent.getAnimJson();
            animJson.getContext().setGiftUrl(animEvent.getAnimUrl());
            giftControl.loadGift(animJson.getContext());
//            mTotalAnimList.add(animJson);
//            if (!flagIsTotalAnimating) {
//                animGift(mTotalAnimList.get(0));
//            }
        } else {
            double seconds = 0;
            if ("MOBILE_GIFT_GIF".equals(animEvent.getAnimJson().getAnimType())
                    || "MOBILE_CAR_GIF".equals(animEvent.getAnimJson().getAnimType())) {
                List<AnimJson.ResourcesEntity> resources = animEvent.getAnimJson().getResources();
                for (int i = 0; i < resources.size(); i++) {
                    AnimJson.ResourcesEntity resourcesEntity = resources.get(i);
                    if ("PARAMS".equals(resourcesEntity.getResType())) {
                        String resValue = resourcesEntity.getResValue();
                        try {
                            JSONObject jsonObject = new JSONObject(resValue);
                            seconds = jsonObject.getDouble("playSeconds");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            if (seconds > 0) {
                animEvent.setSeconds(seconds);
                mGifAnimList.add(animEvent);
                if (!flagIsGifAnimating) {
                    flagIsGifAnimating = true;
                    animGif(mGifAnimList.get(0));
                }
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RunWayEvent runWayEvent) {
        //第一版去掉跑道功能
//        mRunWayList.add(runWayEvent);
//        showRunWay(runWayEvent);
        if (mRunWayGiftControl == null) {
            mRunWayGiftControl = new RunWayGiftControl(runWayText);
        }
        mRunWayGiftControl.load(runWayEvent);
    }

//    private void showRunWay(RunWayEvent runWayEvent) {
//        if (!flagRunwayRunning) {
//            flagIsGifAnimating = true;
//            tvRunWayGift.setVisibility(View.VISIBLE);
//            runWayEvent.showRunWay(tvRunWayGift);
//            tvRunWayGift.postDelayed(mRunWayAction, 2500);
//        }
//    }

    private void animGif(AnimEvent animEvent) {
        ivGiftGif.setVisibility(View.VISIBLE);
        GlideImageLoader.getInstace().loadGif(getApplicationContext(), animEvent.getAnimUrl(), ivGiftGif, new GlideImageLoader.GifListener() {
            @Override
            public void onResourceReady() {
                if (isFinishing()) {
                    return;
                }
                ivGiftGif.postDelayed(mGifAction, ((long) (animEvent.getSeconds() * 1000)));
            }

            @Override
            public void onFail() {
                if (isFinishing()) {
                    return;
                }
                ivGiftGif.setVisibility(View.GONE);
                mGifAnimList.remove(0);
                if (mGifAnimList.size() > 0) {
                    animGif(mGifAnimList.get(0));
                } else {
                    flagIsGifAnimating = false;
                }

            }
        });
    }

//    @BindView(R.id.iv_anim_gift_avatar)
//    CircleImageView ivAnimGiftAvatar;
//    @BindView(R.id.tv_anim_gift_form)
//    TextView tvAnimGiftForm;
//    @BindView(R.id.tv_anim_gift_name)
//    TextView tvAnimGiftName;
//    @BindView(R.id.iv_anim_gift_icon)
//    ImageView ivAnimGiftIcon;
//    @BindView(R.id.tv_anim_gift_count)
//    TextView tvAnimGiftCount;

//    private synchronized void animGift(AnimJson animJson) {
//        flagIsTotalAnimating = true;
//        AnimJson.ContextEntity context = animJson.getContext();
//        giftAnimView.setVisibility(View.VISIBLE);
//        String avatarUrl = ImageUrl.getAvatarUrl(context.getUserId(), "jpg", context.getLastUpdateTime());
//        GlideImageLoader.getInstace().displayImage(this, avatarUrl, ivAnimGiftAvatar);
//        GlideImageLoader.getInstace().displayImage(this, animJson.getGiftUrl(), ivAnimGiftIcon);
//        tvAnimGiftForm.setText(context.getNickname());
//        SpannableString span = new SpannableString("送 " + context.getGoodsName());
//        span.setSpan(new ForegroundColorSpan(Color.parseColor("#fe4b22")), 2, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvAnimGiftName.setText(span);
//        tvAnimGiftCount.setText("x " + context.getGiftTotalCount());
//        giftAnimView.post(() -> {
//            int animGiftWidth = giftAnimView.getWidth();
//            int animX = UIUtil.dip2px(LiveDisplayActivity.this, 13.5f) + animGiftWidth;
//            giftAnimView.animate().translationX(animX).setInterpolator(new DecelerateInterpolator())
//                    .setDuration(300)
//                    .setListener(new Animator.AnimatorListener() {
//
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            if (mTotalAnimList.size() > 1) {
//                                long animatingUserId = mTotalAnimList.get(0).getContext().getUserId();
//                                int animatingGoodsId = mTotalAnimList.get(0).getContext().getGoodsId();
//                                AnimJson.ContextEntity nextContext = mTotalAnimList.get(1).getContext();
//                                long nextUserId = nextContext.getUserId();
//                                int nextGoodId = nextContext.getGoodsId();
//                                if (animatingGoodsId == nextGoodId && animatingUserId == nextUserId) {
//                                    comboCache();
//                                } else {
//                                    giftAnimView.postDelayed(mTotalGiftAnimAction, 2500);
//                                }
//                            } else {
//                                giftAnimView.postDelayed(mTotalGiftAnimAction, 2500);
//                            }
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    })
//                    .start();
//        });
//    }

//    private synchronized void comboCache() {
//        AnimJson.ContextEntity comboContext = mTotalAnimList.get(1).getContext();
//        mTotalAnimList.remove(0);
//        giftAnimView.removeCallbacks(mTotalGiftAnimAction);
//        tvAnimGiftCount.setText("x " + comboContext.getGiftTotalCount());
//        scaleAnim(tvAnimGiftCount);
//        if (mTotalAnimList.size() > 1) {
//            long animatingUserId = mTotalAnimList.get(0).getContext().getUserId();
//            int animatingGoodsId = mTotalAnimList.get(0).getContext().getGoodsId();
//            AnimJson.ContextEntity nextContext = mTotalAnimList.get(1).getContext();
//            long nextUserId = nextContext.getUserId();
//            int nextGoodId = nextContext.getGoodsId();
//            if (animatingGoodsId == nextGoodId && animatingUserId == nextUserId) {
//                giftAnimView.postDelayed(mCacheComboAction, 200);
//            } else {
//                giftAnimView.postDelayed(mTotalGiftAnimAction, 2500);
//            }
//        } else {
//            giftAnimView.postDelayed(mTotalGiftAnimAction, 2500);
//        }
//    }
//
//    private void scaleAnim(View view) {
//        ValueAnimator animator = ValueAnimator.ofFloat(1, 1.8f, 1);
//        animator.setDuration(300);
//        animator.addUpdateListener(animation -> {
//            float animatedValue = (float) animation.getAnimatedValue();
//            view.setScaleX(animatedValue);
//            view.setScaleY(animatedValue);
//        });
//        animator.start();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdatePubChatEvent updatePubChatEvent) {
        FillHolderMessage message = updatePubChatEvent.getMessage();
        if (chatList.size() >= TOTAL_CHAT_MSG) {
            chatList.remove(0);
        }
        chatList.add(message);
        if (!isRecyclerScrolling) {
            if (chatAdapter != null && recycler != null) {
                chatAdapter.notifyDataSetChanged();
                recycler.smoothScrollToPosition(chatList.size() - 1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StartPlayEvent startPlayEvent) {
        StartStopLiveJson.ContextEntity context = startPlayEvent.getStartStopLiveJson().getContext();
        if (context.getHeight() != 0 && context.getWidth() != 0) {
            ratioLayout.setPicRatio(context.getWidth() / ((float) context.getHeight()));
        }
        mStream = startPlayEvent.getStreamAddress();
//        if (mMasterPlayer == null) {
//            ToastUtils.showToast("mMasterPlayer is null");
//            return;
//        }
//        mMasterPlayer.reset();
//        surfaceView.setAlpha(1);
//        try {
//            mMasterPlayer.setDataSource(streamAddress);
//            mMasterPlayer.prepareAsync();
//            mMasterPlayer.setSurface(surfaceView.getHolder().getSurface());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (textureView == null) {
            ToastUtils.showToast("mMasterPlayer is null");
            return;
        }
        textureView.reset();
        try {
            textureView.setDataSource(mStream);
            textureView.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tvStopTip.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StopPlayEvent stopPlayEvent) {
//        mMasterPlayer.reset();
        mStream = null;
        textureView.reset();
        tvStopTip.setVisibility(View.VISIBLE);
    }


    @Override
    public void onRoomInfoSuccess(RoomInfoBean roomInfoBean) {
        mAnchorId = roomInfoBean.getData().getAnchor().getId();
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
                        setDateSourceForPlayer(roomInfoBean.getData().getStream().getStreamAddress().getFlv());
                    } else if (roomInfoBean.getData().getStream().getStreamAddress().getRtmp() != null) {
                        setDateSourceForPlayer(roomInfoBean.getData().getStream().getStreamAddress().getRtmp());
                    } else if (roomInfoBean.getData().getStream().getStreamAddress().getHls() != null) {
                        setDateSourceForPlayer(roomInfoBean.getData().getStream().getStreamAddress().getHls());
                    }
                }
            }
            if (!"T".equals(roomInfoBean.getData().getProgramStatus())) {
                tvStopTip.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void setupPlayerSize(int height, int width) {
        if (height == 0 || width == 0) {
            return;
        }
        ratioLayout.setPicRatio(width / (float) height);
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
        if (data != null) {
            mUserId = data.getUserId();
            if (data.getWeathMap() != null) {
                coin = data.getWeathMap().getCoin();
                LiveHouseUserInfoUpdateEvent liveHouseUserInfoUpdateEvent = new LiveHouseUserInfoUpdateEvent();
                liveHouseUserInfoUpdateEvent.coin = coin;
                EventBus.getDefault().post(liveHouseUserInfoUpdateEvent);
            }
        }

    }

    @Override
    public void onSendGiftSuccess() {
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
    }

    @Override
    public void onError(String meg) {
        showToast(meg);
    }

    @Override
    public void onLiveFaceSuccess(EmjoyInfo emjoyInfo) {

    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        mGiftData = giftInfo;
    }

    public void sendMeeage(String message) {
        chatRoomPresenter.sendMessage(message);
    }

    public void sendGift(int count, int goodId, boolean useBag) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("roomId", mProgramId + "");
        paramsMap.put("programId", mProgramId + "");
        paramsMap.put("targetId", mAnchorId + "");
        paramsMap.put("goodsId", goodId + "");
        paramsMap.put("count", count + "");
        paramsMap.put("userId", mUserId + "");
        paramsMap.put("useBag", useBag + "");
        mLivePresenter.sendGift(paramsMap);
    }

    @Override
    protected void onDestroy() {
        //ToastUtils.showToast("LiveDisplayActivityNew destroy");
//        giftAnimView.removeCallbacks(mTotalGiftAnimAction);
//        giftAnimView.removeCallbacks(mCacheComboAction);
        ivGiftGif.removeCallbacks(mGifAction);
//        tvRunWayGift.removeCallbacks(mRunWayAction);
        tvLuckyGift.removeCallbacks(mLuckyGiftAction);
        giftControl.cleanAll();
        if (mRunWayGiftControl != null) {
            mRunWayGiftControl.destroy();
        }
        super.onDestroy();
        mLivePresenter.onDestory();
        if (chatRoomPresenter != null) {
            chatRoomPresenter.onChatRoomDestroy();
        }
//        if (mMasterPlayer != null) {
//            mMasterPlayer.stop();
//            mMasterPlayer.release();
//            mMasterPlayer = null;
//        }
        if (textureView != null) {
            textureView.stop();
            textureView.release();
            textureView = null;
        }
        mGiftDialog = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                mUserId = Long.parseLong(SPUtils.get(this, "userId", (long) 0).toString());
                mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
                getRoomToken();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfoUpdateEvent event) {
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textureView != null) {
            //mAudioBackgroundPlay为true表示切换到后台后仍然播放音频
            textureView.runInBackground(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textureView != null) {
            textureView.runInForeground();
        }
    }
}
