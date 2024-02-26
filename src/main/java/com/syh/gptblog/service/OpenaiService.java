package com.syh.gptblog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syh.gptblog.api.ApiUtil;
import com.syh.gptblog.domain.Category;
import com.syh.gptblog.domain.Post;
import com.syh.gptblog.domain.PostCategory;
import com.syh.gptblog.dto.OpenApiDto;
import com.syh.gptblog.dto.PostDto;
import com.syh.gptblog.mapper.PostMapper;
import com.syh.gptblog.repository.CategoryRepository;
import com.syh.gptblog.repository.PostCategoryRepository;
import com.syh.gptblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OpenaiService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final ApiUtil apiUtil;

    public PostDto.GptAnswerResponse getAnswerPost(
            PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        // 1.openai 호출을 통해 응답 받아오기(Mono<String)
        OpenApiDto.ApiResponseDto apiResponseDto = apiUtil.getGptAnswer(getGptAnswerRequest);
        log.info(apiResponseDto.toString());

        // 1.1 응답 json -> PostResponseDto 객체 변환
        ObjectMapper objectMapper = new ObjectMapper();
        PostDto.GptAnswerResponse gptAnswerResponse =
                objectMapper.readValue(apiResponseDto.getChoices()
                                                     .get(0)
                                                     .getMessage()
                                                     .getContent(),
                        PostDto.GptAnswerResponse.class);

        // 2. PostResponseDto -> Post
        Post post = PostMapper.INSTANCE.gptResponseDtoToPost(gptAnswerResponse);
        for (String categoryString : gptAnswerResponse.getCategories()) {
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryString);
            PostCategory postCategory = new PostCategory();

            if (!optionalCategory.isEmpty()) {
                postCategory.setCategory(optionalCategory.get());
            } else {
                Category category = new Category();
                category.setName(categoryString);
                categoryRepository.save(category);

                postCategory.setCategory(category);
            }

            postCategory.setPost(post);

            post.getCategories()
                .add(postCategory);
        }

        // 3.해당 응답 게시글 db에 저장하기
        postRepository.save(post);

        gptAnswerResponse.setCreateDate(post.getCreateDate());
        gptAnswerResponse.setUpdateDate(post.getUpdateDate());

        // 4.응답 게시글 내려주기
        return gptAnswerResponse;
    }

    public Post transformPost(String content) {
        Post post = new Post();
        //변환 로직

        return post;
    }
}
