package com.ufma.portalegresso.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MaxAnoAtualValidator  implements ConstraintValidator<MaxAnoAtual, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int anoAtual = LocalDate.now().getYear();
        return value <= anoAtual;
    }
}
