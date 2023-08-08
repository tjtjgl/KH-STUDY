package com.shspring.openAPI;

// 네이버 검색 API 예제 - 블로그 검색

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class ApiExamSearchBlog {

    public static void main(String[] args) {
        String clientId = "rp0qhnstCHF0a7osdA7L"; //애플리케이션 클라이언트 아이디
        String clientSecret = "Todam3ptSm"; //애플리케이션 클라이언트 시크릿


        String text = null; //검색어 저장 변수
        try {
            // URL 뒤에 붙여 보내기 전 인코딩 필요
            text = URLEncoder.encode("날씨", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }


        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;    // JSON 결과 - 뉴스
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // XML 결과 - 블로그

        //요청하기 전 어떤 id로 접속할지 헤더파일을 만들어 저장
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL, requestHeaders);


//        System.out.println(responseBody);

        //json 파싱

        // JSONObject : Map 형식으로 데이터가 감싸져 있을 경우 {}
        // JSONArray : 배열 형식으로 데이터가 감싸져 있을 경우 []
        // 가장 큰 JsonObject을 가져온다
        JSONObject jObject = new JSONObject(responseBody);

        // items 키를 이용하여 배열 가져오기
        JSONArray jArray = jObject.getJSONArray("items");

        for (Object o : jArray) {
            JSONObject jobj = (JSONObject) o;
            String title = jobj.getString("title");
            String originallink = jobj.getString("originallink");
            String description = jobj.getString("description");
            String pubDate = jobj.getString("pubDate");

            System.out.println("===========================");
            System.out.println("title = " + title);
            System.out.println("originallink = " + originallink);
            System.out.println("description = " + description);
            System.out.println("pubDate = " + pubDate);
        }
    }


    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET"); //전송방식을 설정하는 메소드
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            // 응답받음
            // 실제 전송이 잘 되어 응답이 온다면 getResponseCode()이 200을 전송

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출 (responseCode가 200이냐?)
                return readBody(con.getInputStream()); // 응답받은 객체 가져오기
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {

            // String 타입으로 데이터를 붙이면 다른 객체가 되므로(불변) 새로 생성, 새로운 메모리가 계속 생겨남
            // StringBuilder는 변경 가능한 타입. 만들어 놓은 객체 추가/삭제/수정 가능
            StringBuilder responseBody = new StringBuilder();

            String line;

            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}

