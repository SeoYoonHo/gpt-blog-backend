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
        private String system_fingerprint;
        private Usage usage;

        // Getters and Setters
        @Getter
        @Setter
        @ToString
        public static class Choice {
            private int index;
            private String text;
            private Object logprobs; // 실제 타입에 맞게 변경 필요
            private String finish_reason;

            // Getters and Setters
        }

        @Getter
        @Setter
        @ToString
        public static class Usage {
            private int prompt_tokens;
            private int completion_tokens;
            private int total_tokens;
        }
    }

    @Getter
    @Setter
    public static class OpenAIRequest {
        @JsonProperty("model")
        private String model;
        @JsonProperty("prompt")
        private String prompt;
        @JsonProperty("max_tokens")
        private int maxTokens;
        @JsonProperty("temperature")
        private double temperature;
        @JsonProperty("stream")
        private boolean stream;
    }
}
