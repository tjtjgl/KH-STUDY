package com.shspring.openAPI.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/*
공공 데이터를 가져올 떼 데이터를 주고 받는 내용이 있기 때문에(json,xml) 그냥 controller 말고 RestController
*/
@RestController
public class RestTestController {

    /*
    서버 실행하면
    웹 브라우저에서 url을 작성하고 엔터 누르면
    디스팩처 서블릿이 모든 url을 받는다
    전송할 때 겟/포스트 구분해서 매핑
    openapi 메소드가 실행

    공공데이터를 실행하기 위해 필요한 작업
    1) https://www.data.go.kr/ 가입 후 나만의 고유키 발급
       인증키 : dDcKGqUegZQZTNhkt2IcMHcb1oNGg6NZREyF6OES%2FS0SPwT8SqzxwhN5VRZLYYFRmmoLqhMtuhzNAarAGy0Z%2BQ%3D%3D
       요청 URL : http://apis.data.go.kr/6260000/FoodService/getFoodKr
    2)



    * */


    @GetMapping("apitest")
    public String openapi() {
        StringBuffer result = new StringBuffer();
        int pageNo = 1;

        try {
            // 필수요소와 URL을 저장
            String apiUrl = "http://apis.data.go.kr/6260000/FoodService/getFoodKr?" +
                    "serviceKey=dDcKGqUegZQZTNhkt2IcMHcb1oNGg6NZREyF6OES%2FS0SPwT8SqzxwhN5VRZLYYFRmmoLqhMtuhzNAarAGy0Z%2BQ%3D%3D" +
                    "&numOfRows=10" +
                    "&pageNo" + pageNo +
                    "&resultType=xml";
            // URL 객체 생성 클래스
            // 문자열이 지정하는 자원에 대한 url 객체를 생성해 준다.
            // -> 프로토콜(통신방법), 호스트 주소, 포트 번호, 파일 이름을 포함시켜서 객체 생성
            URL url = new URL(apiUrl);

            // url 내용을 읽어오거나 url에 get, post 방식으로 데이터를 전달할 때 사용
            // 웹페이지나 서블릿에 데이터를 전달 할 수 있다.
            // 프로토콜이 http 인 경우 반환된 객체를 httpURLCOnnection 객체로 캐스팅 할 수 있다
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 실제 데이터 전송을 할 때 전송 방식
            urlConnection.setRequestMethod("GET");

            // 실제 연결
//            urlConnection.connect();

            /*
            응답
            [Stream]
            - 데이터가 연속적으로 존재한다는 것을 표현한 객체
            - byte로 데이터를 전달하기 때문에 스트림도 byte의 연속된 집합
            - 사용자의 키보드 입력, 파일 데이터, http 송수신 데이터 등이 모두 스트림
            - InputStream
                - 자바 프로그램 안으로 데이터를 가져온다
                - 1 byte의 int 형의 아스키코드값으로 값을 저장
                **위처럼 하면 느리니까 filterStream 중 bufferStream으로 중간에 임시 저장 후 한번에 가져오기
            - InputStreamReader
                - byte 대신 char 형태로 읽을 수 있게 아스키코드가 아닌 문자열로 출력 가능
                - String 객체로 변환할 수도 있게 된다
                - InputStream 인자로 받아와서 만들어짐
            */

            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());

            //실제 응답받은 데이터를 읽기
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));

            String returnLine;
            //문자,숫자,태그소스 모두 그대로 출력할 수 있게 도와주는 태그
            result.append("<xmp>");
            //데이터 응답 받아서 안에 있는 내용 꺼내야 함, 없으면 null 반환
            //한줄씩 읽기 readLine()
            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine + "\n");
            }



            // url 연결 닫기
            urlConnection.disconnect();

        } catch (Exception e) {
            System.out.println(e);
        }


        return result + "</xmp>";
    }

    @GetMapping("/jsonapi")
    public String openapiJson() {

        // url 세팅
        StringBuffer result = new StringBuffer();
        int pageNo = 1;
        // try안쪽에 변수선언하면 데이터가 try문과 함꼐 사라지는 지역변수!
        String jsonPrintString = null;

        try {
            String apiUrl = "http://apis.data.go.kr/6260000/FoodService/getFoodKr?"
                    + "serviceKey=aCz34R3ycz%2B0IcuBdueR1Qzo7wICjHTJowOpM9iFLFXvD5K718SqKMB34EP9zkf%2ByDq0pKCI1L5FIaI8Mzf78A%3D%3D"
                    + "&numOfRows=10" + "&pageNo=" + pageNo;

            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // 연결
            urlConnection.connect();

            BufferedInputStream bufferedInputStream =
                    new BufferedInputStream(urlConnection.getInputStream());

            // 인코딩 같이함
            // 중간에 데이터를 임시저장공간인 버퍼에 저장한다.
            // 저장한 내용을 한꺼번에 가지고 들어온다.
            // 1byte 가져오면 속도가 느리고 데이터의 용량이 크면 시간이 꾀 오래걸린다.

            BufferedReader bufferedReader =
                    new BufferedReader(
                            new InputStreamReader(bufferedInputStream, "UTF-8")
                    );

            String returnLine;

            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine + "\n");
            }
            // json
            // json 파일을 스프링부트에서 사용할 수 있도록 특정값을 가지고 오는
            // 내용을 작성!

            // Jsonparser 객체의 도움을 받는다.
            // 1. Jsonparser 객체 생성
            // 2. reader 를 이용해서 json 파일을 읽어온다.
            // 3. Array  json코드가 [] 감싸고 있을 경우 List형식으로 index값으로
            //   불러온다.

            // 4. Object json 코드가 {}로 감싸고 있는 경우
            //     Key : Value 형식으로 저장되어있는 값을 불러온다. map형식

            JSONObject jsonObject = XML.toJSONObject(result.toString());
            jsonPrintString = jsonObject.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonPrintString;
    }


}
