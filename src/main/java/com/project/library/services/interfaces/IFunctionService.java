package com.project.library.services.interfaces;

import com.project.library.dtos.FunctionDTO;
import com.project.library.entities.Function;

import java.util.List;

public interface IFunctionService {
    List<Function> getAllFunctions();
    Function getFunctionByCode(String code);
    Function addFunction(FunctionDTO functionDTO);
    Function updateFunction(FunctionDTO functionDTO, String code);
    void deleteFunction(String code);
}
