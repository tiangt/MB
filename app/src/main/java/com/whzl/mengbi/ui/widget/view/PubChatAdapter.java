package com.whzl.mengbi.ui.widget.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;
import com.whzl.mengbi.chat.room.message.messages.PlaceHolderMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;


public class PubChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<FillHolderMessage> messageList = new LinkedList<>();
    private PubChatView pubchatView;

    public PubChatAdapter(PubChatView pubChatView){
        EventBus.getDefault().register(this);
        this.pubchatView = pubChatView;
        PlaceHolderMsg msg = new PlaceHolderMsg();
        messageList.add(msg);
    }

    public void unRegister(){
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onMainEventBus(UpdatePubChatEvent e){
        addMsg(e.getMessage());
    }

    public void addMsg(FillHolderMessage msg){
        if(messageList.size() > 5000){
            messageList.remove(0);
            notifyItemRemoved(0);
        }
        this.messageList.add(messageList.size()-1,msg);
        notifyItemInserted(messageList.size() - 2);
        pubchatView.invalidate();
        pubchatView.scrollToBottomWhenNot();
    }

    @Override
    public int getItemViewType(int position) {
        FillHolderMessage holderMessage = messageList.get(position);
        int type = holderMessage.getHolderType();
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mvh = null;
        if(viewType == FillHolderMessage.SINGLE_TEXTVIEW){
            View holderView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.live_display_message_item_layout, parent, false);
            mvh = new SingleTextViewHolder(holderView);
        }
        return mvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FillHolderMessage msg = messageList.get(position);
        if(msg==null){
            return;
        }
        msg.fillHolder(holder);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
