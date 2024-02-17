package com.syh.gptblog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class OpenApiDto {

    @Getter
    @Setter
    @ToString
    public static class ApiResponseDto {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<Choice> choices;
        private Usage usage;
        private String system_fingerprint;

        // Getters and Setters
        @Getter
        @Setter
        @ToString
        public static class Choice {
            private int index;
            private Message message;
            private Object logprobs; // 실제 타입에 맞게 변경 필요
            private String finish_reason;

            // Getters and Setters
        }
        @Getter
        @Setter
        @ToString
        public static class Message {
            private String role;
            private String content;

            // Getters and Setters
        }
        @Getter
        @Setter
        @ToString
        public static class Usage {
            private int prompt_tokens;
            private int completion_tokens;
            private int total_tokens;

            // Getters and Setters
        }
    }

    @Getter
    @Setter
    public static class OpenAIRequest {
        @JsonProperty("model")
        private String model;

        @JsonProperty("messages")
        private OpenAIMessage[] messages;

        @JsonProperty("max_tokens")
        private int maxTokens;
        @JsonProperty("temperature")
        private double temperature;
        @JsonProperty("top_p")
        private double topP;
        @JsonProperty("stream")
        private boolean stream;
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
