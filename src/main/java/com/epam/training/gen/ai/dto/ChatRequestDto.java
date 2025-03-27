package com.epam.training.gen.ai.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequestDto {
    private String prompt;
    private Double temperature = 1D;
    private Integer maxTokens = 2000;
    private String model;
    private List<String> stopSequence = new ArrayList<>();
}
