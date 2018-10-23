package com.kisscigarette.app.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kisscigarette.app.R;


/**
 * Created by Administrator on 2016/8/1.
 */

public class VerifyButton extends LinearLayout {
    //要显示的view
    private RelativeLayout view;
    private Button getVerify;
    private ImageView verfyingImage;
    private Context mContext;
    private Animation scale;
    private int text;
    private static final int ANIMATION_DELAY = 1;

    @Override
    public void setOnClickListener(OnClickListener l) {
        getVerify.setOnClickListener(l);
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }*/

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

    public VerifyButton(Context context) {
        this(context, null, 0);
        mContext = context;
    }

    public VerifyButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public VerifyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_verify_button, null);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyButton);
        text = a.getResourceId(R.styleable.VerifyButton_buttontext, R.string.str_get_verify);


        a.recycle();
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        addView(view, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        getVerify = (Button) view.findViewById(R.id.btn_verify);
        verfyingImage = (ImageView) view.findViewById(R.id.img_verifying);
        scale = AnimationUtils.loadAnimation(mContext, R.anim.scale);
        getVerify.setText(text);
        changeButton();
    }



    public void startAnima(){
        scale.setFillAfter(true);
        getVerify.setAnimation(scale);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                getVerify.setVisibility(View.GONE);
                verfyingImage.setVisibility(View.VISIBLE);
                Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                verfyingImage.startAnimation(operatingAnim); //设置动画
                getVerify.setClickable(false);
                verfyingImage.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        getVerify.startAnimation(scale);
    }



    private void changeButton() {
    }


    public void resetButton() {
        getVerify.clearAnimation();
        verfyingImage.clearAnimation();
        getVerify.setClickable(true);
        verfyingImage.setVisibility(View.INVISIBLE);
        getVerify.setVisibility(VISIBLE);
        getVerify.setText(text);

    }


    public void setButtonText(String text,boolean clickable){
        resetButton();
        getVerify.setClickable(clickable);
        getVerify.setText(text);
    }


}
