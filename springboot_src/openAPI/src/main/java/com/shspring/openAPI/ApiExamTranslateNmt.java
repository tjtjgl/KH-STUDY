package com.shspring.openAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// 네이버 기계번역 (Papago SMT) API 예제
public class ApiExamTranslateNmt {

    public static void main(String[] args) {
        String clientId = "rp0qhnstCHF0a7osdA7L"; //애플리케이션 클라이언트 아이디
        String clientSecret = "Todam3ptSm"; //애플리케이션 클라이언트 시크릿

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;

        System.out.println("번역할 문장을 입력하세요> ");
        Scanner sc = new Scanner(System.in);
        String content = sc.nextLine();



        try {
            text = URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text);

        //System.out.println(responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        //System.out.println(jsonObject);

        JSONObject jres = jsonObject.getJSONObject("message").getJSONObject("result");

//        System.out.println("jres = " + jres);

        String translatedText = jres.getString("translatedText");
        String srcLangType = jres.getString("srcLangType");
        String engineType = jres.getString("engineType");

        System.out.println("translatedText = " + translatedText);
        System.out.println("srcLangType = " + srcLangType);
        System.out.println("engineType = " + engineType);

//        for (Object o : jArray) {
//            JSONObject res = (JSONObject) o;
//
//        }

    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}

