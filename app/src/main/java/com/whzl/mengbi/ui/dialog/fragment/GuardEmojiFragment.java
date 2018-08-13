package com.whzl.mengbi.ui.dialog.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/7
 */
public class GuardEmojiFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_not_enable_container)
    LinearLayout llNotEnableContainer;
    private List<EmjoyInfo.FaceBean.PublicBean> publicBeans;
    private BaseListAdapter chatEmojiAdapter;

    private EditText messageEditText;

    public static GuardEmojiFragment newInstance(boolean isGuard) {
        GuardEmojiFragment guardEmojiFragment = new GuardEmojiFragment();
        Bundle args = new Bundle();
        args.putBoolean("isGuard", isGuard);
        guardEmojiFragment.setArguments(args);
        return guardEmojiFragment;
    }


    public void setMessageEditText(EditText messageEditText) {
        this.messageEditText = messageEditText;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        FaceReplace.getInstance().init(getContext());
        EmjoyInfo emjoyInfo = FaceReplace.getInstance().getGuardEmjoyInfo();
        publicBeans = emjoyInfo.getFace().getPublicX();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guard_emoji;
    }

    @Override
    public void init() {
        boolean isGuard = getArguments().getBoolean("isGuard");
        llNotEnableContainer.setVisibility(isGuard ? View.GONE : View.VISIBLE);
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 5));
        chatEmojiAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return publicBeans == null ? 0 : publicBeans.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_guard_emoji, parent, false);
                return new EmojiViewHolder(itemView);
            }
        };
        recycler.setAdapter(chatEmojiAdapter);
    }

    class EmojiViewHolder extends BaseViewHolder {
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

    @OnClick({R.id.btn_delete, R.id.btn_guard, R.id.ll_not_enable_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                // 模拟软键盘删除
                int keyCode = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                messageEditText.onKeyDown(keyCode, keyEventDown);
                messageEditText.onKeyUp(keyCode, keyEventUp);
                break;
            case R.id.btn_guard:
                if(listener != null){
                    listener.onGuardClick();
                }
                break;
        }
    }

    public void setListener(OnGuardClickListener listener) {
        this.listener = listener;
    }

    private OnGuardClickListener listener;

    public interface OnGuardClickListener{
        void onGuardClick();
    }
}
