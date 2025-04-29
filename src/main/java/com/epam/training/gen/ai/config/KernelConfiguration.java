package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.dto.Plugin;
import com.epam.training.gen.ai.plugin.AgeBasedOnBirthday;
import com.epam.training.gen.ai.plugin.ConvertTemperaturePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class KernelConfiguration {

    @Value("${client-azureopenai-key}")
    private String azureOpenAiKey;

    @Value("${client-azureopenai-endpoint}")
    private String azureOpenAiEndpoint;

    @Value("${client-azureopenai-deployment-name}")
    private String defaultDeploymentOrModelName;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureOpenAiKey))
                .endpoint(azureOpenAiEndpoint)
                .buildAsyncClient();
    }

    @Bean
    @Scope(value = "prototype")
    public ChatCompletionService chatCompletionService(
            @Value("${client-azureopenai-deployment-name}") String deploymentOrModelName,
            final OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(deploymentOrModelName)
                .withModelId(ObjectUtils.isEmpty(deploymentOrModelName)
                        ? defaultDeploymentOrModelName : deploymentOrModelName)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }


    @Bean
    public KernelPlugin kernelPlugin() {
        return KernelPluginFactory.createFromObject(
                new Plugin(), "Plugin");
    }

    @Bean
    @Scope(value = "prototype")
    public Kernel kernel(final ChatCompletionService chatCompletionService) {

        var convertTemperaturePlugin =
                KernelPluginFactory.createFromObject(new ConvertTemperaturePlugin(), "ConvertTemperaturePlugin");
        var ageBasedOnBirthdayPlugin =
                KernelPluginFactory.createFromObject(new AgeBasedOnBirthday(), "AgeBasedOnBirthday");

        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(convertTemperaturePlugin)
                .withPlugin(ageBasedOnBirthdayPlugin)
                .build();
    }

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(0.0)
                        .build())
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();
    }

    @Bean
    public Map<String, PromptExecutionSettings> promptExecutionsSettingsMap(
            @Value("${client-azureopenai-deployment-name}") String deploymentOrModelName) {
        return Map.of(deploymentOrModelName, PromptExecutionSettings.builder()
                .withTemperature(0.8)
                .build());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}