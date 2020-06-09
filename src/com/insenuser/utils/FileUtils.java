package com.insenuser.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:读取文件
 *
 * @author kjy
 * @since Apr 5, 2020 9:49:58 AM
 */
public class FileUtils {
    /**
     * 读取txt文件
     * 
     * @param filePath
     * @return
     */
    public static List<String> readTxtFile(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        List<String> strList = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr = null;
            // 一次读一行，读入null时文件结束
            while ((tempStr = reader.readLine()) != null) {
                strList.add(tempStr);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return strList;
    }

}