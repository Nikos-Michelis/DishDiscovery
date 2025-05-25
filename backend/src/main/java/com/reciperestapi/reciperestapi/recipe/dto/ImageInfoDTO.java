package com.reciperestapi.reciperestapi.recipe.dto;

import com.reciperestapi.reciperestapi.validators.ImageTypeValidatorConstraint;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"imagePath", "imageData"})
public class ImageInfoDTO implements Serializable, DTOEntity{
    @NotBlank(message = "Image imageData must not be null or empty")
    private String imageData;
    @NotBlank(message = "Original filename must not be null or empty")
    @ImageTypeValidatorConstraint
    private String imagePath;

}
