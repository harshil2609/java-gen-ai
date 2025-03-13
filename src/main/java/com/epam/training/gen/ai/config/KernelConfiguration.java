package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.dto.Plugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
public class KernelConfiguration {

    @Value("${client-azureopenai-key}")
    private String azureOpenAiKey;

    @Value("${client-azureopenai-endpoint}")
    private String azureOpenAiEndpoint;

    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureOpenAiKey))
                .endpoint(azureOpenAiEndpoint)
                .buildAsyncClient();
    }

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(deploymentOrModelName)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }


    @Bean
    public KernelPlugin kernelPlugin() {
        return KernelPluginFactory.createFromObject(
                new Plugin(), "Plugin");
    }

    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService, KernelPlugin kernelPlugin) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(kernelPlugin)
                .build();
    }

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(0.8)
                        .build())
                .build();
    }

    @Bean
    public Map<String, PromptExecutionSettings> promptExecutionsSettingsMap() {
        return Map.of(deploymentOrModelName, PromptExecutionSettings.builder()
                .withTemperature(0.8)
                .build());
    }
}