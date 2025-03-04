package com.web.note.handler.valid;


import com.web.note.handler.valid.validator.DatabaseVarcharValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatabaseVarcharValidator.class)
public @interface DatabaseVarcharValid {
    String message() default "长度必须小于255";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
