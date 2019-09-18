package x.utils;


import jsa.utils.PropertyUtil;

public class ServerInitConfig {
    private static PropertyUtil init = new PropertyUtil("init");

    public static String getWxsecret() {
        return init.getProperties("wxsecret");
    }

    public static int getprintLog() {
        return init.getPropertieAsInt("printLog");
    }


    public static String getWxappid() {
        return init.getProperties("wxappid");
    }


}
