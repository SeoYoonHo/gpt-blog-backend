package com.syh.gptblog.dto;

import lombok.Data;

public class PostDto {
    @Data
    public static class PostResponse {
        private Long id;
        private String contents;
        private Integer cnt;
        private String email;
    }

    @Data
    public static class GetGptAnswerRequest {
        private String contents;
    }



    @Data
    public static class CreatePostRequest {
        private String contents;
    }

    @Data
    public static class UpdatePostRequest {
        private Long id;
        private String contents;
    }

    @Data
    public static class DeletePostRequest {
        private Long id;
    }
}
