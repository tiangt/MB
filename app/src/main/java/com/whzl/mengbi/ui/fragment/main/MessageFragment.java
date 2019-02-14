package com.whzl.mengbi.ui.fragment.main;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

/**
 * 消息
 *
 * @author cliang
 * @date 2019.2.14
 */
public class MessageFragment extends BaseFragment {

    public static MessageFragment newInstance(){
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void init() {

    }
}
