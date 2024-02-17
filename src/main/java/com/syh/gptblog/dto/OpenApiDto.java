package com.syh.gptblog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class OpenApiDto {

    @Getter
    @Setter
    public static class OpenAIRequest {
        @JsonProperty("model")
        private String model;

        @JsonProperty("messages")
        private OpenAIMessage[] messages;
    }

    @Getter
    @Setter
    public static class OpenAIMessage {
        @JsonProperty("role")
        private String role;

        @JsonProperty("content")
        private String content;

        // 생성자, 게터, 세터 등이 있어야 합니다.
    }
}
