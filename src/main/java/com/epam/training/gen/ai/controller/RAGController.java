package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.RequestDto;
import com.epam.training.gen.ai.service.RagService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RAGController {

    private final RagService ragService;

    @PostMapping(path = "/rag/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getRagChatResponse(@RequestBody RequestDto request) throws ServiceNotFoundException {
        return ragService.getResponse(request.getInput());
    }
}
