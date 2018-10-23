package com.kisscigarette.app.httpFrame.net;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * description 测试okhttp请求
 * Created by kisscigarette on 2018-8-29.
 */

public class OkHttpClientInstance {
    private static OkHttpClient instance;

    private OkHttpClientInstance() {}

    public static OkHttpClient getInstance() {
        if (instance == null) {
            synchronized (OkHttpClientInstance.class) {
                if (instance == null) {
                    //配置了网络请求的超时时间
                    instance = createSSlCilent();

                }
            }
        }
        return instance;
    }

    private static OkHttpClient createSSlCilent() {
        HttpLoggingInterceptor LoginInterceptor = new HttpLoggingInterceptor();
        LoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //builder.addInterceptor(new RspCheckInterceptor());
        if (AppConfig.DEBUG) {
            builder.addInterceptor(LoginInterceptor);
        }
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);

        //自动保存session
        /*final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
                Log.e("jsessionid*****>",url.host()+cookies.toString());
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });*/

        builder.retryOnConnectionFailure(true);
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        builder.interceptors().add(new SaveCookiesInterceptor());
        builder.interceptors().add(new ReadCookiesInterceptor());
        // add your other interceptors …
        // add logging as last interceptor
        //OkHttpClient okHttpClient=builder.build();
        //手动设置本地化cookies保存,提交
        //okHttpClient.interceptors().add(new ReadCookiesInterceptor());
        //okHttpClient.interceptors().add(new SaveCookiesInterceptor());
        return builder.build();
    }
}
