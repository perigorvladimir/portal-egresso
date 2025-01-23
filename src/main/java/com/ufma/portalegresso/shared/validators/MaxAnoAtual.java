package com.ufma.portalegresso.shared.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MaxAnoAtualValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxAnoAtual {
    String message() default "O ano não pode ser maior que o ano atual";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
