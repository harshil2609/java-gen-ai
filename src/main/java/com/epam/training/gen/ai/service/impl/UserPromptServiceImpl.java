package com.epam.training.gen.ai.service.impl;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.dto.ChatRequestDto;
import com.epam.training.gen.ai.service.UserPromptService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPromptServiceImpl implements UserPromptService {

    public static final String CALLING_CHATBOT_WITH_PROMPT = "Calling chatbot with prompt ::{}";
    public static final String CHATBOT_RESPONSE = "Chatbot response ::{}";
    private final Kernel kernel;
    private final OpenAIAsyncClient openAIAsyncClient;
    private ChatHistory chatHistory = new ChatHistory();

    @Override
    public String getPromptResponse(String userPrompt) {
        log.debug(CALLING_CHATBOT_WITH_PROMPT, userPrompt);
        var chatBotResponse = kernel.invokePromptAsync(userPrompt).block().getResult().toString();
        log.debug(CHATBOT_RESPONSE, chatBotResponse);
        return chatBotResponse;
    }

    @Override
    public String getPromptResponse(ChatRequestDto requestDto) {
        var chatCompletionService = getChatCompletionService(requestDto.getModel(), openAIAsyncClient);
        log.info(CALLING_CHATBOT_WITH_PROMPT, requestDto.getPrompt());
        chatHistory.addUserMessage(requestDto.getPrompt());
        var response = chatCompletionService.getChatMessageContentsAsync(
                chatHistory, getKernel(chatCompletionService),
                new InvocationContext.Builder().withPromptExecutionSettings(PromptExecutionSettings.builder().withTemperature(requestDto.getTemperature())
                        .withMaxTokens(requestDto.getMaxTokens()).withStopSequences(requestDto.getStopSequence()).build()).build()).block();
        var responseResult = new StringBuilder();
        if (response == null || response.isEmpty()) {
            return StringUtils.EMPTY;
        }
        response.stream().filter(result -> result.getAuthorRole() == AuthorRole.ASSISTANT).forEach(result -> {
            log.info(result.getContent());
            chatHistory.addAssistantMessage(result.getContent());
            responseResult.append(result.getContent());
        });
        log.info(CHATBOT_RESPONSE, responseResult);
        return responseResult.toString();
    }


    @Lookup("chatCompletionService")
    protected ChatCompletionService getChatCompletionService(String model, OpenAIAsyncClient openAIAsyncClient) {
        return null;
    }

    @Lookup("kernel")
    protected Kernel getKernel(ChatCompletionService chatCompletionService) {
        return null;
    }

}
