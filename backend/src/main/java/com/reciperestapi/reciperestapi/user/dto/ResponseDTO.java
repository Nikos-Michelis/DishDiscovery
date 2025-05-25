package com.reciperestapi.reciperestapi.user.dto;

import com.reciperestapi.reciperestapi.common.paging.PageMeta;
import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO implements DTOEntity {
    private String status;
    private String message;
    private String url;
    private ZonedDateTime zonedDateTime;
}
