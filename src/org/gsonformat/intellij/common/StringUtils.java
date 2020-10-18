package org.gsonformat.intellij.common;

import org.apache.http.util.TextUtils;

/**
 * Created by dim on 16/11/5.
 */
public class StringUtils {

    /**
     * 转成驼峰
     *
     * @param text
     * @return
     */
    public static String captureStringLeaveUnderscore(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text = text.trim();

        if (text.equals(text.toUpperCase())) { // 如果全是大写
            text = text.toLowerCase();//转成小写
        }

        String temp = text.replaceAll("^_+", "");
        if (!TextUtils.isEmpty(temp)) {
//            // 转换类似这样的字段USER_NAME->user_name
//            int indexOf = temp.indexOf("_");
//            if (indexOf > 0){
//                temp = temp.toLowerCase();
//            }

            text = temp;
        } else {
            return temp;
        }

        String[] strings = text.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            stringBuilder.append(captureName(strings[i]));
        }
        return stringBuilder.toString();
    }

    /**
     * 转成驼峰
     *
     * @param text
     * @return
     */
    public static String captureClassNameLeaveUnderscore(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }

        text = text.trim();

        // 判断是否是全部大写
        if (text.equals(text.toUpperCase())) {
            return text;
        }

        String temp = text.replaceAll("^_+", "");
        if (!TextUtils.isEmpty(temp)) {
            // 转换类似这样的字段USER_NAME->user_name
            int indexOf = temp.indexOf("_");
            if (indexOf > 0) {
                temp = temp.toLowerCase();
            }

            text = temp;
        } else {
            return temp;
        }

        String[] strings = text.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            stringBuilder.append(captureName(strings[i]));
        }
        return stringBuilder.toString();
    }

    public static String captureName(String text) {

        if (text.length() > 0) {
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        return text;
    }

    public static String getPackage(String generateClassName) {
        int index = generateClassName.lastIndexOf(".");
        if (index > 0) {
            return generateClassName.substring(0, index);
        }
        return null;
    }

}
