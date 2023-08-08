package com.shspring.openAPI.naverai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class Clova_Sentiment {

    @GetMapping("/")
    public String sentiment(){
        StringBuilder response = new StringBuilder();

        //ID,Secret Key
        String clientID = "fzvnjqv0cs";
        String clientSecret="DIF8oSalVCUOJev9BwimDBE3SVzLYdGnjuMweyDj";

        try{
            String content ="{\"content\": \"싸늘하다. 가슴에 비수가 날아와 꽂힌다.\"}";

            String apiURL="https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";
            URL url =  new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID",clientID);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY",clientSecret);
            conn.setRequestProperty("Content-Type","application/json");

            String postParams = "content=" + content;

            //urlconnection이 서버에 데이터를 보내는데 사용할 수 있는지 여부를 설정
            conn.setDoOutput(true);
            //urlconnection이 서버에서 콘텐츠를 읽는데 사용할 수 있는지 여부를 설정
            conn.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);

            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if(responseCode == 200){
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else{ //에러 발생 시
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            String inputLine;
            while ((inputLine= br.readLine()) != null){
                response.append(inputLine);
            }

            br.close();
            System.out.println(response.toString());


        }catch (Exception e){

        }

        return response.toString();
    }

    //가져온 문자열 대치
    private static String removeTag(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }
        text = text.replaceAll("<br>", "\n");
        text = text.replaceAll("&gt;", ">");
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&quot;", "");
        text = text.replaceAll("&nbsp;", " ");
        text = text.replaceAll("&amp;", "&");
        text = text.replaceAll("&apos;", "'");
        return text.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""); // 특수문자 제거
    }


}
