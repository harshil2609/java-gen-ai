package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.OpenAiModel;

import java.util.List;

public interface DeploymentService {
    List<OpenAiModel> getModels();
}
