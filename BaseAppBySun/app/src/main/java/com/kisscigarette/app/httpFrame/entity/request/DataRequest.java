package com.kisscigarette.app.httpFrame.entity.request;


import com.kisscigarette.app.common.AESUtil;
import com.kisscigarette.app.common.RSAUtil;
import com.kisscigarette.app.common.SHA256Util;
import com.kisscigarette.app.httpFrame.entity.BaseRequest;
import com.kisscigarette.app.httpFrame.net.AppConfig;


/**
 * Created by 孙鹏飞 on 2018/9/1.
 */
public class DataRequest extends BaseRequest {

    private String key;           //经过公钥RSA加密的随机AES秘钥
    private String offset;        //经过公钥RSA加密的随机AES秘钥偏移量
    private String data;          //经过RSA,AES加密后数据

    private String sign;           //对加密后内容进行sha256加密以判断来源正确性


    public DataRequest(String data) {



        // 用接收方提供的公钥加密key和salt，接收方会用对应的私钥解密
        try {
            // 随机生成对称加密的密钥和IV
            String aesKeyWithBase64 = AESUtil.genRandomAesSecretKey();
            String aesIVWithBase64 = AESUtil.genRandomIV();
            this.key = RSAUtil.encryptRSA(AppConfig.getPubKey(), aesKeyWithBase64,RSAUtil.RSA_ALGORITHM);
            this.offset = RSAUtil.encryptRSA(AppConfig.getPubKey(), aesIVWithBase64,RSAUtil.RSA_ALGORITHM);
            this.data=AESUtil.encryptAES(aesKeyWithBase64,aesIVWithBase64,data,AESUtil.AES_ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sign= SHA256Util.getSHA256StrJava(this.key + this.data);
    }

    public DataRequest(String key, String offset, String data, String sign) {
        this.key = key;
        this.offset = offset;
        this.data = data;
        this.sign = sign;
    }
}
