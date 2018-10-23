package com.kisscigarette.app.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.ActivityManager;
import com.kisscigarette.app.common.BaseActivity;
import com.kisscigarette.app.common.SharePreferencesUtility;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.iv_entry)
    ImageView mIVEntry;

    private static final int ANIM_TIME = 2000;

    private static final float SCALE_END = 1.07F;

    private static final int[] Imgs = {
            R.drawable.image_welcome, R.drawable.image_welcome,
            R.drawable.image_welcome, R.drawable.image_welcome,
            R.drawable.image_welcome};


    @Override
    protected int getContentViewLayoutID() {
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
        }
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SharePreferencesUtility.get(SharePreferencesUtility.FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        startHomeActivity();
    }

    private void startHomeActivity() {
        Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
        mIVEntry.setImageResource(Imgs[random.nextInt(Imgs.length)]);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long aLong) throws Exception {
                        startAnim();
                    }
                }/*new Action1<Long>() {

                    *//*@Override
                    public void call(Long aLong) {
                        startAnim();
                    }*//*
                }*/);
    }

    private void startAnim() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIVEntry, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIVEntry, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                WelcomeActivity.this.finish();
            }
        });

        /*Boolean isLogin = SharePreferencesUtility.get(SharePreferencesUtility.IS_LOGIN, false);

        if (isLogin && !SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME, "").equals("")) {
            RetrofitManager.getInstance()
                    .createReq(LoginApi.class)
                    //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                    //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                    .loginReq("", new LoginRequest(SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME, ""), SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_PASSWD, ""), false))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomSubscriber<LoginResult>(WelcomeActivity.this) {
                                   @Override
                                   protected void successResult(LoginResult loginResult) {
                                       if (loginResult.getErrcode().equals(AppConfig.HTTP_RESPONSE_ERROR_SUCCESS)) {
                                           ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this);
                                           startActivity(new Intent(WelcomeActivity.this, HomeActivity.class),compat.toBundle());
                                           //WelcomeActivity.this.finish();
                                       } else {
                                           startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                                           //WelcomeActivity.this.finish();
                                       }
                                   }


                                   protected void errorResult(Throwable t) {
                                       startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                                       //WelcomeActivity.this.finish();
                                   }
                               }

                    );


        } else {*/

        //}

    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager.endActivity(this);
    }
}
