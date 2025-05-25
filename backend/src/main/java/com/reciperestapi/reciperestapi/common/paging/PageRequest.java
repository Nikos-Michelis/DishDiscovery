package com.reciperestapi.reciperestapi.common.paging;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest implements Serializable {
    @QueryParam("ingredientCategory")
    @DefaultValue("0")
    private Integer ingredientCategoryId;
    @QueryParam("ingredient")
    @DefaultValue("0")
    private Integer ingredientId;
    @QueryParam("category")
    @DefaultValue("0")
    private Integer categoryId;
    @QueryParam("unit")
    @DefaultValue("0")
    private Integer unitId;
    @QueryParam("sortField")
    @DefaultValue("r.recipe_id")
    private String sortField;
    @QueryParam("sortOrder")
    @DefaultValue("asc")
    private String sortOrder;
    @QueryParam("limit")
    @DefaultValue("20")
    private Integer limit;
    @QueryParam("page")
    @DefaultValue("1")
    private Integer page;
    public boolean hasFilters() {
        return ingredientCategoryId > 0 || ingredientId > 0 || categoryId > 0 || unitId > 0;

    }
    public boolean hasPaging() {
        return page > 0 || limit > 0 || (sortField != null && !sortField.isEmpty()) || (sortOrder != null && !sortOrder.isEmpty());
    }
}
