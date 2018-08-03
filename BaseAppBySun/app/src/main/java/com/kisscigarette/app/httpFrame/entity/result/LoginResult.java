package com.kisscigarette.app.httpFrame.entity.result;


import com.kisscigarette.app.httpFrame.entity.BaseResult;

/**
 * Created by Administrator on 2017/4/14.
 */
public class LoginResult extends BaseResult {

    /**
     * operId : 129
     * phone : 15005155167
     * desc : null
     * roleName : 店长
     * parentId : 128
     * operName : 许菲
     */

    private String cloudSessionId;



    private DataBean data;
    /**
     * data : {"operId":"129","phone":"15005155167","desc":null,"roleName":"店长","parentId":"128","operName":"许菲"}
     * sessionid : 15005155167_1492496099238_sams123456
     */

    private String sessionid;
    /**
     * allowSetICCard : 1
     * allowSetFingerprint : 1
     * allowSetPassword : 0
     * allowSetBlekey : 0
     */



    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }


    public static class DataBean {
        private String operId;
        private String phone;
        private Object desc;
        private String roleName;
        private String parentId;
        private String operName;
        private AuthDeviceBean authDevice;

        public AuthDeviceBean getAuthDevice() {
            return authDevice;
        }

        public void setAuthDevice(AuthDeviceBean authDevice) {
            this.authDevice = authDevice;
        }


        public String getOperId() {
            return operId;
        }

        public void setOperId(String operId) {
            this.operId = operId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getDesc() {
            return desc;
        }

        public void setDesc(Object desc) {
            this.desc = desc;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getOperName() {
            return operName;
        }

        public void setOperName(String operName) {
            this.operName = operName;
        }
    }

    public String getCloudSessionId() {
        return cloudSessionId;
    }

    public void setCloudSessionId(String cloudSessionId) {
        this.cloudSessionId = cloudSessionId;
    }

    public static class AuthDeviceBean {
        private String allowSetICCard;
        private String allowSetFingerprint;
        private String allowSetPassword;
        private String allowSetBlekey;

        public String getAllowSetICCard() {
            return allowSetICCard;
        }

        public void setAllowSetICCard(String allowSetICCard) {
            this.allowSetICCard = allowSetICCard;
        }

        public String getAllowSetFingerprint() {
            return allowSetFingerprint;
        }

        public void setAllowSetFingerprint(String allowSetFingerprint) {
            this.allowSetFingerprint = allowSetFingerprint;
        }

        public String getAllowSetPassword() {
            return allowSetPassword;
        }

        public void setAllowSetPassword(String allowSetPassword) {
            this.allowSetPassword = allowSetPassword;
        }

        public String getAllowSetBlekey() {
            return allowSetBlekey;
        }

        public void setAllowSetBlekey(String allowSetBlekey) {
            this.allowSetBlekey = allowSetBlekey;
        }
    }
}
