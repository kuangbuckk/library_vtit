package com.project.library.services;

import com.project.library.dtos.FunctionDTO;
import com.project.library.responses.FunctionResponse;

import java.util.List;

public interface FunctionService {
    List<FunctionResponse> getAllFunctions();
    FunctionResponse getFunctionByCode(Long id);
    FunctionResponse addFunction(FunctionDTO functionDTO);
    FunctionResponse updateFunction(FunctionDTO functionDTO, Long id);
    void deleteFunction(Long id);
}
