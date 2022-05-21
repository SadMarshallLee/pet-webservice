package com.pet.webservice.validations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

/* this service used for validation any errors */

@Service
public class ResponseErrorValidator {
    public ResponseEntity<Object> mapValidatorService(BindingResult result) {
        /* BindingResult contains info about errors */

        if(result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(result.getAllErrors())) {
                for (ObjectError error : result.getAllErrors()) {
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            /* filed error responsible for LoginRequest attributes, but getAll responsible for Object */

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
