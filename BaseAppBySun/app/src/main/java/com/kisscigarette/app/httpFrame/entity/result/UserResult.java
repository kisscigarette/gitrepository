package com.kisscigarette.app.httpFrame.entity.result;


import com.kisscigarette.app.httpFrame.entity.BaseResult;

/**
 * Created by cm on 2016/7/25.
 */
public class UserResult extends BaseResult {

    private String username;

    private String headImage;
    private String address;

    public UserResult(String username, String headImage) {
        this.username = username;
        this.headImage = headImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
