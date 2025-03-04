package com.web.note.handler.valid;


import com.web.note.handler.valid.validator.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameValid {
    String message() default "用户名长度必须大于6,小于20";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
