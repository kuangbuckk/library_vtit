package com.project.library.services;

import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.FunctionRepository;
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
    public List<Function> getAllFunctions() {
        List<Function> functions = functionRepository.findAll();
        return functions;
    }

    @Override
    public Function getFunctionByCode(String code) {
        Function existingFunction = functionRepository
                .findById(UUID.fromString(code))
                .orElseThrow(()-> new DataNotFoundException("Can't find function with code "+ code));
        return existingFunction;
    }

    @Override
    public Function addFunction(FunctionDTO functionDTO) {
        Function function = Function.builder()
                .functionName(functionDTO.getFunctionName())
                .description(functionDTO.getDescription())
                .build();
        return functionRepository.save(function);
    }

    @Override
    public Function updateFunction(FunctionDTO functionDTO, String code) {
        Function existingFunction = this.getFunctionByCode(code);
        existingFunction.setDescription(functionDTO.getDescription());
        existingFunction.setFunctionName(functionDTO.getFunctionName());

        return functionRepository.save(existingFunction);
    }

    @Override
    public void deleteFunction(String code) {
        functionRepository.deleteById(UUID.fromString(code));
    }
}
