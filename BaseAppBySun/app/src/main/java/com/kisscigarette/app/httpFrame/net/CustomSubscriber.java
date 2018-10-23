package com.kisscigarette.app.httpFrame.net;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.ActivityManager;
import com.kisscigarette.app.common.MyApplication;
import com.kisscigarette.app.httpFrame.entity.BaseResult;
import com.kisscigarette.app.ui.login.LoginActivity;
import com.kisscigarette.app.ui.widget.AlertDialog;
import com.kisscigarette.app.ui.widget.MyDialog;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @param <T>
 * @author kisscigarette
 */
public abstract class CustomSubscriber<T> implements Observer<T> {
    private Context mContext;
    private Disposable mDisposable;

    public CustomSubscriber(Context mContext) {
        this.mContext = mContext;
    }

    public CustomSubscriber() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

        mDisposable = d;
    }

    @Override
    public void onNext(final T t) {
        onRequestEnd();
        try {
            BaseResult result = (BaseResult) t;
            String errorCode = result.getErrcode();
            if (errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_SUCCESS)) {
                successResult(t);
            } else if (errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_CODE_NEWDEV)) {
                new AlertDialog(mContext).builder()
                        .setMsg(result.getErrmsg())
                        .setCancelable(true)
                        .setTitle("新设备提醒")
                        .setPositiveButton("确  定", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                successResult(t);
                            }
                        }).show();

            }else if (errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_CODE_RELOGIN)) {
                new AlertDialog(mContext).builder()
                        .setMsg("会话超时,请重新登陆")
                        .setCancelable(true)
                        .setTitle("超时提醒")
                        .setPositiveButton("确认", 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyDialog dialog = new MyDialog(mContext);
                                dialog.show();
                                //mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                //ActivityManager.finishAllActivityExceptOne(LoginActivity.class);
                            }
                        }).show();
            } else if (errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_CODE)) {
                new AlertDialog(mContext).builder()
                        .setMsg(result.getErrmsg())
                        .setCancelable(true)
                        .setTitle("异常提醒")
                        .setPositiveButton("请稍后重试", mContext.getResources().getColor(R.color.text_color_red), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorResult(null);
                            }
                        }).show();
            } else if (errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_CODE_HOLD)) {
                new AlertDialog(mContext).builder()
                        .setMsg(result.getErrmsg())
                        .setCancelable(true)
                        .setTitle("登陆提醒")
                        .setPositiveButton("强制退出", mContext.getResources().getColor(R.color.text_color_red), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit()
                                        .remove("cookies")
                                        .apply();
                                ActivityManager.finishAllActivityExceptOne(LoginActivity.class);
                            }
                        }).setNegativeButton("允许同时在线", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        successResult(t);
                    }
                }).show();
            } else if (errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_CODE_LOCK)) {
                new AlertDialog(mContext).builder()
                        .setMsg("该账户被锁!联系管理员")
                        .setNegativeButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }

        } catch (Exception e) {
            Toast.makeText(mContext, "返回数据异常,请稍后重试\n" + e, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof SocketException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                Toast.makeText(mContext, "网络异常，请稍后再试\n" + e, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "服务器异常,请稍后重试\n" + e, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        errorResult(e);

    }


    protected abstract void successResult(T t);

    protected  void errorResult(Throwable t){};

    private void stopNetwork() {
        mDisposable.dispose();
    }



    protected void onRequestEnd() {
        stopNetwork();
    }


}
