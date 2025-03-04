package com.web.note.handler.valid.validator;

import com.web.note.handler.valid.AuthCodeValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthCodeValidator implements ConstraintValidator<AuthCodeValid, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        return s==null || s.isEmpty() || s.matches("[a-zA-Z0-9]{4,20}");
    }
}
