package com.kisscigarette.app.ui.login;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.BaseActivity;
import com.kisscigarette.app.common.SharePreferencesUtility;
import com.kisscigarette.app.common.StringUtils;
import com.kisscigarette.app.httpFrame.api.LoginApi;
import com.kisscigarette.app.httpFrame.entity.request.LoginRequest;
import com.kisscigarette.app.httpFrame.entity.result.LoginResult;
import com.kisscigarette.app.httpFrame.net.AppConfig;
import com.kisscigarette.app.httpFrame.net.CustomSubscriber;
import com.kisscigarette.app.httpFrame.net.RetrofitManager;
import com.kisscigarette.app.ui.HomeActivity;
import com.kisscigarette.app.ui.widget.ActionSheetDialog;
import com.kisscigarette.app.ui.widget.LoginButton;
import com.kisscigarette.app.ui.widget.PassEditText;
import com.kisscigarette.app.ui.widget.WholeEditText;
import com.kisscigarette.app.utils.SnackbarUtil;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ted on 2016/7/19.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private static final String DEFAULT_KEY_NAME = "default_key";
    KeyStore keyStore;

    private final static int REGISTER_REQUEST_CODE = 1;
    private final static int FINDPASS_REQUEST_CODE = 2;
    private ImageView logoImage;
    private WholeEditText userName;
    private PassEditText userPass;
    private TextView register;
    private TextView forgetPass;
    private TextView chooseip;
    LoginButton loginButton;
    private RelativeLayout root_layout;
    private String user;
    private String password;
    private static final int LOADING_DELAY = 0;


    private FrameLayout ll_edit;
    private int imageHeight = 0;

    private static long ANIMA_TIME = 250;

    private boolean isShown = false;

    private boolean isGesture = false;
    private ActivityOptions compat;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        compat = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
        //如果是从手势密码登录界面跳转过来
        if (getIntent().getStringExtra("isGesture") != null) {
            isGesture = true;
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getIntent().getBooleanExtra("register",false) ) {
            if (supportFingerprint()) {
                initKey();
                initCipher();
            }
        }

    }

    protected void initView() {
        logoImage = (ImageView) findViewById(R.id.img_logo);
        userName = (WholeEditText) findViewById(R.id.wet_user);
        userPass = (PassEditText) findViewById(R.id.wet_pass);
        loginButton = (LoginButton) findViewById(R.id.btn_login);
        chooseip = findViewById(R.id.choose_ip);
        register = (TextView) findViewById(R.id.btn_register_user);
        forgetPass = (TextView) findViewById(R.id.btn_forget_pwd);
        register.setOnClickListener(this);
        forgetPass.setOnClickListener(this);

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

        loginButton.setmListener(new LoginButton.OnSubmitListener() {
            @Override
            public void onclick() {
                closeKeyboard();
                if (changeUserInfo()) {
                    if (!userName.getText().equals(SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME, ""))) {
                        //切换账号则手势密码清空且变为关闭状态
                        //LockPatternUtils.saveLockPattern(LoginActivity.this, BaseApplication.GESTURE_KEY, "");
                        //BaseApplication.getInstance().setGesture(false);
                    }
                    if (checkpermission()) loginRetrofit();
                    else loginButton.resetButton();
                }
                //密码格式判断弹出框

            }
        });

        chooseip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActionSheetDialog(LoginActivity.this)
                        .builder()
                        .setTitle("请选择服务器节点")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("外网云平台", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        chooseip.setText("外网云平台");
                                    }
                                })
                        .addSheetItem("127测试环境", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        chooseip.setText("127测试环境");
                                    }
                                })
                        .addSheetItem("124测试环境", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        chooseip.setText("124测试环境");
                                    }
                                })
                        .show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REGISTER_REQUEST_CODE) {
                user = data.getStringExtra("username");
                userName.setText(user);
                password = data.getStringExtra("passwd");
                userPass.setText(password);
                loginRetrofit();
                loginButton.isLoading = true;
                loginButton.startClickAnim();
            } else if (requestCode == FINDPASS_REQUEST_CODE) {
                user = data.getStringExtra("username");
                userName.setText(user);
                password = data.getStringExtra("passwd");
                userPass.setText(password);
                loginRetrofit();
                loginButton.isLoading = true;
                loginButton.startClickAnim();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_user:
                closeKeyboard();
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER_REQUEST_CODE);
                break;
            case R.id.btn_forget_pwd:
                closeKeyboard();
                Intent intent1 = new Intent(this, FindPassActivity.class);
                if (StringUtils.isPhoneNumberValid(userName.getText().toString().trim())) {
                    intent1.putExtra("phoneNum", userName.getText().toString());
                }
                startActivityForResult(intent1, FINDPASS_REQUEST_CODE);
                break;
        }
    }


    //登录接口
    public void loginRetrofit() {
        //设置loginRequest各项值
        //loginRequest = new LoginRequest(EncryptionUtils.SHA256(password), user, false);
        //mPresenter.loginAction(loginRequest, true);
        loginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                RetrofitManager.getInstance()
                        .createReq(LoginApi.class)
                        //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                        //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                        .loginReq("", new LoginRequest(user, password, false))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CustomSubscriber<LoginResult>(LoginActivity.this) {
                                       @Override
                                       protected void successResult(LoginResult loginResult) {
                                           loginButton.resetButton();
                                           if (loginResult.getErrcode().equals(AppConfig.HTTP_RESPONSE_ERROR_SUCCESS)) {
                                               SharePreferencesUtility.save(SharePreferencesUtility.KEY_USER_NAME, user);
                                               SharePreferencesUtility.save(SharePreferencesUtility.KEY_USER_PASSWD, password);
                                               loginSuccess(true);
                                           }else
                                               loginSuccess(false);
                                       }

                                       protected void errorResult(Throwable t) {
                                           loginButton.resetButton();
                                       }
                                   }
                        );


            }
        }, 0);

    }


    //用户名密码格式判断
    public boolean changeUserInfo() {
        loginButton.isLoading = false;
        user = userName.getText();
        password = userPass.getText();
        if (StringUtils.delSpace(user).equals("")) {
            SnackbarUtil.ShortSnackbar(loginButton, "请输入手机号码", SnackbarUtil.Confirm).show();
            return false;
        } else if (!StringUtils.isPhoneNumberValid(user)) {
            SnackbarUtil.ShortSnackbar(loginButton, "请输入正确的手机号码", SnackbarUtil.Confirm).show();
            return false;
        } else if (StringUtils.delSpace(password).equals("")) {
            SnackbarUtil.ShortSnackbar(loginButton, "请输入密码", SnackbarUtil.Confirm).show();
            return false;
        } else {
            loginButton.isLoading = true;
            return true;
        }
    }


    //隐藏logo
    private void hideLogo() {
        AnimatorSet animationSet = new AnimatorSet();

        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_edit, "translationY", 0, -imageHeight);
        animationSet.setDuration(ANIMA_TIME);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(logoImage, "scaleX", 1, 0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(logoImage, "scaleY", 1, 0);

        animationSet.playTogether(animator, animator2, animator3);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //loginButton.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void loginSuccess(Boolean result) {
        //登陆成功之后

        if (!result) {
            //登录失败
            SnackbarUtil.ShortSnackbar(loginButton, "登录失败", SnackbarUtil.Alert).show();

            //手势密码清空且变为关闭状态
            //LockPatternUtils.saveLockPattern(this, LockPatternUtils.GESTURE_KEY, "");
            SharePreferencesUtility.save(SharePreferencesUtility.IS_GESTURE, false);
            return;
        }
        SharePreferencesUtility.save(SharePreferencesUtility.IS_LOGIN, true);


        //SharePreferencesUtility.save(SharePreferencesUtility.KEY_PHONE_NUM, "");
        if (isGesture) {
            //startActivity(new Intent(LoginActivity.this, ResetGestureActivity.class));

        } else {

            startActivity(new Intent(LoginActivity.this, HomeActivity.class), compat.toBundle());

        }


    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkpermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public boolean supportFingerprint() {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, "您的系统版本过低，不支持指纹功能", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
            FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
            if (!fingerprintManager.isHardwareDetected()) {
                SnackbarUtil.ShortSnackbar(logoImage, "您的手机不支持指纹功能", SnackbarUtil.Info).show();
                return false;
            } else if (!keyguardManager.isKeyguardSecure()) {
                SnackbarUtil.ShortSnackbar(logoImage, "您还未设置锁屏，请先设置锁屏并添加一个指纹", SnackbarUtil.Info).show();
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                SnackbarUtil.ShortSnackbar(logoImage, "您至少需要在系统设置中添加一个指纹", SnackbarUtil.Info).show();
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    private void initKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(23)
    private void initCipher() {
        try {
            SecretKey key = (SecretKey) keyStore.getKey(DEFAULT_KEY_NAME, null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            showFingerPrintDialog(cipher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showFingerPrintDialog(Cipher cipher) {
        FingerprintDialogFragment fragment = new FingerprintDialogFragment();
        fragment.setCipher(cipher);
        fragment.show(getFragmentManager(), "fingerprint");
    }

    public void onAuthenticated() {

        RetrofitManager.getInstance()
                .createReq(LoginApi.class)
                //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                .loginReq("", new LoginRequest(SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME, ""), SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_PASSWD, ""), false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomSubscriber<LoginResult>(this) {
                               @Override
                               protected void successResult(LoginResult loginResult) {
                                   if (loginResult.getErrcode().equals(AppConfig.HTTP_RESPONSE_ERROR_SUCCESS)) {
                                           loginSuccess(true);
                                   } else {
                                       SnackbarUtil.ShortSnackbar(logoImage, "检测到账号操作异常,请先账号密码登陆!", SnackbarUtil.Info).show();
                                       //WelcomeActivity.this.finish();
                                   }
                               }


                               protected void errorResult(Throwable t) {
                                   SnackbarUtil.ShortSnackbar(logoImage, "检测到账号操作异常,请先账号密码登陆!", SnackbarUtil.Info).show();
                                   //WelcomeActivity.this.finish();
                               }
                           }

                );
    }

}
