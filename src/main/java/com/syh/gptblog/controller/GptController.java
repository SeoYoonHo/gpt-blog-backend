package com.syh.gptblog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syh.gptblog.common.CommonResponse;
import com.syh.gptblog.dto.PostDto;
import com.syh.gptblog.service.OpenaiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class GptController {

    private final OpenaiService openaiService;

    @PostMapping("/question")
    @CrossOrigin
    public ResponseEntity<CommonResponse.DataResponse<PostDto.GptAnswerResponse>> answerPost(@RequestBody PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        PostDto.GptAnswerResponse answerPost = openaiService.getAnswerPost(getGptAnswerRequest);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("001", "Success", answerPost));
    }
}
