package com.syh.gptblog.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syh.gptblog.dto.OpenApiDto;
import com.syh.gptblog.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiUtil {
    private final RestTemplate restTemplate;
    private ObjectMapper mapper =  new ObjectMapper();

    // OpenAI API 엔드포인트 URL 설정
    @Value("${gpt.url}")
    private String apiUrl;

    // OpenAI API 키 설정
    @Value("${gpt.apiKey}")
    private String apiKey;

    public OpenApiDto.ApiResponseDto getGptAnswer(
            PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // 요청문장 조립
        String inputText = getGptAnswerRequest.getContents();

        // HTTP 요청 바디 설정
        OpenApiDto.OpenAIRequest requestBody = new OpenApiDto.OpenAIRequest();
        requestBody.setModel("gpt-3.5-turbo");

        OpenApiDto.Message message1 = new OpenApiDto.Message();
        String requestFormat = ", 원하는 응답 형태 : {\"slug\":\"슬러그명\",\"title\":\"제목\",\"categories\":카테고리 리스트," +
                "\"cover\":\"커버 이미지 url\",\"date\":\"작성날짜\",\"published\":\"게시 여부(true or false)\"," +
                "\"lastEditedAt\":\"마지막 수정날짜 timestamp\",\"blurUrl\":\"blurImageUrl\",\"content\":\"포스트 내용\"}, 그리고 문자열을 java 코드내에서 사용할 수 있게 적절한 이스케이프 처리를 해줘";
        message1.setRole("system");
        message1.setContent(requestFormat);

        OpenApiDto.Message message2 = new OpenApiDto.Message();
        message2.setRole("user");
        message2.setContent(inputText);

        requestBody.getMessages().add(message1);
        requestBody.getMessages().add(message2);

        String requestBodyJson = mapper.writeValueAsString(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        // OpenAI API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
                String.class);

        // 응답 출력
        String responseBody = responseEntity.getBody();
        return mapper.readValue(responseBody, OpenApiDto.ApiResponseDto.class);
    }
}
