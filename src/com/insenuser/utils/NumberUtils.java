package com.insenuser.utils;

/**
 * Description:数学工具类
 *
 * @author kjy
 * @since Apr 21, 2020 5:09:39 PM
 */
public class NumberUtils {
    /**
     * 比较2个整形数字中的最小值
     * 
     * @param one
     * @param two
     * @return
     */
    public static int getMin(int min, int max) {
        if (max < min) {
            return max;
        }
        return min;
    }

    /**
     * 比较2个字符串数字中的最大值
     * 
     * @param minStr
     * @param maxStr
     * @return
     */
    public static String getStrMax(String minStr, String maxStr) {
        float max = Float.valueOf(maxStr);
        float min = Float.valueOf(minStr);
        if (max < min) {
            return minStr;
        }
        return maxStr;
    }

    /**
     * 除法，保留2位数并转为String
     * 
     * @param front
     * @param back
     * @return
     */
    public static String division(int front, int back) {
        float result = (float) front / back;
        String resultStr = String.format("%.2f", result);
        return resultStr;
    }

    /**
     * 除法，保留2位数并转为String
     * 
     * @param front
     * @param back
     * @return
     */
    public static String addStr(String front, String back) {
        float result = Float.valueOf(front) + Float.valueOf(back);
        String resultStr = String.format("%.2f", result);
        return resultStr;
    }

    /**
     * 减法
     * 
     * @param front
     * @param back
     * @return
     */
    public static String minusStr(String front, String back) {
        float result = Float.valueOf(front) - Float.valueOf(back);
        String resultStr = String.format("%.2f", result);
        return resultStr;
    }

    /**
     * 除法
     * 
     * @param front
     * @param back
     * @return
     */
    public static String divisionStr(String front, String back) {
        float result = Float.valueOf(front) / Float.valueOf(back);
        String resultStr = String.format("%.2f", result);
        return resultStr;
    }

    public static String divisionStr2(String front, String back) {
        float result = Float.valueOf(front) / Float.valueOf(back);
        String resultStr = String.format("%.4f", result);
        return resultStr;
    }
}
