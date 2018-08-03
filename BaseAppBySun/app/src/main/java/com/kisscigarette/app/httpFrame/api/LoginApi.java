package com.kisscigarette.app.httpFrame.api;


import com.kisscigarette.app.httpFrame.entity.request.LoginRequest;
import com.kisscigarette.app.httpFrame.entity.result.LoginResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * @author 孙鹏飞
 * @date 2018/01/31
 */
public interface LoginApi {
    @FormUrlEncoded
    @POST
    Observable<LoginResult> loginReq(@Url String url, @Field("un") String un, @Field("pd") String pd);

    @POST("getLogin?cmdid=5485")
    Observable<LoginResult> loginReq2(@Header("md5") String md5, @Body LoginRequest baseRequest);

    @GET("ies/mobile/rest/test/getUserInfor")
    Observable<Object> getUserInfo(@Query("login_name") String userId);
}
