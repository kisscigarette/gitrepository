package com.kisscigarette.app.httpFrame.api;


import com.kisscigarette.app.httpFrame.entity.request.LoginRequest;
import com.kisscigarette.app.httpFrame.entity.request.UserRequest;
import com.kisscigarette.app.httpFrame.entity.result.DataResult;
import com.kisscigarette.app.httpFrame.entity.result.UserResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
/**
 * @author 孙鹏飞
 * @date 2018/01/31
 */
public interface UserManagerApi {
    @FormUrlEncoded
    @POST("downHead?cmdid=1001")
    Observable<UserResult> downUser(@Header("md5") String md5, @Field("username") String username);

    @POST("updateHead?cmdid=1001")
    Observable<DataResult> updateUser(@Header("md5") String md5, @Body UserRequest baseRequest);

    @POST("loginRegister?cmdid=1001")
    Observable<DataResult> loginRegister(@Header("md5") String md5, @Body LoginRequest baseRequest);


}
