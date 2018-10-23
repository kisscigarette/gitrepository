package com.kisscigarette.app.httpFrame.entity.request;


import com.kisscigarette.app.httpFrame.entity.BaseRequest;

/**
 * Created by cm on 2016/7/25.
 */
public class LoginRequest extends BaseRequest {


    public final static String AUTO_LOGIN_TAG = "1";
    public final static String MANUL_LOGIN_TAG = "0";
    private String passwd;
    private String username;
    private String model;
    private String model_type;
    private String location;
    //默认为0，表示不是自动登录；1表示自动登录。
    private String is_auto_login;

    public LoginRequest(String loginname, String passwd, boolean isauto) {
        super();
        this.passwd = passwd;
        this.username = loginname;
        model_type = "0";
        location = "";
        is_auto_login = isauto?AUTO_LOGIN_TAG:MANUL_LOGIN_TAG;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel_type() {
        return model_type;
    }

    public void setModel_type(String model_type) {
        this.model_type = model_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIs_auto_login() {
        return is_auto_login;
    }

    public void setIs_auto_login(String is_auto_login) {
        this.is_auto_login = is_auto_login;
    }
}
