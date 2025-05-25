package com.reciperestapi.reciperestapi.common.utils.dto;

import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import jakarta.ejb.Stateless;
import org.modelmapper.ModelMapper;
@Stateless
public class DtoUtils {
    public DTOEntity convertToDto(Object obj, DTOEntity mapper) {
        return new ModelMapper().map(obj, mapper.getClass());
    }
    public Object convertToEntity(Object obj, DTOEntity mapper) {
        return new ModelMapper().map(mapper, obj.getClass());
    }

}
