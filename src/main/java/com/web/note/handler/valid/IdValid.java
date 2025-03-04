package com.web.note.handler.valid;


import com.web.note.handler.valid.validator.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdValidator.class)
public @interface IdValid {
    String message() default "id格式错误";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
