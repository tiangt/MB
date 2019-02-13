package com.whzl.mengbi.ui.dialog;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.gen.PrivateChatUserDao;
import com.whzl.mengbi.greendao.PrivateChatUser;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.recyclerview.SlideRecyclerView;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2019/2/12
 */
public class PrivateChatListDialog extends BaseAwesomeDialog {
    @BindView(R.id.srv_private)
    SlideRecyclerView srvPrivate;
    @BindView(R.id.view_close)
    View view;

    private ArrayList<PrivateChatUser> roomUsers = new ArrayList<>();
    private BaseListAdapter baseListAdapter;
    private boolean isGuard;
    private int mProgramId;
    private BaseAwesomeDialog awesomeDialog;
    private PrivateChatUserDao privateChatUserDao;

    public static BaseAwesomeDialog newInstance() {
        PrivateChatListDialog privateChatListDialog = new PrivateChatListDialog();
        return privateChatListDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_private_chat_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initRecycler();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded()) {
                    dismiss();
                }
            }
        });
    }

    private void initRecycler() {
        srvPrivate.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_inset));
        srvPrivate.addItemDecoration(itemDecoration);
        baseListAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return roomUsers.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_private_chat_list, parent, false);
                return new PrivateChatListHolder(itemView);
            }
        };
        srvPrivate.setAdapter(baseListAdapter);
    }

    public void setUpWithAnchor(RoomInfoBean.DataBean.AnchorBean anchor) {
        PrivateChatUser roomUser = new PrivateChatUser();
        roomUser.setPrivateUserId((long) anchor.getId());
        roomUser.setName(anchor.getName());
        roomUser.setAvatar(anchor.getAvatar());
        roomUser.setTimestamp(System.currentTimeMillis());
        roomUser.setUserId(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()));

        PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
        List<PrivateChatUser> privateChatUsers = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.
                eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())))
                .orderDesc(PrivateChatUserDao.Properties.Timestamp).list();
        if (checkContain(privateChatUsers, roomUser)) {
            PrivateChatUser user = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.
                            eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                    PrivateChatUserDao.Properties.PrivateUserId.eq(roomUser.getPrivateUserId())).unique();
            user.setTimestamp(System.currentTimeMillis());
            privateChatUserDao.update(user);
            roomUsers.clear();
            List<PrivateChatUser> privateChatUsers2 = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.
                    eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()))).
                    orderDesc(PrivateChatUserDao.Properties.Timestamp).list();
            roomUsers.addAll(privateChatUsers2);
        } else {
            roomUsers.addAll(privateChatUsers);
            roomUsers.add(0, roomUser);
        }
    }

    public void update() {
        PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
        List<PrivateChatUser> privateChatUsers = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.
                eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())))
                .orderDesc(PrivateChatUserDao.Properties.Timestamp).list();
        roomUsers.clear();
        roomUsers.addAll(privateChatUsers);
        if (roomUsers.size() >= 2) {
            PrivateChatUser chatUser = roomUsers.get(1);
            roomUsers.remove(1);
            roomUsers.add(0, chatUser);
            baseListAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkContain(List<PrivateChatUser> privateChatUsers, PrivateChatUser roomUser) {
        for (int i = 0; i < privateChatUsers.size(); i++) {
            if (privateChatUsers.get(i).getPrivateUserId().equals(roomUser.getPrivateUserId())) {
                return true;
            }
        }
        return false;
    }


    public void setIsGuard(boolean isGuard) {
        this.isGuard = isGuard;
    }

    public void setProgramId(int mProgramId) {
        this.mProgramId = mProgramId;
    }


    class PrivateChatListHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_nick)
        TextView tvNick;
        @BindView(R.id.tv_delete)
        TextView tvDelete;

        public PrivateChatListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (position == 0) {
                itemView.setBackgroundColor(Color.parseColor("#FFF7F1DA"));
            } else {
                itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            PrivateChatUser dataBean = roomUsers.get(position);
            RequestOptions requestOptions = new RequestOptions().transform(new CircleCrop());
            Glide.with(getActivity()).load(dataBean.getAvatar()).apply(requestOptions).into(ivAvatar);
            tvNick.setText(dataBean.getName());
//            if (position == 0) {
//                tvDelete.setVisibility(View.GONE);
//            }
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToast("delete");
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (awesomeDialog != null && awesomeDialog.isAdded()) {
                return;
            }
            awesomeDialog = PrivateChatDialog.newInstance(mProgramId)
                    .setShowBottom(true)
                    .setDimAmount(0)
                    .show(getFragmentManager());
            PrivateChatUser chatUser = roomUsers.get(position);
            RoomUserInfo.DataBean dataBean = new RoomUserInfo.DataBean();
            dataBean.setAvatar(chatUser.getAvatar());
            dataBean.setNickname(chatUser.getName());
            dataBean.setUserId(chatUser.getPrivateUserId());
            ((PrivateChatDialog) awesomeDialog).chatTo(dataBean);
            ((PrivateChatDialog) awesomeDialog).setIsGuard(isGuard);
        }
    }

}
