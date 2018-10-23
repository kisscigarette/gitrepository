package com.kisscigarette.app.ui.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 常见问题
 * Created by ted on 2016/1/27.
 */
public class FaqActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView title;

    @BindView(R.id.wv_faq)
    WebView webView;

    /**
     * 处理页面跳转请求和数据
     *
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, FaqActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        //ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
        context.startActivity(intent);
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
        return R.layout.activity_faq;
    }

    @Override
    protected void init() {
        title.setText("常见问题");
        initWebView();
    }


    private void initWebView() {

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webView.requestFocusFromTouch();
        String url = "https://192.168.43.5:8443/SSMspf/img/file.html";
        String url1="file:///android_asset/file/file.html";
        String url2 = "http://www.sigsmart.com.cn/slwechat/siglife_toc/faq.html";

        /**
         * 这里需要根据不同的分辨率设置不同的比例,比如
         * 5寸手机设置190  屏幕宽度 > 650   180
         * 4.5寸手机设置170  屏幕宽度>  500 小于 650  160
         * 4寸手机设置150  屏幕宽度>  450 小于 550  150
         * 3           屏幕宽度>  300 小于 450  120
         * 小于    300  100
         *  320×480  480×800 540×960 720×1280
         */
        /*WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if(width > 650)
        {
            this.webView.setInitialScale(190);
        }else if(width > 520)
        {
            this.webView.setInitialScale(160);
        }else if(width > 450)
        {
            this.webView.setInitialScale(140);
        }else if(width > 300)
        {
            this.webView.setInitialScale(120);
        }else
        {
            this.webView.setInitialScale(100);
        }*/

        webView.loadUrl(url2);

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return true;
    }

    @OnClick(R.id.view_back)
    void viewback() {
        finish();
    }
}
