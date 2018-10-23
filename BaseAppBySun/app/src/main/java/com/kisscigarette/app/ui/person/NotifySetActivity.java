package com.kisscigarette.app.ui.person;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kisscigarette.app.R;
import com.kisscigarette.app.adapter.NotifyItemAdapter;
import com.kisscigarette.app.common.ActivityManager;
import com.kisscigarette.app.common.BaseActivity;
import com.kisscigarette.app.common.LanguageUtil;
import com.kisscigarette.app.common.SharePreferencesUtility;
import com.kisscigarette.app.httpFrame.entity.result.NotifyListResult;
import com.kisscigarette.app.ui.login.LoginActivity;
import com.kisscigarette.app.ui.widget.AlertDialog;
import com.kisscigarette.app.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/9/13.
 */
public class NotifySetActivity extends BaseActivity {


    private List<NotifyListResult.ItemsBean> notifyList = new ArrayList<>();
    private NotifyItemAdapter mAdapter;

    @BindView(R.id.toolbar_title)
    TextView title;

    @BindView(R.id.list_notify)
    ListView listnotify;

    /**
     * 处理页面跳转请求和数据
     *
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, NotifySetActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
        context.startActivity(intent, compat.toBundle());
    }

    private void initDataRedirect() {
        Intent intent = getIntent();
        if (null != intent) {
            intent.getStringExtra("param1");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
            }
        }
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_notyfy_setting;
    }

    @Override
    protected void init() {
        title.setText("通知");
        notifyList.add(new NotifyListResult.ItemsBean(0, "0", "简体中文"));
        notifyList.add(new NotifyListResult.ItemsBean(1, "1", "繁体中文"));
        notifyList.add(new NotifyListResult.ItemsBean(2, "1", "英文"));
        notifyList.add(new NotifyListResult.ItemsBean(3, "0", "设置指纹验证"));
        notifyList.add(new NotifyListResult.ItemsBean(4, "1", "检修告警"));
        updateNotifyList();
    }


    private void updateNotifyList() {
        if (notifyList != null) {
            if (mAdapter == null) {
                mAdapter = new NotifyItemAdapter(this, notifyList);
                listnotify.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

    }


    public void notifySet(NotifyListResult.ItemsBean item,int position, boolean isopen) {
        //switch (item.getType()) {
        //    case 0:
        if (position<2&&isopen) {
            LanguageUtil.selectLang(this, 3);
            new AlertDialog(this).builder().setTitle(getString(R.string.str_quit_account))
                    .setMsg("请重新登录使语言生效")
                    .setPositiveButton("确认", 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharePreferencesUtility.update(SharePreferencesUtility.IS_LOGIN, false);
                            SharePreferencesUtility.remove(SharePreferencesUtility.HEADPHOTO);
                            startActivity(new Intent(NotifySetActivity.this, LoginActivity.class));
                            ActivityManager.finishAllActivityExceptOne(LoginActivity.class);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();

        } else {
            LanguageUtil.selectLang(this, 1);
        }

        if(position==3){
            supportFingerprint();
        }
        //   break;
        //   default:
        //   break;
        //}
        //showLoadingMessage("", true);
        //NotifySetRequest request = new NotifySetRequest();
        //request.setType(item.getType());
        //request.setPushon(isopen ? "1" : "0");
        //notifySetPresenter.notifySetAction(request);
    }

    @OnClick(R.id.view_back)
    void viewback() {
        finish();
    }

    public boolean supportFingerprint() {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, "您的系统版本过低，不支持指纹功能", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
            FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
            if (!fingerprintManager.isHardwareDetected()) {
                SnackbarUtil.ShortSnackbar(title, "您的手机不支持指纹功能", SnackbarUtil.blue).show();
                return false;
            } else if (!keyguardManager.isKeyguardSecure()) {
                SnackbarUtil.ShortSnackbar(title, "您还未设置锁屏，请先设置锁屏并添加一个指纹", SnackbarUtil.blue).show();
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                SnackbarUtil.ShortSnackbar(title, "您至少需要在系统设置中添加一个指纹", SnackbarUtil.blue).show();
                return false;
            }
        }
        return true;
    }
}
