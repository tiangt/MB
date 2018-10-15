package com.whzl.mengbi.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.whzl.mengbi.chat.room.message.events.AnchorLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.events.BroadCastBottomEvent;
import com.whzl.mengbi.chat.room.message.events.GuardOpenEvent;
import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftBigEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.message.events.PkEvent;
import com.whzl.mengbi.chat.room.message.events.RoyalLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.chat.room.message.events.StartPlayEvent;
import com.whzl.mengbi.chat.room.message.events.StopPlayEvent;
import com.whzl.mengbi.chat.room.message.events.UpdateProgramEvent;
import com.whzl.mengbi.chat.room.message.events.UserLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.message.messageJson.RunWayJson;
import com.whzl.mengbi.chat.room.message.messageJson.StartStopLiveJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.eventbus.event.LiveHouseUserInfoUpdateEvent;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.gift.GifGiftControl;
import com.whzl.mengbi.gift.GiftControl;
import com.whzl.mengbi.gift.LuckGiftControl;
import com.whzl.mengbi.gift.PkControl;
import com.whzl.mengbi.gift.RunWayBroadControl;
import com.whzl.mengbi.gift.RunWayGiftControl;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PkInfoBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.receiver.NetStateChangeReceiver;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.AudienceInfoDialog;
import com.whzl.mengbi.ui.dialog.GiftDialog;
import com.whzl.mengbi.ui.dialog.GuardListDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseChatDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseRankDialog;
import com.whzl.mengbi.ui.dialog.PrivateChatListFragment;
import com.whzl.mengbi.ui.dialog.TreasureBoxDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.fragment.ChatListFragment;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView2;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PkLayout;
import com.whzl.mengbi.ui.widget.view.RatioRelativeLayout;
import com.whzl.mengbi.util.OnMultiClickListener;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.util.zxing.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
    @BindView(R.id.texture_view)
    KSYTextureView textureView;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;
    @BindView(R.id.tv_popularity)
    TextView tvPopularity;
    @BindView(R.id.rl_host_info)
    RelativeLayout rlHostInfo;
    @BindView(R.id.ratio_layout)
    RatioRelativeLayout ratioLayout;
    @BindView(R.id.btn_send_gift)
    ImageButton btnSendGift;
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
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tv_run_way_gift)
    AutoScrollTextView runWayText;
    @BindView(R.id.tv_lucky_gift)
    TextView tvLuckyGift;
    @BindView(R.id.guard_recycler)
    RecyclerView mGuardRecycler;
    @BindView(R.id.view_message_notify)
    View viewMessageNotify;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.rl_bottom_container)
    RelativeLayout rlBottomContainer;
    @BindView(R.id.rl_guard_success)
    RelativeLayout rlGuardSuccess;
    @BindView(R.id.btn_chat)
    ImageButton btnChat;
    @BindView(R.id.btn_chat_private)
    ImageButton btnChatPrivate;
    @BindView(R.id.iv_guard_avatar)
    CircleImageView ivGuardAvatar;
    @BindView(R.id.tv_guard_nick_name)
    TextView tvGuardNickName;
    @BindView(R.id.tv_treasure_box_count)
    TextView tvTreasureCount;
    @BindView(R.id.tv_treasure_timer)
    TextView tvTreasureTimer;
    @BindView(R.id.ib_luck_wheel)
    ImageButton ibLuckWheel;
    @BindView(R.id.tv_run_way_broad)
    AutoScrollTextView2 runWayBroad;
    @BindView(R.id.pk_layout)
    PkLayout pkLayout;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;

    private LivePresenterImpl mLivePresenter;
    private int mProgramId;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private GiftInfo mGiftData;
    private long mUserId;
    private BaseAwesomeDialog mGiftDialog;
    private int mAnchorId;
    private int REQUEST_LOGIN = 120;
    private long coin;
    private GiftControl giftControl;
    private String mStream;
    private RunWayGiftControl mRunWayGiftControl;
    private LuckGiftControl mLuckyGiftControl;
    private GifGiftControl mGifGiftControl;
    private ArrayList<GuardListBean.GuardDetailBean> mGuardList;
    private BaseListAdapter mGuardAdapter;
    private RoomInfoBean.DataBean.AnchorBean mAnchor;
    private boolean isGuard;
    private Disposable mDisposable;
    private int currentSelectedIndex;
    private Fragment[] fragments;
    private ObjectAnimator showGuardAnim;
    private ObjectAnimator hideGuardAnim;
    private RoomUserInfo.DataBean mRoomUserInfo;
    private NetStateChangeReceiver mReceiver;
    private BaseAwesomeDialog mRankDialog;
    private BaseAwesomeDialog mChatDialog;
    private BaseAwesomeDialog mGuardListDialog;
    private long mAudienceCount;
    private TreasureBoxDialog mTreasureBoxDialog;
    private HashMap<Integer, Integer> mTreasureStatusMap;
    private Disposable mTreasureODisposable;
    private Long mTime;
    private int currentReceiveTreasureId;
    private RunWayBroadControl mRunWayBroadControl;
    private PkInfoBean.userInfoBean myPkInfo;
    private PkInfoBean.userInfoBean otherPkInfo;
    private PkControl pkControl;

//     1、vip、守护、贵族、主播、运管不受限制
//        2、名士5以上可以私聊，包含名士5

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_live_display_new);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        destroy();
        pkLayout.setVisibility(View.GONE);
        textureView.stop();
        textureView.reset();
        textureView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mProgramId = intent.getIntExtra(BundleConfig.PROGRAM_ID, -1);
        SPUtils.put(this, "programId", mProgramId);
        chatRoomPresenter = new ChatRoomPresenterImpl(mProgramId + "");
        isGuard = false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragments[0]);
        fragmentTransaction.remove(fragments[1]);
        fragmentTransaction.commit();
        initFragment();
        loadData();
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
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetStateChangeReceiver();
        mReceiver.setEvevt(netMobile -> {
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
                showToast(R.string.net_error);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        registerReceiver(mReceiver, intentFilter);
    }

    private void initPlayer() {
        textureView.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        textureView.setOnPreparedListener(iMediaPlayer -> {
            textureView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tvStopTip.setVisibility(View.GONE);
            textureView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            textureView.start();
        });
        textureView.setOnInfoListener((iMediaPlayer, i, i1) -> {
            if (i == IMediaPlayer.MEDIA_INFO_RELOADED) {
                Log.d("LiveDisplayActivity", "Succeed to reload video.");
            }
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
        initProtectRecycler();
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragments = new Fragment[]{ChatListFragment.newInstance(), PrivateChatListFragment.newInstance(mProgramId)};
        fragmentTransaction.add(R.id.fragment_container, fragments[0]);
        fragmentTransaction.add(R.id.fragment_container, fragments[1]);
        fragmentTransaction.hide(fragments[1]);
        fragmentTransaction.commit();
    }

    private void setTabChange(int index) {
        if (index == currentSelectedIndex) {
            return;
        }
        if (index == 1) {
            viewMessageNotify.setVisibility(View.GONE);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments[currentSelectedIndex]);
        if (fragments[index].isAdded()) {
            fragmentTransaction.show(fragments[index]);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragments[index]);
        }
        fragmentTransaction.commit();
        setBottomContainerHeight(index == 0 ? 50 : 0);
        currentSelectedIndex = index;
    }

    public void setBottomContainerHeight(int dpHeight) {
        ViewGroup.LayoutParams layoutParams = rlBottomContainer.getLayoutParams();
        layoutParams.height = UIUtil.dip2px(this, dpHeight);
        rlBottomContainer.post(() -> {
            rlBottomContainer.setLayoutParams(layoutParams);
//            rlBottomContainer.requestLayout();
        });
    }

    private void initProtectRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGuardRecycler.setLayoutManager(layoutManager);
        mGuardAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mGuardList == null ? 0 : 3;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(LiveDisplayActivity.this).inflate(R.layout.item_protect, parent, false);
                return new GuardViewHolder(itemView);
            }
        };
        mGuardRecycler.setAdapter(mGuardAdapter);
    }

    public void showMessageNotify() {
        viewMessageNotify.setVisibility(View.VISIBLE);
    }

    class GuardViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_guard_avatar)
        ImageView ivGuardAvatar;
        @BindView(R.id.tv_guard)
        TextView tvGuard;

        GuardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (position > mGuardList.size() - 1) {
                GlideImageLoader.getInstace().displayImage(LiveDisplayActivity.this, R.drawable.guard_default_icon, ivGuardAvatar);
                tvGuard.setVisibility(View.VISIBLE);
                ivGuardAvatar.setAlpha(1f);
            } else {
                GlideImageLoader.getInstace().displayImage(LiveDisplayActivity.this, mGuardList.get(position).avatar, ivGuardAvatar);
                ivGuardAvatar.setAlpha(mGuardList.get(position).isOnline == 1 ? 1f : 0.5f);
                tvGuard.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
                return;
            }
            mGuardListDialog = GuardListDialog.newInstance(mProgramId, mAnchor, 0, mAudienceCount)
                    .setShowBottom(true)
                    .setDimAmount(0)
                    .show(getSupportFragmentManager());
        }
    }

    @Override
    protected void loadData() {
        mLivePresenter.getProgramFirst(mProgramId);
        getRoomToken();
        mLivePresenter.getRoomInfo(mProgramId);
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
        mLivePresenter.getRunWayList(ParamsUtils.getSignPramsMap(new HashMap<>()));
        mDisposable = Observable.interval(0, 20, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mLivePresenter.getAudienceAccount(mProgramId);
        });
        mLivePresenter.getGuardList(mProgramId);
        if (mUserId > 0) {
            mLivePresenter.getTreasureBoxStatus(mUserId);
        }
        mLivePresenter.getActivityList();
        mLivePresenter.getPkInfo(mProgramId);
    }

    private void getRoomToken() {
        HashMap map = new HashMap();
        map.put("userId", mUserId);
        map.put("programId", mProgramId);
        mLivePresenter.getLiveToken(map);
    }

    private void setDateSourceForPlayer(String stream) {
        mStream = stream;
        try {
            textureView.setDataSource(stream);
            textureView.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_follow, R.id.btn_close, R.id.btn_send_gift, R.id.tv_popularity
            , R.id.btn_contribute, R.id.btn_chat, R.id.btn_chat_private, R.id.rootView
            , R.id.fragment_container
            , R.id.btn_treasure_box, R.id.ib_luck_wheel})
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
                if (mChatDialog != null && mChatDialog.isAdded()) {
                    return;
                }
                mChatDialog = LiveHouseChatDialog.newInstance(isGuard, mProgramId, mAnchor)
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_chat_private:
                if (mUserId == 0) {
                    login();
                    return;
                }
                if (!UserIdentity.getCanChatPaivate(mRoomUserInfo)) {
                    showToast(R.string.private_chat_permission_deny);
                    return;
                }
                setTabChange(1);
                break;
            case R.id.rootView:
                if (currentSelectedIndex == 1) {
                    setTabChange(0);
                }
                break;
            case R.id.btn_send_gift:
                if (mUserId == 0) {
                    login();
                    return;
                }
                if (mGiftData == null || mGiftData.getData() == null) {
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
                if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
                    return;
                }
                mGuardListDialog = GuardListDialog.newInstance(mProgramId, mAnchor, 1, mAudienceCount)
                        .setShowBottom(true)
                        .setDimAmount(0)
                        .show(getSupportFragmentManager());
                break;

            case R.id.btn_contribute:
                if (mRankDialog != null && mRankDialog.isAdded()) {
                    return;
                }
                mRankDialog = LiveHouseRankDialog.newInstance(mProgramId)
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_treasure_box:
                if (mUserId == 0) {
                    login();
                    return;
                }
                if (mTreasureStatusMap == null) {
                    mLivePresenter.getTreasureBoxStatus(mUserId);
                    return;
                }
                if (mTreasureBoxDialog != null && mTreasureBoxDialog.isAdded()) {
                    return;
                }
                mTreasureBoxDialog = TreasureBoxDialog.newInstance(mTreasureStatusMap, mTime);
                mTreasureBoxDialog.setOnReceiveClick((int id) -> {
                    currentReceiveTreasureId = id;
                    mLivePresenter.receiveTreasure(mUserId);
                });
                mTreasureBoxDialog.setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            default:
                break;
        }

    }

    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("from", this.getClass().toString());
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    public void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo) {
        chatRoomPresenter.setupConnection(liveRoomTokenInfo, LiveDisplayActivity.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KickoutEvent kickoutEvent) {
        showToast(kickoutEvent.getNoChatMsg().getNochatType() == 8
                ? getString(R.string.kick_out_message)
                : getString(R.string.force_kick_out_message));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LuckGiftEvent luckGiftEvent) {
        if (mLuckyGiftControl == null) {
            mLuckyGiftControl = new LuckGiftControl(tvLuckyGift);
        }
        mLuckyGiftControl.load(luckGiftEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AnimEvent animEvent) {
        if ("TOTAl".equals(animEvent.getAnimJson().getAnimType())
                || "DIV".equals(animEvent.getAnimJson().getAnimType())) {
            AnimJson animJson = animEvent.getAnimJson();
            animJson.getContext().setGiftUrl(animEvent.getAnimUrl());
            if (giftControl == null) {
                giftControl = new GiftControl(LiveDisplayActivity.this);
                giftControl.setGiftLayout(llGiftContainer, 3);
            }
            giftControl.loadGift(animJson);
        } else {
            if ("MOBILE_GIFT_GIF".equals(animEvent.getAnimJson().getAnimType())
                    || "MOBILE_CAR_GIF".equals(animEvent.getAnimJson().getAnimType())) {
                if (mGifGiftControl == null) {
                    mGifGiftControl = new GifGiftControl(this, ivGiftGif);
                }
                mGifGiftControl.load(animEvent);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RunWayEvent runWayEvent) {
        initRunWay();
        mRunWayGiftControl.load(runWayEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserLevelChangeEvent userLevelChangeEvent) {
        initRunWayBroad();
        mRunWayBroadControl.load(userLevelChangeEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RoyalLevelChangeEvent royalLevelChangeEvent) {
        initRunWayBroad();
        mRunWayBroadControl.load(royalLevelChangeEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AnchorLevelChangeEvent anchorLevelChangeEvent) {
        initRunWayBroad();
        mRunWayBroadControl.load(anchorLevelChangeEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LuckGiftBigEvent luckGiftBigEvent) {
        initRunWayBroad();
        mRunWayBroadControl.load(luckGiftBigEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BroadCastBottomEvent broadCastBottomEvent) {
        initRunWayBroad();
        broadCastBottomEvent.setProgramId(mProgramId);
        mRunWayBroadControl.load(broadCastBottomEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PkEvent pkEvent) {
        PkJson.ContextBean bean = pkEvent.getPkJson().context;
        if (pkControl==null) {
            pkControl = new PkControl(pkLayout,this);
        }
        pkControl.setBean(bean);
        pkControl.setmAnchorId(mAnchorId);
        pkControl.setmProgramId(mProgramId);
        pkControl.setTvCountDown(tvCountDown);
        pkControl.init();
    }

    private void initRunWayBroad() {
        if (mRunWayBroadControl == null) {
            mRunWayBroadControl = new RunWayBroadControl(runWayBroad);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StartPlayEvent startPlayEvent) {
        StartStopLiveJson.ContextEntity context = startPlayEvent.getStartStopLiveJson().getContext();
        if (context.getHeight() != 0 && context.getWidth() != 0) {
            ratioLayout.setPicRatio(context.getWidth() / ((float) context.getHeight()));
        }
        mStream = startPlayEvent.getStreamAddress();
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
        mStream = null;
        textureView.stop();
        textureView.reset();
        textureView.setVisibility(View.INVISIBLE);
        tvStopTip.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateProgramEvent updateProgramEvent) {
        tvFansCount.setText(getString(R.string.subscriptions
                , updateProgramEvent.getmProgramJson().getContext().getProgram().getSubscriptionNum()));
    }

    @Override
    public void onRoomInfoSuccess(RoomInfoBean roomInfoBean) {
        mAnchorId = roomInfoBean.getData().getAnchor().getId();
        if (roomInfoBean.getData() != null) {
            tvFansCount.setText(getString(R.string.subscriptions, roomInfoBean.getData().getSubscriptionNum() + ""));
            ChatRoomInfo.getInstance().setRoomInfoBean(roomInfoBean);
            if (roomInfoBean.getData().getAnchor() != null) {
                mAnchor = roomInfoBean.getData().getAnchor();
                PrivateChatListFragment fragment = (PrivateChatListFragment) fragments[1];
                fragment.setUpWithAnchor(mAnchor);
                GlideImageLoader.getInstace().circleCropImage(this, mAnchor.getAvatar(), ivHostAvatar);
                tvHostName.setText(mAnchor.getName());
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
        tvPopularity.setText(getString(R.string.audience, count));
        mAudienceCount = count;
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
            mRoomUserInfo = data;
            if (data.getWeathMap() != null) {
                coin = data.getWeathMap().getCoin();
                LiveHouseUserInfoUpdateEvent liveHouseUserInfoUpdateEvent = new LiveHouseUserInfoUpdateEvent();
                liveHouseUserInfoUpdateEvent.coin = coin;
                EventBus.getDefault().post(liveHouseUserInfoUpdateEvent);
            }
            if (data.getGoodsList() != null) {
                for (int i = 0; i < data.getGoodsList().size(); i++) {
                    if ("GUARD".equals(data.getGoodsList().get(i).getGoodsType())) {
                        isGuard = true;
                        ((PrivateChatListFragment) fragments[1]).setIsGuard(isGuard);
                    }
                }
            }

        }
    }

    @Override
    public void onSendGiftSuccess() {
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetRunWayListSuccess(RunWayListBean runWayListBean) {
        if (runWayListBean != null && runWayListBean.list != null) {
            ArrayList<String> imageUrlList = new ArrayList<>();
            for (int i = 0; i < runWayListBean.list.size(); i++) {
                imageUrlList.add(runWayListBean.list.get(i).getGoodsPic());
            }
            DownloadImageFile downloadImageFile = new DownloadImageFile(imageSpanList -> {
                for (int i = 0; i < runWayListBean.list.size(); i++) {
                    RunWayJson runWayJson = new RunWayJson();
                    runWayJson.setContext(runWayListBean.list.get(i));
                    RunWayEvent event = new RunWayEvent(runWayJson, imageSpanList.get(i));
                    initRunWay();
                    mRunWayGiftControl.load(event);
                }
            });
            downloadImageFile.doDownload(imageUrlList, this);
        }
    }

    private void initRunWay() {
        if (mRunWayGiftControl == null) {
            mRunWayGiftControl = new RunWayGiftControl(runWayText);
            mRunWayGiftControl.setListener((programId, nickname) -> showJumpLiveHouseDialog(programId, nickname));
        }
    }

    @Override
    public void getGuardListSuccess(GuardListBean guardListBean) {
        if (mGuardList == null) {
            mGuardList = new ArrayList<>();
        }
        mGuardList.clear();
        if (guardListBean != null) {
            mGuardList.addAll(guardListBean.list);
        }
        mGuardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetProgramFirstSuccess(long userId) {
        ChatRoomInfo.getInstance().setProgramFirstId(userId);
    }

    @Override
    public void onTreasureSuccess(TreasureBoxStatusBean treasureBoxStatusBean) {
        mTreasureStatusMap = new HashMap();
        boolean hasTreasureCanReceive = false;
        if (treasureBoxStatusBean != null && treasureBoxStatusBean.list != null) {
            for (int i = 0; i < treasureBoxStatusBean.list.size(); i++) {
                TreasureBoxStatusBean.ListBean listBean = treasureBoxStatusBean.list.get(i);
//                if (listBean.maxOnlineTimes == 0) {
//                    if (listBean.taskId == 6) {
//                        hasTreasureCanReceive = true;
//                        mTreasureStatusMap.put(listBean.taskId, 1);
//                    } else {
//                        mTreasureStatusMap.put(listBean.taskId, 0);
//                    }
//                } else if (listBean.maxUngrandAwardTimes > 0 && listBean.maxUngrandAwardTimes == listBean.maxOnlineTimes) {
//                    mTreasureStatusMap.put(listBean.taskId, 3);
//                }
                if (listBean.taskId == 6) {
                    if (listBean.maxOnlineTimes == 0 && listBean.maxUngrandAwardTimes == 0) {
                        // 倒计时
                        mTreasureStatusMap.put(listBean.taskId, 0);
                    } else if (listBean.maxOnlineTimes == 0 && listBean.maxUngrandAwardTimes > 0) {
                        // 可领取
                        hasTreasureCanReceive = true;
                        mTreasureStatusMap.put(listBean.taskId, 1);
                    } else {
                        // 已领取
                        mTreasureStatusMap.put(listBean.taskId, 3);
                    }
                } else if (listBean.taskId == 7) {
                    if (listBean.maxOnlineTimes == 0 && listBean.maxUngrandAwardTimes == 0) {
                        // 倒计时
                        mTreasureStatusMap.put(listBean.taskId, 0);
                    } else if (listBean.maxOnlineTimes == 0 && listBean.maxUngrandAwardTimes > 0) {
                        // 可领取
                        hasTreasureCanReceive = true;
                        mTreasureStatusMap.put(listBean.taskId, 1);
                    } else {
                        // 已领取
                        mTreasureStatusMap.put(listBean.taskId, 3);
                    }

                } else if (listBean.taskId == 8) {
                    if (listBean.maxOnlineTimes == 0 && listBean.maxUngrandAwardTimes == 0) {
                        // 倒计时
                        mTreasureStatusMap.put(listBean.taskId, 0);
                    } else if (listBean.maxOnlineTimes == 0 && listBean.maxUngrandAwardTimes > 0) {
                        // 可领取
                        hasTreasureCanReceive = true;
                        mTreasureStatusMap.put(listBean.taskId, 1);
                    } else {
                        // 已领取
                        mTreasureStatusMap.put(listBean.taskId, 3);
                    }

                }
            }
        }
        if (hasTreasureCanReceive) {
            tvTreasureCount.setVisibility(View.VISIBLE);
            tvTreasureCount.setText(1 + "");
        } else {
            tvTreasureCount.setVisibility(View.GONE);
        }
        if (mTreasureStatusMap.get(6) == 0) {
            timer59(6);
        } else
            //1 可领 3 已领 0 倒计时
            if ((mTreasureStatusMap.get(6) == 3 && mTreasureStatusMap.get(7) == 0)) {
                timer(7);
            } else if ((mTreasureStatusMap.get(7) == 3 && mTreasureStatusMap.get(8) == 0)) {
                timer(8);
            }
    }

    @Override
    public void onReceiveTreasureSuccess() {
        showToast("领取成功");
        tvTreasureCount.setVisibility(View.GONE);
        mTreasureStatusMap.put(currentReceiveTreasureId, 3);
        if (currentReceiveTreasureId < 8) {
            timer(currentReceiveTreasureId + 1);
        } else {
            if (mTreasureBoxDialog != null && mTreasureBoxDialog.isAdded()) {
                mTreasureBoxDialog.setTreasureStatusMap(mTreasureStatusMap);
            }
        }
    }

    @Override
    public void onActivityListSuccess(GetActivityBean bean) {
        if (bean.list == null || bean.list.size() == 0) {
            return;
        }
        GlideImageLoader.getInstace().displayImage(this, bean.list.get(0).imageUrl, ibLuckWheel);
//        ibLuckWheel.setOnClickListener(v ->
// startActivityForResult(new Intent(getBaseActivity(), JsBridgeActivity.class)
//                .putExtra("anchorId", mAnchorId + "")
//                .putExtra("programId", mProgramId + "")
//                .putExtra("url", bean.list.get(0).linkUrl),REQUEST_LOGIN));
        ibLuckWheel.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                startActivityForResult(new Intent(getBaseActivity(), JsBridgeActivity.class)
                .putExtra("anchorId", mAnchorId + "")
                .putExtra("programId", mProgramId + "")
                .putExtra("url", bean.list.get(0).linkUrl),REQUEST_LOGIN);
            }
        });
    }

    @Override
    public void onPkInfoSuccess(PkInfoBean bean) {
        if (pkControl==null) {
            pkControl = new PkControl(pkLayout,this);
        }
        pkControl.setmAnchorId(mAnchorId);
        pkControl.setmProgramId(mProgramId);
        pkControl.setTvCountDown(tvCountDown);
        pkControl.initNet(bean);
    }

    private void timer(int id) {
        mTreasureODisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    mTime = aLong;
                    tvTreasureTimer.setText(getString(R.string.two, (599 - aLong) / 60, (599 - aLong) % 60));
                    if (aLong == 599) {
                        mTreasureODisposable.dispose();
                        mTreasureStatusMap.put(id, 1);
                        tvTreasureCount.setVisibility(View.VISIBLE);
                        tvTreasureCount.setText("1");
                        tvTreasureTimer.setText("开宝箱");
                        if (mTreasureBoxDialog != null && mTreasureBoxDialog.isAdded()) {
                            mTreasureBoxDialog.setTreasureStatusMap(mTreasureStatusMap);
                        }
                    }
                    if (mTreasureBoxDialog != null && mTreasureBoxDialog.isAdded()) {
                        mTreasureBoxDialog.setmTime(aLong);
                    }
                });
    }

    private void timer59(int id) {
        mTreasureODisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    mTime = aLong;
                    tvTreasureTimer.setText(getString(R.string.two, (59 - aLong) / 60, (59 - aLong) % 60));
                    if (aLong == 59) {
                        mTreasureODisposable.dispose();
                        mTreasureStatusMap.put(id, 1);
                        tvTreasureCount.setVisibility(View.VISIBLE);
                        tvTreasureCount.setText("1");
                        tvTreasureTimer.setText("开宝箱");
                        if (mTreasureBoxDialog != null && mTreasureBoxDialog.isAdded()) {
                            mTreasureBoxDialog.setTreasureStatusMap(mTreasureStatusMap);
                        }
                    }
                    if (mTreasureBoxDialog != null && mTreasureBoxDialog.isAdded()) {
                        mTreasureBoxDialog.setmTime(aLong);
                    }
                });
    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        mGiftData = giftInfo;
    }

    public void sendMessage(String message, RoomUserInfo.DataBean chatToUser) {
        if (chatToUser == null) {
            chatRoomPresenter.sendMessage(message);
        } else {
            chatRoomPresenter.sendPrivateMessage(chatToUser, message);
        }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfoUpdateEvent event) {
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GuardOpenEvent event) {
        if (event.userId == mUserId) {
            mLivePresenter.getGuardList(mProgramId);
            mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
        }
        showGuard(event.avatar, event.nickName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PrivateChatSelectedEvent event) {
        setTabChange(1);
    }

    public void showGuard(String avatar, String nickName) {
        GlideImageLoader.getInstace().displayImage(this, avatar, ivGuardAvatar);
        tvGuardNickName.setText(nickName);
        showGuardAnim = ObjectAnimator.ofFloat(rlGuardSuccess, "alpha", 0f, 1f);
        showGuardAnim.setDuration(3000);
        showGuardAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hideGuard();
                showGuardAnim = null;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                rlGuardSuccess.setVisibility(View.VISIBLE);
            }
        });
        showGuardAnim.start();
    }

    public void showAudienceInfoDialog(long viewedUserID, boolean isShowBottom) {
        AudienceInfoDialog.newInstance(viewedUserID, mProgramId, mRoomUserInfo)
                .setListener(() -> {
                    if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
                        mGuardListDialog.dismiss();
                    }
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .setDimAmount(0)
                .setShowBottom(isShowBottom)
                .show(getSupportFragmentManager());
    }

    public void showAudienceInfoDialog(String nickName) {
        AudienceInfoDialog.newInstance(nickName, mProgramId, mRoomUserInfo)
                .setListener(() -> {
                    if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
                        mGuardListDialog.dismiss();
                    }
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .setDimAmount(0)
                .show(getSupportFragmentManager());
    }

    private void hideGuard() {
        hideGuardAnim = ObjectAnimator.ofFloat(rlGuardSuccess, "alpha", 1f, 0f);
        hideGuardAnim.setDuration(3000);
        hideGuardAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rlGuardSuccess.setVisibility(View.VISIBLE);
                hideGuardAnim = null;
            }
        });
        hideGuardAnim.setStartDelay(3000);
        hideGuardAnim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            destroy();
            return;
        }
        if (textureView != null) {
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

    @Override
    protected void onDestroy() {
        if (textureView != null) {
            textureView.stop();
            textureView.release();
            textureView = null;
        }
        mLivePresenter.onDestory();
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void destroy() {

        if (mGifGiftControl != null) {
            mGifGiftControl.destroy();
        }
        if (giftControl != null) {
            giftControl.destroy();
        }
        if (mRunWayGiftControl != null) {
            mRunWayGiftControl.destroy();
        }
        if (mLuckyGiftControl != null) {
            mLuckyGiftControl.destroy();
        }
        if (showGuardAnim != null) {
            showGuardAnim.cancel();
            showGuardAnim = null;
        }
        if (hideGuardAnim != null) {
            hideGuardAnim.cancel();
            hideGuardAnim = null;
        }
        if (chatRoomPresenter != null) {
            chatRoomPresenter.onChatRoomDestroy();
        }
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        if (mTreasureODisposable != null) {
            mTreasureODisposable.dispose();
        }
        pkLayout.destroy();
        if (pkControl!=null) {
            pkControl.destroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                mUserId = (long) SPUtils.get(this, "userId", 0L);
                mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
                getRoomToken();
                mLivePresenter.getTreasureBoxStatus(mUserId);
            }
        }
    }

    private void showJumpLiveHouseDialog(int programId, String nickName) {
        if (programId == mProgramId) {
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.alert);
        dialog.setMessage(getString(R.string.jump_live_house, nickName));
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.setPositiveButton(R.string.confirm, (dialog1, which) -> {
            Intent intent = new Intent(LiveDisplayActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, programId);
            startActivity(intent);
        });
        dialog.show();
    }

}
