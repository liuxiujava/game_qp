package x.utils;

import jsa.utils.RandomUtil;

import java.util.*;

public class GenerateCodeUtil {
    private static final String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private static final String[] arr1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public static void main(String[] args) {
        List pswdList = new ArrayList();
        for (int i = 0; i < 100000; i++) {
            String aa = getInvitationCode();
            if (pswdList.contains(aa)) {
                System.out.println("" + aa);
            }
            pswdList.add(aa);
        }
        for (int i = 0; i < pswdList.size(); i++) {
            System.out.println(pswdList.get(i));
        }

        System.out.println();
    }

    public static String getInvitationCode() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            b.append(String.valueOf(arr[RandomUtil.getNexInt(arr.length)]));
        }
        return b.toString();
    }

    public static String getCode() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            b.append(String.valueOf(arr1[RandomUtil.getNexInt(arr1.length)]));
        }
        return b.toString();
    }


}

