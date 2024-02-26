package com.syh.gptblog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
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
            private Message message;
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
    @ToString
    public static class OpenAIRequest {
        @JsonProperty("model")
        private String model;
        @JsonProperty("messages")
        private List<Message> messages = new ArrayList<>();
    }

    @Getter
    @Setter
    @ToString
    public static class Message{
        @JsonProperty("role")
        private String role;
        @JsonProperty("content")
        private String content;
    }


}
