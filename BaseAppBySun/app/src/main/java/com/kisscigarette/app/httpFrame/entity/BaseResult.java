package com.kisscigarette.app.httpFrame.entity;

import java.io.Serializable;

/**
 * @Description 后台接口返回方法
 * @author  孙鹏飞
 * @date 2018年8月6日
 */
public class BaseResult implements Serializable {
    /*{"errcode":"818",
       "errmsg":"已在android设备:huawei MATE8上登陆",
       "transid":"",
       "pubKey":"151516sdacd",
       "isencrypted":"1"}*/
    protected String errcode;       //错误码
    protected String errmsg;        //错误信息
    protected String transid;       //序列号
    protected String pubKey;        //加密公钥
    protected String isencrypted;   //是否加密

    public BaseResult() {
    }

    public BaseResult(String errcode, String errmsg, String transid, String pubKey, String isencrypted) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.transid = transid;
        this.pubKey = pubKey;
        this.isencrypted = isencrypted;
    }



    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
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




    @Override
    public String toString() {
        return "BaseResult{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", transid='" + transid + '\'' +
                ", pubKey='" + pubKey + '\'' +
                ", isencrypted='" + isencrypted  + '\'' +
                '}';
    }
}
