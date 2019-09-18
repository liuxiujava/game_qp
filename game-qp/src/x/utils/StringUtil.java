package x.utils;


import x.utils.commons_lang.StrBuilder;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author JackChu
 * @version 2011-4-6 下午02:22:14
 */
public class StringUtil {

    public static boolean isChinese(char ch) {
        return ch >= '\u4e00' && ch <= '\u9f05';
    }

    public static boolean includeChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAlpha(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    /**
     * 字母成或者数字
     *
     * @param str
     * @return
     * @author JackChu
     * @version 2011-7-6 上午10:15:10
     */
    public static boolean isAlphanumeric(String str) {
        for (char ch : str.toCharArray()) {
            if (!Character.isDigit(ch) && !isAlpha(ch) && ch != '_' && ch != '.' && ch != '@')
                return false;
        }
        return true;
    }


    /**
     * @param str
     * @return
     * @author JackChu
     * @version 2011-3-26 下午01:43:29
     */
    public static boolean isBlank(String str) {
//		for (String str : strs) {
//			if (StringUtils.isBlank(str)) {
//				return true;
//			}
//		}
//		return false;


        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 附加全角空格
     *
     * @param s
     * @return
     * @author JackChu
     * @version 2011-7-25 下午04:47:17
     */
    public static String trim(String s) {
        if (s == null)
            return s;

        int len = s.length();
        int st = 0, ed = len;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (!Character.isWhitespace(ch)) {
                st = i;
                break;
            }
        }

        for (int i = len - 1; i >= 0; i--) {
            char ch = s.charAt(i);
            if (!Character.isWhitespace(ch)) {
                ed = i + 1;
                break;
            }
        }

        return (st > 0 || ed < len) ? s.substring(st, ed) : s;
    }

    /**
     * 深度trim 内部的空格也trim掉
     *
     * @param str
     * @return
     * @author JackChu
     * @version 2011-8-18 下午08:48:10
     */
    public static String deeplyTrim(String str) {
        StringBuilder result = new StringBuilder();
        if (str == null) {
            return str;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (!Character.isWhitespace(ch)) {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * @return
     * @author JackChu
     * @version 2011-11-21 下午03:41:02
     */
    public static boolean isNull(String... strs) {
        for (String str : strs) {
            if (null == str) {
                return true;
            }
        }
        return false;
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 用split 分隔字符串 str 程序自动去掉空白符号
     *
     * @param str   原始字符串
     * @param split 分隔字符串
     * @return 字符串数组
     */
    public static List<String> splitString(String str, String split) {
        if (str == null || str.equals("")) {
            return new ArrayList<>();
        }
        String[] result = null;
        String sPattern = "[\\s]+";
        if (split != null) {
            sPattern = "[\\s" + split + "]+";
            Pattern p = Pattern.compile(sPattern);
            Matcher m = p.matcher(str);
            String strtmp = m.replaceAll(split);
            p = Pattern.compile(split);
            result = p.split(strtmp);
        }
        if (result != null) {
            List<String> test = new ArrayList<>();
            for (String t : result) {
                test.add(t);
            }
            return test;
        }
        return new ArrayList<>();
    }

    /**
     * 将List集合中数据用连接符拼接成字符串
     *
     * @param list  原始字符串.非基本数据类型对象，需重写对象toString方法
     * @param merge 连接字符串
     * @return 字符串数组(字符串末尾保留分隔符)
     */
    public static String mergeString(List list, String merge) {
        String result = "";
        for (Object str : list) {
            result += str + merge;
        }
        return result;
    }

    /**
     * 将List集合中数据用连接符拼接成字符串
     *
     * @param list  原始字符串.非基本数据类型对象，需重写对象toString方法
     * @param merge 连接字符串
     * @return 字符串数组(字符串末尾不保留分隔符)
     */
    public static String mergeString2(List list, String merge) {
        StringBuilder result = new StringBuilder();
        for (Object str : list) {
            result.append(str + merge);
        }
        return result.length() == 0 ? "" : result.substring(0, result.length() - 1);
    }


    /**
     * 将Map对象中的数据用连接符拼接成字符串
     *
     * @return 字符串数组
     */
    public static String mergeString(Map<Integer, Integer> map, String merge1, String merge2) {
        String result = "";
        Set<Integer> key = map.keySet();
        for (Iterator it = key.iterator(); it.hasNext(); ) {
            Integer s = (Integer) it.next();
            result += s;
            result += merge1;
            result += map.get(s);
            result += merge2;
        }
        return result;
    }

    /**
     * 判断字符串是否为数字
     */
    public static Boolean isNumberic(String str) {
        // 正则表达式
        Pattern p = Pattern.compile("[0-9]*");
        return p.matcher(str).matches();
    }

    public static String getFirstName(String name) {
        int index = name.lastIndexOf(".");

        return name.substring(0, index);
    }

    public static String getLastName(String name) {
        int index = name.lastIndexOf(".");

        return name.substring(index + 1);
    }

    public static String getFirstUpper(String lower) {
        String upper = "";

        upper = lower.substring(0, 1).toUpperCase().concat(lower.substring(1));

        if (upper.indexOf("_") >= 0) {
            upper = upper.substring(0, upper.indexOf("_")).concat(getFirstUpper(upper.substring(upper.indexOf("_") + 1, upper.length())));
        }
        return upper;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        int timeToLive = searchList == null ? 0 : searchList.length;
        return replaceEach(text, searchList, replacementList, true, timeToLive);
    }

    private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
        if (text != null && text.length() != 0 && searchList != null && searchList.length != 0 && replacementList != null && replacementList.length != 0) {
            if (timeToLive < 0) {
                throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
            } else {
                int searchLength = searchList.length;
                int replacementLength = replacementList.length;
                if (searchLength != replacementLength) {
                    throw new IllegalArgumentException("Search and Replace array lengths don\'t match: " + searchLength + " vs " + replacementLength);
                } else {
                    boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
                    int textIndex = -1;
                    int replaceIndex = -1;
                    boolean tempIndex = true;

                    int start;
                    int var16;
                    for (start = 0; start < searchLength; ++start) {
                        if (!noMoreMatchesForReplIndex[start] && searchList[start] != null && searchList[start].length() != 0 && replacementList[start] != null) {
                            var16 = text.indexOf(searchList[start]);
                            if (var16 == -1) {
                                noMoreMatchesForReplIndex[start] = true;
                            } else if (textIndex == -1 || var16 < textIndex) {
                                textIndex = var16;
                                replaceIndex = start;
                            }
                        }
                    }

                    if (textIndex == -1) {
                        return text;
                    } else {
                        start = 0;
                        int increase = 0;

                        int textLength;
                        for (int buf = 0; buf < searchList.length; ++buf) {
                            if (searchList[buf] != null && replacementList[buf] != null) {
                                textLength = replacementList[buf].length() - searchList[buf].length();
                                if (textLength > 0) {
                                    increase += 3 * textLength;
                                }
                            }
                        }

                        increase = Math.min(increase, text.length() / 5);
                        StrBuilder var17 = new StrBuilder(text.length() + increase);

                        while (textIndex != -1) {
                            for (textLength = start; textLength < textIndex; ++textLength) {
                                var17.append(text.charAt(textLength));
                            }

                            var17.append(replacementList[replaceIndex]);
                            start = textIndex + searchList[replaceIndex].length();
                            textIndex = -1;
                            replaceIndex = -1;
                            tempIndex = true;

                            for (textLength = 0; textLength < searchLength; ++textLength) {
                                if (!noMoreMatchesForReplIndex[textLength] && searchList[textLength] != null && searchList[textLength].length() != 0 && replacementList[textLength] != null) {
                                    var16 = text.indexOf(searchList[textLength], start);
                                    if (var16 == -1) {
                                        noMoreMatchesForReplIndex[textLength] = true;
                                    } else if (textIndex == -1 || var16 < textIndex) {
                                        textIndex = var16;
                                        replaceIndex = textLength;
                                    }
                                }
                            }
                        }

                        textLength = text.length();

                        for (int result = start; result < textLength; ++result) {
                            var17.append(text.charAt(result));
                        }

                        String var18 = var17.toString();
                        if (!repeat) {
                            return var18;
                        } else {
                            return replaceEach(var18, searchList, replacementList, repeat, timeToLive - 1);
                        }
                    }
                }
            }
        } else {
            return text;
        }
    }

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        } else if (repeat <= 0) {
            return "";
        } else {
            int inputLength = str.length();
            if (repeat != 1 && inputLength != 0) {
                if (inputLength == 1 && repeat <= 8192) {
                    return padding(repeat, str.charAt(0));
                } else {
                    int outputLength = inputLength * repeat;
                    switch (inputLength) {
                        case 1:
                            char ch = str.charAt(0);
                            char[] output1 = new char[outputLength];

                            for (int var11 = repeat - 1; var11 >= 0; --var11) {
                                output1[var11] = ch;
                            }

                            return new String(output1);
                        case 2:
                            char ch0 = str.charAt(0);
                            char ch1 = str.charAt(1);
                            char[] output2 = new char[outputLength];

                            for (int buf = repeat * 2 - 2; buf >= 0; --buf) {
                                output2[buf] = ch0;
                                output2[buf + 1] = ch1;
                                --buf;
                            }

                            return new String(output2);
                        default:
                            StrBuilder var12 = new StrBuilder(outputLength);

                            for (int i = 0; i < repeat; ++i) {
                                var12.append(str);
                            }

                            return var12.toString();
                    }
                }
            } else {
                return str;
            }
        }
    }

    public static String repeat(String str, String separator, int repeat) {
        if (str != null && separator != null) {
            String result = repeat(str + separator, repeat);
            return removeEnd(result, separator);
        } else {
            return repeat(str, repeat);
        }
    }


    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        } else {
            char[] buf = new char[repeat];

            for (int i = 0; i < buf.length; ++i) {
                buf[i] = padChar;
            }

            return new String(buf);
        }
    }

    public static String removeEnd(String str, String remove) {
        return !isEmpty(str) && !isEmpty(remove) ? (str.endsWith(remove) ? str.substring(0, str.length() - remove.length()) : str) : str;
    }

    public static int indexOfIgnoreCase(String str, String searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    public static int indexOfIgnoreCase(String str, String searchStr, int startPos) {
        if (str != null && searchStr != null) {
            if (startPos < 0) {
                startPos = 0;
            }

            int endLimit = str.length() - searchStr.length() + 1;
            if (startPos > endLimit) {
                return -1;
            } else if (searchStr.length() == 0) {
                return startPos;
            } else {
                for (int i = startPos; i < endLimit; ++i) {
                    if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
                        return i;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public static int indexOf(String str, String searchStr, int startPos) {
        return str != null && searchStr != null ? (searchStr.length() == 0 && startPos >= str.length() ? str.length() : str.indexOf(searchStr, startPos)) : -1;
    }

    public static int indexOf(String str, char searchChar) {
        return isEmpty(str) ? -1 : str.indexOf(searchChar);
    }

    public static int indexOf(String str, char searchChar, int startPos) {
        return isEmpty(str) ? -1 : str.indexOf(searchChar, startPos);
    }

    public static int indexOf(String str, String searchStr) {
        return str != null && searchStr != null ? str.indexOf(searchStr) : -1;
    }


    public static String readFileToString(String fileName) {
        String value = "";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                value += tempString;
            }
            reader.close();
        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return value;
    }

    public static String readToString(String fileName) {
        File file = new File(fileName);
        Long filelength = file.length();     //获取文件长度
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return new String(filecontent);//返回文件内容,默认编码
    }

    /**
     * map2合并到map1
     *
     * @param map1
     * @param map2
     */
    public static void mergeMap(Map<Integer, Integer> map1, Map<Integer, Integer> map2) {
        for (Map.Entry<Integer, Integer> entry : map2.entrySet()) {
            if (map1.containsKey(entry.getKey())) {
                map1.put(entry.getKey(), map1.get(entry.getKey()) + entry.getValue());
            } else {
                map1.put(entry.getKey(), entry.getValue());
            }
        }
    }


    /**
     * 检测源字符串中包含匹配字符串的个数
     *
     * @param sourceStr 源字符串
     * @param matchStr  匹配字符串
     * @return 包含字符串的个数
     */
    public static int containStrNum(String sourceStr, String matchStr) {
        int counter = 0;
        if (sourceStr == null || matchStr == null) {
            return counter;
        }
        int fromIndex = sourceStr.indexOf(matchStr, 0);
        while (fromIndex != -1) {
            counter++;
            fromIndex = sourceStr.indexOf(matchStr, fromIndex + matchStr.length());
        }
        return counter;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 分割字符串，封装成Map集合
     * @param splitString : 分割字符串
     * @param firstSplitFlag:第一个分隔符
     * @param secondSplitFlag：第二个分隔符
     * @return Map<Integer,Integer> 结果集
     */
    public static Map<Integer, Integer> splitStringToMap(String splitString, String firstSplitFlag, String secondSplitFlag) {
        List<String> firstSplitStrList = splitString(splitString,firstSplitFlag);
        Map<Integer,Integer> resultMap = new HashMap<>();
        if(StringUtil.isNotBlank(splitString) && StringUtil.isNotBlank(firstSplitFlag)&&StringUtil.isNotBlank(secondSplitFlag)){
            for(String firstTempStr:firstSplitStrList){
                List<String> secondTempStrList = splitString(firstTempStr,secondSplitFlag);
                if(secondTempStrList.size() == 2){
                    resultMap.put(Integer.parseInt(secondTempStrList.get(0)),Integer.parseInt(secondTempStrList.get(1)));
                }
            }
        }
        return resultMap;
    }
}
