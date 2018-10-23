package com.kisscigarette.app.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.kisscigarette.app.R;
import com.kisscigarette.app.utils.RevealBackgroundView;

public class SeccondActivity extends Activity {
    private RevealBackgroundView revealBackgroundView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        revealBackgroundView = (RevealBackgroundView) findViewById(R.id.RevealBackgroundView);
        //setupRevealBackground(savedInstanceState);
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        revealBackgroundView.setOnStateChangeListener(new RevealBackgroundView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {

                    // 如果当前状态完成，就显示底部布局，隐藏RevealBackgroundView
                    if (RevealBackgroundView.STATE_FINISHED == state) {
                        //linea.setVisibility(View.VISIBLE);
                        revealBackgroundView.setVisibility(View.GONE);
                    }

            }
        });
        //初始化时，当前的savedInstanceState为空
        if (savedInstanceState == null) {
            final int[] startLocation = getIntent()
                    .getIntArrayExtra("location");
            // 初始化控件时的监听
            revealBackgroundView.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            revealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                            // 设置单击坐标点的半径
                            revealBackgroundView.setCurrentRadius(50);
                            // 设置绘制填充画笔的颜色
                            revealBackgroundView.setFillPaintColor(0xff34A67B);
                            // 开启动画
                            revealBackgroundView.startFromLocation(startLocation);
                            return true;
                        }
                    });
        } else {
            // 完成动画
            revealBackgroundView.setToFinishedFrame();
        }
    }


}
