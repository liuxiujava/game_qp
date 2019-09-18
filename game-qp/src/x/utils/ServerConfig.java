package x.utils;


import jsa.utils.PropertyUtil;

/**
 * Created by admin on 2014/9/4.
 */
public class ServerConfig {
    private static final PropertyUtil a = new PropertyUtil("server");

    private static String b = null;
    private static String c = null;

    public static int getPort() {
        return a.getPropertieAsInt("port");
    }

    public static int getBizThreads() {
        return a.getPropertieAsInt("bizthreads");
    }

    public static int getIoThreads() {
        return a.getPropertieAsInt("iothreads");
    }

    public static int getMaxOnline() {
        return a.getPropertieAsInt("online_max");
    }

    public static String getBasicDataPath() {
        return a.getProperties("basic_data_path");
    }

    public static String getBasicDataPackage() {
        return a.getProperties("basic_data_package");
    }

    public static String getServerKey() {
        if (b == null) {
            b = a.getProperties("server_key");
        }
        return b;
    }

    public static String getPrivateKey() {
        if (c == null) {
            c = a.getProperties("private_key");
        }
        return c;
    }
}
