package com.ufma.portalegresso.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Stream;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Permitir nulos caso a anotação não especifique "não pode ser nulo"
        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("O valor de '%s' não pode ser nulo", enumClass.getSimpleName())).addConstraintViolation();
            return false;
        }

        // Verifica se o valor está entre os valores válidos do Enum
        boolean isValid = Stream.of(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));

        if (!isValid) {
            // Adiciona uma mensagem customizada de erro ao contexto
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("'%s' não é um valor válido para %s. Valores válidos: %s",
                            value, enumClass.getSimpleName(), Arrays.toString(enumClass.getEnumConstants()))
            ).addConstraintViolation();
        }

        return isValid;
    }
}