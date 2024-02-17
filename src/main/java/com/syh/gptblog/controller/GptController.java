package com.syh.gptblog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syh.gptblog.dto.PostDto;
import com.syh.gptblog.service.OpenaiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class GptController {

    private final OpenaiService openaiService;

    @PostMapping("/question")
    public void answerPost(@RequestBody PostDto.GetGptAnswerRequest getGptAnswerRequest) throws JsonProcessingException {
        openaiService.getAnswerPost(getGptAnswerRequest);
//        return
    }
}
