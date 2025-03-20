package com.epam.training.gen.ai.service.impl;

import com.epam.training.gen.ai.service.UserPromptService;
import com.microsoft.semantickernel.Kernel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPromptServiceImpl implements UserPromptService {

    private final Kernel kernel;

    @Override
    public String getPromptResponse(String userPrompt) {
        log.debug("Calling chatbot with prompt ::{}", userPrompt);
        var chatBotResponse = kernel.invokePromptAsync(userPrompt).block().getResult().toString();
        log.debug("Chatbot response ::{}", chatBotResponse);
        return chatBotResponse;
    }

}
