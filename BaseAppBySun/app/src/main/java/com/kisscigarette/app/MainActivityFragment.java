package com.kisscigarette.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kisscigarette.app.httpFrame.api.LoginApi;
import com.kisscigarette.app.httpFrame.entity.request.LoginRequest;
import com.kisscigarette.app.httpFrame.entity.result.LoginResult;
import com.kisscigarette.app.httpFrame.net.CustomSubscriber;
import com.kisscigarette.app.httpFrame.net.RetrofitManager;
import com.kisscigarette.app.utils.SharePreferencesUtility;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private Unbinder unbinder;


    @BindView(R.id.texttest)
    TextView texttest;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        SharePreferencesUtility.save("test","this is a proguard test");
    }

    @OnClick(R.id.buttontest)
    void buttonClick() {
        RetrofitManager.getInstance()
                .createReq(LoginApi.class)
                //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                .loginReq2("", new LoginRequest("1111","sun",false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomSubscriber<LoginResult>(getActivity()) {
                            @Override
                            protected void successResult(LoginResult loginResult) {
                                //Toast.makeText(getActivity(),loginResult.toString(),Toast.LENGTH_LONG).show();
                            }
                        }

                );

        texttest.setText(SharePreferencesUtility.get("test","111"));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }
}
