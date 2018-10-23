package com.kisscigarette.app.common;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <B>Activity基类，所有Activity应该继承此类</B>
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    /**
     * butterKnife
     */
    protected Unbinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.oncreateBefore();
        // savedInstanceState建议在onRestoreInstanceState方法中处理
        super.onCreate(savedInstanceState);
        this.initBefore();
        this.setContentView(getContentViewLayoutID());
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        this.init();
        ActivityManager.pushActivity(this);
    }

    /**
     * 活动前期准备
     */
    protected void oncreateBefore() {
    }

    /**
     * 设置界面前准备
     */
    protected void initBefore() {
    }

    /**
     * 获取布局ID
     *
     * @return 布局id
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 具体实现
     */
    protected abstract void init();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.mBinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        // 取消绑定
        this.mBinder.unbind();
        super.onDestroy();
        // 从活动中移除
        ActivityManager.popActivity(this);
    }



}
