package com.kisscigarette.app.httpFrame.entity.request;


import com.kisscigarette.app.httpFrame.entity.BaseRequest;

/**
 * Created by cm on 2016/7/25.
 */
public class LoginRequest extends BaseRequest {


    public final static String AUTO_LOGIN_TAG = "1";
    public final static String MANUL_LOGIN_TAG = "0";
    private String pd;
    private String un;
    private String model;
    private String model_type;
    private String location;
    //默认为0，表示不是自动登录；1表示自动登录。
    private String is_auto_login;

    public LoginRequest(String passwd, String loginname, boolean isauto) {
        super();
        this.pd = passwd;
        this.un = loginname;
        model_type = "0";
        location = "";
        is_auto_login = isauto?AUTO_LOGIN_TAG:MANUL_LOGIN_TAG;
    }



}
