package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.FileUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * VIP表情
 *
 * @author cliang
 * @date 2018.10.19
 */
public class VipEmojiFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_not_enable_container)
    LinearLayout llNotEnableContainer;
    private List<EmjoyInfo.FaceBean.PublicBean> publicBeans;
    private BaseListAdapter chatEmojiAdapter;

    private EditText messageEditText;

    public static VipEmojiFragment newInstance(boolean isVip) {
        VipEmojiFragment vipEmojiFragment = new VipEmojiFragment();
        Bundle args = new Bundle();
        args.putBoolean("isVip", isVip);
        vipEmojiFragment.setArguments(args);
        return vipEmojiFragment;
    }

    public void setMessageEditText(EditText messageEditText) {
        this.messageEditText = messageEditText;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        FaceReplace.getInstance().init(getContext());
        EmjoyInfo emjoyInfo = FaceReplace.getInstance().getVipEmojiInfo();
        publicBeans = emjoyInfo.getFace().getPublicX();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vip_emoji;
    }

    @Override
    public void init() {
        boolean isVip = getArguments().getBoolean("isVip");
        llNotEnableContainer.setVisibility(isVip ? View.GONE : View.VISIBLE);
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 5));
        chatEmojiAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return publicBeans == null ? 0 : publicBeans.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_vip_emoji, parent, false);
                return new EmojiViewHolder(itemView);
            }
        };
        recycler.setAdapter(chatEmojiAdapter);
    }

    class EmojiViewHolder extends BaseViewHolder{
        @BindView(R.id.iv_emoji)
        ImageView ivEmoji;

        public EmojiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            EmjoyInfo.FaceBean.PublicBean publicBean = publicBeans.get(position);
            Bitmap emojiBitmap = FileUtils.readBitmapFromAssetsFile(publicBean.getIcon(), getContext());
            ivEmoji.setImageBitmap(emojiBitmap);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            String emojiDesc = publicBeans.get(position).getValue();
            messageEditText.append(emojiDesc);
        }
    }
}
