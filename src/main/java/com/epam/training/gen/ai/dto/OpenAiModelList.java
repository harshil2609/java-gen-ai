package com.epam.training.gen.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenAiModelList {

    private List<OpenAiModel> data;
}