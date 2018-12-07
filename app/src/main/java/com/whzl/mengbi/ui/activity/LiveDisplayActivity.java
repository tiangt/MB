package com.whzl.mengbi.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.opensource.svgaplayer.SVGAImageView;
import com.umeng.socialize.UMShareAPI;
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
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.events.UserLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.WeekStarEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.message.messageJson.StartStopLiveJson;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.message.messages.PkMessage;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
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
import com.whzl.mengbi.gift.RoyalEnterControl;
import com.whzl.mengbi.gift.RunWayBroadControl;
import com.whzl.mengbi.gift.RunWayGiftControl;
import com.whzl.mengbi.gift.WeekStarControl;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PunishWaysBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.RunwayBean;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.receiver.NetStateChangeReceiver;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.AudienceInfoDialog;
import com.whzl.mengbi.ui.dialog.FreeGiftDialog;
import com.whzl.mengbi.ui.dialog.GiftDialog;
import com.whzl.mengbi.ui.dialog.GuardListDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseChatDialog;
import com.whzl.mengbi.ui.dialog.LiveHouseRankDialog;
import com.whzl.mengbi.ui.dialog.LoginDialog;
import com.whzl.mengbi.ui.dialog.PersonalInfoDialog;
import com.whzl.mengbi.ui.dialog.PrivateChatListFragment;
import com.whzl.mengbi.ui.dialog.ShareDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.fragment.AnchorTaskFragment;
import com.whzl.mengbi.ui.fragment.ChatListFragment;
import com.whzl.mengbi.ui.fragment.LiveWebFragment;
import com.whzl.mengbi.ui.fragment.LiveWeekRankFragment;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.widget.recyclerview.AutoPollAdapter;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView2;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView3;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PkLayout;
import com.whzl.mengbi.ui.widget.view.RatioRelativeLayout;
import com.whzl.mengbi.ui.widget.view.RoyalEnterView;
import com.whzl.mengbi.ui.widget.view.WeekStarView;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.util.zxing.NetUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
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
    @BindView(R.id.tv_contribute)
    TextView tvContribute;
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
    @BindView(R.id.iv_enter_car)
    ImageView ivEnterCar;
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
    ImageView ivWeekstar;
    @BindView(R.id.wsv_weekstar)
    WeekStarView wsvWeekstar;
    @BindView(R.id.cl_weekstar)
    ConstraintLayout clWeekstar;
    @BindView(R.id.rl_weekstar)
    RelativeLayout rlWeekstar;
    @BindView(R.id.btn_more)
    ImageButton btnMore;

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
    private List<GetActivityBean.ListBean> mBannerInfoList;
    private ArrayList<Fragment> mActivityGrands = new ArrayList<>();
    private FragmentPagerAdaper mGrandAdaper;
    private AutoPollAdapter pollAdapter;
    private ArrayList<AudienceListBean.AudienceInfoBean> mAudienceList = new ArrayList<>();
    private RoyalEnterControl royalEnterControl;
    private boolean showActivityGrand = true;
    private boolean showBanner = false;
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
        progressBar.setVisibility(View.VISIBLE);
        mProgramId = intent.getIntExtra(BundleConfig.PROGRAM_ID, -1);
        SPUtils.put(this, "programId", mProgramId);
        chatRoomPresenter = new ChatRoomPresenterImpl(mProgramId + "");
        isGuard = false;
        isVip = false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragments[0]);
        fragmentTransaction.remove(fragments[1]);
        fragmentTransaction.commit();
        initFragment();
        loadData();
        showActivityGrand = false;
        showBanner = false;
    }

    @Override
    protected void initEnv() {
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#181818"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mLivePresenter = new LivePresenterImpl(this);
        if (getIntent() != null) {
            mProgramId = getIntent().getIntExtra(BundleConfig.PROGRAM_ID, -1);
            //主播封面
            mAnchorCover = getIntent().getStringExtra(BundleConfig.ANCHOR_COVER);
            SPUtils.put(this, "programId", mProgramId);
        }
        chatRoomPresenter = new ChatRoomPresenterImpl(mProgramId + "");
        mUserId = Long.parseLong(SPUtils.get(this, "userId", 0L).toString());
        mLivePresenter.getLiveGift();
        initReceiver();
        initPkNetwork();
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
        initFragment();
        initBanner();
        initVp();
        initProtectRecycler();
    }

    private void initProtectRecycler() {
        mAudienceRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pollAdapter = new AutoPollAdapter(mAudienceList, this);
        pollAdapter.setListerner(position -> {
            long userId = mAudienceList.get(position + 1).getUserid();
//            showAudienceInfoDialog(userId, false);
            PersonalInfoDialog.newInstance(mRoomUserInfo, userId, mProgramId, mUserId)
                    .setAnimStyle(R.style.dialogWindowAnim)
                    .setDimAmount(0)
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
                startActivityForResult(new Intent(getBaseActivity(), JsBridgeActivity.class)
                        .putExtra("anchorId", mAnchorId + "")
                        .putExtra("programId", mProgramId + "")
                        .putExtra("title", listBean.name)
                        .putExtra("url", listBean.linkUrl), REQUEST_LOGIN);
            }
        });
    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ChatListFragment chatListFragment = ChatListFragment.newInstance(mProgramId);
//        chatListFragment.setLlEnter(llEnter);
//        chatListFragment.setTvEnter(tvEnter);
//        chatListFragment.setIvEnter(ivEnterCar);
        fragments = new Fragment[]{chatListFragment, PrivateChatListFragment.newInstance(mProgramId)};
        fragmentTransaction.add(R.id.fragment_container, fragments[0]);
        fragmentTransaction.add(R.id.fragment_container, fragments[1]);
        fragmentTransaction.hide(fragments[1]);
        fragmentTransaction.commit();
    }

    public void setTabChange(int index) {
        if (index == currentSelectedIndex) {
            return;
        }
        if (index == 1) {
            viewMessageNotify.setVisibility(View.GONE);

            if (showActivityGrand) {
                vpActivity.setVisibility(View.GONE);
                llPagerIndex.setVisibility(View.GONE);
            }
            if (showBanner) {
                banner.setVisibility(View.GONE);
            }
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
        mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
        mLivePresenter.getRunWayList(ParamsUtils.getSignPramsMap(new HashMap<>()));
        mLivePresenter.getActivityList();
        mLivePresenter.getPkInfo(mProgramId);
        roomOnlineDisposable = Observable.interval(0, 60, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mLivePresenter.getAudienceList(mProgramId);
        });
        mLivePresenter.getGuardTotal(mProgramId);
        roomRankTotalDisposable = Observable.interval(0, 60, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mLivePresenter.getRoomRankTotal(mProgramId, "sevenDay");
        });
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
            , R.id.tv_popularity, R.id.tv_contribute, R.id.btn_chat, R.id.btn_chat_private
            , R.id.rootView, R.id.fragment_container, R.id.rl_guard_number
            , R.id.btn_share, R.id.btn_free_gift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_host_avatar:
                if (mUserId == 0) {
                    login();
                    return;
                }
//                showAudienceInfoDialog(mAnchorId, false);
                PersonalInfoDialog.newInstance(mRoomUserInfo, mAnchorId, mProgramId, mUserId, mIsFollowed, mLiveState)
                        .setAnimStyle(R.style.dialogWindowAnim)
                        .setDimAmount(0)
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
                setTabChange(1);


                break;
            case R.id.rootView:
                if (currentSelectedIndex == 1) {
                    setTabChange(0);

                    if (showActivityGrand) {
                        vpActivity.setVisibility(View.VISIBLE);
                        vpActivity.setVisibility(View.VISIBLE);
                    }
                    if (showBanner) {
                        banner.setVisibility(View.VISIBLE);
                    }
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
                if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
                    return;
                }
                mGuardListDialog = GuardListDialog.newInstance(mProgramId, mAnchor, 1, mAudienceCount)
                        .setShowBottom(true)
                        .setDimAmount(0)
                        .show(getSupportFragmentManager());
                break;

            case R.id.tv_contribute:
                if (mRankDialog != null && mRankDialog.isAdded()) {
                    return;
                }
                mRankDialog = LiveHouseRankDialog.newInstance(mProgramId)
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.rl_guard_number:
                mGuardListDialog = GuardListDialog.newInstance(mProgramId, mAnchor, 0, mAudienceCount)
                        .setShowBottom(true)
                        .setDimAmount(0)
                        .show(getSupportFragmentManager());
                break;
            case R.id.btn_share:
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
                if (mFreeGiftDialog == null) {
                    mFreeGiftDialog = FreeGiftDialog.newInstance(mProgramId, mAnchorId)
                            .setShowBottom(true)
                            .setDimAmount(0);
                }
                mFreeGiftDialog.show(getSupportFragmentManager());
                break;
            default:
                break;
        }

    }

    public void login() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.putExtra("from", this.getClass().toString());
//        startActivityForResult(intent, REQUEST_LOGIN);
        LoginDialog.newInstance()
                .setLoginSuccessListener(new LoginDialog.LoginSuccessListener() {
                    @Override
                    public void onLoginSuccessListener() {
                        LogUtils.e("sssssssss   onLoginSuccessListener");
                        mUserId = (long) SPUtils.get(LiveDisplayActivity.this, "userId", 0L);
                        mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
                        getRoomToken();
                        isVip = true;
                    }
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
        mRunWayBroadControl.load(broadCastBottomEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PkEvent pkEvent) {
        PkJson.ContextBean bean = pkEvent.getPkJson().context;
        if ("PK_RECORD".equals(bean.busiCode)) {
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
                progressBar.setVisibility(View.GONE);
            }

            mShareUrl = roomInfoBean.getData().getShareUrl();

            mLiveState = roomInfoBean.getData().getProgramStatus();
        }

        initAboutAnchor(mProgramId, mAnchorId);
    }

    /**
     * 周星 主播任务 活动页面
     */
    private void initAboutAnchor(int mProgramId, int mAnchorId) {
        mLivePresenter.getActivityGrand(mProgramId, mAnchorId);
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
                        ((PrivateChatListFragment) fragments[1]).setIsGuard(isGuard);
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
        mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
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
            mRunWayGiftControl = new RunWayGiftControl(runWayText, frameSupercarTrack, ivRocket);
            mRunWayGiftControl.setListener((programId, nickname) -> showJumpLiveHouseDialog(programId, nickname));
        }
    }

    @Override
    public void onGetProgramFirstSuccess(long userId) {
        ChatRoomInfo.getInstance().setProgramFirstId(userId);
    }


    @Override
    public void onActivityListSuccess(GetActivityBean bean) {
        if (bean == null
                || bean.list == null
                || bean.list.isEmpty()) {
            banner.setVisibility(View.GONE);
            showBanner = false;
            return;
        }
        showBanner = true;
        banner.setVisibility(View.VISIBLE);
        mBannerInfoList = bean.list;
        ArrayList<String> banners = new ArrayList<>();
        for (int i = 0; i < mBannerInfoList.size(); i++) {
            banners.add(mBannerInfoList.get(i).imageUrl);
        }
        banner.setImages(banners);
        banner.start();
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

        if (bean.otherStream != null) {
            rlOtherSide.setVisibility(View.VISIBLE);
            rlOtherSideInfo.setVisibility(View.VISIBLE);
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
        otherSideLive();

        pkControl.initNet(bean);

    }

    /**
     * 直播间的活动（常规活动）
     */
    @Override
    public void onActivityGrandSuccess(ActivityGrandBean bean) {
        if (bean.list != null && bean.list.size() != 0) {
            vpActivity.setOffscreenPageLimit(bean.list.size());
            for (int i = 0; i < bean.list.size(); i++) {
                ActivityGrandBean.ListBean listBean = bean.list.get(i);
                LiveWebFragment liveWebFragment = LiveWebFragment.newInstance(listBean.linkUrl, mAnchorId + "", mProgramId + "");
                liveWebFragment.setOnclickListener(new LiveWebFragment.ClickListener() {
                    @Override
                    public void clickListener() {
                        startActivityForResult(new Intent(getBaseActivity(), JsBridgeActivity.class)
                                .putExtra("anchorId", mAnchorId + "")
                                .putExtra("programId", mProgramId + "")
                                .putExtra("title", listBean.name)
                                .putExtra("url", listBean.jumpUrl), REQUEST_LOGIN);
                    }
                });
                mActivityGrands.add(liveWebFragment);
            }
//            mGrandAdaper.notifyDataSetChanged();
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
            initActivityPoints();
        }
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
            tvContribute.setText(bean.total + "");
        } else {
            BigDecimal divide = bean.total.divide(new BigDecimal(10000), 1, BigDecimal.ROUND_HALF_DOWN);
            tvContribute.setText(divide + "万");
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
        mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GuardOpenEvent event) {
        if (event.userId == mUserId) {
            mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
        }
        showGuard(event.avatar, event.nickName);
        mLivePresenter.getGuardTotal(mProgramId);
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
//        AudienceInfoDialog.newInstance(viewedUserID, mProgramId, mRoomUserInfo)
//                .setListener(() -> {
//                    if (mGuardListDialog != null && mGuardListDialog.isAdded()) {
//                        mGuardListDialog.dismiss();
//                    }
//                })
//                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
//                .setDimAmount(0)
//                .setShowBottom(isShowBottom)
//                .show(getSupportFragmentManager());

        PersonalInfoDialog.newInstance(mRoomUserInfo, viewedUserID, mProgramId, mUserId)
                .setAnimStyle(R.style.dialogWindowAnim)
                .setDimAmount(0)
                .setShowBottom(false)
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
        mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
//        mLivePresenter.getPkInfo(mProgramId);
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
        mLivePresenter.onDestory();
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unregisterReceiver(mPkReceiver);
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
        pkLayout.destroy();
        if (pkControl != null) {
            pkControl.destroy();
        }
        if (roomRankTotalDisposable != null) {
            roomRankTotalDisposable.dispose();
        }
        if (roomOnlineDisposable != null) {
            roomOnlineDisposable.dispose();
        }
        if (mActivityGrands.size() > 0) {
            mActivityGrands.clear();
            mGrandAdaper.notifyDataSetChanged();
        }
        if (llPagerIndex.getChildCount() > 0) {
            llPagerIndex.removeAllViews();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                mUserId = (long) SPUtils.get(this, "userId", 0L);
                mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
                getRoomToken();
                isVip = true;
                LogUtils.e("sssssssss   onActivityResult");
            }
        }
        if (requestCode == AppUtils.REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                mUserId = (long) SPUtils.get(this, "userId", 0L);
                mLivePresenter.getRoomUserInfo(mUserId, mAnchorId, mProgramId);
                getRoomToken();
                isVip = true;
                LogUtils.e("sssssssss   onActivityResult");
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
            Intent intent = new Intent(LiveDisplayActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, programId);
            startActivity(intent);
            rlOtherSide.setVisibility(View.GONE);
            rlOtherSideInfo.setVisibility(View.GONE);
            textureView.setVisibility(View.INVISIBLE);
            pkLayout.setVisibility(View.GONE);
            ivCountDown.clearAnimation();
        });
        dialog.show();
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
                    royalEnterControl.setIvEnter(ivEnterCar);
                    royalEnterControl.setContext(this);
                    royalEnterControl.setClEnter(clEnter);
                }
//            String imageUrl = ImageUrl.getImageUrl(((WelcomeMsg) message).getCarId(), "jpg");
//            GlideImageLoader.getInstace().displayImage(getContext(), imageUrl, ivEnter);
//            royalEnterControl.showEnter(welcomeJson.getContext().getInfo().getNickname());
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
            textureView2.setDataSource(stream);
            textureView2.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
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
}
