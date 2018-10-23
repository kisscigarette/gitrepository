package com.kisscigarette.app.httpFrame.entity;


import com.kisscigarette.app.common.SystemUtil;

/**
 * @Description 请求体抽象方法封装,适用ios,webkit,web
 * @author  孙鹏飞
 * @date 2018年8月6日
 */
public abstract class BaseRequest {
    /*{"devId":"818",
       "devInfo":"Product: BKL-AL00, DEVICE: HWBKL, CPU_ABI: arm64-v8a, MANUFACTURER: HUAWEI, BRAND: HONOR, BOARD: BKL, MODEL: BKL-AL00, SDK: 26, USER: andorid, VERSION.RELEASE: 8.0.0, DISPLAY: BKL-AL00 8.0.0.172(C00), FINGERPRINT: HONOR/BKL-AL00/HWBKL:8.0.0/HUAWEIBKL-AL00/172(C00):user/release-keys, ID: HUAWEIBKL-AL00",
       "devName":"综合能源 HUAWEIBKL-AL00",
       "isencrypted":"0",
       "source":"app",
       "transid":"423F88B4B388A946994682E27C8CD7D7"}*/
    protected String isencrypted ="0";                       //是否加密
    protected String appid = null;                           //预留字段
    protected String cmdid = null;                           //预留字段
    protected String authid = null;                          //用户id
    protected String transid = SystemUtil.Alias();           //序列号,设备唯一标示,有硬件信息和imei和mac联合MD5加密生成
    protected String devInfo = SystemUtil.getDeviceInfo() ;  //设备信息
    protected String devName = SystemUtil.deviceName() ;     //设备名称
    protected String source = "app";//ios,webkit,web         //请求类型,便于后台区分拦截app,ios,web

    public BaseRequest() {
    }

    public BaseRequest(String isencrypted, String appid, String cmdid, String authid, String transid, String devInfo, String devName, String source) {
        this.isencrypted = isencrypted;
        this.appid = appid;
        this.cmdid = cmdid;
        this.authid = authid;
        this.transid = transid;
        this.devInfo = devInfo;
        this.devName = devName;
        this.source = source;
    }

    public String getIsencrypted() {
        return isencrypted;
    }

    public void setIsencrypted(String isencrypted) {
        this.isencrypted = isencrypted;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getCmdid() {
        return cmdid;
    }

    public void setCmdid(String cmdid) {
        this.cmdid = cmdid;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(String devInfo) {
        this.devInfo = devInfo;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
