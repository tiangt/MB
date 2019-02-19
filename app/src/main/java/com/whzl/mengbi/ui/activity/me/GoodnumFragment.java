package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.model.entity.GoodNumBean;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.recyclerview.SpaceItemDecoration;
import com.whzl.mengbi.ui.widget.view.PrettyNumText;
import com.whzl.mengbi.util.KeyBoardUtil;
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
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.whzl.mengbi.util.ToastUtils.showToast;

/**
 * @author niko
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
    Unbinder unbinder;
    @BindView(R.id.ll_list)
    LinearLayout llList;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.ib_clear)
    ImageButton ibClear;

    private ArrayList<GoodNumBean.DigitsBean> list6 = new ArrayList();
    private ArrayList<GoodNumBean.DigitsBean> list7 = new ArrayList();
    private ArrayList<GoodNumBean.DigitsBean> list5 = new ArrayList();
    private ArrayList<GoodNumBean.DigitsBean> list4 = new ArrayList();
    private BaseListAdapter adapter4;
    private BaseListAdapter adapter5;
    private BaseListAdapter adapter6;
    private BaseListAdapter adapter7;

    private GoodNumBean.DigitsBean digitsBean;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_goodnum_shop;
    }

    @Override
    public void init() {
        initEdit();
        initRecycler();
        getGoodNumData("");
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
                return list4 == null ? 0 : list4.size();
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
                return list5 == null ? 0 : list5.size();
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
                return list6 == null ? 0 : list6.size();
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
                return list7 == null ? 0 : list7.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_goodnum_shop, parent, false);
                return new AnchorViewHolder(itemView, 7);
            }
        };
        rv7.setAdapter(adapter7);
    }

    @OnClick({R.id.tv_search, R.id.tv_qq, R.id.ib_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                tvTips.setVisibility(View.GONE);
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
        }
    }

    private void search(String keyWord) {
        KeyBoardUtil.closeKeybord(etSearchNum, getMyActivity());
        list4.clear();
        list5.clear();
        list6.clear();
        list7.clear();
        getGoodNumData(keyWord);
    }


    class AnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_price_item_goodnum)
        TextView tvPrice;
        @BindView(R.id.tv_num_item_goodnum)
        TextView tvNum;
        @BindView(R.id.tv_buy_item_goodnum)
        TextView tvBuy;
        @BindView(R.id.tv_send_item_goodnum)
        TextView tvSend;
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
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    break;
                case 5:
                    digitsBean = list5.get(position);
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    break;
                case 6:
                    digitsBean = list6.get(position);
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                    break;
                case 7:
                    digitsBean = list7.get(position);
                    tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                    break;
            }
            switch (digitsBean.goodsColor) {
                case "A":
                    tvNum.setTextColor(Color.parseColor("#f42434"));
                    tvNum.setBackgroundResource(R.drawable.bg_pretty_num_a);
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_a, ivNum);
                    break;
                case "B":
                    tvNum.setTextColor(Color.parseColor("#ff9601"));
                    tvNum.setBackgroundResource(R.drawable.bg_pretty_num_b);
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_b, ivNum);
                    break;
                case "C":
                    tvNum.setTextColor(Color.parseColor("#9887f9"));
                    tvNum.setBackgroundResource(R.drawable.bg_pretty_num_c);
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_c, ivNum);
                    break;
                case "D":
                    tvNum.setTextColor(Color.parseColor("#5ecac2"));
                    tvNum.setBackgroundResource(R.drawable.bg_pretty_num_d);
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_d, ivNum);
                    break;
                case "E":
                    tvNum.setTextColor(Color.parseColor("#5dbaf6"));
                    tvNum.setBackgroundResource(R.drawable.bg_pretty_num_e);
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_e, ivNum);
                    break;
                default:
                    tvNum.setTextColor(Color.parseColor("#5dbaf6"));
                    tvNum.setBackgroundResource(R.drawable.bg_pretty_num_e);
                    GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_e, ivNum);
                    break;
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
                Intent intent = new Intent(getMyActivity(), BuyGoodnumActivity.class);
                intent.putExtra("type", "buy");
                intent.putExtra("bean", digitsBean);
                intent.putExtra("listKind", listKind);
                startActivityForResult(intent, 100);
            });
            tvSend.setOnClickListener(v -> {
                switch (i) {
                    case 4:
                        digitsBean = list4.get(position);
                        break;
                    case 5:
                        digitsBean = list5.get(position);
                        break;
                    case 6:
                        digitsBean = list6.get(position);
                        break;
                    case 7:
                        digitsBean = list7.get(position);
                        break;
                }
                startActivityForResult(new Intent(getMyActivity(), BuyGoodnumActivity.class)
                        .putExtra("type", "send")
                        .putExtra("bean", digitsBean), 100);
            });
        }
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

    private void getGoodNumData(String keyWord) {
        tvTips.setVisibility(TextUtils.isEmpty(keyWord) ? View.GONE : View.VISIBLE);
        HashMap paramsMap = new HashMap();
        paramsMap.put("keyWord", keyWord);
        ApiFactory.getInstance().getApi(Api.class)
                .getPrettys(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GoodNumBean>(GoodnumFragment.this) {

                    @Override
                    public void onSuccess(GoodNumBean bean) {
                        if (bean.fourDigits != null && bean.fourDigits.size() > 0) {
                            ll4.setVisibility(View.VISIBLE);
                            list4.addAll(bean.fourDigits);
                        }
                        adapter4.notifyDataSetChanged();

                        if (bean.fiveDigits != null && bean.fiveDigits.size() > 0) {
                            ll5.setVisibility(View.VISIBLE);
                            list5.addAll(bean.fiveDigits);
                        }
                        adapter5.notifyDataSetChanged();

                        if (bean.sixDigits != null && bean.sixDigits.size() > 0) {
                            ll6.setVisibility(View.VISIBLE);
                            list6.addAll(bean.sixDigits);

                        }
                        adapter6.notifyDataSetChanged();

                        if (bean.seveDigits != null && bean.seveDigits.size() > 0) {
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
                            tvSearch.setText("取消");
                            tvTips.setVisibility(View.GONE);
                        } else {
                            tvTips.setText("搜索到与 ");
                            tvTips.append(LightSpanString.getLightString(keyWord, Color.parseColor("#FFFF2D4E")));
                            tvTips.append(" 匹配的靓号");
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
