package com.rental.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;
import java.lang.annotation.*;

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    @Size(min = 8, max = 72)
    public @interface ValidPassword {
        String message() default "Пароль должен содержать не менее 8 символов";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }


