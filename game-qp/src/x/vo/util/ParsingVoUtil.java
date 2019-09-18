package x.vo.util;

import x.vo.RankInfo;
import x.vo.WinInfo;

import java.util.ArrayList;
import java.util.List;

public class ParsingVoUtil {


    public static List<RankInfo> splitString(String str) {
        List<RankInfo> list = new ArrayList<>();
        if (str == null || str.equals("") || str.equals("0")) {
            return list;
        }
        String[] result = str.split(";");
        for (String s : result) {
            String[] r = s.split(",");
            RankInfo rankInfo = new RankInfo(r[0], r[1]);
            if (r.length > 2) {
                rankInfo.setResult(r[2]);
            }
            list.add(rankInfo);
        }
        return list;
    }

    public static String mergeString(List<RankInfo> list) {
        StringBuilder result = new StringBuilder();
        for (RankInfo str : list) {
            result.append(str.getUserName()).append(",");
            result.append(str.getPtId()).append(",");
            result.append(str.getResult()).append(";");
        }
        return result.toString();
    }

    public static List<WinInfo> splitWinString(String str) {
        List<WinInfo> list = new ArrayList<>();
        if (str == null || str.equals("") || str.equals("0")) {
            return list;
        }
        String[] result = str.split(";");
        for (String s : result) {
            String[] r = s.split(",");
            list.add(new WinInfo(r[0], Long.parseLong(r[1])));
        }
        return list;
    }

    public static String mergeWinString(List<WinInfo> list) {
        StringBuilder result = new StringBuilder();
        for (WinInfo str : list) {
            result.append(str.getPtId()).append(",");
            result.append(str.getGameTime()).append(";");
        }
        return result.toString();
    }


}
