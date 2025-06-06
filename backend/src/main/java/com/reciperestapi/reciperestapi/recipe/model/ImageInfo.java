package com.reciperestapi.reciperestapi.recipe.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo implements Serializable {
    private String imagePath;
    private String imageData;

}
