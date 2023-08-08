  $(document).ready(function(){

    $.ajax({
        url : 'http://localhost:8083/jsonapi',
        type : 'GET',
        dataType : 'text', //리턴타입 지정
        beforeSend : function(jqXHR){ // 서버 요청 전 호출되는 함수 return, false 일 경우 요청을 중단
        console.log("ajax 실행");
        },
        success : function(data){
        console.log("호출 성공");
        console.log(JSON.parse(data));
        },
        error : function(jqXHR){
       console.log("호출 실패");
       }

    });
    });