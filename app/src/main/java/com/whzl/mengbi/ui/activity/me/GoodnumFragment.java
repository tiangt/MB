package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.GoodNumBean;
import com.whzl.mengbi.model.entity.ProgramInfoByAnchorBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.recyclerview.SpaceItemDecoration;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.whzl.mengbi.util.ToastUtils.showToast;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class GoodnumFragment extends BaseFragment {
    @BindView(R.id.rv_4)
    RecyclerView rv4;
    @BindView(R.id.rv_5)
    RecyclerView rv5;
    @BindView(R.id.rv_6)
    RecyclerView rv6;
    @BindView(R.id.rv_7)
    RecyclerView rv7;
    @BindView(R.id.ll_4)
    LinearLayout ll4;
    @BindView(R.id.ll_5)
    LinearLayout ll5;
    @BindView(R.id.ll_6)
    LinearLayout ll6;
    @BindView(R.id.ll_7)
    LinearLayout ll7;
    @BindView(R.id.et_search_num)
    EditText etSearchNum;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.ll_list)
    LinearLayout llList;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_tips)
    LinearLayout llTips;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_search_empty)
    TextView tvSearchEmpty;
    @BindView(R.id.ib_clear)
    ImageButton ibClear;

    private static final int BUY = 1;
    private static final int SEND = 2;
    private static final int PAGESIZE = 6;
    @BindView(R.id.tv_refresh4)
    TextView tvRefresh4;
    @BindView(R.id.tv_refresh5)
    TextView tvRefresh5;
    @BindView(R.id.tv_refresh6)
    TextView tvRefresh6;
    @BindView(R.id.tv_refresh7)
    TextView tvRefresh7;

    private ArrayList<GoodNumBean.DigitsBean> list6 = new ArrayList();
    private ArrayList<GoodNumBean.DigitsBean> list7 = new ArrayList();
    private ArrayList<GoodNumBean.DigitsBean> list5 = new ArrayList();
    private ArrayList<GoodNumBean.DigitsBean> list4 = new ArrayList();
    private BaseListAdapter adapter4, adapter5, adapter6, adapter7;

    private GoodNumBean.DigitsBean digitsBean;
    private boolean idIsOK;
    private String toUserId;
    private BaseAwesomeDialog buyDialog;
    private String salerId;
    private int page4 = 1;
    private int page5 = 1;
    private int page6 = 1;
    private int page7 = 1;
    private String currentKey = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_goodnum_shop;
    }

    @Override
    public void init() {
        initEdit();
        initRecycler();
        getGoodNumData("", PAGESIZE, PAGESIZE, PAGESIZE, PAGESIZE, 1);
    }

    private void initEdit() {
        etSearchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ibClear.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }
        });
        etSearchNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(etSearchNum.getText())) {
                        return true;
                    }
                    if (etSearchNum.getText().toString().length() < 2) {
                        ToastUtils.showToastUnify(getMyActivity(), "至少输入两位数字");
                        return true;
                    }
                    search(etSearchNum.getText().toString());
                    tvSearch.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

    private void initRecycler() {
        rv4.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv4.addItemDecoration(new SpaceItemDecoration(15));
        adapter4 = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list4.size() > PAGESIZE ? PAGESIZE : list4.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_goodnum_shop, parent, false);
                return new AnchorViewHolder(itemView, 4);
            }
        };
        rv4.setAdapter(adapter4);

        rv5.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv5.addItemDecoration(new SpaceItemDecoration(15));
        adapter5 = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list5.size() > PAGESIZE ? PAGESIZE : list5.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_goodnum_shop, parent, false);
                return new AnchorViewHolder(itemView, 5);
            }
        };
        rv5.setAdapter(adapter5);

        rv6.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv6.addItemDecoration(new SpaceItemDecoration(15));
        adapter6 = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list6.size() > PAGESIZE ? PAGESIZE : list6.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_goodnum_shop, parent, false);
                return new AnchorViewHolder(itemView, 6);
            }
        };
        rv6.setAdapter(adapter6);

        rv7.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv7.addItemDecoration(new SpaceItemDecoration(15));
        adapter7 = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list7.size() > PAGESIZE ? PAGESIZE : list7.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_goodnum_shop, parent, false);
                return new AnchorViewHolder(itemView, 7);
            }
        };
        rv7.setAdapter(adapter7);
    }

    @OnClick({R.id.tv_search, R.id.tv_qq, R.id.ib_clear, R.id.tv_refresh4, R.id.tv_refresh5, R.id.tv_refresh6, R.id.tv_refresh7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                llTips.setVisibility(View.GONE);
                tvSearch.setVisibility(View.GONE);
                llEmpty.setVisibility(View.GONE);
                llList.setVisibility(View.VISIBLE);
                search("");
                break;
            case R.id.tv_qq:
                startQQChat(NetConfig.CUSTOM_OFFICIAL_SERVICE_QQ);
                break;
            case R.id.ib_clear:
                etSearchNum.setText("");
                break;
            case R.id.tv_refresh4:
                freshGoodNumData(currentKey, PAGESIZE, 0, 0, 0, page4);
                break;
            case R.id.tv_refresh5:
                freshGoodNumData(currentKey, 0, PAGESIZE, 0, 0, page5);
                break;
            case R.id.tv_refresh6:
                freshGoodNumData(currentKey, 0, 0, PAGESIZE, 0, page6);
                break;
            case R.id.tv_refresh7:
                freshGoodNumData(currentKey, 0, 0, 0, PAGESIZE, page7);
                break;
        }
    }

    private void freshGoodNumData(String keyWord, int fourDigits, int fiveDigits, int sixDigits, int seveDigits, int page) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("keyWord", keyWord);
        paramsMap.put("fourDigits", fourDigits);
        paramsMap.put("fiveDigits", fiveDigits);
        paramsMap.put("sixDigits", sixDigits);
        paramsMap.put("seveDigits", seveDigits);
        paramsMap.put("page", page);
        ApiFactory.getInstance().getApi(Api.class)
                .getPrettysByPage(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GoodNumBean>(GoodnumFragment.this) {

                    @Override
                    public void onSuccess(GoodNumBean bean) {
                        if (fourDigits != 0) {
                            list4.clear();
                            list4.addAll(bean.fourDigits);
                            adapter4.notifyDataSetChanged();
                            if (bean.fourDigits == null || bean.fourDigits.size() < PAGESIZE) {
                                page4 = 1;
                            } else {
                                page4 += 1;
                            }
                        } else if (fiveDigits != 0) {
                            list5.clear();
                            list5.addAll(bean.fiveDigits);
                            adapter5.notifyDataSetChanged();
                            if (bean.fiveDigits == null || bean.fiveDigits.size() < PAGESIZE) {
                                page5 = 1;
                            } else {
                                page5 += 1;
                            }
                        } else if (sixDigits != 0) {
                            list6.clear();
                            list6.addAll(bean.sixDigits);
                            adapter6.notifyDataSetChanged();
                            if (bean.sixDigits == null || bean.sixDigits.size() < PAGESIZE) {
                                page6 = 1;
                            } else {
                                page6 += 1;
                            }
                        } else if (seveDigits != 0) {
                            list7.clear();
                            list7.addAll(bean.seveDigits);
                            adapter7.notifyDataSetChanged();
                            if (bean.seveDigits == null || bean.seveDigits.size() < PAGESIZE) {
                                page7 = 1;
                            } else {
                                page7 += 1;
                            }
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }


    private void search(String keyWord) {
        currentKey = keyWord;
        KeyBoardUtil.closeKeybord(etSearchNum, getMyActivity());
        list4.clear();
        list5.clear();
        list6.clear();
        list7.clear();
        page4 = page5 = page6 = page7 = 1;
        getGoodNumData(keyWord, PAGESIZE, PAGESIZE, PAGESIZE, PAGESIZE, 1);
    }


    class AnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_price_item_goodnum)
        TextView tvPrice;
        @BindView(R.id.tv_num_item_goodnum)
        TextView tvNum;
        @BindView(R.id.tv_buy_item_goodnum)
        TextView tvBuy;
        @BindView(R.id.iv_num_item_goodnum)
        ImageView ivNum;
        private int i = 0;


        public AnchorViewHolder(View itemView, int i) {
            super(itemView);
            this.i = i;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            switch (i) {
                case 4:
                    digitsBean = list4.get(position);
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    break;
                case 5:
                    digitsBean = list5.get(position);
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    break;
                case 6:
                    digitsBean = list6.get(position);
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    break;
                case 7:
                    digitsBean = list7.get(position);
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    break;
            }
            if ("A".equals(digitsBean.goodsColor)) {
                tvNum.setTextColor(Color.parseColor("#f42434"));
                tvNum.setBackgroundResource(R.drawable.bg_pretty_num_a);
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_a, ivNum);
            } else if ("B".equals(digitsBean.goodsColor)) {
                tvNum.setTextColor(Color.parseColor("#ff9601"));
                tvNum.setBackgroundResource(R.drawable.bg_pretty_num_b);
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_b, ivNum);
            } else if ("C".equals(digitsBean.goodsColor)) {
                tvNum.setTextColor(Color.parseColor("#9887f9"));
                tvNum.setBackgroundResource(R.drawable.bg_pretty_num_c);
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_c, ivNum);
            } else if ("D".equals(digitsBean.goodsColor)) {
                tvNum.setTextColor(Color.parseColor("#5ecac2"));
                tvNum.setBackgroundResource(R.drawable.bg_pretty_num_d);
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_d, ivNum);
            } else if ("E".equals(digitsBean.goodsColor)) {
                tvNum.setTextColor(Color.parseColor("#5dbaf6"));
                tvNum.setBackgroundResource(R.drawable.bg_pretty_num_e);
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_e, ivNum);
            } else {
                tvNum.setTextColor(Color.parseColor("#5dbaf6"));
                tvNum.setBackgroundResource(R.drawable.bg_pretty_num_e);
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_e, ivNum);
            }
            tvNum.setText(digitsBean.goodsName);
            tvPrice.setText(getString(R.string.goodnum, digitsBean.rent));
            tvBuy.setOnClickListener(v -> {
                int listKind = 0;
                switch (i) {
                    case 4:
                        digitsBean = list4.get(position);
                        listKind = 4;
                        break;
                    case 5:
                        digitsBean = list5.get(position);
                        listKind = 5;
                        break;
                    case 6:
                        digitsBean = list6.get(position);
                        listKind = 6;
                        break;
                    case 7:
                        digitsBean = list7.get(position);
                        listKind = 7;
                        break;
                }
                showDialog(BUY);
//                Intent intent = new Intent(getMyActivity(), BuyGoodnumActivity.class);
//                intent.putExtra("type", "buy");
//                intent.putExtra("bean", digitsBean);
//                intent.putExtra("listKind", listKind);
//                startActivityForResult(intent, 100);

            });
        }
    }

    private void showDialog(int type) {
        idIsOK = false;
        toUserId = "";
        salerId = "";
        UserInfo.DataBean currentUser = ((ShopActivity) getMyActivity()).currentUser;
        buyDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_preety_shop).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                if (currentUser != null && currentUser.getAvatar() != null && !TextUtils.isEmpty(currentUser.getAvatar())) {
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), currentUser.getAvatar()
                            , holder.getView(R.id.iv_avatar));
                }
                holder.setText(R.id.tv_nick, currentUser.getNickname());
                holder.setText(R.id.tv_pretty_num, digitsBean.goodsName);
                holder.setText(R.id.tv_pretty_price, AmountConversionUitls.amountConversionFormat(digitsBean.rent));
                holder.setVisible(R.id.ll_buy, type == BUY);
                holder.setVisible(R.id.ll_send, type == SEND);
                TextView tvSend = holder.getView(R.id.tv_type);
                tvSend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                holder.setText(R.id.tv_type, type == BUY ? "赠送他人" : "自己购买");
                holder.setOnClickListener(R.id.tv_type, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showDialog(type == BUY ? SEND : BUY);
                    }
                });
                holder.setOnClickListener(R.id.ib_clear, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.setText(R.id.et_send, "");
                    }
                });
                EditText editText = (EditText) holder.getView(R.id.et_send);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        holder.setVisible(R.id.ib_clear, TextUtils.isEmpty(s) ? false : true);
                    }
                });
                //搜索
                holder.setOnClickListener(R.id.btn_send, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(editText.getText())) {
                            return;
                        }
                        BusinessUtils.getUserInfo(getMyActivity(), editText.getText().toString().trim(),
                                new BusinessUtils.UserInfoListener() {
                                    @Override
                                    public void onSuccess(UserInfo.DataBean bean) {
                                        holder.setVisible(R.id.ll_buy, true);
                                        holder.setVisible(R.id.ll_send, false);
                                        GlideImageLoader.getInstace().displayImage(getMyActivity(), bean.getAvatar()
                                                , holder.getView(R.id.iv_avatar));
                                        holder.setText(R.id.tv_nick, bean.getNickname());
                                        toUserId = String.valueOf(bean.getUserId());
                                        idIsOK = true;
                                    }

                                    @Override
                                    public void onError(int code) {
                                        ToastUtils.showToastUnify(getMyActivity(), "该萌号用户不存在");
                                        idIsOK = false;
                                    }
                                });
                    }
                });
                //购买
                holder.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == BUY) {
                            buy();
                        } else {
                            if (idIsOK) {
                                buy();
                            }
                        }
                    }
                });

                initAnchorEdit(holder);
            }
        }).setDimAmount(0).setShowBottom(true).show(getFragmentManager());
    }

    private void initAnchorEdit(ViewHolder holder) {
        EditText etAnchor = holder.getView(R.id.et_anchor);
        etAnchor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.setVisible(R.id.ib_anchor, TextUtils.isEmpty(s) ? false : true);
            }
        });
        holder.setOnClickListener(R.id.ib_anchor, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAnchor.setText("");
            }
        });
        Button btnAnchor = holder.getView(R.id.btn_anchor);
        holder.setOnClickListener(R.id.btn_anchor, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAnchor.getText())) {
                    return;
                }
                if (btnAnchor.getText().equals("取消")) {
                    salerId = "";
                    holder.setVisible(R.id.rl_edit_anchor, true);
                    holder.setVisible(R.id.tv_anchor, false);
                    holder.setText(R.id.et_anchor, "");
                    btnAnchor.setBackgroundResource(R.drawable.shape_purple_btn);
                    btnAnchor.setText("确定");
                    return;
                }
                BusinessUtils.getAnchorInfo(getMyActivity(), etAnchor.getText().toString().trim(), new BusinessUtils.AnchorInfoListener() {
                    @Override
                    public void onSuccess(ProgramInfoByAnchorBean bean) {
                        if (bean.list == null || bean.list.size() == 0) {
                            salerId = "";
                            ToastUtils.showToastUnify(getMyActivity(), "主播萌号错误，请重新输入");
                            return;
                        }
                        KeyBoardUtil.closeKeybord(etAnchor, getMyActivity());
                        ProgramInfoByAnchorBean.ListBean listBean = bean.list.get(0);
                        holder.setVisible(R.id.rl_edit_anchor, false);
                        holder.setVisible(R.id.tv_anchor, true);
                        holder.setText(R.id.tv_anchor, listBean.anchorNickname);

                        btnAnchor.setBackgroundResource(R.drawable.shape_gray_btn);
                        btnAnchor.setText("取消");
                        salerId = String.valueOf(listBean.anchorId);
                    }

                    @Override
                    public void onError(int code) {
                    }
                });
            }
        });
    }

    private void buy() {
        BusinessUtils.mallBuy(getMyActivity(), String.valueOf(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L))
                , digitsBean.goodsId + "", digitsBean.priceId + "", "1", toUserId, salerId
                , new BusinessUtils.MallBuyListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showToastUnify(getMyActivity(), "购买成功");
                        search("");
                        if (buyDialog != null) {
                            buyDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 200) {
                boolean state = data.getBooleanExtra("state", false);
                if (state) {
                    if (list4.contains(digitsBean)) {
                        list4.remove(digitsBean);
                        adapter4.notifyDataSetChanged();
                        if (list4.size() == 0) {
                            ll4.setVisibility(View.GONE);
                        }
                    }
                    if (list5.contains(digitsBean)) {
                        list5.remove(digitsBean);
                        adapter5.notifyDataSetChanged();
                        if (list5.size() == 0) {
                            ll5.setVisibility(View.GONE);
                        }
                    }
                    if (list6.contains(digitsBean)) {
                        list6.remove(digitsBean);
                        adapter6.notifyDataSetChanged();
                        if (list6.size() == 0) {
                            ll6.setVisibility(View.GONE);
                        }
                    }
                    if (list7.contains(digitsBean)) {
                        list7.remove(digitsBean);
                        adapter7.notifyDataSetChanged();
                        if (list7.size() == 0) {
                            ll7.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    private void getGoodNumData(String keyWord, int fourDigits, int fiveDigits, int sixDigits, int seveDigits, int page) {
        llTips.setVisibility(TextUtils.isEmpty(keyWord) ? View.GONE : View.VISIBLE);
        HashMap paramsMap = new HashMap();
        paramsMap.put("keyWord", keyWord);
        paramsMap.put("fourDigits", fourDigits);
        paramsMap.put("fiveDigits", fiveDigits);
        paramsMap.put("sixDigits", sixDigits);
        paramsMap.put("seveDigits", seveDigits);
        paramsMap.put("page", page);
        ApiFactory.getInstance().getApi(Api.class)
                .getPrettys(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GoodNumBean>(GoodnumFragment.this) {

                    @Override
                    public void onSuccess(GoodNumBean bean) {
                        if (bean.fourDigits != null && bean.fourDigits.size() > 0) {
                            if (bean.fourDigits.size() >= PAGESIZE) {
                                page4 += 1;
                            }
                            ll4.setVisibility(View.VISIBLE);
                            list4.addAll(bean.fourDigits);
                        }
                        adapter4.notifyDataSetChanged();

                        if (bean.fiveDigits != null && bean.fiveDigits.size() > 0) {
                            if (bean.fiveDigits.size() == PAGESIZE) {
                                page5 += 1;
                            }
                            ll5.setVisibility(View.VISIBLE);
                            list5.addAll(bean.fiveDigits);
                        }
                        adapter5.notifyDataSetChanged();

                        if (bean.sixDigits != null && bean.sixDigits.size() > 0) {
                            if (bean.sixDigits.size() == PAGESIZE) {
                                page6 += 1;
                            }
                            ll6.setVisibility(View.VISIBLE);
                            list6.addAll(bean.sixDigits);

                        }
                        adapter6.notifyDataSetChanged();

                        if (bean.seveDigits != null && bean.seveDigits.size() > 0) {
                            if (bean.seveDigits.size() == PAGESIZE) {
                                page7 += 1;
                            }
                            ll7.setVisibility(View.VISIBLE);
                            list7.addAll(bean.seveDigits);

                        }
                        adapter7.notifyDataSetChanged();
                        if (list4.size() == 0) {
                            ll4.setVisibility(View.GONE);
                        }
                        if (list5.size() == 0) {
                            ll5.setVisibility(View.GONE);
                        }
                        if (list6.size() == 0) {
                            ll6.setVisibility(View.GONE);
                        }
                        if (list7.size() == 0) {
                            ll7.setVisibility(View.GONE);
                        }
                        if (list4.size() == 0 && list5.size() == 0 && list6.size() == 0 && list7.size() == 0) {
                            llList.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                            tvSearchEmpty.setText(currentKey);
                            tvSearch.setText("取消");
                            llTips.setVisibility(View.GONE);
                        } else {
                            tvTips.setText(keyWord);
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void startQQChat(String qq) {
        if (!isQQClientAvailable()) {
            showToast("QQ未安装");
            return;
        }
        final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
    }

    public boolean isQQClientAvailable() {
        final PackageManager packageManager = getMyActivity().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}
