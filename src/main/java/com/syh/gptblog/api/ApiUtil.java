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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class ApiUtil {
    private static RestTemplate restTemplate;

    // OpenAI API 엔드포인트 URL 설정
    private static String apiUrl = "https://api.openai.com/v1/completions";

    // OpenAI API 키 설정
    private static String apiKey = "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";

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ApiUtil(RestTemplate restTemplate) {
        ApiUtil.restTemplate = restTemplate;
    }

    public static OpenApiDto.ApiResponseDto getGptAnswer(PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
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
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // 응답 출력
        String responseBody = responseEntity.getBody();
        return mapper.readValue(responseBody, OpenApiDto.ApiResponseDto.class);
    }

    public static Mono<String> getGptAnswerByWebClient(
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
                                             .map(ApiUtil::processData);

        return responseMono;
    }

    private static String processData(List<String> dataList) {
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
