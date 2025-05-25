package com.reciperestapi.reciperestapi;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("/api")
@DeclareRoles({"ADMIN", "USER"})
public class RecipeApplication extends Application {
}