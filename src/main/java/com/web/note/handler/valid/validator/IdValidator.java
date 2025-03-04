package com.web.note.handler.valid.validator;

import com.web.note.handler.valid.IdValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<IdValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() == 32 && value.matches("^[0-9a-f]+$");
    }
}
