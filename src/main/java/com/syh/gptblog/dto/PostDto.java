package com.syh.gptblog.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDto {
    @Data
    public static class GptAnswerResponse {
        private String slug;
        private String title;
        private List<String> categories = new ArrayList<>();
        private String cover;
        private String date;
        private String published;
        private String lastEditedAt;
        private String blurUrl;
        private String content;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
    }

    @Data
    public static class GetGptAnswerRequest {
        private String contents;
    }

    @Data
    public static class PostResponse {
        private String slug;
        private String title;
        private String cover;
        private String date;
        private String published;
        private String lastEditedAt;
        private String blurUrl;
        private String content;
        private List<String> categoryList;
    }

}
