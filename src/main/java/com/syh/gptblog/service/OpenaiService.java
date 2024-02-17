package com.syh.gptblog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syh.gptblog.dto.OpenApiDto;
import com.syh.gptblog.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenaiService {
    private final RestTemplate restTemplate;
    public void getAnswerPost(PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        log.info(getGptAnswerRequest.toString());
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

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        // OpenAI API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // 응답 출력
        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);
    }
}
