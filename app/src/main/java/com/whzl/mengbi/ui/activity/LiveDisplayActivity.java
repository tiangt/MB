package com.whzl.mengbi.ui.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.jaeger.library.StatusBarUtil;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.umeng.socialize.UMShareAPI;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.chat.room.message.events.AnchorLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.events.BetsEndEvent;
import com.whzl.mengbi.chat.room.message.events.BroadCastBottomEvent;
import com.whzl.mengbi.chat.room.message.events.EverydayEvent;
import com.whzl.mengbi.chat.room.message.events.FirstPrizeUserEvent;
import com.whzl.mengbi.chat.room.message.events.GuardOpenEvent;
import com.whzl.mengbi.chat.room.message.events.HeadLineEvent;
import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftBigEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.message.events.PkEvent;
import com.whzl.mengbi.chat.room.message.events.PlayNotifyEvent;
import com.whzl.mengbi.chat.room.message.events.PrizePoolFullEvent;
import com.whzl.mengbi.chat.room.message.events.RedPackTreasureEvent;
import com.whzl.mengbi.chat.room.message.events.RoyalLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.chat.room.message.events.SendBroadEvent;
import com.whzl.mengbi.chat.room.message.events.StartPlayEvent;
import com.whzl.mengbi.chat.room.message.events.StopPlayEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatEvent;
import com.whzl.mengbi.chat.room.message.events.UpdateProgramEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.events.UserLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.WeekStarEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.message.messageJson.PlayNotifyJson;
import com.whzl.mengbi.chat.room.message.messageJson.StartStopLiveJson;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.message.messages.PkMessage;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.LiveHouseUserInfoUpdateEvent;
import com.whzl.mengbi.eventbus.event.LivePkEvent;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.eventbus.event.SendGiftSuccessEvent;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.gen.CommonGiftDao;
import com.whzl.mengbi.gen.PrivateChatContentDao;
import com.whzl.mengbi.gen.PrivateChatUserDao;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.gift.GifGiftControl;
import com.whzl.mengbi.gift.GiftControl;
import com.whzl.mengbi.gift.HeadLineControl;
import com.whzl.mengbi.gift.LuckGiftControl;
import com.whzl.mengbi.gift.PkControl;
import com.whzl.mengbi.gift.RedPackRunWayControl;
import com.whzl.mengbi.gift.RoyalEnterControl;
import com.whzl.mengbi.gift.RunWayBroadControl;
import com.whzl.mengbi.gift.RunWayGiftControl;
import com.whzl.mengbi.gift.SvgaGiftControl;
import com.whzl.mengbi.gift.WeekStarControl;
import com.whzl.mengbi.greendao.CommonGift;
import com.whzl.mengbi.greendao.CommonGiftBean;
import com.whzl.mengbi.greendao.PrivateChatContent;
import com.whzl.mengbi.greendao.PrivateChatUser;
import com.whzl.mengbi.greendao.User;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GetDailyTaskStateBean;
import com.whzl.mengbi.model.entity.GetUserSetBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PunishWaysBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomRedPackTreasure;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.RunwayBean;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.receiver.NetStateChangeReceiver;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.control.RedPacketControl;
import com.whzl.mengbi.ui.dialog.AudienceInfoDialog;
import com.whzl.mengbi.ui.dialog.FreeGiftDialog;
import com.whzl.mengbi.ui.dialog.GiftDialog;
import com.whzl.mengbi.ui.dialog.GuardianListDialog;
import com.whzl.mengbi.ui.dialog.HeadlineDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseChatDialog;
import com.whzl.mengbi.ui.dialog.LoginDialog;
import com.whzl.mengbi.ui.dialog.PersonalInfoDialog;
import com.whzl.mengbi.ui.dialog.PrivateChatDialog;
import com.whzl.mengbi.ui.dialog.PrivateChatListDialog;
import com.whzl.mengbi.ui.dialog.ShareDialog;
import com.whzl.mengbi.ui.dialog.UserListDialog;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseFullScreenDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.fragment.AnchorTaskFragment;
import com.whzl.mengbi.ui.fragment.ChatListFragment;
import com.whzl.mengbi.ui.fragment.LiveWebFragment;
import com.whzl.mengbi.ui.fragment.LiveWeekRankFragment;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.widget.loading.LoadLayout;
import com.whzl.mengbi.ui.widget.recyclerview.AutoPollAdapter;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView2;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView3;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.HeadLineView;
import com.whzl.mengbi.ui.widget.view.HeadlineLayout;
import com.whzl.mengbi.ui.widget.view.PkLayout;
import com.whzl.mengbi.ui.widget.view.RatioRelativeLayout;
import com.whzl.mengbi.ui.widget.view.RoyalEnterView;
import com.whzl.mengbi.ui.widget.view.WeekStarView;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.BitmapUtils;
import com.whzl.mengbi.util.HttpCallBackListener;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.util.zxing.NetUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class LiveDisplayActivity extends BaseActivity implements LiveView {
    @BindView(R.id.iv_host_avatar)
    CircleImageView ivHostAvatar;
    @BindView(R.id.tv_host_name)
    AutoScrollTextView3 tvHostName;
    @BindView(R.id.btn_follow)
    TextView btnFollow;
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
    @BindView(R.id.btn_close)
    ImageButton btnClose;
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
    @BindView(R.id.audience_recycler)
    RecyclerView mAudienceRecycler;
    @BindView(R.id.view_message_notify)
    View viewMessageNotify;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.rl_bottom_container)
    RelativeLayout rlBottomContainer;
    @BindView(R.id.btn_chat)
    ImageButton btnChat;
    @BindView(R.id.btn_chat_private)
    ImageButton btnChatPrivate;
    @BindView(R.id.tv_run_way_broad)
    AutoScrollTextView2 runWayBroad;
    @BindView(R.id.pk_layout)
    PkLayout pkLayout;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.vp_activity)
    ViewPager vpActivity;
    @BindView(R.id.frame_supercar_track)
    FrameLayout frameSupercarTrack;
    @BindView(R.id.iv_live_rocket)
    ImageView ivRocket;
    @BindView(R.id.tv_guard_count)
    TextView tvGuardCount;
    @BindView(R.id.ll_enter)
    LinearLayout llEnter;
    @BindView(R.id.tv_enter)
    RoyalEnterView tvEnter;
    @BindView(R.id.svga_start_pk)
    SVGAImageView svgaStartPk;
    @BindView(R.id.cl_entenr)
    ConstraintLayout clEnter;
    @BindView(R.id.iv_count_down)
    ImageView ivCountDown;
    @BindView(R.id.rl_other_side)
    RelativeLayout rlOtherSide;
    @BindView(R.id.texture_view2)
    KSYTextureView textureView2;
    @BindView(R.id.circle_other_side)
    CircleImageView ivOtherSide;
    @BindView(R.id.tv_other_side)
    TextView tvOtherSide;
    @BindView(R.id.rl_guard_number)
    RelativeLayout rlGuardNumber;
    @BindView(R.id.rl_other_side_info)
    RelativeLayout rlOtherSideInfo;
    @BindView(R.id.btn_share)
    ImageButton btnShare;
    @BindView(R.id.ll_pager_index)
    LinearLayout llPagerIndex;
    @BindView(R.id.btn_free_gift)
    ImageButton btnFreeGift;
    @BindView(R.id.iv_weekstar)
    GifImageView ivWeekstar;
    @BindView(R.id.wsv_weekstar)
    WeekStarView wsvWeekstar;
    @BindView(R.id.cl_weekstar)
    ConstraintLayout clWeekstar;
    @BindView(R.id.rl_weekstar)
    RelativeLayout rlWeekstar;
    @BindView(R.id.btn_more)
    ImageButton btnMore;
    @BindView(R.id.draw_layout_out_live)
    DrawerLayout drawerLayoutOut;
    @BindView(R.id.head_line)
    HeadLineView headLineView;
    @BindView(R.id.draw_layout_include_live)
    NestedScrollView drawLayoutInclude;
    @BindView(R.id.hl_layout)
    HeadlineLayout hlLayout;
    @BindView(R.id.svga_guard_success)
    SVGAImageView svgaGuardSuccess;
    @BindView(R.id.ll_black_room)
    LinearLayout llBlackRoom;
    @BindView(R.id.tv_time_black_room)
    TextView tvTimeBlackRoom;
    @BindView(R.id.svga_gift)
    SVGAImageView svgaGift;
    @BindView(R.id.ll_red_bag)
    LinearLayout llRedBag;
    @BindView(R.id.tv_red_bag)
    TextView tvRedBag;
    @BindView(R.id.tv_red_bag_run_way)
    TextView tvRedBagRunWay;
    @BindView(R.id.rv_redpack)
    RecyclerView rvRedPack;
    @BindView(R.id.chatActivityContainer)
    RelativeLayout chatActivityContainer;
    @BindView(R.id.ll_loading)
    LoadLayout loadLayout;

    private LivePresenterImpl mLivePresenter;
    public int mProgramId;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private GiftInfo mGiftData;
    public long mUserId;
    private BaseAwesomeDialog mGiftDialog;
    public int mAnchorId = 0;
    private int REQUEST_LOGIN = 120;
    private long coin;
    private GiftControl giftControl;
    private String mStream;
    private RunWayGiftControl mRunWayGiftControl;
    private LuckGiftControl mLuckyGiftControl;
    private GifGiftControl mGifGiftControl;
    private SvgaGiftControl mSvgaGiftControl;
    private RoomInfoBean.DataBean.AnchorBean mAnchor;
    public boolean isGuard;
    private boolean isVip;
    private int currentSelectedIndex;
    private Fragment[] fragments;
    private ObjectAnimator showGuardAnim;
    private ObjectAnimator hideGuardAnim;
    private RoomUserInfo.DataBean mRoomUserInfo;
    private NetStateChangeReceiver mReceiver;
    private BaseAwesomeDialog mRankDialog;
    private BaseAwesomeDialog mChatDialog;
    private BaseAwesomeDialog mGuardListDialog;
    private BaseAwesomeDialog mShareDialog;
    private long mAudienceCount;
    private RunWayBroadControl mRunWayBroadControl;
    private PkControl pkControl;
    /**
     * 抽奖活动
     */
    private List<GetActivityBean.ListBean> mBannerInfoList;
    private ArrayList<Fragment> mActivityGrands = new ArrayList<>();
    private FragmentPagerAdaper mGrandAdaper;
    private AutoPollAdapter pollAdapter;
    private ArrayList<AudienceListBean.AudienceInfoBean> mAudienceList = new ArrayList<>();
    private RoyalEnterControl royalEnterControl;
    private Disposable roomRankTotalDisposable;
    private Disposable roomOnlineDisposable;
    private NetStateChangeReceiver mPkReceiver;
    private String pkStream;
    private BaseAwesomeDialog mFreeGiftDialog;
    private String mAnchorCover;
    private String mShareUrl;
    private String mLiveState;
    private String mIsFollowed;
    private WeekStarControl weekStarControl;
    private LiveWeekRankFragment weekRankFragment;
    private BaseFullScreenDialog mGuardianDialog;
    private BaseFullScreenDialog mUserListDialog;
    private String mRanking;
    private DrawLayoutControl drawLayoutControl;
    private BaseAwesomeDialog headlineDialog;
    public String mAnchorName;
    public String mAnchorAvatar;
    private String mHeadlineRank;
    public int anchorLevel;
    private Disposable headlineDisposable;
    private int totalHours;
    private Disposable blackRoomDisposable;
    private HeadLineControl headLineControl;
    private boolean ignoreChat = false;
    private boolean playNotify = false;
    private PopupWindow redPopupWindow;
    private RedPackRunWayControl redPackRunWayControl;
    private RedPacketControl redPacketControl;
    private CompositeDisposable compositeDisposable;
    private BaseAwesomeDialog privateChatListDialog;
    private BaseAwesomeDialog awesomeDialog;
    private BaseAwesomeDialog personalInfoDialog;

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
        textureView2.stop();
        textureView2.reset();

        textureView.setVisibility(View.INVISIBLE);
        textureView2.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
        loadLayout.setVisibility(View.VISIBLE);

        if (currentSelectedIndex == 1) {
            currentSelectedIndex = 0;
            setBottomContainerHeight(50);
            chatActivityContainer.setVisibility(View.VISIBLE);
        }

        mProgramId = intent.getIntExtra(BundleConfig.PROGRAM_ID, -1);
        SPUtils.put(this, "programId", mProgramId);
        chatRoomPresenter = new ChatRoomPresenterImpl(mProgramId + "");
        isGuard = false;
        isVip = false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragments[0]);
//        fragmentTransaction.remove(fragments[1]);
        fragmentTransaction.commit();
        initFragment();
        loadData();
    }

    @Override
    protected void initEnv() {
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
        initPkNetwork();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#181818"));
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
//                progressBar.setVisibility(View.VISIBLE);
                loadLayout.setVisibility(View.VISIBLE);
            }
        });
        registerReceiver(mReceiver, intentFilter);
    }

    private void initPkNetwork() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mPkReceiver = new NetStateChangeReceiver();
        mPkReceiver.setEvevt(netMobile -> {
            if (netMobile != NetUtils.NETWORK_NONE) {
                if (textureView2 != null && pkStream != null) {
                    textureView2.softReset();
                    try {
                        textureView2.setDataSource(pkStream);
                        textureView2.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                showToast(R.string.net_error);
            }
        });
        registerReceiver(mPkReceiver, intentFilter);
    }

    private void initPlayer() {
        textureView.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        textureView.setOnPreparedListener(iMediaPlayer -> {
            textureView.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
            loadLayout.setVisibility(View.GONE);
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
//                    progressBar.setVisibility(View.VISIBLE);
                    loadLayout.setVisibility(View.VISIBLE);
                    break;
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
//                    progressBar.setVisibility(View.GONE);
                    loadLayout.setVisibility(View.GONE);
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
        compositeDisposable = new CompositeDisposable();
        drawerLayoutOut.setScrimColor(Color.TRANSPARENT);
        initPlayer();
        initFragment();
        initBanner();
        initVp();
        initProtectRecycler();
        initDrawLayout(this);
        initRvRedpack();
    }

    private void initRvRedpack() {
        redPacketControl = new RedPacketControl(this, rvRedPack);
        redPacketControl.init();
    }


    /**
     * 头条榜单
     */
    private void initHeadline() {
        String[] lines = {mRanking, mHeadlineRank};
        setHeadLine(lines);
    }

    /**
     * 侧滑菜单
     */
    private void initDrawLayout(Activity liveDisplayActivity) {
        drawLayoutControl = new DrawLayoutControl(liveDisplayActivity, drawLayoutInclude);
        drawLayoutControl.init();
        drawerLayoutOut.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (pkLayout.popupWindow != null && pkLayout.popupWindow.isShowing()) {
                    pkLayout.popupWindow.dismiss();
                    pkLayout.tvFansRank.setText("点击打开助力粉丝榜");
                }
            }
        });

    }


    private void initProtectRecycler() {
        mAudienceRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pollAdapter = new AutoPollAdapter(mAudienceList, this);
        pollAdapter.setListerner(position -> {
            AudienceListBean.AudienceInfoBean audienceInfoBean = mAudienceList.get(position + 1);
            long userId = audienceInfoBean.getUserid();
            if (personalInfoDialog != null && personalInfoDialog.isAdded()) {
                return;
            }
            personalInfoDialog = PersonalInfoDialog.newInstance(mRoomUserInfo, userId, mProgramId, mUserId)
                    .setListener((RoomUserInfo.DataBean mViewedUser) -> {
                        if (mUserListDialog != null && mUserListDialog.isAdded()) {
                            mUserListDialog.dismiss();
                        }
                        if (mRankDialog != null && mRankDialog.isAdded()) {
                            mRankDialog.dismiss();
                        }
                        if (mGuardianDialog != null && mGuardianDialog.isAdded()) {
                            mGuardianDialog.dismiss();
                        }
                        if (headlineDialog != null && headlineDialog.isAdded()) {
                            headlineDialog.dismiss();
                        }
                        if (mViewedUser != null) {
                            PrivateChatUser chatUser = new PrivateChatUser();
                            chatUser.setPrivateUserId(mViewedUser.getUserId());
                            chatUser.setAvatar(mViewedUser.getAvatar());
                            chatUser.setName(mViewedUser.getNickname());
                            showPrivateChatDialog(chatUser);
                        }
                    })
                    .setAnimStyle(R.style.dialog_enter_from_bottom_out_from_top)
                    .setDimAmount(0)
                    .setOutCancel(false)
                    .show(getSupportFragmentManager());

        });
        mAudienceRecycler.setAdapter(pollAdapter);
    }


    /**
     * 活动viewpager
     */
    private void initVp() {
        mGrandAdaper = new FragmentPagerAdaper(getSupportFragmentManager(), mActivityGrands);
        vpActivity.setAdapter(mGrandAdaper);
        vpActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mActivityGrands.size() < 2) {
                    return;
                }
                for (int i = 0; i < mActivityGrands.size(); i++) {
                    llPagerIndex.getChildAt(i).setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(GlideImageLoader.getInstace());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(position -> {
            if (mBannerInfoList != null && mBannerInfoList.size() > 0) {
                GetActivityBean.ListBean listBean = mBannerInfoList.get(position);
                jumpToBannerActivity(listBean);
            }
        });
    }

    public void jumpToBannerActivity(GetActivityBean.ListBean listBean) {
        startActivityForResult(new Intent(getBaseActivity(), JsBridgeActivity.class)
                .putExtra("anchorId", mAnchorId + "")
                .putExtra("programId", mProgramId + "")
                .putExtra("title", listBean.name)
                .putExtra("url", listBean.linkUrl), REQUEST_LOGIN);
    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ChatListFragment chatListFragment = ChatListFragment.newInstance(mProgramId);
        fragments = new Fragment[]{chatListFragment};
        fragmentTransaction.add(R.id.fragment_container, fragments[0]);
        fragmentTransaction.commit();
    }

    public void setTabChange(int index) {
        if (index == currentSelectedIndex) {
            return;
        }
        if (index == 1) {
//            viewMessageNotify.setVisibility(View.GONE);
            chatActivityContainer.setVisibility(View.GONE);
        } else {
            chatActivityContainer.setVisibility(View.VISIBLE);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
        });
    }


    public void showMessageNotify() {
        viewMessageNotify.setVisibility(View.VISIBLE);
    }

    private int getMarqueeWidth() {
        int w = View.MeasureSpec.makeMeasureSpec(50, View.MeasureSpec.EXACTLY);
        int h = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        tvHostName.measure(w, h);
        int height = tvHostName.getMeasuredHeight();
        int width = tvHostName.getMeasuredWidth();
        return width;
    }

    @Override
    protected void loadData() {
        mLivePresenter.getProgramFirst(mProgramId);
        getRoomToken();
        mLivePresenter.getRoomInfo(mProgramId);
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
        mLivePresenter.getRunWayList(ParamsUtils.getSignPramsMap(new HashMap<>()));
        mLivePresenter.getActivityList();
        mLivePresenter.getPkInfo(mProgramId);
        roomOnlineDisposable = Observable.interval(0, 60, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mLivePresenter.getAudienceList(mProgramId);
        });
        compositeDisposable.add(roomOnlineDisposable);
        mLivePresenter.getGuardTotal(mProgramId);
        roomRankTotalDisposable = Observable.interval(0, 60, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mLivePresenter.getRoomRankTotal(mProgramId, "day");
        });
        compositeDisposable.add(roomRankTotalDisposable);
        if (mUserId == 0) {
            btnFreeGift.setImageResource(R.drawable.ic_live_free_gift);
        } else {
            mLivePresenter.getDailyTaskState(mUserId);
        }
        mLivePresenter.getUserSet(mUserId);
        mLivePresenter.getRedPackTreasure(mProgramId);
        mLivePresenter.getRedPackList(mProgramId, mUserId);
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

    @OnClick({R.id.iv_host_avatar, R.id.btn_follow, R.id.btn_close, R.id.btn_send_gift
            , R.id.tv_popularity, R.id.btn_chat, R.id.btn_chat_private
            , R.id.rootView, R.id.fragment_container, R.id.rl_guard_number
            , R.id.btn_share, R.id.btn_free_gift, R.id.btn_more, R.id.ll_black_room, R.id.ll_red_bag})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_host_avatar:
                if (personalInfoDialog != null && personalInfoDialog.isAdded()) {
                    return;
                }
                personalInfoDialog = PersonalInfoDialog.newInstance(mRoomUserInfo, mAnchorId, mProgramId, mUserId, mIsFollowed, mLiveState)
                        .setListener((RoomUserInfo.DataBean mViewedUser) -> {
                            if (mUserListDialog != null && mUserListDialog.isAdded()) {
                                mUserListDialog.dismiss();
                            }
                            if (mRankDialog != null && mRankDialog.isAdded()) {
                                mRankDialog.dismiss();
                            }
                            if (mGuardianDialog != null && mGuardianDialog.isAdded()) {
                                mGuardianDialog.dismiss();
                            }
                            if (headlineDialog != null && headlineDialog.isAdded()) {
                                headlineDialog.dismiss();
                            }
                            if (mViewedUser != null) {
                                PrivateChatUser chatUser = new PrivateChatUser();
                                chatUser.setPrivateUserId(mViewedUser.getUserId());
                                chatUser.setAvatar(mViewedUser.getAvatar());
                                chatUser.setName(mViewedUser.getNickname());
                                showPrivateChatDialog(chatUser);
                            }
                        })
                        .setAnimStyle(R.style.dialog_enter_from_bottom_out_from_top)
                        .setDimAmount(0)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                //主播信息
                break;
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
                if (mUserId == 0 && ignoreChat) {
                    login();
                    return;
                }
                if (mChatDialog != null && mChatDialog.isAdded()) {
                    return;
                }
                mChatDialog = LiveHouseChatDialog.newInstance(isGuard, isVip, mProgramId, mAnchor)
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
//                setTabChange(1);
                PrivateChatUser user = new PrivateChatUser();
                user.setPrivateUserId(Long.valueOf(mAnchor.getId()));
                user.setName(mAnchor.getName());
                user.setAvatar(mAnchor.getAvatar());
                showPrivateChatListDialog(user);
                break;
            case R.id.rootView:
                if (currentSelectedIndex == 1) {
//                    setTabChange(0);
                }
                break;
            case R.id.btn_send_gift:
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
                if (mUserListDialog != null && mUserListDialog.isAdded()) {
                    return;
                }

                mUserListDialog = UserListDialog.newInstance(mProgramId)
                        .setAnimStyle(R.style.dialog_enter_from_right_out_from_right)
                        .show(getSupportFragmentManager());
                break;
            case R.id.rl_guard_number:
                if (mGuardianDialog != null && mGuardianDialog.isAdded()) {
                    return;
                }
                mGuardianDialog = GuardianListDialog.newInstance(mProgramId, mAnchor)
                        .setAnimStyle(R.style.dialog_enter_from_right_out_from_right)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_share:
                if (mShareDialog != null && mShareDialog.isAdded()) {
                    return;
                }
                mShareDialog = ShareDialog.newInstance(mProgramId, mAnchor, mAnchorCover, mShareUrl)
                        .setShowBottom(true)
                        .setDimAmount(0)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_free_gift:
                if (mUserId == 0) {
                    login();
                    return;
                }
                if (mFreeGiftDialog != null && mFreeGiftDialog.isAdded()) {
                    return;
                }
                mFreeGiftDialog = FreeGiftDialog.newInstance(mProgramId, mAnchorId)
                        .setShowBottom(true)
                        .setDimAmount(0);
                ((FreeGiftDialog) mFreeGiftDialog).setListener(() -> mLivePresenter.getDailyTaskState(mUserId));
                mFreeGiftDialog.show(getSupportFragmentManager());
                break;
            case R.id.btn_more:
                if (drawerLayoutOut.isDrawerOpen(drawLayoutInclude)) {
                    drawerLayoutOut.closeDrawer(drawLayoutInclude);
                } else {
                    drawerLayoutOut.openDrawer(drawLayoutInclude);
                }
                break;
            case R.id.ll_black_room:
                if (mUserId == 0) {
                    login();
                    return;
                }
                startActivity(new Intent(LiveDisplayActivity.this, PkRecordActivity.class)
                        .putExtra("anchorLever", anchorLevel)
                        .putExtra("anchorName", mAnchorName)
                        .putExtra("anchorId", mAnchorId)
                        .putExtra("anchorAvatar", mAnchorAvatar));
                break;
            case R.id.ll_red_bag:
                showRedbagPopwindow();
                break;
            default:
                break;
        }

    }

    /**
     * 私聊列表
     * 对象ID 昵称 头像
     */
    private void showPrivateChatListDialog(PrivateChatUser user) {
        if (privateChatListDialog != null && privateChatListDialog.isAdded()) {
            return;
        }
        viewMessageNotify.setVisibility(View.GONE);
        privateChatListDialog = PrivateChatListDialog.newInstance()
                .setShowBottom(true)
                .setDimAmount(0)
                .show(getSupportFragmentManager());
        ((PrivateChatListDialog) privateChatListDialog).setUpWithAnchor(user);
        ((PrivateChatListDialog) privateChatListDialog).setIsGuard(isGuard);
        ((PrivateChatListDialog) privateChatListDialog).setIsVip(isVip);
        ((PrivateChatListDialog) privateChatListDialog).setProgramId(mProgramId);
    }


    private void showPrivateChatDialog(PrivateChatUser user) {
        if (awesomeDialog != null && awesomeDialog.isAdded()) {
            return;
        }
        awesomeDialog = PrivateChatDialog.newInstance(mProgramId)
                .setShowBottom(true)
                .setDimAmount(0)
                .show(getSupportFragmentManager());
        RoomUserInfo.DataBean dataBean = new RoomUserInfo.DataBean();
        dataBean.setAvatar(user.getAvatar());
        dataBean.setNickname(user.getName());
        dataBean.setUserId(user.getPrivateUserId());
        ((PrivateChatDialog) awesomeDialog).chatTo(dataBean);
        ((PrivateChatDialog) awesomeDialog).setIsGuard(isGuard);
    }

    private void showRedbagPopwindow() {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_redbag_live, null);
        TextView tvSend = popView.findViewById(R.id.tv_send);
        tvSend.setVisibility(mAnchorId == mUserId ? View.VISIBLE : View.GONE);
        tvSend.setSelected(mAnchorId == mUserId);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSend.isSelected()) {
                    sendRedPack();
                }
            }
        });
        redPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        redPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        redPopupWindow.setOutsideTouchable(true);
        redPopupWindow.setFocusable(true);
        redPopupWindow.showAsDropDown(llRedBag, 0, UIUtil.dip2px(LiveDisplayActivity.this, 1));
    }

    /**
     * 发送红包
     */
    private void sendRedPack() {
        HashMap hashMap = new HashMap();
        hashMap.put("amount", 180000);
        hashMap.put("contentType", "COIN");
        hashMap.put("objectType", "PROGRAM_TREASURE");
        hashMap.put("programId", mProgramId);
        hashMap.put("quantity", 20);
        hashMap.put("redPacketType", "RANDOM");
        ApiFactory.getInstance().getApi(Api.class)
                .sendRedPacket(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        if (redPopupWindow != null && redPopupWindow.isShowing()) {
                            redPopupWindow.dismiss();
                        }
                        EventBus.getDefault().post(new UserInfoUpdateEvent());
                        ToastUtils.showToastUnify(LiveDisplayActivity.this, "发送成功");
                    }

                    @Override
                    public void onError(ApiResult<JsonElement> body) {
                        switch (body.code) {
                            case -1265:
                                ToastUtils.showToastUnify(LiveDisplayActivity.this, getString(R.string.red_pack_full));
                                break;
                            case -1135:
                                ToastUtils.showToastUnify(LiveDisplayActivity.this, getString(R.string.red_pack_unplay));
                                break;
                            case -1211:
                                ToastUtils.showToastUnify(LiveDisplayActivity.this, getString(R.string.red_pack_money_unfull));
                                break;
                            default:
                                ToastUtils.showToastUnify(LiveDisplayActivity.this, body.msg);
                                break;
                        }
                    }
                });
    }

    /**
     * 关闭侧滑菜单
     */
    public void closeDrawLayout() {
        if (drawerLayoutOut.isDrawerOpen(drawLayoutInclude)) {
            drawerLayoutOut.closeDrawer(drawLayoutInclude);
        }
    }

    /**
     * 守护列表弹窗
     */
    public void showGuardDialog() {
        if (mGuardianDialog != null && mGuardianDialog.isAdded()) {
            return;
        }
        mGuardianDialog = GuardianListDialog.newInstance(mProgramId, mAnchor)
                .setAnimStyle(R.style.dialog_enter_from_right_out_from_right)
                .show(getSupportFragmentManager());
    }

    public void login() {
        LoginDialog.newInstance()
                .setLoginSuccessListener(() -> {
                    mUserId = (long) SPUtils.get(LiveDisplayActivity.this, "userId", 0L);
                    mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
                    getRoomToken();
                    isVip = true;
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .setDimAmount(0.7f)
                .show(getSupportFragmentManager());
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
        } else if ("MOBILE_GIFT_GIF".equals(animEvent.getAnimJson().getAnimType())
                || "MOBILE_CAR_GIF".equals(animEvent.getAnimJson().getAnimType())) {
            if (mGifGiftControl == null) {
                mGifGiftControl = new GifGiftControl(this, ivGiftGif);
            }
            mGifGiftControl.loadGif(animEvent);
        } else if ("MOBILE_CAR_SVGA".equals(animEvent.getAnimJson().getAnimType())
                || "MOBILE_GIFT_SVGA".equals(animEvent.getAnimJson().getAnimType())) {
            if (mSvgaGiftControl == null) {
                mSvgaGiftControl = new SvgaGiftControl(this, svgaGift);
            }
            WeakReference<SvgaGiftControl> weakReference = new WeakReference<>(mSvgaGiftControl);
            if (weakReference.get() != null) {
                weakReference.get().load(animEvent);
            }
//            mSvgaGiftControl.load(animEvent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RunWayEvent runWayEvent) {
        initRunWay();
        mRunWayGiftControl.load(runWayEvent, "socket");
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
        broadCastBottomEvent.setPkLayoutVisibility(rlOtherSide, rlOtherSideInfo, textureView, pkLayout, ivCountDown);
        mRunWayBroadControl.load(broadCastBottomEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PkEvent pkEvent) {
        PkJson.ContextBean bean = pkEvent.getPkJson().context;
        if ("PK_RECORD".equals(bean.busiCode)) {
            return;
        }
        if ("BALCK_HOUSE".equals(bean.busiCode) || "uRescueAnchor".equals(bean.busiCode)) {
            mLivePresenter.getBlackRoomTime(mAnchorId);
            return;
        }
        if (pkControl == null) {
            pkControl = new PkControl(pkLayout, this);
        }
        pkControl.setUserId(mUserId);
        pkControl.setStartAnim(svgaStartPk);
        pkControl.setIvCountDown(ivCountDown);
        pkControl.setmAnchorId(mAnchorId);
        pkControl.setmProgramId(mProgramId);
        pkControl.setTvCountDown(tvCountDown);
        pkControl.setRightInfo(rlOtherSide, ivOtherSide, tvOtherSide);
        pkControl.setOtherLive(textureView2);
        pkControl.setOtherSideInfo(rlOtherSideInfo);
        pkControl.setBean(bean);
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
        loadLayout.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateProgramEvent updateProgramEvent) {
        tvFansCount.setText(getString(R.string.subscriptions
                , updateProgramEvent.getmProgramJson().getContext().getProgram().getSubscriptionNum()));
    }

    /**
     * 开播提醒
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PlayNotifyEvent playNotifyEvent) {
        PlayNotifyJson.ContextBean contextBean = playNotifyEvent.playNotifyJson.context;
        if (!playNotify || contextBean.programId == mProgramId) {
            return;
        }
        AwesomeDialog.init().setLayoutId(R.layout.dialog_play_notify)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                        holder.setText(R.id.tv_nick_play_notify, contextBean.nickname);
                        String jpg = ImageUrl.getAvatarUrl(contextBean.userId, "jpg", contextBean.lastUpdateTime);
                        GlideImageLoader.getInstace().circleCropImage(
                                LiveDisplayActivity.this, jpg, holder.getView(R.id.iv_avatar_play_notify));
                        holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                        holder.setOnClickListener(R.id.tv_transfer, v -> {
                            Intent intent = new Intent(LiveDisplayActivity.this, LiveDisplayActivity.class);
                            intent.putExtra("programId", contextBean.programId);
                            startActivity(intent);
                            dialog.dismiss();
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }

    /**
     * 发送红包消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RedPackTreasureEvent redPackTreasureEvent) {
        if (!redPackTreasureEvent.treasureNum.context.busiCodeName.equals(AppConfig.OPEN_REDPACKET) &&
                !redPackTreasureEvent.treasureNum.context.busiCodeName.equals("RP_RETURN_TO_U") &&
                !redPackTreasureEvent.treasureNum.context.busiCodeName.equals("RP_HAD_FINISHED")) {
            tvRedBag.setText(redPackTreasureEvent.treasureNum.context.programTreasureNum + "");
        }
        if (redPackRunWayControl == null) {
            redPackRunWayControl = new RedPackRunWayControl(this, tvRedBagRunWay);
        }
        redPackRunWayControl.load(redPackTreasureEvent);
        if ((redPackTreasureEvent.treasureNum.context.busiCodeName.equals(AppConfig.USER_SEND_REDPACKET)
                || redPackTreasureEvent.treasureNum.context.busiCodeName.equals(AppConfig.PROGRAM_TREASURE_SEND_REDPACKET) ||
                redPackTreasureEvent.treasureNum.context.busiCodeName.equals(AppConfig.OFFICIAL_SEND_REDPACKET))) {
            if (redPackTreasureEvent.treasureNum.context.programId != mProgramId) {
                return;
            }
            if (redPacketControl == null) {
                redPacketControl = new RedPacketControl(this, rvRedPack);
                redPacketControl.init();
            }
            redPacketControl.destroy();
            RoomRedpackList.ListBean bean = new RoomRedpackList.ListBean();
            bean.leftSeconds = redPackTreasureEvent.treasureNum.context.leftSeconds;
            bean.effDate = redPackTreasureEvent.treasureNum.context.effDate;
            bean.expDate = redPackTreasureEvent.treasureNum.context.expDate;
            bean.redPacketId = redPackTreasureEvent.treasureNum.context.redPacketId;
            redPacketControl.redPackList.add(bean);
            redPacketControl.redpackAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRoomInfoSuccess(RoomInfoBean roomInfoBean) {
        mAnchorId = roomInfoBean.getData().getAnchor().getId();
        if (roomInfoBean.getData() != null) {
            tvFansCount.setText(getString(R.string.subscriptions, roomInfoBean.getData().getSubscriptionNum() + ""));
            ChatRoomInfo.getInstance().setRoomInfoBean(roomInfoBean);
            if (roomInfoBean.getData().getAnchor() != null) {
                mAnchor = roomInfoBean.getData().getAnchor();
//                PrivateChatListFragment fragment = (PrivateChatListFragment) fragments[1];
//                fragment.setUpWithAnchor(mAnchor);
                mAnchorName = mAnchor.getName();
                mAnchorAvatar = mAnchor.getAvatar();
                anchorLevel = LevelUtil.getAnchorLevel(mAnchor);
                GlideImageLoader.getInstace().circleCropImage(this, mAnchor.getAvatar(), ivHostAvatar);

                tvHostName.setText(mAnchor.getName());
                tvHostName.init(getWindowManager(), getMarqueeWidth());
                tvHostName.setTextColor(Color.WHITE);
                tvHostName.startScroll();
            }
            if (roomInfoBean.getData().getStream() != null) {
                setupPlayerSize(roomInfoBean.getData().getStream().getHeight(), roomInfoBean.getData().getStream().getWidth());
                if (roomInfoBean.getData().getStream().getStreamAddress() != null) {
                    if (roomInfoBean.getData().getStream().getStreamAddress().getFlv() != null) {
                        setDateSourceForPlayer(roomInfoBean.getData().getStream().getStreamAddress().getFlv());
                    } else if (roomInfoBean.getData().getStream().getStreamAddress().getRtmp() != null) {
                        setDateSourceForPlayer(roomInfoBean.getData().getStream().getStreamAddress().getRtmp());
                    }
                }
            }
            if (!"T".equals(roomInfoBean.getData().getProgramStatus())) {
                tvStopTip.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
                loadLayout.setVisibility(View.GONE);
            }

            mShareUrl = roomInfoBean.getData().getShareUrl();

            mLiveState = roomInfoBean.getData().getProgramStatus();
            mAnchorCover = roomInfoBean.getData().getCover();
        }

        initAboutAnchor(mProgramId, mAnchorId);
        initIgnore(roomInfoBean);
    }

    /**
     * 屏蔽消息
     */
    private void initIgnore(RoomInfoBean roomInfoBean) {
        if (roomInfoBean.getData().extendInfo != null) {
            for (int i = 0; i < roomInfoBean.getData().extendInfo.size(); i++) {
                if ("noSpeakServer".equals(roomInfoBean.getData().extendInfo.get(i).itemKey) &&
                        roomInfoBean.getData().extendInfo.get(i).itemValue.contains("80")) {
                    ignoreChat = true;
                }
            }
        }
    }

    /**
     * 周星 主播任务 活动页面
     */
    private void initAboutAnchor(int mProgramId, int mAnchorId) {
        mLivePresenter.getActivityGrand(mProgramId, mAnchorId);
        //头条榜单
        mLivePresenter.getHeadlineRank(mAnchorId, "F");
        mLivePresenter.getBlackRoomTime(mAnchorId);
        headlineDisposable = Observable.interval(0, 60, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mLivePresenter.getHeadlineRank(mAnchorId, "F");
        });
        compositeDisposable.add(headlineDisposable);
    }

    private void setupPlayerSize(int height, int width) {
        if (height == 0 || width == 0) {
            return;
        }
        ratioLayout.setPicRatio(width / (float) height);
    }


    @Override
    public void onFollowHostSuccess() {
        btnFollow.setVisibility(View.GONE);
        mIsFollowed = "T";
    }

    @Override
    public void onGetRoomUserInFoSuccess(RoomUserInfo.DataBean data) {
        btnFollow.setVisibility(data.isIsSubs() ? View.GONE : View.VISIBLE);
        mIsFollowed = data.getIsFollowed();
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
//                        ((PrivateChatListFragment) fragments[1]).setIsGuard(isGuard);
                    }

                    //是否为VIP用户
                    if ("VIP".equals(data.getGoodsList().get(i).getGoodsType())) {
                        isVip = true;
                    }
                }
            }
        }
    }

    @Override
    public void onSendGiftSuccess() {
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
        if (mGiftDialog != null && ((GiftDialog) mGiftDialog).superValue) {
            EventBus.getDefault().post(new SendGiftSuccessEvent());
        }
    }

    /**
     * 发送广播成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendBroadEvent event) {
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
                    RunwayBean runWayBean = new RunwayBean();
                    runWayBean.setContext(runWayListBean.list.get(i));
                    RunWayEvent event;
                    if (imageSpanList == null) {
                        event = new RunWayEvent(runWayBean, null);
                    } else {
                        event = new RunWayEvent(runWayBean, imageSpanList.get(i));
                    }
                    initRunWay();
                    mRunWayGiftControl.load(event, "net");
                }
            });
            downloadImageFile.doDownload(imageUrlList, this);
        }
    }

    private void initRunWay() {
        if (mRunWayGiftControl == null) {
            mRunWayGiftControl = new RunWayGiftControl(this, runWayText, frameSupercarTrack, ivRocket);
            mRunWayGiftControl.setListener((programId, nickname) -> showJumpLiveHouseDialog(programId, nickname));
        }
    }

    @Override
    public void onGetProgramFirstSuccess(long userId) {
        ChatRoomInfo.getInstance().setProgramFirstId(userId);
    }


    /**
     * 抽奖活动
     */
    @Override
    public void onActivityListSuccess(GetActivityBean bean) {
        if (bean == null
                || bean.list == null
                || bean.list.isEmpty()) {
            banner.setVisibility(View.GONE);
            return;
        }
        banner.setVisibility(View.VISIBLE);
        mBannerInfoList = bean.list;
        ArrayList<String> banners = new ArrayList<>();
        for (int i = 0; i < mBannerInfoList.size(); i++) {
            banners.add(mBannerInfoList.get(i).imageUrl);
        }
        banner.setImages(banners);
        banner.start();

        drawLayoutControl.notifyData(mBannerInfoList);
    }

    @Override
    public void onPkInfoSuccess(PKResultBean bean) {
        if (pkControl == null) {
            pkControl = new PkControl(pkLayout, this);
        }
        pkControl.setUserId(mUserId);
        pkControl.setStartAnim(svgaStartPk);
        pkControl.setIvCountDown(ivCountDown);
        pkControl.setmAnchorId(mAnchorId);
        pkControl.setmProgramId(mProgramId);
        pkControl.setTvCountDown(tvCountDown);
        pkControl.setRightInfo(rlOtherSide, ivOtherSide, tvOtherSide);
        pkControl.setOtherLive(textureView2);
        pkControl.setOtherSideInfo(rlOtherSideInfo);

        rlOtherSideInfo.setVisibility(View.VISIBLE);

        if (bean.pkSingleVideo == 0 && bean.otherStream != null) {
            rlOtherSide.setVisibility(View.VISIBLE);
            List<String> rtmpList = bean.otherStream.getRtmp();
            List<String> flvList = bean.otherStream.getFlv();
            List<String> hlsList = bean.otherStream.getHls();
            if (rtmpList != null) {
                for (int i = 0; i < rtmpList.size(); i++) {
                    setDateSourceForPlayer2(rtmpList.get(i));
                }
            } else if (flvList != null) {
                for (int i = 0; i < flvList.size(); i++) {
                    setDateSourceForPlayer2(flvList.get(i));
                }
            } else if (hlsList != null) {
                for (int i = 0; i < hlsList.size(); i++) {
                    setDateSourceForPlayer2(hlsList.get(i));
                }
            }
        }
        if (bean.pkSingleVideo == 0) {
            otherSideLive();
        }

        pkControl.initNet(bean);

    }

    /**
     * 直播间的活动（常规活动）
     */
    @Override
    public void onActivityGrandSuccess(ActivityGrandBean bean) {
        if (bean.list != null && bean.list.size() != 0) {
            for (int i = 0; i < bean.list.size(); i++) {
                ActivityGrandBean.ListBean listBean = bean.list.get(i);
                LiveWebFragment liveWebFragment = LiveWebFragment.newInstance(listBean.linkUrl, mAnchorId + "", mProgramId + "");
                liveWebFragment.setOnclickListener(new LiveWebFragment.ClickListener() {
                    @Override
                    public void clickListener() {
                        if (TextUtils.isEmpty(listBean.jumpUrl)) {
                            return;
                        }
                        startActivityForResult(new Intent(getBaseActivity(), JsBridgeActivity.class)
                                .putExtra("anchorId", mAnchorId + "")
                                .putExtra("programId", mProgramId + "")
                                .putExtra("title", listBean.name)
                                .putExtra("url", listBean.jumpUrl), REQUEST_LOGIN);
                    }
                });
                mActivityGrands.add(liveWebFragment);
            }
        }
        //周星榜
        weekRankFragment = LiveWeekRankFragment.newInstance(mProgramId, mAnchorId);
        mActivityGrands.add(weekRankFragment);
        mGrandAdaper.notifyDataSetChanged();
        initActivityPoints();
        //主播任务
        mLivePresenter.getAnchorTask(mAnchorId);
    }

    @Override
    public void onGetAudienceListSuccess(AudienceListBean.DataBean bean) {
        mAudienceCount = bean.total;
        if (bean.getList() == null && bean.getList().size() == 0) {
            tvPopularity.setText(getString(R.string.audience, 0));
        } else {
            tvPopularity.setText(getString(R.string.audience, bean.total));
            mAudienceList.clear();
            mAudienceList.addAll(bean.getList());
            pollAdapter.setmAudienceList(mAudienceList);
            pollAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetTotalGuardSuccess(GuardTotalBean.DataBean guardTotalBean) {
        tvGuardCount.setText(guardTotalBean.guardTotal + "");
    }

    @Override
    public void onGetPunishWaysSuccess(PunishWaysBean bean) {
        pkControl = new PkControl(pkLayout, this);
    }

    /**
     * 主播任务成功
     */
    @Override
    public void onGetAnchorTaskSuccess(AnchorTaskBean dataBean) {
        if (dataBean != null) {
            AnchorTaskFragment anchorTaskFragment = AnchorTaskFragment.newInstance(dataBean);
            mActivityGrands.add(anchorTaskFragment);
            mGrandAdaper.notifyDataSetChanged();
            vpActivity.setOffscreenPageLimit(mActivityGrands.size());
            initActivityPoints();
        }
    }

    @Override
    public void onGetDailyTaskStateSuccess(GetDailyTaskStateBean dailyTaskStateBean) {
        if ("T".equals(dailyTaskStateBean.receive)) {
            btnFreeGift.setImageResource(R.drawable.ic_live_free_gift_receive);
        } else {
            btnFreeGift.setImageResource(R.drawable.ic_live_free_gift);
        }
    }

    @Override
    public void onGetHeadlineRankSuccess(HeadlineRankBean dataBean) {
        if (dataBean != null) {
            if (dataBean.rank < 0) {
                mHeadlineRank = "未上榜";
            } else {
                mHeadlineRank = getString(R.string.headline_rank, dataBean.rank);
            }
            initHeadline();
        }
    }

    /**
     * 小黑屋时间
     */
    @Override
    public void onGetBlackRoomTimeSuccess(BlackRoomTimeBean dataBean) {
        if (blackRoomDisposable != null) {
            blackRoomDisposable.dispose();
        }
        double l = dataBean.time / 60 / 60;
        totalHours = (int) Math.ceil(l);
        if (totalHours <= 0) {
            llBlackRoom.setVisibility(View.GONE);
            return;
        }
        llBlackRoom.setVisibility(View.VISIBLE);
        tvTimeBlackRoom.setText(totalHours + "");
        blackRoomDisposable = Observable.interval(0, 60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Long aLong) -> {
                    double v = (dataBean.time - aLong * 60) / 60 / 60;
                    totalHours = (int) Math.ceil(v);
                    if (totalHours <= 0) {
                        llBlackRoom.setVisibility(View.GONE);
                        return;
                    }
                    llBlackRoom.setVisibility(View.VISIBLE);
                    tvTimeBlackRoom.setText(totalHours + "");
                });
        compositeDisposable.add(blackRoomDisposable);
    }

    /**
     * 开播提醒
     */
    @Override
    public void onGetUsetSetSuccesd(GetUserSetBean bean) {
        if (bean.list == null || bean.list.size() == 0) {
            playNotify = false;
        } else {
            for (int i = 0; i < bean.list.size(); i++) {
                if (bean.list.get(i).setType.equals("subscribe")) {
                    playNotify = bean.list.get(i).setValue.equals("1");
                    break;
                }
            }
        }
    }

    /**
     * 直播间红包池金额
     */
    @Override
    public void onGetRoomRedpackTreasureSuccess(RoomRedPackTreasure dataBean) {
        llRedBag.setVisibility(View.VISIBLE);
        tvRedBag.setText(String.valueOf(dataBean.amountTotal));
    }

    /**
     * 直播间红包列表
     */
    @Override
    public void onGetRoomRedListSuccess(RoomRedpackList dataBean) {
        if (redPacketControl == null) {
            redPacketControl = new RedPacketControl(this, rvRedPack);
            redPacketControl.init();
        }
        redPacketControl.redPackList.clear();
        redPacketControl.redPackList.addAll(dataBean.list);
        redPacketControl.redpackAdapter.notifyDataSetChanged();
    }


    /**
     * 活动下标黄点
     */
    private void initActivityPoints() {
        if (llPagerIndex.getChildCount() > 0) {
            llPagerIndex.removeAllViews();
        }
        if (mActivityGrands.size() < 2) {
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(this, 5), UIUtil.dip2px(this, 5));
        for (int i = 0; i < mActivityGrands.size(); i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.selector_live_activity_pager_index);
            if (i == 0) {
                view.setSelected(true);
            } else {
                params.leftMargin = UIUtil.dip2px(this, 5);
                params.rightMargin = UIUtil.dip2px(this, 5);
            }
            llPagerIndex.addView(view, params);
        }
    }

    @Override
    public void onGetRoomRankTotalSuccess(RoomRankTotalBean bean) {
        if (bean.total.compareTo(new BigDecimal(10000)) < 0) {
            mRanking = bean.total + "";
        } else {
            BigDecimal divide = bean.total.divide(new BigDecimal(10000), 1, BigDecimal.ROUND_DOWN);
            mRanking = divide + "万";
        }
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
            UserDao userDao = BaseApplication.getInstance().getDaoSession().getUserDao();
            PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
            User unique = userDao.queryBuilder().where(UserDao.Properties.UserId.eq(mUserId)).unique();
            if (unique == null) {
                User user = new User();
                user.setAvatar(mRoomUserInfo.getAvatar());
                user.setUserId(mUserId);
                userDao.insert(user);
            }
            PrivateChatUser privateChatUser = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.eq(mUserId),
                    PrivateChatUserDao.Properties.PrivateUserId.eq(chatToUser.getUserId())).unique();
            if (privateChatUser == null) {
                PrivateChatUser chatUser = new PrivateChatUser();
                chatUser.setAvatar(chatToUser.getAvatar());
                chatUser.setName(chatToUser.getNickname());
                chatUser.setPrivateUserId(chatToUser.getUserId());
                chatUser.setUserId(mUserId);
                chatUser.setTimestamp(System.currentTimeMillis());
                privateChatUserDao.insert(chatUser);
            } else {
                privateChatUser.setTimestamp(System.currentTimeMillis());
                privateChatUser.setId(privateChatUser.getId());
                privateChatUserDao.update(privateChatUser);
                if (privateChatListDialog != null && privateChatListDialog.isAdded()) {
                    ((PrivateChatListDialog) privateChatListDialog).update();
                }
            }
            PrivateChatContentDao privateChatContentDao = BaseApplication.getInstance().getDaoSession().getPrivateChatContentDao();
            List<PrivateChatContent> list = privateChatContentDao.queryBuilder().where(
                    PrivateChatContentDao.Properties.UserId.eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                    PrivateChatContentDao.Properties.PrivateUserId.eq(chatToUser.getUserId())).list();
            if (list != null && list.size() > AppUtils.PRIVATE_MAX_NUM) {
                privateChatContentDao.deleteByKey(list.get(0).getId());
            }
            PrivateChatContent chatContent = new PrivateChatContent();
            chatContent.setContent(message);
            chatContent.setTimestamp(System.currentTimeMillis());
            chatContent.setPrivateUserId(chatToUser.getUserId());
            chatContent.setFromId(mUserId);
            chatContent.setUserId(mUserId);
            privateChatContentDao.insert(chatContent);
        }

    }

    public void sendGift(int count, int goodId, boolean useBag, String runwayShowMark, String runwayAppend) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("roomId", mProgramId + "");
        paramsMap.put("programId", mProgramId + "");
        paramsMap.put("targetId", mAnchorId + "");
        paramsMap.put("goodsId", goodId + "");
        paramsMap.put("count", count + "");
        paramsMap.put("userId", mUserId + "");
        paramsMap.put("useBag", useBag + "");
        paramsMap.put("runwayShowMark", useBag ? "T" : runwayShowMark);
        if (runwayShowMark.equals("T") && TextUtils.isEmpty(runwayAppend)) {
            paramsMap.put("runwayAppend", getResources().getString(R.string.edit_hint_gift_dialog_super_run));
        } else {
            paramsMap.put("runwayAppend", runwayAppend);
        }
        mLivePresenter.sendGift(paramsMap);
        CommonGiftBean commonGift = new CommonGiftBean();
        commonGift.giftId = goodId;
        addCommonGift(commonGift, goodId + "");
    }

    /**
     * 常送礼物
     */
    private void addCommonGift(CommonGiftBean commonGift, String goodId) {
        if (SPUtils.get(this, SpConfig.FREE_GOODS_IDS, "").toString().contains(goodId)) {
            return;
        }
        CommonGiftDao commonGiftDao = BaseApplication.getInstance().getDaoSession().getCommonGiftDao();
        CommonGift unique = commonGiftDao.queryBuilder().
                where(CommonGiftDao.Properties.UserId.eq(Long.parseLong(SPUtils.get(this, "userId", 0L).toString())))
                .unique();
        if (unique == null) {
            CommonGift commonGift1 = new CommonGift();
            commonGift1.setUserId(Long.parseLong(SPUtils.get(this, "userId", 0L).toString()));
            commonGift.times = 1;
            List<CommonGiftBean> list = new ArrayList<>();
            list.add(commonGift);
            commonGift1.setHobbyList(list);
            commonGiftDao.insert(commonGift1);
        } else {
            if (checkGift(unique.getHobbyList(), commonGift) != null) {
                CommonGiftBean commonGiftBean = checkGift(unique.getHobbyList(), commonGift);
                for (int i = 0; i < unique.getHobbyList().size(); i++) {
                    if (unique.getHobbyList().get(i).giftId.intValue() == commonGiftBean.giftId.intValue()) {
                        unique.getHobbyList().get(i).times = unique.getHobbyList().get(i).times + 1;
                        break;
                    }
                }
                commonGiftDao.update(unique);
            } else {
                commonGift.times = 1;
                unique.getHobbyList().add(commonGift);
                commonGiftDao.update(unique);
            }
        }
    }

    private CommonGiftBean checkGift(List<CommonGiftBean> hobbyList, CommonGiftBean commonGift) {
        if (hobbyList.isEmpty()) {
            return null;
        }
        for (int i = 0; i < hobbyList.size(); i++) {
            if (hobbyList.get(i).giftId.intValue() == commonGift.giftId.intValue()) {
                return hobbyList.get(i);
            }
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfoUpdateEvent event) {
        mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GuardOpenEvent event) {
        if (event.userId == mUserId) {
            mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
        }
        showOpenGuardAnim(event.avatar, event.nickName);
        mLivePresenter.getGuardTotal(mProgramId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PrivateChatSelectedEvent event) {
//        setTabChange(1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdatePrivateChatEvent updatePubChatEvent) {
        ChatMessage message = (ChatMessage) updatePubChatEvent.getMessage();
        if (message.from_uid != mUserId) {
            if (privateChatListDialog != null && privateChatListDialog.isAdded()) {
                return;
            }
            showMessageNotify();
        }
    }

    /**
     * 开通守护
     *
     * @param avatar
     * @param nickName
     */
    private void showOpenGuardAnim(String avatar, String nickName) {
        svgaGuardSuccess.setVisibility(View.VISIBLE);
        svgaGuardSuccess.setLoops(1);
        SVGAParser parser = new SVGAParser(this);
        try {
            parser.parse("svga/guard1.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem, requestImage(avatar, nickName));
                    svgaGuardSuccess.setImageDrawable(drawable);
                    svgaGuardSuccess.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    private SVGADynamicEntity requestImage(String avatar, String nickName) {
        final SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        SpannableStringBuilder sbNickName = new SpannableStringBuilder(nickName);
        SpannableStringBuilder sbAnchorName = new SpannableStringBuilder(mAnchorName);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(15);
        dynamicEntity.setDynamicText(new StaticLayout(
                sbNickName,
                0,
                sbNickName.length(),
                textPaint,
                0,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                false
        ), "Bitmap6");
        dynamicEntity.setDynamicText(new StaticLayout(
                sbAnchorName,
                0,
                sbAnchorName.length(),
                textPaint,
                0,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                false
        ), "Bitmap7");
        BitmapUtils.returnBitmap(avatar, new HttpCallBackListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                dynamicEntity.setDynamicImage(BitmapUtils.getOvalBitmap(bitmap), "Bitmap8");
            }

            @Override
            public void onError(Exception e) {

            }
        });
        BitmapUtils.returnBitmap(mAnchorAvatar, new HttpCallBackListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                dynamicEntity.setDynamicImage(BitmapUtils.getOvalBitmap(bitmap), "Bitmap10");
            }

            @Override
            public void onError(Exception e) {

            }
        });
        return dynamicEntity;
    }


    public void showAudienceInfoDialog(long viewedUserID, boolean isShowBottom) {
        if (personalInfoDialog != null && personalInfoDialog.isAdded()) {
            return;
        }
        personalInfoDialog = PersonalInfoDialog.newInstance(mRoomUserInfo, viewedUserID, mProgramId, mUserId)
                .setListener((RoomUserInfo.DataBean mViewedUser) -> {
                    if (mUserListDialog != null && mUserListDialog.isAdded()) {
                        mUserListDialog.dismiss();
                    }
                    if (mRankDialog != null && mRankDialog.isAdded()) {
                        mRankDialog.dismiss();
                    }
                    if (mGuardianDialog != null && mGuardianDialog.isAdded()) {
                        mGuardianDialog.dismiss();
                    }
                    if (headlineDialog != null && headlineDialog.isAdded()) {
                        headlineDialog.dismiss();
                    }
                    if (mViewedUser != null) {
                        PrivateChatUser chatUser = new PrivateChatUser();
                        chatUser.setPrivateUserId(mViewedUser.getUserId());
                        chatUser.setAvatar(mViewedUser.getAvatar());
                        chatUser.setName(mViewedUser.getNickname());
                        showPrivateChatDialog(chatUser);
                    }
                })
                .setAnimStyle(R.style.dialog_enter_from_bottom_out_from_top)
                .setDimAmount(0)
                .setShowBottom(false)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    public void showAudienceInfoDialog(String nickName) {
        AudienceInfoDialog.newInstance(nickName, mProgramId, mRoomUserInfo)
                .setListener(() -> {
                    if (mUserListDialog != null && mUserListDialog.isAdded()) {
                        mUserListDialog.dismiss();
                    }
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .setDimAmount(0)
                .show(getSupportFragmentManager());
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
        if (textureView2 != null) {
            textureView2.runInBackground(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textureView != null) {
            textureView.runInForeground();
        }
        if (textureView2 != null) {
            textureView2.runInForeground();
        }
        if (mUserId == 0) {
            mUserId = Long.parseLong(SPUtils.get(this, "userId", 0L).toString());
            mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
            getRoomToken();
        }
    }

    @Override
    protected void onDestroy() {
        if (textureView != null) {
            textureView.stop();
            textureView.release();
            textureView = null;
        }
        if (textureView2 != null) {
            textureView2.stop();
            textureView2.release();
            textureView2 = null;
        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
        mLivePresenter.onDestory();
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unregisterReceiver(mPkReceiver);
    }

    private void destroy() {
        if (tvHostName != null) {
            tvHostName.destyoy();
        }
        if (mGifGiftControl != null) {
            mGifGiftControl.destroy();
        }
        if (mSvgaGiftControl != null) {
            mSvgaGiftControl.destroy();
            mSvgaGiftControl = null;
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
        pkLayout.destroy();
        if (pkControl != null) {
            pkControl.destroy();
        }
        if (mActivityGrands.size() > 0) {
            mActivityGrands.clear();
            mGrandAdaper.notifyDataSetChanged();
        }
        if (llPagerIndex.getChildCount() > 0) {
            llPagerIndex.removeAllViews();
        }
        if (headLineControl != null) {
            headLineControl.destroy();
        }
        if (royalEnterControl != null) {
            royalEnterControl.destroy();
        }
        if (weekStarControl != null) {
            weekStarControl.destroy();
        }
        if (redPackRunWayControl != null) {
            redPackRunWayControl.destroy();
        }
        if (redPacketControl != null) {
            redPacketControl.destroy();
        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                mUserId = (long) SPUtils.get(this, "userId", 0L);
                mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
                getRoomToken();
                isVip = true;
                LogUtils.e("sssssssss   onActivityResult");
            }
        }
        if (requestCode == AppUtils.REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                mUserId = (long) SPUtils.get(this, "userId", 0L);
                mLivePresenter.getRoomUserInfo(mUserId, mProgramId);
                getRoomToken();
                isVip = true;
                LogUtils.e("sssssssss   onActivityResult");
            }
        }
        if (requestCode == AppUtils.OPEN_VIP) {
            if (resultCode == RESULT_OK) {
                isVip = true;
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
            jumpToLive(programId);
        });
        dialog.show();
    }

    public void jumpToLive(int programId) {
        Intent intent = new Intent(LiveDisplayActivity.this, LiveDisplayActivity.class);
        intent.putExtra(BundleConfig.PROGRAM_ID, programId);
        startActivity(intent);
        rlOtherSide.setVisibility(View.GONE);
        rlOtherSideInfo.setVisibility(View.GONE);
        textureView.setVisibility(View.INVISIBLE);
        pkLayout.setVisibility(View.GONE);
        ivCountDown.clearAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdatePubChatEvent updatePubChatEvent) {
        FillHolderMessage message = updatePubChatEvent.getMessage();

        if (message instanceof WelcomeMsg) {
            WelcomeJson welcomeJson = ((WelcomeMsg) message).getmWelcomeJson();
            int royalLevel = ((WelcomeMsg) message).getRoyalLevel(welcomeJson.getContext().getInfo().getLevelList());
            if (royalLevel > 0) {
                if (royalEnterControl == null) {
                    royalEnterControl = new RoyalEnterControl();
                    royalEnterControl.setLlEnter(llEnter);
                    royalEnterControl.setTvEnter(tvEnter);
                    royalEnterControl.setContext(this);
                    royalEnterControl.setClEnter(clEnter);
                }
                royalEnterControl.showEnter((WelcomeMsg) message);
            }
        } else if (message instanceof PkMessage && ((PkMessage) message).pkJson.context.busiCode.equals("PK_RECORD")) {
            initRunWayBroad();
            PkEvent pkEvent = new PkEvent(((PkMessage) message).pkJson, this);
            mRunWayBroadControl.load(pkEvent);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeekStarEvent weekStarEvent) {
        if (weekStarControl == null) {
            weekStarControl = new WeekStarControl(LiveDisplayActivity.this);
            weekStarControl.setTvEnter(wsvWeekstar);
            weekStarControl.setIvEnter(ivWeekstar);
            weekStarControl.setRlEnter(rlWeekstar);
        }
        weekStarControl.showEnter(weekStarEvent);
        if (weekRankFragment != null) {
            weekRankFragment.loaddata();
        }
    }

    /**
     * 奖池满用户下注消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PrizePoolFullEvent weekStarEvent) {
        if (weekStarControl == null) {
            weekStarControl = new WeekStarControl(LiveDisplayActivity.this);
            weekStarControl.setTvEnter(wsvWeekstar);
            weekStarControl.setIvEnter(ivWeekstar);
            weekStarControl.setRlEnter(rlWeekstar);
        }
        weekStarControl.showEnter(weekStarEvent);
    }

    /**
     * 下注时间结束
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BetsEndEvent weekStarEvent) {
        if (weekStarControl == null) {
            weekStarControl = new WeekStarControl(LiveDisplayActivity.this);
            weekStarControl.setTvEnter(wsvWeekstar);
            weekStarControl.setIvEnter(ivWeekstar);
            weekStarControl.setRlEnter(rlWeekstar);
        }
        weekStarControl.showEnter(weekStarEvent);
    }

    /**
     * 用户中奖消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FirstPrizeUserEvent weekStarEvent) {
        if (weekStarControl == null) {
            weekStarControl = new WeekStarControl(LiveDisplayActivity.this);
            weekStarControl.setTvEnter(wsvWeekstar);
            weekStarControl.setIvEnter(ivWeekstar);
            weekStarControl.setRlEnter(rlWeekstar);
        }
        weekStarControl.showEnter(weekStarEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HeadLineEvent event) {
        if (event.headLineJson.context.rank == 1) {
            if (weekStarControl == null) {
                weekStarControl = new WeekStarControl(LiveDisplayActivity.this);
                weekStarControl.setTvEnter(wsvWeekstar);
                weekStarControl.setIvEnter(ivWeekstar);
                weekStarControl.setRlEnter(rlWeekstar);
            }
            weekStarControl.showEnter(event);
        }
        if (event.headLineJson.context.programId == mProgramId) {
            if (headLineControl == null) {
                headLineControl = new HeadLineControl(hlLayout);
            }
            headLineControl.load(event);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EverydayEvent event) {
        mLivePresenter.getDailyTaskState(mUserId);
    }

    private void otherSideLive() {
        textureView2.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        textureView2.setOnPreparedListener(iMediaPlayer -> {
            textureView2.setVisibility(View.VISIBLE);
            textureView2.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            textureView2.start();
        });

        textureView2.setOnCompletionListener(iMediaPlayer -> {
            textureView2.stop();
            textureView2.release();
        });
    }

    private void setDateSourceForPlayer2(String stream) {
        pkStream = stream;
        try {
            boolean pkVoice = (boolean) SPUtils.get(this, SpConfig.PK_VIOCE_LIVE, true);
            if (pkVoice) {
                textureView2.setVolume(1, 1);
            } else {
                textureView2.setVolume(0, 0);
            }
            textureView2.setDataSource(stream);
            textureView2.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LivePkEvent event) {
        if (textureView2 != null && textureView2.isPlaying()) {
            boolean pkVoice = (boolean) SPUtils.get(this, SpConfig.PK_VIOCE_LIVE, true);
            if (pkVoice) {
                textureView2.setVolume(1, 1);
            } else {
                textureView2.setVolume(0, 0);
            }
        }
    }

    public void showAtChat(String at) {
        if (mChatDialog != null && mChatDialog.isAdded()) {
            return;
        }
        mChatDialog = LiveHouseChatDialog.newInstance(isGuard, isVip, mProgramId, at, mAnchor)
                .setDimAmount(0)
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 直播间头条轮播
     *
     * @param line
     */
    private void setHeadLine(String[] line) {
        if (line == null || line.length == 0) {
            return;
        }
        int images[] = {R.drawable.live_display_cup, R.drawable.ic_head_line};
        List<View> views = new ArrayList<>();
        for (int i = 0; i < line.length; i++) {
            View v = View.inflate(this, R.layout.item_head_line, null);
            TextView text = v.findViewById(R.id.tv_head_line);
            ImageView image = v.findViewById(R.id.iv_head_line);
            text.setText(line[i]);
            image.setImageResource(images[i]);
            views.add(v);
        }
        headLineView.setViews(views);
        headLineView.setOnItemClickListener((position, view) -> {
            if (headlineDialog != null && headlineDialog.isAdded()) {
                return;
            }
            if (1 == position) {
                headlineDialog = HeadlineDialog.newInstance(0, mProgramId, mAnchorId, mAnchorName, mAnchorAvatar)
                        .show(getSupportFragmentManager());
            } else {
                headlineDialog = HeadlineDialog.newInstance(1, mProgramId, mAnchorId, mAnchorName, mAnchorAvatar)
                        .show(getSupportFragmentManager());
            }
        });
    }

    public void jumpToWeekRank() {
        if (headlineDialog != null && headlineDialog.isAdded()) {
            return;
        }
        headlineDialog = HeadlineDialog.newInstance(2, mProgramId, mAnchorId, mAnchorName, mAnchorAvatar)
                .show(getSupportFragmentManager());
    }
}
