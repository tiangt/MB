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
import android.text.SpannableString;
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
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.chat.room.message.events.StartPlayEvent;
import com.whzl.mengbi.chat.room.message.events.StopPlayEvent;
import com.whzl.mengbi.chat.room.message.events.UpdateProgramEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.message.messageJson.RunWayJson;
import com.whzl.mengbi.chat.room.message.messageJson.StartStopLiveJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.DownloadEvent;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.LiveHouseUserInfoUpdateEvent;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.gift.GifGiftControl;
import com.whzl.mengbi.gift.GiftControl;
import com.whzl.mengbi.gift.LuckGiftControl;
import com.whzl.mengbi.gift.RunWayGiftControl;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.chat.room.message.events.GuardOpenEvent;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
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
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.fragment.ChatListFragment;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.RatioRelativeLayout;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
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

//     1、vip、守护、贵族、主播、运管不受限制
//        2、名士5以上可以私聊，包含名士5

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_live_display_new);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initEnv();
        setupContentView();
        setupContentView();
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
                showToast("网络连接断开，请检测网络设置");
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        registerReceiver(mReceiver, intentFilter);
    }

    private void initPlayer() {
        textureView.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        textureView.setOnPreparedListener(iMediaPlayer -> {
            progressBar.setVisibility(View.GONE);
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
        setBottomContainerHieght(index == 0 ? 50 : 0);
        currentSelectedIndex = index;
    }

    public void setBottomContainerHieght(int dpHeight) {
        ViewGroup.LayoutParams layoutParams = rlBottomContainer.getLayoutParams();
        layoutParams.height = UIUtil.dip2px(this, dpHeight);
        rlBottomContainer.post(new Runnable() {
            @Override
            public void run() {
                rlBottomContainer.setLayoutParams(layoutParams);
                rlBottomContainer.requestLayout();
            }
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
            mGuardListDialog = GuardListDialog.newInstance(mProgramId, mAnchor, 0)
                    .setShowBottom(true)
                    .setDimAmount(0)
                    .show(getSupportFragmentManager());
        }
    }

    @Override
    protected void loadData() {
        getRoomToken();
        mLivePresenter.getRoomInfo(mProgramId);
        SPUtils.put(this, SpConfig.KEY_PRIVATE_CHAT, false);
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
        mLivePresenter.getRunWayList(ParamsUtils.getSignPramsMap(new HashMap<>()));
        mDisposable = Observable.interval(0, 20, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mLivePresenter.getAudienceAccount(mProgramId);
        });
        mLivePresenter.getGuardList(mProgramId);
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
            , R.id.fragment_container})
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
                if (!UserIdentity.getCanChatPaivate(mRoomUserInfo)) {
                    showToast("当前用户没有私聊权限");
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
                if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
                    return;
                }
                mGuardListDialog = GuardListDialog.newInstance(mProgramId, mAnchor, 1)
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
        showToast(kickoutEvent.getNoChatMsg().getNochatType() == 8 ? "你已经被踢出踢出直播间" : "用多个手机打开同一个直播间，强制退出之前的直播间");
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
        if ("TOTAl".equals(animEvent.getAnimJson().getAnimType())) {
            AnimJson animJson = animEvent.getAnimJson();
            animJson.getContext().setGiftUrl(animEvent.getAnimUrl());
            if (giftControl == null) {
                giftControl = new GiftControl(LiveDisplayActivity.this);
                giftControl.setGiftLayout(llGiftContainer, 3).setHideMode(false);
            }
            giftControl.loadGift(animJson.getContext());
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
        tvStopTip.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateProgramEvent updateProgramEvent) {
        tvFansCount.setText("粉丝：" + updateProgramEvent.getmProgramJson().getContext().getProgram().getSubscriptionNum());
    }


    @Override
    public void onRoomInfoSuccess(RoomInfoBean roomInfoBean) {
        mAnchorId = roomInfoBean.getData().getAnchor().getId();
        if (roomInfoBean.getData() != null) {
            tvFansCount.setText("粉丝：" + roomInfoBean.getData().getSubscriptionNum() + "");
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
            DownloadImageFile downloadImageFile = new DownloadImageFile(new DownloadEvent() {
                @Override
                public void finished(List<SpannableString> imageSpanList) {
                    for (int i = 0; i < runWayListBean.list.size(); i++) {
                        RunWayJson runWayJson = new RunWayJson();
                        runWayJson.setContext(runWayListBean.list.get(i));
                        RunWayEvent event = new RunWayEvent(runWayJson, imageSpanList.get(i));
                        initRunWay();
                        mRunWayGiftControl.load(event);
                    }
                }
            });
            downloadImageFile.doDownload(imageUrlList, this);
        }
    }

    private void initRunWay() {
        if (mRunWayGiftControl == null) {
            mRunWayGiftControl = new RunWayGiftControl(runWayText);
            mRunWayGiftControl.setListener(new RunWayGiftControl.OnClickListener() {
                @Override
                public void onClick(int programId, String nickname) {
                    showJumpLiveHouseDialog(programId, nickname);
                }
            });
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

    public void showAudienceInfoDialog(long viewedUserID) {
        AudienceInfoDialog.newInstance(viewedUserID, mProgramId, mRoomUserInfo)
                .setListener(() -> {
                    if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
                        mGuardListDialog.dismiss();
                    }
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .setDimAmount(0)
                .setShowBottom(true)
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
        ondestory();
        super.onDestroy();
    }

    private void ondestory() {
        if (mGifGiftControl != null) {
            mGifGiftControl.destroy();
        }
        if (giftControl != null) {
            giftControl.cleanAll();
        }
        if (mRunWayGiftControl != null) {
            mRunWayGiftControl.destroy();
        }
        if (mLuckyGiftControl != null) {
            mLuckyGiftControl.destroy();
        }
        if (textureView != null) {
            textureView.stop();
            textureView.release();
            textureView = null;
        }
        if (showGuardAnim != null) {
            showGuardAnim.cancel();
            showGuardAnim = null;
        }
        if (hideGuardAnim != null) {
            hideGuardAnim.cancel();
            hideGuardAnim = null;
        }
        mGiftDialog = null;
        mLivePresenter.onDestory();
        if (chatRoomPresenter != null) {
            chatRoomPresenter.onChatRoomDestroy();
        }
        mDisposable.dispose();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                mUserId = (long) SPUtils.get(this, "userId", 0L);
                mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
                getRoomToken();
            }
        }
    }

    private void showJumpLiveHouseDialog(int programId, String nickName) {
        if (programId == mProgramId) {
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否离开当前直播间\n跳转到" + nickName + "直播间");
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", (dialog1, which) -> {
            Intent intent = new Intent(LiveDisplayActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, programId);
            startActivity(intent);
            finish();
        });
        dialog.show();
    }
}
