package com.project.library.services;

import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.FunctionRepository;
import com.project.library.responses.FunctionResponse;
import com.project.library.services.interfaces.IFunctionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FunctionServiceImpl implements IFunctionService {
    private final FunctionRepository functionRepository;

    @Override
    public List<FunctionResponse> getAllFunctions() {
        List<Function> functions = functionRepository.findAll();
        return functions.stream().map(FunctionResponse::fromFunction).toList();
    }

    @Override
    public FunctionResponse getFunctionByCode(UUID code) {
        Function existingFunction = functionRepository
                .findById(code)
                .orElseThrow(()-> new DataNotFoundException("Can't find function with code "+ code));
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
    public FunctionResponse updateFunction(FunctionDTO functionDTO, UUID code) {
        Function existingFunction = functionRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException("Can't find function with code "+ code));
        existingFunction.setDescription(functionDTO.getDescription());
        existingFunction.setFunctionName(functionDTO.getFunctionName());

        functionRepository.save(existingFunction);
        return FunctionResponse.fromFunction(existingFunction);
    }

    @Override
    public void deleteFunction(UUID code) {
        functionRepository.deleteById(code);
    }
}
