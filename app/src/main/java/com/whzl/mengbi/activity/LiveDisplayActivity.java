package com.whzl.mengbi.activity;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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


import com.google.gson.Gson;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.application.BaseAppliaction;
import com.whzl.mengbi.bean.message.ChatCommonMesBean;
import com.whzl.mengbi.bean.message.UserMesBean;
import com.whzl.mengbi.bean.EmjoyBean;
import com.whzl.mengbi.bean.GiftBean;
import com.whzl.mengbi.bean.LiveRoomTokenBean;
import com.whzl.mengbi.chat.client.ServerAddr;
import com.whzl.mengbi.chat.room.ChatRoomPresenterImpl;
import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventCode;
import com.whzl.mengbi.glide.GlideImageLoader;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.thread.LiveChatRoomTokenThread;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;

import com.whzl.mengbi.widget.BottomNavigationViewHelper;
import com.whzl.mengbi.widget.recyclerview.CommonAdapter;
import com.whzl.mengbi.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.widget.recyclerview.base.ViewHolder;
import com.whzl.mengbi.widget.view.CircleImageView;
import com.whzl.mengbi.widget.view.CustomPopWindow;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播间
 */
public class LiveDisplayActivity extends BaseAtivity implements View.OnClickListener{

    private SurfaceView mMasterSurfaceView;
    private KSYMediaPlayer mMasterPlayer;
    private Context  mContext;
    private View mView;
    private RecyclerView.LayoutManager mGiftLayoutManager;
    private RecyclerView.LayoutManager mTalkLayoutManager;
    private RecyclerView.LayoutManager mMessageLayoutManager;
    private ViewPager  mGiftViewPager;
    private MenuItem mMenuItem;
    /**
     *
     */
    private int mProgramId;
    private String mAnchorNickName;
    private int mRoomUserCount;
    private String mCover;
    private String mStream;
    @BindView(R.id.live_display_profile_photo)
    public CircleImageView mProfilePhoto;
    @BindView(R.id.live_display_anchornickname)
    public TextView mAnchorNickNameTv;
    @BindView(R.id.live_display_fans)
    public TextView mFans;
    @BindView(R.id.live_display_follow)
    public ImageView mFollow;
    @BindView(R.id.live_display_contribution)
    public Button mContribution;
    @BindView(R.id.live_display_close)
    public ImageView mClose;

    /**
     *礼物
     */
    @BindView(R.id.live_display_gift)
    public CircleImageView mCircleImageViewGift;
    private BottomNavigationView mBottomNavigationView;
    private CustomPopWindow mGiftPopWindow;
    private RecyclerView mGiftRecyclerView;
    private CommonAdapter mGiftCommonAdapter;
    private List<GiftBean.DataBean.推荐Bean> giftRecommendList = new ArrayList<>();
    private List<GiftBean.DataBean.幸运Bean> giftLuckyList = new ArrayList<>();
    private List<GiftBean.DataBean.普通Bean> giftNormalList = new ArrayList<>();
    private List<GiftBean.DataBean.豪华Bean> giftLuxuryList = new ArrayList<>();
    private List<EmjoyBean.FaceBean.PublicBean> mFaceData = new ArrayList();

    /**
     * 聊天
     */
    @BindView(R.id.live_display_talk)
    public CircleImageView mCircleImageViewTalk;
    private CustomPopWindow mTalkPopWindow;
    private ImageView mFaceImageView;
    private Button sendMessageBut;
    private RecyclerView mTalkRecyclerView;
    private CommonAdapter mTalkCommonAdapter;

    private EditText talkEditText;
    private ChatRoomPresenterImpl chatRoomPresenter;
    private CommonAdapter mMessageAdapter;
    private RecyclerView mMessageRecyclerView;
    private List mMessageData = new ArrayList();
    private LiveChatRoomTokenThread liveChatRoomTokenThread;
    private LiveChatRoomTokenHandler liveChatRoomTokenHandler = new LiveChatRoomTokenHandler(this);
    private LiveRoomTokenBean liveRoomTokenBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_display_layout);
        ButterKnife.bind(this);
        mContext = this;
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
        mMasterSurfaceView = (SurfaceView) findViewById(R.id.player_master);
        mMessageLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mMessageRecyclerView = findViewById(R.id.live_display_message_recyclerview);
        mMessageRecyclerView.setLayoutManager(mMessageLayoutManager);
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
        GlideImageLoader.getInstace().displayImage(mContext,mCover,mProfilePhoto);
        mCircleImageViewGift.setOnClickListener(this);
        mCircleImageViewTalk.setOnClickListener(this);

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
                giftViewAndData(mView);
                mGiftPopWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                        .setView(mView)//显示的布局，还可以通过设置一个View
                        .size(width,height) //设置显示的大小，不设置就默认包裹内容
                        .setFocusable(true)//是否获取焦点，默认为ture
                        .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                        .create()//创建PopupWindow
                        .showAtLocation(mCircleImageViewGift, Gravity.BOTTOM,0,0);
                break;
            case R.id.live_display_talk:
                mView = getLayoutInflater().inflate(R.layout.activity_live_display_talk_layout,null);
                loadFaceViewAndData(mView);
                mTalkPopWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                        .setView(mView)//显示的布局，还可以通过设置一个View
                        .size(width,height) //设置显示的大小，不设置就默认包裹内容
                        .setFocusable(true)//是否获取焦点，默认为ture
                        .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                        .create()//创建PopupWindow
                        .showAtLocation(mCircleImageViewTalk, Gravity.BOTTOM,0,0);
                break;
        }
    }

    /**
     * 礼物列表
     */
    public void giftData(){
        HashMap parmarMap = new HashMap();
        RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.GIFT_LIST,RequestManager.TYPE_POST_JSON,parmarMap,
        new RequestManager.ReqCallBack<Object>() {

            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                GiftBean giftBean = GsonUtils.GsonToBean(strJson,GiftBean.class);
                if(giftBean.getCode()==200){
                    giftRecommendList.addAll(giftBean.getData().get推荐());
                    giftLuckyList.addAll(giftBean.getData().get幸运());
                    giftNormalList.addAll(giftBean.getData().get普通());
                    giftLuxuryList.addAll(giftBean.getData().get豪华());
                }
            }
            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    /**
     *装载礼物数据
     * @param contentView
     */
    public void giftViewAndData(View contentView){
        mGiftRecyclerView = contentView.findViewById(R.id.live_display_gift_rv);
        mGiftLayoutManager = new GridLayoutManager(this,4);
        mGiftRecyclerView.setLayoutManager(mGiftLayoutManager);
        giftChange(0);
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
                                giftChange(0);
                                break;
                            case R.id.item_ive_display_gift_lucky:
                                giftChange(1);
                                break;
                            case R.id.item_live_display_gift_normal:
                                giftChange(2);
                                break;
                            case R.id.item_live_display_gift_luxuryn:
                                giftChange(3);
                                break;
                        }
                        return false;
                    }
                });

    }

    /**
     * 礼物切换
     * flag 0：推荐，1幸运，2普通，3豪华
     * @param flag
     */
    public void giftChange(int flag){
        if(flag==0){
            //推荐
            mGiftRecyclerView.setAdapter(new CommonAdapter<GiftBean.DataBean.推荐Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftRecommendList) {
                @Override
                protected void convert(ViewHolder holder, GiftBean.DataBean.推荐Bean o, int position) {
                    GlideImageLoader.getInstace().displayImage(mContext,o.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
                    holder.setText(R.id.live_display_rvitem_gift_name,o.getGoodsName());
                    holder.setText(R.id.live_display_rvitem_gift_rent,o.getRent()+"");
                }
            });
        }else if(flag==1){
            //幸运
            mGiftRecyclerView.setAdapter(new CommonAdapter<GiftBean.DataBean.幸运Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftLuckyList) {
                @Override
                protected void convert(ViewHolder holder, GiftBean.DataBean.幸运Bean o, int position) {
                    GlideImageLoader.getInstace().displayImage(mContext,o.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
                    holder.setText(R.id.live_display_rvitem_gift_name,o.getGoodsName());
                    holder.setText(R.id.live_display_rvitem_gift_rent,o.getRent()+"");
                }
            });
        }else if(flag==2){
            //普通
            mGiftRecyclerView.setAdapter(new CommonAdapter<GiftBean.DataBean.普通Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftNormalList) {
                @Override
                protected void convert(ViewHolder holder, GiftBean.DataBean.普通Bean o, int position) {
                    GlideImageLoader.getInstace().displayImage(mContext,o.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
                    holder.setText(R.id.live_display_rvitem_gift_name,o.getGoodsName());
                    holder.setText(R.id.live_display_rvitem_gift_rent,o.getRent()+"");
                }
            });
        }else if(flag==3){
            //豪华
            mGiftRecyclerView.setAdapter(new CommonAdapter<GiftBean.DataBean.豪华Bean>(mContext,R.layout.activity_live_display_rvitem_layout,giftLuxuryList) {
                @Override
                protected void convert(ViewHolder holder, GiftBean.DataBean.豪华Bean o, int position) {
                    GlideImageLoader.getInstace().displayImage(mContext,o.getGoodPic(),holder.getView(R.id.live_display_rvitem_gift_img));
                    holder.setText(R.id.live_display_rvitem_gift_name,o.getGoodsName());
                    holder.setText(R.id.live_display_rvitem_gift_rent,o.getRent()+"");
                }
            });
        }
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
                mTalkRecyclerView.setAdapter(mTalkCommonAdapter = new CommonAdapter<EmjoyBean.FaceBean.PublicBean>(mContext,R.layout.activity_live_display_talk_pop_rvitem_layout,mFaceData) {
                    @Override
                    protected void convert(ViewHolder holder,EmjoyBean.FaceBean.PublicBean publicBean, int position) {
                        Bitmap path = FileUtils.readBitmapFromAssetsFile(publicBean.getIcon(),mContext);
                        GlideImageLoader.getInstace().displayImage(mContext,path,holder.getView(R.id.live_display_talk_rvitme_face));
                    }
                });
                mTalkCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
        String strJson= FileUtils.getJson(fileName,mContext);
        EmjoyBean emjoyBean = GsonUtils.GsonToBean(strJson,EmjoyBean.class);
        mFaceData.addAll(emjoyBean.getFace().getPublicX());
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
        liveChatRoomTokenThread = new LiveChatRoomTokenThread(this,map,liveChatRoomTokenHandler);
        liveChatRoomTokenThread.start();
    }




    @Override
    protected void receiveEvent(EventBusBean event) {
        // 接受到Event后的相关逻辑
        switch (event.getCode()){
            case EventCode.SEND_SYSTEM_MESSAGE:
                Object sysmes = event.getData();
                break;
            case EventCode.CHAT_COMMON:
                ChatCommonMesBean commonMesBean = (ChatCommonMesBean)event.getData();
                SpannableStringBuilder spannableStringBuilder = faceReplace(commonMesBean);
                mMessageRecyclerView.setAdapter(mMessageAdapter = new CommonAdapter(mContext,R.layout.live_display_message_item_layout,mMessageData) {
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
                mMessageAdapter.notifyDataSetChanged();
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
    public SpannableStringBuilder  faceReplace(ChatCommonMesBean commonMesBean){
        String contentVal = commonMesBean.getContent();
        SpannableStringBuilder spannableString = new SpannableStringBuilder(contentVal);
        //循环得到用户等级勋章
        if(commonMesBean.getFrom_json().getLevelList()!=null){
            for(ChatCommonMesBean.FromJsonBean.LevelListBean levelListBean:commonMesBean.getFrom_json().getLevelList()){
                if(levelListBean.getLevelType().equals("ROYAL_EXP")){
                    try {
                        int levelVal = levelListBean.getLevelValue();
                        Field field =R.mipmap.class.getField(""+levelVal);
                        int fieldId = field.getInt(new R.mipmap());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(levelListBean.getLevelType().equals("USER_LEVEL")){
                    try {
                        int levelVal = levelListBean.getLevelValue();
                        Field field =R.mipmap.class.getField("usergrade"+levelVal);
                        int fieldId = field.getInt(new R.mipmap());
                        mMessageData.add(fieldId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //循环得到用户身份勋章
        if(commonMesBean.getFrom_json().getGoodsList()!=null){
            int [] mGoodsIcon = new  int[commonMesBean.getFrom_json().getGoodsList().size()];
            String [] mGoodsIconId;
            String []imageUrl;
            for (int i=0; i<commonMesBean.getFrom_json().getGoodsList().size();i++){
                mGoodsIcon[i] = commonMesBean.getFrom_json().getGoodsList().get(i).getGoodsIcon();
                mGoodsIconId = new String[mGoodsIcon.length];
                mGoodsIconId[i] = String.format("%09d",mGoodsIcon[i]);
                imageUrl = new String[mGoodsIconId.length];
                imageUrl[i] = URLContentUtils.BASE_IMAGE_URL+"default/"+
                        mGoodsIconId[i].substring(0,3)+"/"+mGoodsIconId[i].substring(3,5)+"/"+
                        mGoodsIconId[i].substring(5,7)+"/"+mGoodsIconId[i].substring(7,9)+""+".jpg";
                mMessageData.add(imageUrl);
            }
        }
        //循环替换聊天信息表情图片
        for(int i = 0; i < mFaceData.size(); ++i) {
            String faceVal = mFaceData.get(i).getValue();
            String tmpValue = contentVal;
            int pos = tmpValue.indexOf(faceVal);
            int spanPos = 0;
            while (pos>=0){
                String faceIcon = mFaceData.get(i).getIcon();
                Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(faceIcon,mContext);
                ImageSpan imageSpan = new ImageSpan(bitmap);
                spannableString.setSpan(imageSpan,spanPos, spanPos + faceVal.length(),SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                tmpValue = tmpValue.substring(pos + faceVal.length());
                pos = tmpValue.indexOf(faceVal);
                spanPos += faceVal.length();
            }
        }
        return spannableString;
    }

    /**
     * 获取直播间TokenHandler
     */
    private static class LiveChatRoomTokenHandler extends Handler{

        private final WeakReference<Activity> activityWeakReference;

        LiveChatRoomTokenHandler(Activity activity){
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LiveDisplayActivity liveDisplayActivity = (LiveDisplayActivity) activityWeakReference.get();
            switch (msg.what){
                case 1:
                    liveDisplayActivity.liveRoomTokenBean = (LiveRoomTokenBean) msg.obj;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            liveDisplayActivity.chatRoomPresenter.setupConnection(liveDisplayActivity.liveRoomTokenBean);
                        }
                    }).start();
                    break;
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(chatRoomPresenter!=null){
            //chatRoomPresenter.disconnectChat();
        }

        if (mMasterPlayer != null)
            mMasterPlayer.release();
            mMasterPlayer = null;
        }

}
