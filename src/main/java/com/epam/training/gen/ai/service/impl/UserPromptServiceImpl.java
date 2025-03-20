package com.epam.training.gen.ai.service.impl;

import com.epam.training.gen.ai.dto.ChatRequestDto;
import com.epam.training.gen.ai.service.UserPromptService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPromptServiceImpl implements UserPromptService {

    private final Kernel kernel;
    private ChatHistory chatHistory = new ChatHistory();

    @Override
    public String getPromptResponse(String userPrompt) {
        log.debug("Calling chatbot with prompt ::{}", userPrompt);
        var chatBotResponse = kernel.invokePromptAsync(userPrompt).block().getResult().toString();
        log.debug("Chatbot response ::{}", chatBotResponse);
        return chatBotResponse;
    }

    @Override
    public String getPromptResponse(ChatRequestDto requestDto) {
        var response = kernel.invokeAsync(getChat())
                .withArguments(getKernelFunctionArguments(requestDto.getPrompt()))
                .withPromptExecutionSettings(
                        PromptExecutionSettings.builder()
                                .withTemperature(requestDto.getTemperature())
                                .withMaxTokens(requestDto.getMaxTokens())
                                .withStopSequences(requestDto.getStopSequence())
                                .build())
                .block();
        chatHistory.addUserMessage(requestDto.getPrompt());
        var responseResult = response.getResult();
        log.info("Assistant response : {}", responseResult);
        chatHistory.addAssistantMessage(responseResult);
        return responseResult;
    }

    private KernelFunction<String> getChat() {
        return KernelFunction.<String>createFromPrompt("""
                        {{$chatHistory}}
                        <message role="user">{{$request}}</message>""")
                .build();
    }

    private KernelFunctionArguments getKernelFunctionArguments(String prompt) {
        return KernelFunctionArguments.builder()
                .withVariable("request", prompt)
                .withVariable("chatHistory", chatHistory)
                .build();
    }

}
