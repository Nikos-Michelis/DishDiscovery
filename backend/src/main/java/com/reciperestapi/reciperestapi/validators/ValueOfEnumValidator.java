package com.reciperestapi.reciperestapi.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnumValidatorConstraint, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnumValidatorConstraint annotation) {
        // get a stream with enum values and save that values in list
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return acceptedValues.contains(value.toString());
    }
}
