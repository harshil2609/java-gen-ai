package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.ChatRequestDto;

public interface UserPromptService {

    String getPromptResponse(String userPrompt);

    String getPromptResponse(ChatRequestDto chatRequestDto);
}
