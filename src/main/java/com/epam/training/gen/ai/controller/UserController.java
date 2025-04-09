package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatRequestDto;
import com.epam.training.gen.ai.dto.OpenAiModel;
import com.epam.training.gen.ai.dto.RequestDto;
import com.epam.training.gen.ai.service.DeploymentService;
import com.epam.training.gen.ai.service.UserPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserPromptService userPromptService;
    private final DeploymentService deploymentService;

    @PostMapping("/prompt-response")
    public ResponseEntity<String> getPromptResponse(@RequestBody RequestDto requestDto) {
        return ResponseEntity.ok(userPromptService.getPromptResponse(requestDto.getInput()));
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chatWithHistory(@RequestBody ChatRequestDto requestDto) {
        return ResponseEntity.ok(userPromptService.getPromptResponse(requestDto));
    }

    @GetMapping("open/ai/models")
    public ResponseEntity<List<OpenAiModel>> getModels() {
        return ResponseEntity.ok(deploymentService.getModels());
    }
}
