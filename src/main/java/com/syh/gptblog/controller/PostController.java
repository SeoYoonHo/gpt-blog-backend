package com.syh.gptblog.controller;

import com.syh.gptblog.common.CommonResponse;
import com.syh.gptblog.dto.PostDto;
import com.syh.gptblog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/searchList")
    public ResponseEntity<CommonResponse.DataResponse<Page<PostDto.PostResponse>>> getAllPost(@PageableDefault Pageable pageable) {
        Page<PostDto.PostResponse> postResponses = postService.findPostList(pageable);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("001", "Success", postResponses));
    }
}
