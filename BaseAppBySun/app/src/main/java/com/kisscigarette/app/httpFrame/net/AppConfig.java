package com.kisscigarette.app.httpFrame.net;


import com.kisscigarette.app.common.RSAUtil;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
/**
 * @author 孙鹏飞
 * @date 2018/02/01
 */
public class AppConfig {
    private static PublicKey publicKey;
    public static final String PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyzpc1LOsWOiyP+f8QgBDw7E/k40PXtUtf9p0we6uFKO9Seeri5HYeAm7aEy50LZHkvjR0CXQkHQfl0wYt50BFp1lsIOtVeMA/GPrELnGpdhavhtlID0EOC9R7RuwRc9le1WgzhCS4VzIrEyVA1ZAqxNSCdbZv3fgu2SRbBgsWv/tKX55b8uLtdl5zBq8Jo+KBPkY60tjtBBJwwkPdp121NT8xQRpNgGMSXEjRgye+AJA9IGi5bHYxfaeznxAfjnvEE1AKP0/KetxdkMh+MLZGf06bfAHDEwQuQKmlXxG8As+/z3EK8OCLsFgclbhr2hDjEGmdT4pllnQ91fii9SiHwIDAQAB";
    public static final String HTTP_RESPONSE_ERROR_SUCCESS = "200";//服务器异常
    public static final String HTTP_RESPONSE_ERROR_CODE = "500";//服务器异常
    public static final String HTTP_RESPONSE_ERROR_CODE_RELOGIN = "514";//会话超时
    public static final String HTTP_RESPONSE_ERROR_CODE_HOLD = "818";   //设备被占用
    public static final String HTTP_RESPONSE_ERROR_CODE_LOCK = "616";   //用户被锁
    public static final String HTTP_RESPONSE_ERROR_CODE_NEWDEV = "717";   //新设备
    public static final boolean DEBUG = true;
    //登陆内网服务端
    //public static final String BASE_URL = "http://61.136.27.61:9000/ies/mobile/rest/test/";
    //public static final String BASE_URL = SharePreferencesUtility.get(SharePreferencesUtility.NETWORK_NODE,"http://192.168.177.127:9000/");
    //登陆内网服务端
    //public static final String BASE_URL = "https://192.168.43.5:8443/SSMspf/interface/";
    public static final String BASE_URL = "http://116.62.232.65/SSMspf/interface/";

    public AppConfig() {
    }
    //王宗山电脑后台接口
    // public static final String BASE_URL = "http://192.168.43.5:8089/SSMspf/";


    public static PublicKey getPubKey() {
        if (publicKey == null) {
            try {
                publicKey = RSAUtil.getRSAPublicKeyByBase64String(PUBKEY);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return publicKey;

        } else {
            return publicKey;
        }
    }

}
