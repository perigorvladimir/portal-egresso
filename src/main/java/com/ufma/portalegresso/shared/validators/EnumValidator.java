package com.ufma.portalegresso.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        System.out.println("initialize foi chamado");
        this.enumClass = constraintAnnotation.enumClass();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("O valor de '%s' não pode ser nulo", enumClass.getSimpleName()));
        }

        boolean isValid =Stream.of(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));

        if (!isValid) {
            throw new IllegalArgumentException(
                    String.format("'%s' não é um valor válido para %s. Valores válidos: %s", value, enumClass.getSimpleName(), Arrays.toString(enumClass.getEnumConstants())));
        }

        return true;
    }
}
