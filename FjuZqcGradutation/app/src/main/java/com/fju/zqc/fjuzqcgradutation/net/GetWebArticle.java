package com.fju.zqc.fjuzqcgradutation.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zqc on 2015/9/9.
 */
public class GetWebArticle {
    public static String getWebArticle(String htmlUrl){
        try{
           // Log.d("qqqq","HtmlUrl"+htmlUrl);
            URL url=new URL(htmlUrl);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream(),"gb2312");
            BufferedReader reader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();
            String content;
            while ((content=reader.readLine())!=null){
                stringBuffer.append(content);

            }
           // Log.d("qqqq", stringBuffer.toString() + "\n");
            return stringBuffer.toString();

        }catch (Exception e){
           // Log.d("qqqq","Errormessage"+e.getMessage());
            return "Errormessage"+e.getMessage();

        }

    }
}
