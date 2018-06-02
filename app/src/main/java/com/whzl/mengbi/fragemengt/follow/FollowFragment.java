package com.whzl.mengbi.fragemengt.follow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.fragemengt.BaseFragement;


public class FollowFragment extends BaseFragement {


    public static FollowFragment newInstance(String info) {
        Bundle args = new Bundle();
        FollowFragment fragment = new FollowFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_follow_layout,null);
        return mView;
    }
}
