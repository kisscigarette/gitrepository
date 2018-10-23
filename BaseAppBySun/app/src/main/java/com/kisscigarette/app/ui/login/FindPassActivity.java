package com.kisscigarette.app.ui.login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.StringUtils;
import com.kisscigarette.app.ui.widget.AlertDialog;
import com.kisscigarette.app.ui.widget.LoginButton;
import com.kisscigarette.app.ui.widget.PassEditText;
import com.kisscigarette.app.ui.widget.ValideCodeText;
import com.kisscigarette.app.ui.widget.VerifyButton;
import com.kisscigarette.app.ui.widget.WholeEditText;
import com.kisscigarette.app.utils.AdvancedCountdownTimer;
import com.kisscigarette.app.utils.SnackbarUtil;


/**
 * Created by Administrator on 2016/7/30.
 */

public class FindPassActivity extends Activity implements View.OnClickListener {

    private static final String PHONE_REGISTERED = "3";
    //倒计时时间（s），一分钟
    private static final int COUNT_DOWN_SECONDS = 60;
    //倒计时时间间隔 毫秒
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private WholeEditText phoneText;
    private ValideCodeText verifyText;
    private PassEditText passText;
    private VerifyButton verifyButton;

    private LoginButton findpassButton;
    private ImageView logoImage;
    private RelativeLayout root_layout;
    //电话号码已注册提示框
    private AlertDialog errorDialog;
    //注册成功提示框
    private AlertDialog successDialog;
    private String phone;
    //验证码
    private String verCode;
    private String pass;

    private FrameLayout ll_edit;
    private int imageHeight = 0;
    private static long ANIMA_TIME = 250;
    private boolean isShown = false;
    private static final int LOADING_DELAY = 0;

    //倒计时工具
    private AdvancedCountdownTimer countdownTimer;

    String phoneNum = "";


    /**
     * 处理页面跳转请求和数据
     */
    private void initDataRedirect() {
        Intent intent = getIntent();
        if (null != intent) {
            phoneNum = intent.getStringExtra("phoneNum");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpass);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ((TextView) findViewById(R.id.toolbar_title)).setText("重置密码");
        findViewById(R.id.view_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );
        initDataRedirect();
        initView();
    }


    protected void initView() {
        passText = (PassEditText) findViewById(R.id.wet_pass);
        phoneText = (WholeEditText) findViewById(R.id.wet_phone);
        if (!StringUtils.isEmpty(phoneNum)) phoneText.setText(phoneNum);
        verifyText = (ValideCodeText) findViewById(R.id.wet_verify);
        verifyButton = (VerifyButton) findViewById(R.id.btn_verify);

        findpassButton = (LoginButton) findViewById(R.id.btn_findpass);
        logoImage = (ImageView) findViewById(R.id.img_logo);
        verifyButton.setOnClickListener(this);


        changeText();

        //监听软键盘状态隐藏logo
        ll_edit = (FrameLayout) findViewById(R.id.ll_edit);
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);


        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        logoImage.measure(width, height);

        imageHeight = logoImage.getMeasuredHeight();

        root_layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root_layout.getWindowVisibleDisplayFrame(rect);
                        // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                        int rootInvisibleHeight = root_layout.getRootView()
                                .getHeight() - rect.bottom;
                        Log.i("tag", "最外层的高度" + root_layout.getRootView().getHeight());

                        Log.i("tag", "rootInvisibleHeight" + rootInvisibleHeight);
                        // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                        if (rootInvisibleHeight > 300) {
                            //软键盘弹出来的时候
                            if (!isShown) {
                                hideLogo();
                            }
                            isShown = true;
                        } else {
                            // 软键盘没有弹出来的时候
                            if (isShown) {
                                showLogo();
                                isShown = false;
                            }
                        }
                    }
                });

        countdownTimer = new AdvancedCountdownTimer() {
            @Override
            public void onTick(int millisUntilFinished, int percent) {

                //进度
                verifyButton.setButtonText("(" + millisUntilFinished + "s) 重新获取", false);
            }

            @Override
            public void onFinish() {
                closeKeyboard();
                //结束
                verifyButton.resetButton();
            }
        };

        findpassButton.setmListener(new LoginButton.OnSubmitListener() {
            @Override
            public void onclick() {
                closeKeyboard();
                showToast();
            }
        });

    }


    //设置button字体改变
    public void changeText() {
        phoneText.setTextChangedListener(new WholeEditText.OnTextChangedListener() {
            @Override
            public void textChanged(Editable editable) {
                verCode = verifyText.getText();
                pass = passText.getText();


            }
        });
        verifyText.setTextChangedListener(new ValideCodeText.OnTextChangedListener() {
            @Override
            public void textChanged(Editable editable) {
                phone = phoneText.getText();
                pass = passText.getText();


            }
        });
        passText.setTextChangedListener(new PassEditText.OnTextChangedListener() {
            @Override
            public void textChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify:
                verifyButton.startAnima();
                phone = phoneText.getText();
                if (!StringUtils.isPhoneValid(phone)) {
                    SnackbarUtil.LongSnackbar(verifyButton, "请输入正确的手机号码", SnackbarUtil.Confirm).show();
                    verifyButton.resetButton();
                    return;
                }
                countdownTimer.setMills(COUNT_DOWN_SECONDS, COUNT_DOWN_INTERVAL);
                countdownTimer.start();
                break;


            case R.id.btn_findpass:

                break;


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    //隐藏logo
    private void hideLogo() {

        AnimatorSet animationSet = new AnimatorSet();

        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_edit, "translationY", 0, -imageHeight);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(logoImage, "scaleX", 1, 0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(logoImage, "scaleY", 1, 0);
        animationSet.setDuration(ANIMA_TIME);

        animationSet.playTogether(animator, animator2, animator3);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                findpassButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (Build.VERSION.SDK_INT < 14) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_edit.getLayoutParams();
                    params.topMargin = params.topMargin - imageHeight;
                    ll_edit.setLayoutParams(params);
                    ll_edit.clearAnimation();

                }
                logoImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationSet.start();


    }

    //显示logo
    private void showLogo() {

        AnimatorSet animationSet = new AnimatorSet();

        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_edit, "translationY", -imageHeight, 0);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                logoImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (Build.VERSION.SDK_INT < 14) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_edit.getLayoutParams();
                    params.topMargin = params.topMargin - imageHeight;
                    ll_edit.setLayoutParams(params);
                    ll_edit.clearAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(logoImage, "scaleX", 0, 1);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(logoImage, "scaleY", 0, 1);
        animationSet.playTogether(animator, animator2, animator3);
        animationSet.setDuration(ANIMA_TIME);
        animationSet.start();

    }

    //弹出修改成功提示框
    private void showSuccessDialog(String message) {
        if (successDialog == null) {
            successDialog = new AlertDialog(this).builder()
                    .setMsg(message)
                    .setPositiveButton("修改成功", 0,new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            successDialog.dismiss();
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", phone);
                            bundle.putString("passwd", pass);
                            resultIntent.putExtras(bundle);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    });
        }
        successDialog.show();
    }

    //弹出号码已被注册提示框
    private void showPhoneRegisteredDialog(String message) {
        phone = phoneText.getText();
        if (errorDialog == null) {
            errorDialog = new AlertDialog(this).builder()
                    .setMsg(message)
                    .setNegativeButton("我知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
        }
        errorDialog.show();
    }

    //手机号、验证码格式判断
    public void showToast() {
        findpassButton.isLoading = false;
        phone = phoneText.getText();
        verCode = verifyText.getText();
        pass = passText.getText();
        if (StringUtils.delSpace(phone).equals("")) {
            SnackbarUtil.ShortSnackbar(phoneText, "请输入手机号码", SnackbarUtil.Confirm).show();
            return;
        }
        if (!StringUtils.isPhoneValid(phone)) {
            SnackbarUtil.ShortSnackbar(phoneText, "请输入正确的手机号码", SnackbarUtil.Confirm).show();
            return;
        }
        if (StringUtils.delSpace(verCode).equals("")) {
            SnackbarUtil.ShortSnackbar(phoneText, "请输入验证码", SnackbarUtil.Confirm).show();
            return;
        }
        if (!StringUtils.isNumeric(verCode)) {
            SnackbarUtil.ShortSnackbar(phoneText, "请输入正确的验证码", SnackbarUtil.Confirm).show();
        }
        if (StringUtils.delSpace(pass).equals("")) {
            SnackbarUtil.ShortSnackbar(phoneText, "请输入密码", SnackbarUtil.Confirm).show();
            return;
        }
        if (!StringUtils.isNumericAndChar(pass)) {
            SnackbarUtil.ShortSnackbar(phoneText, "密码格式错误，请输入6–20位数字、字母的组合，区分大小写", SnackbarUtil.Confirm).show();
            return;
        } else if (!(StringUtils.delSpace(phone).equals("")) && !(StringUtils.delSpace(verCode).equals(""))
                && !(StringUtils.delSpace(pass).equals(""))) {
            findpassButton.isLoading = true;
            findpassButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSuccessDialog("修改成功");
                    findpassButton.resetButton();
                }
            }, 2000);
        }
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
