package com.whzl.mengbi.ui.dialog;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatUIEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.CLickGuardOrVipEvent;
import com.whzl.mengbi.gen.PrivateChatUserDao;
import com.whzl.mengbi.greendao.ChatDbUtils;
import com.whzl.mengbi.greendao.PrivateChatContent;
import com.whzl.mengbi.greendao.PrivateChatUser;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.BaseAnimation;
import com.whzl.mengbi.ui.adapter.ChatMsgAnimation;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * @author nobody
 * @date 2019/2/12
 */
public class PrivateChatDialog extends BaseAwesomeDialog {
    private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int WARN = 3;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_input_change)
    Button btnInputChange;
    @BindView(R.id.ll_bottom_container)
    LinearLayout llBottomContainer;
    @BindView(R.id.view_close)
    View viewClose;
    @BindView(R.id.ib_close)
    LinearLayout ibClose;

    private RecyclerView.Adapter chatAdapter;
    private static final int TOTAL_CHAT_MSG = 100;
    private boolean isRecyclerScrolling;
    private ArrayList<FillHolderMessage> chatList = new ArrayList<>();
    private boolean isGuard;
    private boolean isVip;
    private int mProgramId;
    private ArrayList<RoomUserInfo.DataBean> roomUsers = new ArrayList<>();
    private RoomInfoBean.DataBean.AnchorBean mAnchor;
    private PrivateChatUser mCurrentChatToUser;
    private boolean isHidden;
    private BaseAwesomeDialog mChatDialog;
    private long mUserId;
    private long current = System.currentTimeMillis();
    private String[] tips = new String[]{"小哥哥，怎么不理我呀！", "小哥哥哪里人？", "小哥哥，在吗？", "HI，这里可以跟我私聊哦！"};
    private long anchorId;

    public static BaseAwesomeDialog newInstance(int programId) {
        PrivateChatDialog privateChatDialog = new PrivateChatDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("programId", programId);
        privateChatDialog.setArguments(bundle);
        return privateChatDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_private_chat;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        tvTitle.setText(mCurrentChatToUser.getName());
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mProgramId = getArguments().getInt("programId");
        mUserId = (long) SPUtils.get(getActivity(), SpConfig.KEY_USER_ID, 0L);
        initChatRecycler();
        viewClose.setOnClickListener(v -> {
            if (isAdded()) {
                dismiss();
            }
        });
        ibClose.setOnClickListener(v -> {
            if (isAdded()) {
                dismiss();
            }
        });
        if (mUserId == 0) {
            if (mCurrentChatToUser.getIsAnchor() && anchorId == mCurrentChatToUser.getPrivateUserId()) {
                ChatCommonJson chatCommonJson = new ChatCommonJson();
                Random random = new Random();
                chatCommonJson.setContent(tips[random.nextInt(tips.length)]);
                chatCommonJson.setFrom_uid(String.valueOf(mCurrentChatToUser.getPrivateUserId()));
                ChatMessage chatMessage = new ChatMessage(chatCommonJson, getActivity(), null, true);
                chatMessage.timeStamp = System.currentTimeMillis();
                chatMessage.isAnchor = mCurrentChatToUser.getIsAnchor();
                chatList.add(chatMessage);

//                ChatCommonJson warn = new ChatCommonJson();
//                warn.setFrom_uid(String.valueOf(mCurrentChatToUser.getPrivateUserId()));
//                ChatMessage warnMsg = new ChatMessage(chatCommonJson, getActivity(), null, true);
//                warnMsg.timeStamp = System.currentTimeMillis();
//                warnMsg.isWarn = 1;
//                chatList.add(warnMsg);
                chatAdapter.notifyDataSetChanged();
            }
        } else {
            initData();
        }
    }

    private void initData() {
        Observable.create((ObservableOnSubscribe<List<PrivateChatContent>>) emitter -> {
            List<PrivateChatContent> list = ChatDbUtils.getInstance().queryChatContent(mCurrentChatToUser.getPrivateUserId());
            emitter.onNext(list);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    for (int i = 0; i < list.size(); i++) {
                        PrivateChatContent chatContent = list.get(i);
                        ChatCommonJson chatCommonJson = new ChatCommonJson();
                        chatCommonJson.setContent(chatContent.getContent());
                        chatCommonJson.setFrom_uid(chatContent.getFromId().toString());
                        ChatMessage chatMessage = new ChatMessage(chatCommonJson, getActivity(), null, true);
                        chatMessage.isAnchor = chatContent.getIsAnchor();
                        chatMessage.timeStamp = chatContent.getTimestamp();
                        chatList.add(chatMessage);
                    }
                    chatAdapter.notifyDataSetChanged();
                    if (chatList != null && !chatList.isEmpty()) {
                        recycler.smoothScrollToPosition(chatList.size() - 1);
                    }
                });
    }


    class RightViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivAvatar;
        private final ImageView ivWarn;
        private final TextView tvContent;
        private final TextView tvTime;

        public RightViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivWarn = itemView.findViewById(R.id.iv_warn);
            tvTime = itemView.findViewById(R.id.tv_time_private_chat);
        }

        public void bindData(ChatMessage chatMessage, int position) {
            Glide.with(BaseApplication.getInstance()).load(ImageUrl.getAvatarUrl(Long.parseLong(String.valueOf(chatMessage.from_uid)), "jpg", current))
                    .apply(new RequestOptions().transform(new CircleCrop())).into(ivAvatar);
            tvContent.setText("");
            SpannableString spanString = new SpannableString(chatMessage.chatJson.getContent());
            FaceReplace.getInstance().faceReplace(tvContent, spanString, BaseApplication.getInstance());
            if (isGuard) {
                FaceReplace.getInstance().guardFaceReplace(tvContent, spanString, BaseApplication.getInstance());
            }
            if (isVip) {
                FaceReplace.getInstance().vipFaceReplace(tvContent, spanString, BaseApplication.getInstance());
            }
            tvContent.append(spanString);
            if (chatMessage.isWarn == 1 || chatMessage.isWarn == 2) {
                ivWarn.setVisibility(View.VISIBLE);
            } else {
                ivWarn.setVisibility(View.GONE);
            }

            if (position == 0) {
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(DateUtils.getDateToString(chatMessage.timeStamp, "HH:mm"));
            } else {
                tvTime.setVisibility(View.GONE);
            }

            if (position - 1 >= 0) {
                ChatMessage lastChatMessage = (ChatMessage) chatList.get(position - 1);
                if ((chatMessage.timeStamp - lastChatMessage.timeStamp) / 1000 > 600) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(DateUtils.getDateToString(chatMessage.timeStamp, "HH:mm"));
                } else {
                    tvTime.setVisibility(View.GONE);
                }
            }
        }
    }

    class SingleTextViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivAvatar;
        private final ImageView ivAnchor;
        private final TextView tvContent;
        private final TextView tvTime;

        public SingleTextViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivAnchor = itemView.findViewById(R.id.iv_anchor_private);
            tvTime = itemView.findViewById(R.id.tv_time_private_chat);
        }

        public void bindData(ChatMessage chatMessage, int position) {
            Glide.with(BaseApplication.getInstance()).load(ImageUrl.getAvatarUrl(Long.parseLong(String.valueOf(chatMessage.from_uid)), "jpg", current))
                    .apply(new RequestOptions().transform(new CircleCrop())).into(ivAvatar);
            tvContent.setText("");
            SpannableString spanString = new SpannableString(chatMessage.chatJson.getContent());
            FaceReplace.getInstance().faceReplace(tvContent, spanString, BaseApplication.getInstance());
            if (isGuard) {
                FaceReplace.getInstance().guardFaceReplace(tvContent, spanString, BaseApplication.getInstance());
            }
            if (isVip) {
                FaceReplace.getInstance().vipFaceReplace(tvContent, spanString, BaseApplication.getInstance());
            }
            tvContent.append(spanString);
            if (chatMessage.isAnchor && anchorId == chatMessage.from_uid) {
                ivAnchor.setVisibility(View.VISIBLE);
            } else {
                ivAnchor.setVisibility(View.GONE);
            }

            if (position == 0) {
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(DateUtils.getDateToString(chatMessage.timeStamp, "HH:mm"));
            } else {
                tvTime.setVisibility(View.GONE);
            }

            if (position - 1 >= 0) {
                ChatMessage lastChatMessage = (ChatMessage) chatList.get(position - 1);
                if ((chatMessage.timeStamp - lastChatMessage.timeStamp) / 1000 > 600) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(DateUtils.getDateToString(chatMessage.timeStamp, "HH:mm"));
                } else {
                    tvTime.setVisibility(View.GONE);
                }
            }
        }
    }

    class WarnViewHolder extends RecyclerView.ViewHolder {


        private final TextView tvWarn;

        public WarnViewHolder(View itemView) {
            super(itemView);
            tvWarn = itemView.findViewById(R.id.tv_warn);
        }

        public void bindData(ChatMessage chatMessage, int position) {
            if (chatMessage.isWarn == 1) {
                tvWarn.setMovementMethod(LinkMovementMethod.getInstance());
                tvWarn.setText("还未登录，无法发送私聊哦，现在");
                tvWarn.append(LightSpanString.getClickSpan(getActivity(), "登录", Color.rgb(255, 43, 63), false, 12, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        ((LiveDisplayActivity) getActivity()).login();
                    }
                }));
            } else {
                tvWarn.setText("VIP、守护、贵族或富豪6及以上玩家才能私聊哦！");
            }
        }
    }


    private void initChatRecycler() {
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == RIGHT) {
                    View item = LayoutInflater.from(getContext()).inflate(R.layout.item_priavet_chat_right, parent, false);
                    return new RightViewHolder(item);
                } else if (viewType == LEFT) {
                    View item = LayoutInflater.from(getContext()).inflate(R.layout.item_priavet_chat_left, parent, false);
                    return new SingleTextViewHolder(item);
                } else {
                    View item = LayoutInflater.from(getContext()).inflate(R.layout.item_priavet_chat_warn, parent, false);
                    return new WarnViewHolder(item);
                }
            }


            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ChatMessage chatMessage = (ChatMessage) chatList.get(position);
                if (holder.getItemViewType() == LEFT) {
                    ((SingleTextViewHolder) holder).bindData(chatMessage, position);
                } else if (holder.getItemViewType() == RIGHT) {
                    ((RightViewHolder) holder).bindData(chatMessage, position);
                } else {
                    ((WarnViewHolder) holder).bindData(chatMessage, position);
                }
                addAnimation(holder, position);
            }

            @Override
            public int getItemCount() {
                return chatList == null ? 0 : chatList.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = (ChatMessage) chatList.get(position);
                if (chatMessage.from_uid == mUserId) {
                    return RIGHT;
                } else {
                    if (chatMessage.isWarn == 1 || chatMessage.isWarn == 2) {
                        return WARN;
                    } else {
                        return LEFT;
                    }
                }
            }
        };
        recycler.addItemDecoration(new SpacesItemDecoration(10));
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
    }

    private int mLastPositionB = -1;

    public void addAnimation(RecyclerView.ViewHolder holder, int position) {
        if (position >= TOTAL_CHAT_MSG - 1 && !isRecyclerScrolling) {
            mLastPositionB = mLastPositionB - 1;
        }
        if (position > mLastPositionB) {
            BaseAnimation animation = new ChatMsgAnimation();
            for (Animator anim : animation.getAnimators(holder.itemView)) {
                startAnim(anim);
            }
            mLastPositionB = position;
        }
    }

    private void startAnim(Animator animator) {
        animator.setDuration(300).start();
        animator.setInterpolator(new LinearInterpolator());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdatePrivateChatEvent updatePubChatEvent) {
        FillHolderMessage message = updatePubChatEvent.getMessage();
        if (mCurrentChatToUser.getPrivateUserId() == ((ChatMessage) message).from_uid || mCurrentChatToUser.getPrivateUserId() == ((ChatMessage) message).to_uid) {
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CLickGuardOrVipEvent event) {
        if (isAdded()) {
            dismiss();
        }
    }


    public void setIsGuard(boolean isGuard) {
        this.isGuard = isGuard;
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    public void setAnchorId(long mAnchorId) {
        this.anchorId = mAnchorId;
    }

    @Override
    public void onDestroy() {
        PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
        PrivateChatUser unique = privateChatUserDao.queryBuilder().where(
                PrivateChatUserDao.Properties.UserId.eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                PrivateChatUserDao.Properties.PrivateUserId.eq(mCurrentChatToUser.getPrivateUserId())).unique();
        if (unique != null) {
            unique.setUncheckTime(0L);
            privateChatUserDao.update(unique);
            EventBus.getDefault().post(new UpdatePrivateChatUIEvent());
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.tv_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_content:
                if (mChatDialog != null && mChatDialog.isAdded()) {
                    return;
                }
                mChatDialog = LiveHouseChatDialog.newInstance(isGuard, isVip, mProgramId, mAnchor, mCurrentChatToUser)
                        .setShowBottom(true)
                        .setDimAmount(0)
                        .show(getFragmentManager());
                break;
        }

    }

    public void chatTo(PrivateChatUser dataBean) {
        mCurrentChatToUser = dataBean;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
    }
}
