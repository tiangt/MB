package com.whzl.mengbi.ui.dialog.fragment;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.adapter.ChatEmojiAdapter;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author shaw
 * @date 2018/7/7
 */
public class EmojiFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    Unbinder unbinder1;
    private List<EmjoyInfo.FaceBean.PublicBean> publicBeans;
    private ChatEmojiAdapter chatEmojiAdapter;
    private int index;

    public void setMessageEditText(EditText messageEditText) {
        this.messageEditText = messageEditText;
    }

    private EditText messageEditText;

    public static EmojiFragment newInstance(int index) {
        EmojiFragment emojiFragment = new EmojiFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        emojiFragment.setArguments(args);
        return emojiFragment;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        index = getArguments().getInt("index");
        //String strJson = FileUtils.getJson("images/face/face.json", BaseApplication.getInstance());
        EmjoyInfo emjoyInfo = FaceReplace.getInstance().getEmjoyInfo();
        publicBeans = null;
        if (index < 2) {
            publicBeans = emjoyInfo.getFace().getPublicX().subList(index * 27, (index + 1) * 27);
        } else {
            publicBeans = emjoyInfo.getFace().getPublicX().subList(index * 27, emjoyInfo.getFace().getPublicX().size());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_emoji;
    }

    @Override
    public void init() {
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 7));
        chatEmojiAdapter = new ChatEmojiAdapter(getContext(), R.layout.item_emoji, publicBeans);
        chatEmojiAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String emojiPath = publicBeans.get(position).getIcon();
                String emojiDesc = publicBeans.get(position).getValue();
                Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(emojiPath, getContext());
                BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawable.setBounds(0, 0, UIUtil.dip2px(getContext(), 20), UIUtil.dip2px(getContext(), 20));
                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                SpannableString spannableString = new SpannableString(emojiDesc);
                spannableString.setSpan(imageSpan, 0, emojiDesc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                messageEditText.append(spannableString);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler.setAdapter(chatEmojiAdapter);
    }


    @OnClick(R.id.btn_delete)
    public void onClick() {
        // 模拟软键盘删除
        int keyCode = KeyEvent.KEYCODE_DEL;
        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        messageEditText.onKeyDown(keyCode, keyEventDown);
        messageEditText.onKeyUp(keyCode, keyEventUp);
    }
}
