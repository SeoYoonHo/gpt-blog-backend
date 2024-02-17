package com.syh.gptblog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syh.gptblog.api.ApiUtil;
import com.syh.gptblog.dto.OpenApiDto;
import com.syh.gptblog.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenaiService {

    public PostDto.GptAnswerResponse getAnswerPost(PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        // 1.openai 호출을 통해 응답 받아오기
        OpenApiDto.ApiResponseDto apiResponseDto = ApiUtil.getGptAnswer(getGptAnswerRequest);
        log.info(apiResponseDto.toString());

        // 1.1 응답 json -> Post 객체 변환

        // 2.해당 응답 게시글 db에 저장하기

        // 3.응답 게시글 내려주기
        return null;
    }
}
