package com.kisscigarette.app.httpFrame.net;

import android.preference.PreferenceManager;

import com.kisscigarette.app.common.MyApplication;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by kisscigarette on 2018-2-2.
 */

public class SaveCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit()
                    .putStringSet("cookies", cookies)
                    .apply();
        }

        return originalResponse;
    }
}
