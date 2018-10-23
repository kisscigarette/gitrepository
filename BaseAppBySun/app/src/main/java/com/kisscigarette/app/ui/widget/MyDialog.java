package com.kisscigarette.app.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.ActivityManager;
import com.kisscigarette.app.common.SharePreferencesUtility;
import com.kisscigarette.app.common.StringUtils;
import com.kisscigarette.app.httpFrame.api.LoginApi;
import com.kisscigarette.app.httpFrame.entity.request.LoginRequest;
import com.kisscigarette.app.httpFrame.entity.result.LoginResult;
import com.kisscigarette.app.httpFrame.net.CustomSubscriber;
import com.kisscigarette.app.httpFrame.net.RetrofitManager;
import com.kisscigarette.app.ui.HomeActivity;
import com.kisscigarette.app.utils.Rotate3dAnimation;
import com.kisscigarette.app.utils.SnackbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.OptAnimationLoader;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by <a href="http://blog.csdn.net/student9128">Kevin</a> on 2017/8/17.
 * <p>
 * <h3>Description:</h3>
 * <p/>
 * <p/>
 */
public class MyDialog extends Dialog {


    @BindView(R.id.bt_change)
    Button btChange;
    @BindView(R.id.bt_exit)
    Button btExit;

    @BindView(R.id.ll_content)
    LinearLayout llContent;

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.container)
    RelativeLayout container;
    private Context context;

    @BindView(R.id.ll_register)
    LinearLayout llRegister;

    @BindView(R.id.et_user_name)
    EditText et_user_name;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.cb_auto_login)
    CheckBox cb_auto_login;

    @BindView(R.id.btn_login)
    Button btn_login;


    //接口回调传递参数
    private OnClickListenerInterface mListener;
    private View view;
    //
    private String strContent;


    private int centerX;
    private int centerY;
    private int depthZ = 700;
    private int duration = 300;
    private Rotate3dAnimation openAnimation;
    private Rotate3dAnimation closeAnimation;

    AnimationSet mModalInAnim;

    private boolean isOpen = false;

    public interface OnClickListenerInterface {
        /**
         * 确认,
         */
        void doConfirm();

        /**
         * 取消
         */
        void doCancel();
    }

    public MyDialog(Context context) {
        super(context);

        this.context = context;
    }

    public MyDialog(Context context, String content) {
        super(context);
        this.context = context;
        this.strContent = content;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉系统的黑色矩形边框
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_my, null);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        setContentView(view);
        ButterKnife.bind(this);

        btChange.setOnClickListener(new OnWidgetClickListener());
        btnBack.setOnClickListener(new OnWidgetClickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        setCancelable(true);

        et_user_name.setText(SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME, ""));
        et_password.setText(SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_PASSWD, ""));


    }

    public void show() {
        super.show();
        view.startAnimation(mModalInAnim);
    }


    public void setClicklistener(OnClickListenerInterface clickListenerInterface) {
        this.mListener = clickListenerInterface;
    }

    private class OnWidgetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id) {
                case R.id.bt_change:
                    startAnimation();
                    break;
                case R.id.btn_back:
                    startAnimation();
                    break;
            }
        }
    }


    @OnClick(R.id.bt_exit)
    void btExit() {
        this.cancel();
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("退出")
                .setContentText("确定退出应用程序？")
                .setCancelText("不，我再看看")
                .setConfirmText("是，我要退出")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((HomeActivity) context).finish();
                        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
                        launcherIntent.addCategory(Intent.CATEGORY_HOME);
                        context.startActivity(launcherIntent);
                        ActivityManager.exitApp();

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();

                        MyDialog.this.show();
                    }
                })
                .show();


    }

    @OnClick(R.id.btn_login)
    void login() {
        String user= et_user_name.getText().toString();
        String password= et_password.getText().toString();
        if (StringUtils.delSpace(password).equals("")) {
            SnackbarUtil.ShortSnackbar(et_user_name, "请输入密码", SnackbarUtil.Confirm).show();
        }else {
            RetrofitManager.getInstance()
                    .createReq(LoginApi.class)
                    //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                    //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                    .loginReq("", new LoginRequest(user, password, false))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomSubscriber<LoginResult>(context) {
                                   @Override
                                   protected void successResult(LoginResult loginResult) {
                                           MyDialog.this.dismiss();

                                   }

                                   protected void errorResult(Throwable t) {
                                       SnackbarUtil.ShortSnackbar(et_user_name, "登录失败", SnackbarUtil.Alert).show();
                                   }
                               }
                    );

        }
    }


    private void startAnimation() {
        //接口回调传递参数
//                    mListener.doConfirm();
//                    centerX = mContainer.getWidth() / 2;
//                    centerY = mContainer.getHeight() / 2;
        centerX = container.getWidth() / 2;
        centerY = container.getHeight() / 2;
        if (openAnimation == null) {
            initOpenAnim();
            initCloseAnim();
        }

        //用作判断当前点击事件发生时动画是否正在执行
        if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
            return;
        }
        if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
            return;
        }

        //判断动画执行
        if (isOpen) {
//                        mContainer.startAnimation(openAnimation);
            container.startAnimation(openAnimation);

        } else {
//                        mContainer.startAnimation(closeAnimation);
            container.startAnimation(closeAnimation);

        }
        isOpen = !isOpen;
    }

    /**
     * 卡牌文本介绍打开效果：注意旋转角度
     */
    private void initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        openAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegister.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(270, 360, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                container.startAnimation(rotateAnimation);
            }
        });
    }

    /**
     * 卡牌文本介绍关闭效果：旋转角度与打开时逆行即可
     */
    private void initCloseAnim() {
        closeAnimation = new Rotate3dAnimation(360, 270, centerX, centerY, depthZ, true);
        closeAnimation.setDuration(duration);
        closeAnimation.setFillAfter(true);
        closeAnimation.setInterpolator(new AccelerateInterpolator());
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegister.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(90, 0, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                container.startAnimation(rotateAnimation);
            }
        });
    }


}