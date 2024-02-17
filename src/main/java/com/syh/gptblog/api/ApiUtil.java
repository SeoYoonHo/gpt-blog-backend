package com.syh.gptblog.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syh.gptblog.dto.OpenApiDto;
import com.syh.gptblog.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ApiUtil {
    private static RestTemplate restTemplate;

    @Autowired
    public ApiUtil(RestTemplate restTemplate){
        ApiUtil.restTemplate = restTemplate;
    }

    public static OpenApiDto.ApiResponseDto getGptAnswer(PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // OpenAI API 엔드포인트 URL 설정
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // OpenAI API 키 설정
        String apiKey = "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";

        // 입력 텍스트 설정
        String inputText = getGptAnswerRequest.getContents();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // HTTP 요청 바디 설정
        OpenApiDto.OpenAIRequest requestBody = new OpenApiDto.OpenAIRequest();
        requestBody.setModel("gpt-3.5-turbo");

        OpenApiDto.OpenAIMessage[] messages = new OpenApiDto.OpenAIMessage[1];
        messages[0] = new OpenApiDto.OpenAIMessage();
        messages[0].setRole("system");
        messages[0].setContent(inputText);
        requestBody.setMessages(messages);

        String requestBodyJson = mapper.writeValueAsString(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        // OpenAI API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // 응답 출력
        String responseBody = responseEntity.getBody();
        return mapper.readValue(responseBody, OpenApiDto.ApiResponseDto.class);
    }
}
