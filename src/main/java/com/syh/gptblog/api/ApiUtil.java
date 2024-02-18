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
        requestBody.setModel("gpt-3.5-turbo-instruct");
        requestBody.setStream(false);
        requestBody.setMaxTokens(1000); // 응답의 길이를 늘리기 위해 max_tokens 값을 증가
        requestBody.setTemperature(0); // 응답의 창의성을 위해 temperature 값을 설정
        requestBody.setPrompt(inputText);

        String requestBodyJson = mapper.writeValueAsString(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        // OpenAI API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
                String.class);

        // 응답 출력
        String responseBody = responseEntity.getBody();
        return mapper.readValue(responseBody, OpenApiDto.ApiResponseDto.class);
    }

    public Mono<String> getGptAnswerByWebClient(
            PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // WebClient 인스턴스 생성
        WebClient webClient = WebClient.builder()
                                       .baseUrl(apiUrl)
                                       .defaultHeader("Authorization", "Bearer " + apiKey) // API 키 설정
                                       .defaultHeader("Content-Type", "application/json")
                                       .build();

        // 요청문장 조립
        String inputText = getGptAnswerRequest.getContents();

        // HTTP 요청 바디 설정
        OpenApiDto.OpenAIRequest requestBody = new OpenApiDto.OpenAIRequest();
        requestBody.setModel("gpt-3.5-turbo-instruct");
        requestBody.setStream(true);
        requestBody.setMaxTokens(1000); // 응답의 길이를 늘리기 위해 max_tokens 값을 증가
        requestBody.setTemperature(0); // 응답의 창의성을 위해 temperature 값을 설정
        requestBody.setPrompt(inputText);

        String requestBodyJson = mapper.writeValueAsString(requestBody);

        // 요청 보내기
        Mono<String> responseMono = webClient.post() // HTTP POST 요청
                                             .bodyValue(requestBodyJson) // 이미 준비된 JSON 요청 바디 사용
                                             .retrieve() // 응답 받기
                                             .bodyToFlux(String.class)
                                             .collectList()
                                             .map(this::processData);

        return responseMono;
    }

    private String processData(List<String> dataList) {
        dataList.remove(dataList.size() - 1);
        StringBuilder sb = new StringBuilder();
        try {
            for (String str : dataList) {
                OpenApiDto.ApiResponseDto apiResponseDto = mapper.readValue(str, OpenApiDto.ApiResponseDto.class);

                sb.append(apiResponseDto.getChoices()
                                        .get(0)
                                        .getText());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }
}
