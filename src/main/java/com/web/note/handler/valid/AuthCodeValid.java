package com.web.note.handler.valid;


import com.web.note.handler.valid.validator.AuthCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthCodeValidator.class)
public @interface AuthCodeValid {
    String message() default "无效的授权码,必须为4位以上大小写字母或数字";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
