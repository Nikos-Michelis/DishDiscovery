package com.reciperestapi.reciperestapi.recipe.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"id", "name"})
public class MeasurementsDTO implements Serializable, DTOEntity {
    @JsonbProperty("id")
    private int unitId;
    @JsonbProperty("name")
    private String unitName;
}
