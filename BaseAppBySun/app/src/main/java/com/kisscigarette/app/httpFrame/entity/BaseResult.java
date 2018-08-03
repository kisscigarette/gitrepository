package com.kisscigarette.app.httpFrame.entity;

import java.io.Serializable;

/**
 * Created by ted on 2016/7/26.
 */
public class BaseResult implements Serializable {

    protected String errcode;      //错误码
    protected String errmsg;       //错误信息
    protected String transid;      //序列号
    protected String md5;          //加密秘钥
    protected String isencrypted;  //是否加密

    protected String deviceid;
    protected String devicetype;   //设备类型

    public BaseResult() {
    }

    public BaseResult(String errcode, String errmsg, String transid, String md5, String isencrypted) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.transid = transid;
        this.md5 = md5;
        this.isencrypted = isencrypted;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


    public String getIsencrypted() {
        return isencrypted;
    }

    public void setIsencrypted(String isencrypted) {
        this.isencrypted = isencrypted;
    }


    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", transid='" + transid + '\'' +
                ", md5='" + md5 + '\'' +
                ", isencrypted='" + isencrypted + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", devicetype='" + devicetype + '\'' +
                '}';
    }
}
