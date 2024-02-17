package com.syh.gptblog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syh.gptblog.api.ApiUtil;
import com.syh.gptblog.domain.Post;
import com.syh.gptblog.dto.OpenApiDto;
import com.syh.gptblog.dto.PostDto;
import com.syh.gptblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OpenaiService {
    private final PostRepository postRepository;

    public PostDto.GptAnswerResponse getAnswerPost(
            PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        // 1.openai 호출을 통해 응답 받아오기(Mono<String)
        OpenApiDto.ApiResponseDto apiResponseDto = ApiUtil.getGptAnswer(getGptAnswerRequest);
        System.out.println(apiResponseDto);

        // 1.1 응답 json -> Post 객체 변환
        Post post = new Post();
        post.setContent(apiResponseDto.getChoices().get(0).getText());

        // 2.해당 응답 게시글 db에 저장하기
        postRepository.save(post);

        // 3.응답 게시글 내려주기
        PostDto.GptAnswerResponse gptAnswerResponse = new PostDto.GptAnswerResponse();

        // 3.1. Post -> PostResponseDto

        return gptAnswerResponse;
    }

    public Post transformPost(String content) {
        Post post = new Post();
        //변환 로직

        return post;
    }
}
