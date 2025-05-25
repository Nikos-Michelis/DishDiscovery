package com.reciperestapi.reciperestapi.recipe.dto.response;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class LinkServiceBuilder {

    public Map<String, String> generateLinks(UriInfo uriInfo, String selfUrlPath, String uiUrlPath) {
        Map<String, String> links = new HashMap<>();

        String selfUrl = uriInfo.getBaseUriBuilder()
                .path(selfUrlPath)
                .build()
                .toString();
        links.put("self", selfUrl);
        if(!uiUrlPath.isEmpty()) {
            links.put("ui", uiUrlPath);
        }
        return links;
    }
}