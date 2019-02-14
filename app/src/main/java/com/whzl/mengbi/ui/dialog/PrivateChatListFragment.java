package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatEvent;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * @author shaw
 * @date 2018/8/7
 */
public class PrivateChatListFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_bottom_container)
    LinearLayout llBottomContainer;
    @BindView(R.id.tv_chat_to)
    TextView tvChatTo;
    private RecyclerView.Adapter chatAdapter;
    private static final int TOTAL_CHAT_MSG = 100;
    private boolean isRecyclerScrolling;
    private ArrayList<FillHolderMessage> chatList = new ArrayList<>();
    private boolean isGuard;
    private boolean isVip;
    private int mProgramId;
    private PopupWindow popupWindow;
    private ArrayList<RoomUserInfo.DataBean> roomUsers = new ArrayList<>();
    private RoomInfoBean.DataBean.AnchorBean mAnchor;
    private RoomUserInfo.DataBean mCurrentChatToUser;
    private boolean isHidden;
    private BaseAwesomeDialog mChatDialog;


    public static PrivateChatListFragment newInstance(int programId) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        PrivateChatListFragment privateChatListFragment = new PrivateChatListFragment();
        privateChatListFragment.setArguments(args);
        return privateChatListFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_private_chat;
    }

    @Override
    public void init() {
        initChatRecycler();
        EventBus.getDefault().register(this);
        mProgramId = getArguments().getInt("programId");
    }

    private void initChatRecycler() {
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.item_priavet_chat_msg, null);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PrivateChatSelectedEvent event) {
        chatTo(event.getDataBean());
        for (int i = 0; i < roomUsers.size(); i++) {
            if (event.getDataBean().getUserId() == roomUsers.get(i).getUserId()) {
                Collections.swap(roomUsers, i, 1);
                return;
            }
        }
        if (roomUsers.size() == 5) {
            roomUsers.remove(4);
        }
        roomUsers.add(1, event.getDataBean());
    }

    public void setUpWithAnchor(RoomInfoBean.DataBean.AnchorBean anchor) {
        RoomUserInfo.DataBean roomUser = new RoomUserInfo.DataBean();
        roomUser.setUserId(anchor.getId());
        roomUser.setNickname(anchor.getName());
        chatTo(roomUser);
        roomUsers.add(0, roomUser);
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

    @OnClick({R.id.tv_content, R.id.tv_chat_to})
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
            case R.id.tv_chat_to:
                showPopWindow();
                break;
        }

    }

    private void showPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_private_chat_to_list, null);
        RecyclerView recyclerView = popView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BaseListAdapter adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return roomUsers == null ? 0 : roomUsers.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_private_chat_to, parent, false);
                return new NickNameViewHolder(itemView);
            }
        };
        recyclerView.setAdapter(adapter);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(tvChatTo, -UIUtil.dip2px(getContext(), 48), UIUtil.dip2px(getContext(), 6));
    }

    class NickNameViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.view_divider)
        View viewDivider;

        NickNameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            RoomUserInfo.DataBean dataBean = roomUsers.get(position);
            viewDivider.setVisibility(position == roomUsers.size() - 1 ? View.GONE : View.VISIBLE);
            tvNickName.setText(dataBean.getNickname());
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            chatTo(roomUsers.get(position));
            if (position > 1) {
                Collections.swap(roomUsers, 1, position);
            }
            popupWindow.dismiss();
        }
    }

    private void chatTo(RoomUserInfo.DataBean dataBean) {
        mCurrentChatToUser = dataBean;
        tvContent.setText("对" + dataBean.getNickname() + "私聊");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
    }
}
