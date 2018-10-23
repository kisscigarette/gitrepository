package com.kisscigarette.app.httpFrame.entity.result;

import android.text.TextUtils;

import com.kisscigarette.app.httpFrame.entity.BaseResult;

import java.util.List;


/**
 * Created by Administrator on 2017/9/25.
 */
public class NotifyListResult extends BaseResult {

    /**
     * pushon : 1
     * items : [{"type":"1","pushon":"1","info":"低电量告警"},{"type":"129","pushon":"1","info":"开门提醒"}]
     */

    private String pushon;
    /**
     * type : 1
     * pushon : 1
     * info : 低电量告警
     */

    private List<ItemsBean> items;

    public String getPushon() {
        return pushon;
    }

    public void setPushon(String pushon) {
        this.pushon = pushon;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        private int type;
        private String pushon;
        private String info;

        public ItemsBean() {
        }

        public ItemsBean(int type, String pushon, String info) {
            this.type = type;
            this.pushon = pushon;
            this.info = info;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPushon() {
            return pushon;
        }

        public void setPushon(String pushon) {
            this.pushon = pushon;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }


        public boolean isOpen() {
            if (!TextUtils.isEmpty(pushon) && pushon.equals("1")) {
                return true;
            }else{
                return false;
            }

        }
    }
}
