package com.insenuser.utils;

import java.util.Random;

/**
 * Description:随机工具类
 *
 * @author kjy
 * @since Apr 5, 2020 9:47:14 AM
 */
public class RandomUtils {

    /**
     * 随机算法
     * 
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
