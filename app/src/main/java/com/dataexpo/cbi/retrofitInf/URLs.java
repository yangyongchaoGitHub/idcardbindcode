package com.dataexpo.cbi.retrofitInf;

public class URLs {
    //public static final String baseUrl = "https://auth.dataexpo.com.cn/";
    public static final String baseUrl = "http://192.168.0.109:8090/";

    //private static final String postfix = ".do";

    // 登录
    //public static final String loginUrl = "login/login2m" + postfix;
    public static final String VerifyExpo = "gate/verifyExpoById";


    //根据eucode获取用户数据
    public static final String queryUserInfoUrl = "gate/findUserInfoByFileCode";

    //绑定身份证到用户信息
    public static final String bindIdCardToUserUrl = "gate/bindIdCardToUser";

    //上传一个门禁签到数据
    public static final String uploadOneSignInUrl = "gate/uploadOneSignIn";


}
