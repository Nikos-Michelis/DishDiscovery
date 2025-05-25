package com.reciperestapi.reciperestapi.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reciperestapi.reciperestapi.security.model.Roles;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonbPropertyOrder({"uuid", "username", "roles"})
public class UserDTO {
    @JsonbProperty("uuid")
    private String userUUId;
    private String username;
    @JsonbProperty("roles")
    private Set<Roles> roles;
}
