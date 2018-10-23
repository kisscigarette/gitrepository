package com.kisscigarette.app.httpFrame.entity.request;


import com.kisscigarette.app.httpFrame.entity.BaseRequest;

/**
 * Created by cm on 2016/7/25.
 */
public class UserRequest extends BaseRequest {

    private String username;
    private String headImage;

    public UserRequest(String username, String headImage) {
        this.username = username;
        this.headImage = headImage;
    }


}
