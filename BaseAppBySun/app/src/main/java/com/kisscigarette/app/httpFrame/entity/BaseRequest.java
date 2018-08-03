package com.kisscigarette.app.httpFrame.entity;


/**
 * Created by ted on 2016/7/26.
 */
public abstract class BaseRequest {
    protected String appid = null;
    protected String cmdid = null;
    protected String cmdtype = null;
    protected String placecode;
    protected String isencrypted ="0";
    protected String authid = null;
    protected String transid = null;
    protected String sourceid = null ;
    protected String source = "app";
    protected String operId;

    public BaseRequest() {
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

    public String getCmdtype() {
        return cmdtype;
    }

    public void setCmdtype(String cmdtype) {
        this.cmdtype = cmdtype;
    }

    public String getPlacecode() {
        return placecode;
    }

    public void setPlacecode(String placecode) {
        this.placecode = placecode;
    }

    public String getIsencrypted() {
        return isencrypted;
    }

    public void setIsencrypted(String isencrypted) {
        this.isencrypted = isencrypted;
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

    public String getSourceid() {
        return sourceid;
    }

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }
}
