package com.pet.webservice.validations;

import com.pet.webservice.annotations.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    /* isValid() must return true or false to validation */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return validatedEmail(email);
    }

    public boolean validatedEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches(); // return true or false
    }
}
