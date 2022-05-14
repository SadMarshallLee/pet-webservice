package com.pet.webservice.validations;

import com.pet.webservice.annotations.PasswordMatches;
import com.pet.webservice.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest signupRequest = (SignupRequest) object;
        return signupRequest.getConfirmPassword().equals(signupRequest.getConfirmPassword());
    }
}
