package com.web.note.handler.valid.validator;

import com.web.note.handler.valid.UsernameValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() >= 6 && value.length() < 20;
    }
}
