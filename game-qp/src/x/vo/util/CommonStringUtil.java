package x.vo.util;

import java.util.ArrayList;
import java.util.List;

public class CommonStringUtil {

    public static List<CommonInfo> splitString(String str) {
        List<CommonInfo> list = new ArrayList<>();
        if (str == null || str.equals("") || str.equals("0")) {
            return list;
        }
        String[] result = str.split(";");
        for (String s : result) {
            String[] r = s.split(",");
            list.add(new CommonInfo(Integer.parseInt(r[0]), Integer.parseInt(r[1])));
        }
        return list;
    }

    public static String mergeString(List<CommonInfo> list) {
        StringBuilder result = new StringBuilder();
        for (CommonInfo str : list) {
            result.append(str.getId()).append(",");
            result.append(str.getValue()).append(";");
        }
        return result.toString();
    }

    public static String mergeString(CommonInfo commonInfo) {
        StringBuilder result = new StringBuilder();
        result.append(commonInfo.getId()).append(",");
        result.append(commonInfo.getValue()).append(";");
        return result.toString();
    }
}
