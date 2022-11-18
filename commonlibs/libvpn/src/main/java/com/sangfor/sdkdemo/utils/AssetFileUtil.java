package com.sangfor.sdkdemo.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetFileUtil {

    /**
     * 获取assert 目录下某个文件内容
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssertFileContent(Context context, String fileName){
        StringBuffer stringBuffer = new StringBuffer();

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            // 分行读取
            while ((line = buffreader.readLine()) != null) {
                stringBuffer.append(line);
            }
            buffreader.close();
            inputreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuffer.toString();
    }

}
