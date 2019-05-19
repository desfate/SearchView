package com.defate.searchview;

import java.util.ArrayList;
import java.util.List;

public class PinYinUtils {
    /**
     * 单个汉字转成ASCII码
     */
    private static int getASCII(String chs) {
        int asc = 0;
        try {
            byte[] bytes = chs.getBytes("gb2312");
            if (bytes.length > 2 || bytes.length <= 0) {
                throw new RuntimeException("illegal resource string");
            }
            if (bytes.length == 1) {
                asc = bytes[0];
            }
            if (bytes.length == 2) {
                int heightByte = 256 + bytes[0];
                int lowByte = 256 + bytes[1];
                asc = (256 * heightByte + lowByte) - 256 * 256;
            }
        } catch (Exception e) {
            System.out.println("ERROR:ChineseSpelling.class-char2Ascii(String chs)" + e);
        }
        return asc;
    }

    /**
     * 单个汉字转换成拼音
     **/
    private static String getSinglePinYin(String str) {
        String result = null;
        int ascii = getASCII(str);
        if (ascii > 0 && ascii < 160) {
            result = String.valueOf((char) ascii);
        } else {
            for (int i = (PinYinValues.sPinYinValue.length - 1); i >= 0; i--) {
                if (PinYinValues.sPinYinValue[i] <= ascii) {
                    result = PinYinValues.sPinYinStr[i];
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 中文转换成拼音，返回结果是list
     *
     * @param source 原始字符
     * @return 中国->["zhong", "guo"]
     */
    public static List<String> getPinYinList(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        List<String> pinyinList = new ArrayList<>();
        for (int i = 0; i < source.length(); i++) {
            String item = source.substring(i, i + 1);
            if (item.getBytes().length >= 2) {
                String pinyin = getSinglePinYin(item);
                if (pinyin == null) {
                    pinyin = item;
                }
                pinyinList.add(pinyin);
            } else {
                pinyinList.add(item);
            }
        }
        return pinyinList;
    }

    /**
     * 中文转换成拼音
     *
     * @param source 原始字符
     * @return 中国->"zhongguo"
     */
    public static String getPinYin(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        StringBuilder pinyinList = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            String item = source.substring(i, i + 1);
            if (item.getBytes().length >= 2) {
                String pinyin = getSinglePinYin(item);
                if (pinyin == null) {
                    pinyin = item;
                }
                pinyinList.append(pinyin);
            } else {
                pinyinList.append(item);
            }
        }
        return pinyinList.toString();
    }
}
