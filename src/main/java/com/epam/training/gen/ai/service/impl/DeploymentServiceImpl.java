package com.epam.training.gen.ai.service.impl;

import com.epam.training.gen.ai.dto.OpenAiModelList;
import com.epam.training.gen.ai.dto.OpenAiModel;
import com.epam.training.gen.ai.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeploymentServiceImpl implements DeploymentService {

    public static final String OPENAI_DEPLOYMENTS = "/openai/deployments";
    public static final String API_KEY = "Api-Key";
    private final RestTemplate restTemplate;

    @Value("${client-azureopenai-key}")
    private String azureOpenAiKey;

    @Value("${client-azureopenai-endpoint}")
    private String azureOpenAiEndpoint;

    @Override
    public List<OpenAiModel> getModels() {
        var headers = new HttpHeaders();
        headers.add(API_KEY, azureOpenAiKey);
        var response = restTemplate.exchange(azureOpenAiEndpoint + OPENAI_DEPLOYMENTS, HttpMethod.GET, new HttpEntity<>(headers), OpenAiModelList.class);
        return response.getBody().getData();
    }
}