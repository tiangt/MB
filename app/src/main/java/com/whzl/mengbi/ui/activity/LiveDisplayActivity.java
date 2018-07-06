package com.whzl.mengbi.ui.activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
<<<<<<< HEAD
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventCode;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.message.ChatCommonMesBean;
import com.whzl.mengbi.model.entity.message.UserMesBean;
import com.whzl.mengbi.thread.LiveChatRoomTokenThread;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.widget.BottomNavigationViewHelper;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.PubChatView;
=======
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.message.ChatCommonMesBean;
import com.whzl.mengbi.model.entity.message.UserMesBean;
import com.whzl.mengbi.presenter.LivePresenter;
import com.whzl.mengbi.presenter.impl.LivePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventCode;
import com.whzl.mengbi.ui.adapter.LiveChatFaceAdapter;
import com.whzl.mengbi.ui.adapter.LiveGiftLuckyAdapter;
import com.whzl.mengbi.ui.adapter.LiveGiftLuxuryAdapter;
import com.whzl.mengbi.ui.adapter.LiveGiftNormalAdapter;
import com.whzl.mengbi.ui.adapter.LiveGiftRecommendAdapter;
import com.whzl.mengbi.ui.adapter.LiveMessageAdapter;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.ui.widget.BottomNavigationViewHelper;
import com.whzl.mengbi.ui.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.CustomPopWindow;
import com.whzl.mengbi.util.CustomPopWindowUtils;
>>>>>>> b3735764531344faa4f06f02fa0e00a252f6d862
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.SPUtils;
<<<<<<< HEAD
=======

import com.whzl.mengbi.util.SpannableStringUitls;
>>>>>>> b3735764531344faa4f06f02fa0e00a252f6d862
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播间
 */
public class LiveDisplayActivity extends BaseAtivity implements LiveView, View.OnClickListener{

    private SurfaceView mMasterSurfaceView;
    private KSYMediaPlayer mMasterPlayer;
    private Context  mContext;
    private View mView;
    private RecyclerView.LayoutManager mGiftLayoutManager;
    private RecyclerView.LayoutManager mTalkLayoutManager;
    //private RecyclerView.LayoutManager mMessageLayoutManager;
    private ViewPager  mGiftViewPager;
    private MenuItem mMenuItem;

    /**
     *顶部
     */
    private int mProgramId;
    private String mAnchorNickName;
    private int mRoomUserCount;
    private String mCover;
    private String mStream;
    private CircleImageView mProfilePhoto;
    private TextView mAnchorNickNameTV;
    private TextView mFans;
    private ImageView mFollow;
    private Button mContribution;
    private ImageView mClose;
    private TextView mPopularity;
    /**
     *礼物
     */
    private CircleImageView mCircleImageViewGift;
    private BottomNavigationView mBottomNavigationView;
    private CustomPopWindow mGiftPopWindow;
    private RecyclerView mGiftRecyclerView;
    private CommonAdapter mGiftCommonAdapter;
    private List<GiftInfo.DataBean.推荐Bean> giftRecommendList = new ArrayList<>();
    private List<GiftInfo.DataBean.幸运Bean> giftLuckyList = new ArrayList<>();
    private List<GiftInfo.DataBean.普通Bean> giftNormalList = new ArrayList<>();
    private List<GiftInfo.DataBean.豪华Bean> giftLuxuryList = new ArrayList<>();
    private List<EmjoyInfo.FaceBean.PublicBean> mFaceData = new ArrayList();

    /**
     * 聊天
     */
    private CircleImageView mCircleImageViewTalk;
    private CustomPopWindow mTalkPopWindow;
    private ImageView mFaceImageView;
    private Button sendMessageBut;
    private RecyclerView mTalkRecyclerView;
    private LiveChatFaceAdapter<EmjoyInfo.FaceBean.PublicBean> mLiveChatFaceAdapter;

    private EditText talkEditText;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private CommonAdapter mMessageAdapter;
    //private RecyclerView mMessageRecyclerView;
    private PubChatView chatView;
    private List mMessageData = new ArrayList();
    private LiveRoomTokenInfo liveRoomTokenInfo;

    private LivePresenter livePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_display_layout);
        mContext = this;
        livePresenter = new LivePresenterImpl(this);
        if(getIntent()!=null){
            mProgramId = getIntent().getIntExtra("ProgramId",-1);
            SPUtils.put(mContext,"programId",mProgramId);
            mAnchorNickName = getIntent().getStringExtra("AnchorNickname");
            mRoomUserCount = getIntent().getIntExtra("RoomUserCount",-1);
            mCover = getIntent().getStringExtra("Cover");
            mStream = getIntent().getStringExtra("Stream");
        }

        giftData();
        readFaceData();
        getLiveRoomToken();
        initPlayers();
        initViews();

        if(chatRoomPresenter==null){
            chatRoomPresenter = new ChatRoomPresenterImpl();
        }
    }

    private void initViews() {
        mProfilePhoto =(CircleImageView) findViewById(R.id.live_display_profile_photo);
        mAnchorNickNameTV = (TextView)findViewById(R.id.live_display_anchornickname);
        mFans = (TextView)findViewById(R.id.live_display_fans);
        mFollow = (ImageView)findViewById(R.id.live_display_follow);
        mContribution = (Button) findViewById(R.id.live_display_contribution);
        mPopularity = (TextView) findViewById(R.id.live_display_popularity);
        mClose = (ImageView)findViewById(R.id.live_display_close);
        mMasterSurfaceView = (SurfaceView) findViewById(R.id.player_master);
<<<<<<< HEAD
        chatView = (PubChatView)findViewById(R.id.pub_chat_view);
//        mMessageLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
//        mMessageRecyclerView = findViewById(R.id.live_display_message_recyclerview);
//        mMessageRecyclerView.setLayoutManager(mMessageLayoutManager);
=======
        mMessageRecyclerView =(RecyclerView) findViewById(R.id.live_display_message_recyclerview);
        mCircleImageViewTalk = (CircleImageView) findViewById(R.id.live_display_talk);
        mCircleImageViewGift = (CircleImageView) findViewById(R.id.live_display_gift);
        mMessageLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mMessageRecyclerView.setLayoutManager(mMessageLayoutManager);
>>>>>>> b3735764531344faa4f06f02fa0e00a252f6d862
        mMasterSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
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

        //显示数据
        GlideImageLoader.getInstace().circleCropImage(mContext,mCover,mProfilePhoto);
        mAnchorNickNameTV.setText(mAnchorNickName);
        SpannableStringBuilder string =  new SpannableStringBuilder("人气");
        string.append("\n");
        string.append(mRoomUserCount+"");
        mPopularity.setText(string);
        //绑定事件
        mCircleImageViewGift.setOnClickListener(this);
        mCircleImageViewTalk.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    private void initPlayers() {
        mMasterPlayer = new KSYMediaPlayer.Builder(LiveDisplayActivity.this).build();
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

    /**
     * 事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = DensityUtil.dp2px(400);
        switch (v.getId()){
            case R.id.live_display_gift:
                mView = getLayoutInflater().inflate(R.layout.activity_live_display_gift_pop_bottom_layout,null);
                mGiftPopWindow = CustomPopWindowUtils.giftCustomPopWindow(mContext,mView,mCircleImageViewGift,width,height);
                giftViewAndData(mView);
                break;
            case R.id.live_display_talk:
                mView = getLayoutInflater().inflate(R.layout.activity_live_display_talk_layout,null);
                mTalkPopWindow = CustomPopWindowUtils.talkCustomPopWindow(mContext,mView,mCircleImageViewTalk,width,height);
                loadFaceViewAndData(mView);
                break;
            case R.id.live_display_close:
                finish();
                break;
        }
    }

    /**
     * 礼物列表
     */
    public void giftData(){
       livePresenter.getLiveGift();
    }

    /**
     *装载礼物数据
     * @param contentView
     */
    public void giftViewAndData(View contentView){
        mGiftRecyclerView = contentView.findViewById(R.id.live_display_gift_rv);
        mGiftLayoutManager = new GridLayoutManager(this,4);
        mGiftRecyclerView.setLayoutManager(mGiftLayoutManager);
        //底部导航切换，滑动
        //mGiftViewPager = contentView.findViewById(R.id.live_display_gift_viewpager);
        mBottomNavigationView = (BottomNavigationView) contentView.findViewById(R.id.live_display_bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_live_display_gfit_recommend:
                                mGiftRecyclerView.setAdapter(new LiveGiftRecommendAdapter<GiftInfo.DataBean.推荐Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftRecommendList));
                                break;
                            case R.id.item_ive_display_gift_lucky:
                                mGiftRecyclerView.setAdapter(new LiveGiftLuckyAdapter<GiftInfo.DataBean.幸运Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftLuckyList));
                                break;
                            case R.id.item_live_display_gift_normal:
                                mGiftRecyclerView.setAdapter(new LiveGiftNormalAdapter<GiftInfo.DataBean.普通Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftNormalList));
                                break;
                            case R.id.item_live_display_gift_luxuryn:
                                mGiftRecyclerView.setAdapter(new LiveGiftLuxuryAdapter<GiftInfo.DataBean.豪华Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftLuxuryList));
                                break;
                        }
                        return false;
                    }
                });
    }


    /**
     * 装载表情数据
     * @param contentView
     */
    public void loadFaceViewAndData(View contentView){
        mFaceImageView = contentView.findViewById(R.id.live_display_talk_face);
        talkEditText = contentView.findViewById(R.id.live_display_talk_message);
        sendMessageBut = contentView.findViewById(R.id.live_display_talk_send_message);

        //显示表情
        mFaceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTalkLayoutManager = new GridLayoutManager(mContext,7);
                mTalkRecyclerView = contentView.findViewById(R.id.live_display_talk_rv);
                mTalkRecyclerView.setLayoutManager(mTalkLayoutManager);
                mLiveChatFaceAdapter = new LiveChatFaceAdapter(mContext,R.layout.activity_live_display_talk_pop_rvitem_layout,mFaceData);
                mTalkRecyclerView.setAdapter(mLiveChatFaceAdapter);
                mLiveChatFaceAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        String faceIcon = mFaceData.get(position).getIcon();
                        String faceVal= mFaceData.get(position).getValue();
                        Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(faceIcon,mContext);
                        ImageSpan imageSpan = new ImageSpan(mContext,bitmap);
                        SpannableString spannableString=new SpannableString(faceVal);
                        spannableString.setSpan(imageSpan,0,faceVal.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        talkEditText.append(spannableString);
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
            }
        });

        //聊天信息发送
        sendMessageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes = talkEditText.getEditableText().toString().trim();
                chatRoomPresenter.sendMessage(mes);
                talkEditText.setText("");
            }
        });
    }

    /**
     * 加载表情数据
     */
    public void readFaceData(){
        String fileName = "images/face/face.json";
<<<<<<< HEAD
        String strJson= FileUtils.getJson(fileName,mContext);
        EmjoyInfo emjoyInfo = GsonUtils.GsonToBean(strJson,EmjoyInfo.class);
        mFaceData.addAll(emjoyInfo.getFace().getFaceList());
=======
        livePresenter.getLiveFace(fileName);
>>>>>>> b3735764531344faa4f06f02fa0e00a252f6d862
    }

    /**
     * 获取直播间token
     */
    public void getLiveRoomToken(){
        int userId = (Integer) SPUtils.get(mContext,"userId",0);
        String sessionId = SPUtils.get(mContext,"sessionId","0").toString();
        HashMap map = new HashMap();
        map.put("userId",userId);
        map.put("programId",mProgramId);
        map.put("sessionId", sessionId);
        livePresenter.getLiveToken(map);
    }

    @Override
    protected void receiveEvent(EventBusBean event) {
        // 接收到Event后的相关逻辑
        switch (event.getCode()){
            case EventCode.SEND_SYSTEM_MESSAGE:
                Object sysmes = event.getData();
                break;
            case EventCode.CHAT_COMMON:
                ChatCommonMesBean commonMesBean = (ChatCommonMesBean)event.getData();
<<<<<<< HEAD
                SpannableStringBuilder spannableStringBuilder = faceReplace(commonMesBean);
                /*mMessageRecyclerView.setAdapter(mMessageAdapter = new CommonAdapter(mContext,R.layout.live_display_message_item_layout,mMessageData) {
                    @Override
                    protected void convert(ViewHolder holder, Object obj, int position) {
                        if(mMessageData!=null){
                                GlideImageLoader.getInstace().displayImage(mContext,mMessageData.get(position),holder.getView(R.id.live_display_message_item_userlevel_img));
                                GlideImageLoader.getInstace().displayImage(mContext,mMessageData.get(position),holder.getView(R.id.live_display_message_item_usermedal_img));
                                GlideImageLoader.getInstace().displayImage(mContext,mMessageData.get(position),holder.getView(R.id.live_display_message_item_usermedal2_img));
                                GlideImageLoader.getInstace().displayImage(mContext,mMessageData.get(position),holder.getView(R.id.live_display_message_item_usermedal3_img));
                                holder.getView(R.id.live_display_message_item_usermedal_img).setVisibility(View.VISIBLE);
                                holder.getView(R.id.live_display_message_item_usermedal2_img).setVisibility(View.VISIBLE);
                                holder.getView(R.id.live_display_message_item_usermedal3_img).setVisibility(View.VISIBLE);
                        }else{
                            holder.getView(R.id.live_display_message_item_usermedal_img).setVisibility(View.GONE);
                            holder.getView(R.id.live_display_message_item_usermedal2_img).setVisibility(View.GONE);
                            holder.getView(R.id.live_display_message_item_usermedal3_img).setVisibility(View.GONE);
                        }

                        TextView mesage= (TextView)holder.getView(R.id.live_display_message_item_text);
                        mesage.append(commonMesBean.getFrom_nickname());
                        mesage.append(spannableStringBuilder);
                        //mMessageAdapter.notifyItemInserted(position);
                    }
                });
=======
                SpannableStringBuilder spannableStringBuilder = SpannableStringUitls.faceReplace(mContext,commonMesBean,mFaceData);
                mMessageRecyclerView.setAdapter(new LiveMessageAdapter(mContext,R.layout.live_display_message_item_layout,mMessageData));
                TextView mesage= (TextView)findViewById(R.id.live_display_message_item_text);
                mesage.append(commonMesBean.getFrom_nickname());
                mesage.append(spannableStringBuilder);
>>>>>>> b3735764531344faa4f06f02fa0e00a252f6d862
                mMessageAdapter.notifyDataSetChanged();
                */
                break;
            case EventCode.CHAT_PRIVATE:
                break;
            case EventCode.TOURIST:
                Object nickname = event.getData();
                break;
            case EventCode.LOGIN_USER:
                UserMesBean userMesBean = (UserMesBean) event.getData();
                break;
        }
    }

    @Override
    public void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo) {
        this.liveRoomTokenInfo = liveRoomTokenInfo;
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatRoomPresenter.setupConnection(liveRoomTokenInfo);
            }
        }).start();
    }

    @Override
    public void onLiveFaceSuccess(EmjoyInfo emjoyInfo) {
        mFaceData.addAll(emjoyInfo.getFace().getPublicX());
    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        giftRecommendList.addAll(giftInfo.getData().get推荐());
        giftLuckyList.addAll(giftInfo.getData().get幸运());
        giftNormalList.addAll(giftInfo.getData().get普通());
        giftLuxuryList.addAll(giftInfo.getData().get豪华());
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
<<<<<<< HEAD
        if (chatView != null) {
            chatView.unregister();
        }
=======
        livePresenter.onDestory();
>>>>>>> b3735764531344faa4f06f02fa0e00a252f6d862
        if(chatRoomPresenter!=null){
            chatRoomPresenter.disconnectChat();
        }
        if (mMasterPlayer != null)
            mMasterPlayer.release();
            mMasterPlayer = null;
        }
}
