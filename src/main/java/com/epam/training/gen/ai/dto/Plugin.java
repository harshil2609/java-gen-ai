package com.epam.training.gen.ai.dto;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Plugin {

    @DefineKernelFunction(name = "searchForResults", description = "Searches for information in web.")
    public String searchForQueryResults(@KernelFunctionParameter(description = "Searches for information based on query in web.", name = "query") String query) {
        return query;
    }
}