package com.kisscigarette.app.ui.person;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.BaseActivity;
import com.kisscigarette.app.common.SystemUtil;
import com.kisscigarette.app.ui.widget.AlertDialog;
import com.kisscigarette.app.utils.SnackbarUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 常见问题
 * Created by ted on 2016/1/27.
 */
public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView title;

    @BindView(R.id.tv_cur_ver)
    TextView curver;

    private AlertDialog phoneDialog;


    /**
     * 处理页面跳转请求和数据
     *
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
        context.startActivity(intent,compat.toBundle());
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
        return R.layout.activity_about_us;
    }

    @Override
    protected void init() {
        title.setText("关于");
        curver.setText("客户端版本：V" + SystemUtil.getVersionName());
    }

    @OnClick(R.id.lay_url)
    void layurl() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://www.naritech.cn");
        intent.setData(content_url);
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent,compat.toBundle());
    }
    @OnClick(R.id.lay_tel)
    void laytel() {
        showSureCallDialog();
    }
    @OnClick(R.id.lay_qq)
    void layqq() {
        copy("2744450873", this);
        SnackbarUtil.ShortSnackbar(curver,"QQ号已复制到粘贴板,请打开QQ添加",SnackbarUtil.Info).show();
        //Toast.makeText(this, "复制售后QQ成功", Toast.LENGTH_SHORT).show();
    }


    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 拨打电话提示框
     */
    private void showSureCallDialog() {
        if (phoneDialog == null) {
            phoneDialog = new AlertDialog(this).builder()
                    .setMsg("是否拨打电话:400-111-5670")
                    .setCancelable(true)
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            phoneDialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定",0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            phoneDialog.dismiss();
                            checkpermission();
                        }
                    });
        }

        phoneDialog.show();
    }

    private void checkpermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.PROCESS_OUTGOING_CALLS},
                        0);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:18851812340"));
                ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(intent,compat.toBundle());
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:18851812340"));
            ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent,compat.toBundle());
        }
    }

    @OnClick(R.id.view_back)
    void viewback() {
        finish();
    }
}
