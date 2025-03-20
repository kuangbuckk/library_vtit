package com.project.library.services.impl;

import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.FunctionRepository;
import com.project.library.responses.FunctionResponse;
import com.project.library.services.FunctionService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FunctionServiceImpl implements FunctionService {
    private final FunctionRepository functionRepository;

    @Override
    public List<FunctionResponse> getAllFunctions() {
        List<Function> functions = functionRepository.findAll();
        return functions.stream().map(FunctionResponse::fromFunction).toList();
    }

    @Override
    public FunctionResponse getFunctionByCode(Long id) {
        Function existingFunction = functionRepository
                .findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.FUNCTION_NOT_FOUND, id));
        return FunctionResponse.fromFunction(existingFunction);
    }

    @Override
    public FunctionResponse addFunction(FunctionDTO functionDTO) {
        Function function = Function.builder()
                .functionName(functionDTO.getFunctionName())
                .description(functionDTO.getDescription())
                .build();
        functionRepository.save(function);
        return FunctionResponse.fromFunction(function);
    }

    @Override
    public FunctionResponse updateFunction(FunctionDTO functionDTO, Long id) {
        Function existingFunction = functionRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.FUNCTION_NOT_FOUND, id));
        existingFunction.setDescription(functionDTO.getDescription());
        existingFunction.setFunctionName(functionDTO.getFunctionName());

        functionRepository.save(existingFunction);
        return FunctionResponse.fromFunction(existingFunction);
    }

    @Override
    public void deleteFunction(Long id) {
        functionRepository.deleteById(id);
    }
}
