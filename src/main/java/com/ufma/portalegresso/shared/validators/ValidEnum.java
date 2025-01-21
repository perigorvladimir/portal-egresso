package com.ufma.portalegresso.shared.validators;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class) // classe que valida
@Target({ElementType.FIELD, ElementType.PARAMETER}) // onde pode ser usado
@Retention(RetentionPolicy.RUNTIME) // pode ser acessada por reflexao
public @interface ValidEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "O valor informado é inválido no enum " + "{enumClass}.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
