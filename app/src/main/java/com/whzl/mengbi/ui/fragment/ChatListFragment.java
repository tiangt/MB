package com.whzl.mengbi.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.message.messages.SystemMessage;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.gift.RoyalEnterControl;
import com.whzl.mengbi.ui.activity.CommWebActivity;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.ui.viewholder.WelcomeTextViewHolder;
import com.whzl.mengbi.ui.widget.view.RollTextView;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * @author shaw
 * @date 2018/8/7
 */
public class ChatListFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
//    @BindView(R.id.ll_enter)
    LinearLayout llEnter;
//    @BindView(R.id.tv_enter)
    RollTextView tvEnter;
    ImageView ivEnter;

    public void setIvEnter(ImageView ivEnter) {
        this.ivEnter = ivEnter;
    }

    public void setLlEnter(LinearLayout llEnter) {
        this.llEnter = llEnter;
    }

    public void setTvEnter(RollTextView tvEnter) {
        this.tvEnter = tvEnter;
    }

    private RecyclerView.Adapter chatAdapter;
    private static final int TOTAL_CHAT_MSG = 100;
    private boolean isRecyclerScrolling;
    private ArrayList<FillHolderMessage> chatList = new ArrayList<>();
    private RoyalEnterControl royalEnterControl;


    public static ChatListFragment newInstance() {
        return new ChatListFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.comm_recycler;
    }

    @Override
    public void init() {
        initChatRecycler();
        EventBus.getDefault().register(this);
    }

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

        if (message instanceof WelcomeMsg) {
            WelcomeJson welcomeJson = ((WelcomeMsg) message).getmWelcomeJson();
            int royalLevel = ((WelcomeMsg) message).getRoyalLevel(welcomeJson.getContext().getInfo().getLevelList());
            if (royalLevel > 0) {
                if (royalEnterControl == null) {
                    royalEnterControl = new RoyalEnterControl();
                    royalEnterControl.setLlEnter(llEnter);
                    royalEnterControl.setTvEnter(tvEnter);
                    royalEnterControl.setIvEnter(ivEnter);
                    royalEnterControl.setContext(getMyActivity());
                }
//            String imageUrl = ImageUrl.getImageUrl(((WelcomeMsg) message).getCarId(), "jpg");
//            GlideImageLoader.getInstace().displayImage(getContext(), imageUrl, ivEnter);
//            royalEnterControl.showEnter(welcomeJson.getContext().getInfo().getNickname());
                royalEnterControl.showEnter((WelcomeMsg) message);
            }
        }
    }




    private void initChatRecycler() {
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setHasFixedSize(true);
        chatAdapter = new RecyclerView.Adapter() {
            @Override
            public int getItemViewType(int position) {
                FillHolderMessage fillHolderMessage = chatList.get(position);
                if (fillHolderMessage instanceof WelcomeMsg) {
                    if (((WelcomeMsg) fillHolderMessage).isHasGuard()) {
                        return 1;
                    } else {
                        return 2;
                    }
                }
                return 3;
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 1) {
                    View item = LayoutInflater.from(getContext()).inflate(R.layout.welcome_guard_item_chat_msg, parent, false);
                    return new WelcomeTextViewHolder(item);
                } else if (viewType == 2) {
                    View item = LayoutInflater.from(getContext()).inflate(R.layout.welcome_item_chat_msg, parent, false);
                    return new WelcomeTextViewHolder(item);
                }
                View item = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_msg, parent, false);
                return new SingleTextViewHolder(item);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                FillHolderMessage message = chatList.get(position);
                message.fillHolder(holder);
                holder.itemView.setOnClickListener(null);
                if (getItemViewType(position) == 1 || getItemViewType(position) == 2) {
                    WelcomeMsg welcomeMsg = (WelcomeMsg) message;
//                    RelativeLayout rlCarContainer = holder.itemView.findViewById(R.id.rl_car_container);
                    LinearLayout llCar = holder.itemView.findViewById(R.id.ll_car);
                    if (welcomeMsg.hasBagCar()) {
                        llCar.setVisibility(View.VISIBLE);
                        ImageView ivCar = holder.itemView.findViewById(R.id.iv_car);
                        TextView tvCarName = holder.itemView.findViewById(R.id.tv_car_name);
//                        TextView tvPrettyDesc = holder.itemView.findViewById(R.id.tv_pretty_num_desc);
//                        TextView tvPrettyNum = holder.itemView.findViewById(R.id.tv_pretty_num);
                        tvCarName.setText(welcomeMsg.getCarName());
//                        tvPrettyDesc.setText(welcomeMsg.getPrettyNum() == 0 ? "普号" : "靓号");
//                        tvPrettyNum.setText(welcomeMsg.getPrettyNum() == 0 ? welcomeMsg.getUid() + "" : welcomeMsg.getPrettyNum() + "");
                        String imageUrl = ImageUrl.getImageUrl(welcomeMsg.getCarId(), "jpg");
                        GlideImageLoader.getInstace().displayImage(getContext(), imageUrl, ivCar);
//                        String goodsColor = welcomeMsg.getGoodsColor();
//                        if ("A".equals(goodsColor)) {
//                            tvPrettyDesc.setBackgroundResource(R.drawable.shape_chat_msg_pretty_a_text_bg);
//                            tvPrettyNum.setBackgroundResource(R.drawable.shape_chat_msg_pretty_a_text_bg);
//                        } else if ("B".equals(goodsColor)) {
//                            tvPrettyDesc.setBackgroundResource(R.drawable.shape_chat_msg_pretty_b_text_bg);
//                            tvPrettyNum.setBackgroundResource(R.drawable.shape_chat_msg_pretty_b_text_bg);
//                        } else if ("C".equals(goodsColor)) {
//                            tvPrettyDesc.setBackgroundResource(R.drawable.shape_chat_msg_pretty_num_c_text_bg);
//                            tvPrettyNum.setBackgroundResource(R.drawable.shape_chat_msg_pretty_num_c_text_bg);
//                        } else if ("D".equals(goodsColor)) {
//                            tvPrettyDesc.setBackgroundResource(R.drawable.shape_chat_msg_pretty_num_d_text_bg);
//                            tvPrettyNum.setBackgroundResource(R.drawable.shape_chat_msg_pretty_num_d_text_bg);
//                        } else {
//                            tvPrettyDesc.setBackgroundResource(R.drawable.shape_chat_msg_pretty_num_default);
//                            tvPrettyNum.setBackgroundResource(R.drawable.shape_chat_msg_pretty_num_default);
//                        }
                    } else {
                        llCar.setVisibility(View.GONE);
                    }

                } else {
                    ImageView ivAnnounce = holder.itemView.findViewById(R.id.iv_announce);
                    if (message instanceof SystemMessage) {
                        SystemMessage systemMessage = (SystemMessage) message;
                        if (TextUtils.isEmpty(systemMessage.getPic())) {
                            ivAnnounce.setVisibility(View.GONE);
                        } else {
                            ivAnnounce.setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(systemMessage.getPic())
                                    .into(ivAnnounce);
                        }
                        if (!TextUtils.isEmpty(((SystemMessage) message).getLink())) {
                            holder.itemView.setOnClickListener(v -> {
                                Intent intent = new Intent(getContext(), CommWebActivity.class);
                                intent.putExtra("title", "公告");
                                intent.putExtra("url", systemMessage.getLink());
                                startActivity(intent);
                            });
                        }
                    } else {
                        ivAnnounce.setVisibility(View.GONE);
                    }
                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
