package com.kisscigarette.app.ui.message;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kisscigarette.app.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 告警消息-统计分析Created by jxd on 2018/1/6.
 */

public class MessageFragment extends Fragment {
    private View rootView;
    private Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_home_message, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }




}
