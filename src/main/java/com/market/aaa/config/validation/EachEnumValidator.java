package com.market.aaa.config.validation;

import com.market.aaa.config.annotation.EachEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EachEnumValidator implements ConstraintValidator<EachEnum, List<String>> {

    private List<String> enumValues;

    @Override
    public void initialize(EachEnum constraintAnnotation) {
        Class<? extends Enum<?>> enumSelected = constraintAnnotation.enumClass();
        this.enumValues = Arrays.stream(enumSelected.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if (values == null) {
            return true;
        }

        for (String value : values) {
            if (!enumValues.contains(value)) {
                return false;
            }
        }

        return true;
    }
}

