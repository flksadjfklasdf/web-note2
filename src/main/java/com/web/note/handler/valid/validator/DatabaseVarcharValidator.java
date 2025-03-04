package com.web.note.handler.valid.validator;

import com.web.note.handler.valid.DatabaseVarcharValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DatabaseVarcharValidator implements ConstraintValidator<DatabaseVarcharValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.length() < 255;
    }
}
