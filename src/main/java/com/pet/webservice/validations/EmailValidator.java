package com.pet.webservice.validations;

import com.pet.webservice.annotations.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN = "^[_A-Za-z\\d-+]+(.[_A-Za-z\\d-]+)*@" + "[A-Za-z\\d-]+(.[A-Za-z\\d]+)*(.[A-Za-z]{2,})$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    /* isValid() must return true or false to validation */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return (validateEmail(email));
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
