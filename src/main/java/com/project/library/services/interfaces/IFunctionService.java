package com.project.library.services.interfaces;

import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;
import com.project.library.responses.FunctionResponse;

import java.util.List;
import java.util.UUID;

public interface IFunctionService {
    List<FunctionResponse> getAllFunctions();
    FunctionResponse getFunctionByCode(UUID code);
    FunctionResponse addFunction(FunctionDTO functionDTO);
    FunctionResponse updateFunction(FunctionDTO functionDTO, UUID code);
    void deleteFunction(UUID code);
}
