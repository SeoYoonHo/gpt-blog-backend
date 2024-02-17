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

@Slf4j
@Component
public class ApiUtil {
    private static RestTemplate restTemplate;

    // OpenAI API 엔드포인트 URL 설정
    private static String apiUrl = "https://api.openai.com/v1/chat/completions";

    // OpenAI API 키 설정
    private static String apiKey = "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";

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
        inputText = inputText + ". 응답 문장을 내용을 요약한 title과, 내용부분인 content를 나눠줘.'title: 내용, content: 내용' 형식으로 구성해서 " +
                "보내줘";

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // HTTP 요청 바디 설정
        OpenApiDto.OpenAIRequest requestBody = new OpenApiDto.OpenAIRequest();
        requestBody.setModel("gpt-3.5-turbo");
        requestBody.setMaxTokens(2000); // 응답의 길이를 늘리기 위해 max_tokens 값을 증가
        requestBody.setTemperature(0.7); // 응답의 창의성을 위해 temperature 값을 설정
    // requestBody.setTopP(1.0); // 필요에 따라 설정

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

    public static void getGptAnswerByWebClient(PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // WebClient 인스턴스 생성
        WebClient webClient = WebClient.builder()
                                       .baseUrl("https://api.openai.com/v1/chat/completions")
                                       .defaultHeader("Authorization", "Bearer " + apiKey) // API 키 설정
                                       .defaultHeader("Content-Type", "application/json")
                                       .build();

        // HTTP 요청 바디 설정
        OpenApiDto.OpenAIRequest requestBody = new OpenApiDto.OpenAIRequest();
        requestBody.setModel("gpt-3.5-turbo-0125");
        requestBody.setStream(true);
        requestBody.setMaxTokens(2000); // 응답의 길이를 늘리기 위해 max_tokens 값을 증가
        requestBody.setTemperature(0.7); // 응답의 창의성을 위해 temperature 값을 설정
//        requestBody.setTopP(1.0); // 필요에 따라 설정

        OpenApiDto.OpenAIMessage[] messages = new OpenApiDto.OpenAIMessage[2];
        messages[0] = new OpenApiDto.OpenAIMessage();
        messages[0].setRole("system");
        messages[0].setContent("응답 문장을 내용을 요약한 title과, 내용부분인 content를 나눈 json 형식으로 구성해줘");

        messages[1] = new OpenApiDto.OpenAIMessage();
        messages[1].setRole("user");
        messages[1].setContent(getGptAnswerRequest.getContents());
        requestBody.setMessages(messages);

        String requestBodyJson = mapper.writeValueAsString(requestBody);

        // 요청 보내기
        Flux<String> responseFlux = webClient.post() // HTTP POST 요청
                                             .bodyValue(requestBodyJson) // 이미 준비된 JSON 요청 바디 사용
                                             .retrieve() // 응답 받기
                                             .bodyToFlux(String.class); // 응답을 String으로 변환

        // 응답 처리
        responseFlux.subscribe(
                responseBody -> {
                    System.out.println(responseBody);
//                    try {
//                        OpenApiDto.ApiResponseDto responseDto = mapper.readValue(responseBody,
//                                OpenApiDto.ApiResponseDto.class);
//                        System.out.println(responseDto.getChoices().get(0).getMessage().getContent());
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
                }, // 스트림의 각 아이템 처리
                error -> error.printStackTrace(), // 에러 처리
                () -> System.out.println("Stream completed")); // 스트림 완료 시 처리
    }
}
