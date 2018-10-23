package com.kisscigarette.app.httpFrame.api;


import com.kisscigarette.app.httpFrame.entity.BaseResult;
import com.kisscigarette.app.httpFrame.entity.request.DataRequest;
import com.kisscigarette.app.httpFrame.entity.request.LoginRequest;
import com.kisscigarette.app.httpFrame.entity.result.DataResult;
import com.kisscigarette.app.httpFrame.entity.result.LoginResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


/**
 * @author 孙鹏飞
 * @date 2018/01/31
 */
public interface LoginApi {
    @POST("loginValid?cmdid=1001")
    Observable<LoginResult> loginReq(@Header("md5") String md5,@Body LoginRequest baseRequest);

    @POST("searchGrid?cmdid=1001")
    Observable<DataResult> loginReq2(@Header("md5") String md5, @Body DataRequest baseRequest);

    @POST("logout?cmdid=1003")
    Observable<BaseResult> logout(@Header("md5") String md5);
}
