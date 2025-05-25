package com.reciperestapi.reciperestapi.recipe.dto.response;
import com.reciperestapi.reciperestapi.common.paging.PageMeta;
import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonbPropertyOrder({"status", "date", "message", "uuid", "self_url", "ui_url", "data", "meta", "links", "rememberMe"})
public class ResponseDTO implements DTOEntity {
    private String status;
    private String uuid;
    private String message;
    private String self_url;
    private String ui_url;
    private Object data;
    private PageMeta meta;
    private ZonedDateTime date;
    private Map<String, String> links;
    private Boolean rememberMe;
}
