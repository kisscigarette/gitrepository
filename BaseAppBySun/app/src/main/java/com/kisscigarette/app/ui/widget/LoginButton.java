package com.kisscigarette.app.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kisscigarette.app.R;

import java.util.Calendar;


/**
 * Created by Administrator on 2016/8/1.
 */

public class LoginButton extends LinearLayout {
    //要显示的view
    private RelativeLayout view;
    public Button enterButton;
    private int text;
    private ImageView loadingImage;
    private Context mContext;

    private Animation scale;
    private OnSubmitListener mListener;
    private static final int ANIMATION_DELAY = 1;
    public boolean isLoading = true;
    private InputMethodManager mInputManager;

    public OnSubmitListener getmListener() {
        return mListener;
    }

    public void setmListener(OnSubmitListener mListener) {
        this.mListener = mListener;
    }


    public interface OnSubmitListener {
        public void onclick();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIMATION_DELAY:
                    resetButton();
                    break;
            }

        }
    };

    public LoginButton(Context context) {
        this(context, null, 0);
//        mContext = context;
//        mInputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public LoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        mContext = context;
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_login_button, null);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoginButton);
        text = a.getResourceId(R.styleable.LoginButton_buttontext2, -1);

        a.recycle();
        initView();
        changeButton();
    }





    /**
     * 初始化View
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        addView(view, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        enterButton = (Button) view.findViewById(R.id.btn_enter);
        loadingImage = (ImageView) view.findViewById(R.id.img_loading);

        scale = AnimationUtils.loadAnimation(mContext, R.anim.scale);
        enterButton.setText(text);
    }


    //按钮消失loading开始旋转
    public void startClickAnim() {
        scale.setFillAfter(true);
        enterButton.setAnimation(scale);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isLoading) {
                    enterButton.setVisibility(View.GONE);
                    loadingImage.setVisibility(View.VISIBLE);
                    Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
                    LinearInterpolator lin = new LinearInterpolator();
                    operatingAnim.setInterpolator(lin);
                    loadingImage.startAnimation(operatingAnim); //设置动画
                    loadingImage.setClickable(false);
                    enterButton.setClickable(false);
                } else {
                    resetButton();
                }
//                handler.sendEmptyMessageDelayed(0,2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        enterButton.startAnimation(scale);
    }


    // 旋转停止按钮重现
    public void resetButton() {
        isLoading = false;
        enterButton.clearAnimation();
        loadingImage.clearAnimation();
        enterButton.setClickable(true);
        enterButton.setVisibility(VISIBLE);
        loadingImage.setVisibility(GONE);

    }



    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;



    private void changeButton() {
        enterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    Log.e("test","changeButton");
                    lastClickTime = currentTime;
                    if (mListener != null) {
                        mListener.onclick();
                    }
                    /*if (mContext instanceof BaseActivity && ((BaseActivity) mContext).getCurrentFocus() != null) {
                        mInputManager.hideSoftInputFromWindow(((BaseActivity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }*/


                    if (isLoading) {
                        startClickAnim();
                    }
                }

            }
        });
    }

    //不可点击时按钮样式
    public void unPressed() {
        enterButton.setTextColor(getResources().getColor(R.color.actionsheet_gray));
        enterButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_22));
        enterButton.setAlpha(0.7f);
    }

    //可点击时按钮样式
    public void pressed() {
        enterButton.setTextColor(getResources().getColor(R.color.white));
        enterButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_21));
        enterButton.setAlpha(1.0f);
    }

    public String getText(){
        return enterButton.getText().toString();
    }
}
