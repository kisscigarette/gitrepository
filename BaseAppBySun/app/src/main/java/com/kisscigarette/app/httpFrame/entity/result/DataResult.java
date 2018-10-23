package com.kisscigarette.app.httpFrame.entity.result;


import com.google.gson.annotations.SerializedName;
import com.kisscigarette.app.httpFrame.entity.BaseResult;

/**
 * Created by Administrator on 2017/4/14.
 */
public class DataResult extends BaseResult {

    //序列化的时候转化为dev_id,反序列化的时候dev_id,deviceid,device_id都转化为devId
    //@Expose
    @SerializedName(value = "dev_id", alternate = { "devicesid", "device_id" })
    private String devId;



    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }



}
