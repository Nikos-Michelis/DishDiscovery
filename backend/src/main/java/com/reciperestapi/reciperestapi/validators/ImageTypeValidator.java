package com.reciperestapi.reciperestapi.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

@ApplicationScoped
public class ImageTypeValidator implements ConstraintValidator<ImageTypeValidatorConstraint, String> {
    @Override
    public void initialize(ImageTypeValidatorConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(String imagePath, ConstraintValidatorContext context) {
        String[] supportedTypes = {"jpg", "jpeg", "png"};
        // get a substring from . and after (+1)
        String extension = imagePath.substring(imagePath.lastIndexOf('.') + 1).toLowerCase();
        return Arrays.asList(supportedTypes).contains(extension);
    }
}
