package com.syh.gptblog.service;

import com.syh.gptblog.domain.Post;
import com.syh.gptblog.dto.PostDto;
import com.syh.gptblog.mapper.PostMapper;
import com.syh.gptblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public Page<PostDto.PostResponse> findPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostDto.PostResponse> postResponses = posts.map(PostMapper.INSTANCE::postToPostResponseDto);

        return postResponses;
    }
}
