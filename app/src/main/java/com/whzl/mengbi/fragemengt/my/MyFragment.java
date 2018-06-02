package com.whzl.mengbi.fragemengt.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.fragemengt.BaseFragement;


public class MyFragment extends BaseFragement {

    public static MyFragment newInstance(String info) {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
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
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_my_layout,null);
        return mView;
    }

}
