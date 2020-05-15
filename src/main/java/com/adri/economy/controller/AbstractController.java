package com.adri.economy.controller;

import com.adri.economy.exception.InvalidFieldException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.stream.Collectors;

public abstract class AbstractController {

    /**
     * Validate request parameters
     * @param bindingResult
     * @throws InvalidFieldException
     */
    public void validateFields(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            throw new InvalidFieldException(message);
        }
    }

    public String getLoggedUsername(){
        SecurityContext context = SecurityContextHolder.getContext();
        String userName = (String) context.getAuthentication().getPrincipal();

        return userName;
    }
}
