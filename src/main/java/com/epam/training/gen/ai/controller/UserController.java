package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatRequestDto;
import com.epam.training.gen.ai.dto.RequestDto;
import com.epam.training.gen.ai.service.UserPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserPromptService userPromptService;

    @PostMapping("/prompt-response")
    public ResponseEntity<String> getPromptResponse(@RequestBody RequestDto requestDto) {
        return ResponseEntity.ok(userPromptService.getPromptResponse(requestDto.getInput()));
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chatWithHistory(@RequestBody ChatRequestDto requestDto) {
        return ResponseEntity.ok(userPromptService.getPromptResponse(requestDto));
    }
}
