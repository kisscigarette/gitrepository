package com.kisscigarette.app.httpFrame.net;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kisscigarette.app.httpFrame.entity.BaseResult;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @author kisscigarette
 * @param <T>
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
        onRequestStart();
        mDisposable=d;
    }

    @Override
    public void onNext(T t) {
        onRequestEnd();
        try {
            BaseResult result = (BaseResult) t;
            String errorCode = result.getErrcode();

            /*if (!TextUtils.isEmpty(result.getDeviceid()) && (!result.getDeviceid().contains("SA2B") && !result.getDeviceid().contains("sa2b")) && result.getCmdid() != null && result.getCmdid().startsWith("3") && errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_OFFLINE)
                    && (!TextUtils.isEmpty(result.getDevicetype()) && result.getDevicetype().equals("7"))
                    ) {
                ((BaseActivity) AppManager.getAppManager().currentActivity()).dismissLoadingDialog();
                AppManager.getAppManager().showChangeOfflineMode(result.getDeviceid());
                successResult(t);
            } else if (errorCode.equals(AppConfig.HTTP_RESPONSE_ERROR_CODE_RELOGIN)) {
                ((BaseActivity) AppManager.getAppManager().currentActivity()).dismissLoadingDialog();
                AppManager.getAppManager().reLogin(false, result.getErrmsg());
            } else {
                successResult(t);
            }*/
            if(TextUtils.isEmpty(errorCode)||errorCode.equals("null")){
                Toast.makeText(mContext, "数据异常,请稍后重试", Toast.LENGTH_LONG).show();
            }
            Log.i("result*******-->",result.toString());
            successResult(t);
        }catch (Exception e){
            Log.i("数据异常*******-->",e.toString());
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
                Toast.makeText(mContext, "网络异常，请稍后再试"+e, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "服务器异常,请稍后重试"+e, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }


    protected abstract void successResult(T t);

    public void stopNetwork(){
        mDisposable.dispose();
    }

    protected void onRequestStart() {
    }

    protected void onRequestEnd() {
        closeProgressDialog();
    }

    public void showProgressDialog() {
        ProgressDialog.show(mContext, "", "请稍后",false);
    }

    public void closeProgressDialog() {

    }

}
