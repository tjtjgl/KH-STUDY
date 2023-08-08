package com.shspring.openAPI.controller;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class RestKHController {


    @GetMapping("/tourstninfo")
    public String openapiJson() {
        StringBuilder result = new StringBuilder(); // StringBuffer 대신 StringBuilder를 사용합니다. (싱글 스레드 애플리케이션에 더 효율적입니다.)

        try {
            String apiUrl = "http://apis.data.go.kr/B550928/dissForecastInfoSvc?ServiceKey=dDcKGqUegZQZTNhkt2IcMHcb1oNGg6NZREyF6OES%2FS0SPwT8SqzxwhN5VRZLYYFRmmoLqhMtuhzNAarAGy0Z%2BQ%3D%3D" +
                    "&dissCd=1"+
                    "&znCd=11";
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));

            String returnLine;

            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine).append("\n");
            }

            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString(); // StringBuilder를 String으로 변환하여 JSON 응답으로 반환합니다.
    }


}
