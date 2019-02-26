package com.whzl.mengbi.ui.dialog;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.gen.PrivateChatContentDao;
import com.whzl.mengbi.gen.PrivateChatUserDao;
import com.whzl.mengbi.greendao.PrivateChatContent;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.adapter.BaseAnimation;
import com.whzl.mengbi.ui.adapter.ChatMsgAnimation;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    FrameLayout ibClose;

    private RecyclerView.Adapter chatAdapter;
    private static final int TOTAL_CHAT_MSG = 100;
    private boolean isRecyclerScrolling;
    private ArrayList<FillHolderMessage> chatList = new ArrayList<>();
    private boolean isGuard;
    private boolean isVip;
    private int mProgramId;
    private ArrayList<RoomUserInfo.DataBean> roomUsers = new ArrayList<>();
    private RoomInfoBean.DataBean.AnchorBean mAnchor;
    private RoomUserInfo.DataBean mCurrentChatToUser;
    private boolean isHidden;
    private BaseAwesomeDialog mChatDialog;
    private long mUserId;
    private long current = System.currentTimeMillis();

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
        tvTitle.setText(mCurrentChatToUser.getNickname());
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mProgramId = getArguments().getInt("programId");
        mUserId = (long) SPUtils.get(getActivity(), SpConfig.KEY_USER_ID, 0L);
        initChatRecycler();
        viewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded()) {
                    dismiss();
                }
            }
        });
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded()) {
                    dismiss();
                }
            }
        });
        initData();
    }

    private void initData() {
        PrivateChatContentDao privateChatContentDao = BaseApplication.getInstance().getDaoSession().getPrivateChatContentDao();
        List<PrivateChatContent> list = privateChatContentDao.queryBuilder().where(
                PrivateChatContentDao.Properties.UserId.eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                PrivateChatContentDao.Properties.PrivateUserId.eq(mCurrentChatToUser.getUserId())).list();
        for (int i = 0; i < list.size(); i++) {
            PrivateChatContent chatContent = list.get(i);
            ChatCommonJson chatCommonJson = new ChatCommonJson();
            chatCommonJson.setContent(chatContent.getContent());
            chatCommonJson.setFrom_uid(chatContent.getFromId().toString());
            ChatMessage chatMessage = new ChatMessage(chatCommonJson, getActivity(), null, true);
            chatList.add(chatMessage);
        }
        chatAdapter.notifyDataSetChanged();
        if (chatList != null && !chatList.isEmpty()) {
            recycler.smoothScrollToPosition(chatList.size() - 1);
        }
    }

    class SingleTextViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivAvatar;
        private final TextView tvContent;

        public SingleTextViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
        }

        public void bindData(ChatMessage chatMessage) {
            Glide.with(BaseApplication.getInstance()).load(ImageUrl.getAvatarUrl(Long.parseLong(chatMessage.chatJson.getFrom_uid()), "jpg", current))
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
                    View item = LayoutInflater.from(getContext()).inflate(R.layout.item_priavet_chat_right, null);
                    return new SingleTextViewHolder(item);
                } else {
                    View item = LayoutInflater.from(getContext()).inflate(R.layout.item_priavet_chat_left, null);
                    return new SingleTextViewHolder(item);
                }
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ChatMessage chatMessage = (ChatMessage) chatList.get(position);
                ((SingleTextViewHolder) holder).bindData(chatMessage);
                addAnimation(holder, position);
            }

            @Override
            public int getItemCount() {
                return chatList == null ? 0 : chatList.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = (ChatMessage) chatList.get(position);
                return chatMessage.from_uid == mUserId ? RIGHT : LEFT;
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
//        if (!isAdded()) {
//            ((LiveDisplayActivity) getActivity()).showMessageNotify();
//        }
    }

    public void setIsGuard(boolean isGuard) {
        this.isGuard = isGuard;
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    public void chatTo(RoomUserInfo.DataBean dataBean) {
        mCurrentChatToUser = dataBean;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
    }
}
