package com.reciperestapi.reciperestapi.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Measurement implements Serializable {
    private int unitId;
    private String unitName;
}
