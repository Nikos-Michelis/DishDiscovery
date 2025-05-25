package com.reciperestapi.reciperestapi.common.utils.paging_meta;

import com.reciperestapi.reciperestapi.recipe.repository.impl_service.RecipeDAOImpl;
import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import com.reciperestapi.reciperestapi.common.paging.PageMeta;
import com.reciperestapi.reciperestapi.common.paging.PageRequest;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.lang.reflect.Field;
import java.util.*;

@ApplicationScoped
public class MetaDataUtils {
    public static PageMeta getMetaData(PageRequest pageRequest,
                                       boolean isWishlist, User user,
                                       List<DTOEntity> recipesDTO,
                                       UriInfo uriInfo,
                                       RecipeDAOImpl genericDAO) {
        int totalRows = genericDAO.getTotalRows(pageRequest, isWishlist, user);
       // System.out.println("totalRows: " + totalRows);
       // System.out.println("pageRequest.getPage(): " + pageRequest.getPage());
       // System.out.println("pageRequest.getLimit(): " + pageRequest.getLimit());
        int rowsRemaining = Math.max(totalRows - (pageRequest.getPage() * pageRequest.getLimit()), 0);
       // System.out.println("rowsRemaining: " + rowsRemaining);
        int totalPages = (int) Math.ceil((double) totalRows / pageRequest.getLimit());
       System.out.println("totalPages: " + totalPages);
        int pagesRemaining = totalPages > 0 ? totalPages - pageRequest.getPage() : 0;
       // System.out.println("pagesRemaining: " + pagesRemaining);
        int nextPageOffset = pagesRemaining > 0 ? (pageRequest.getPage() + 1) * pageRequest.getLimit() : 0;
       // System.out.println("nextPageOffset: " + nextPageOffset);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
       // System.out.println("uriBuilder: " + uriBuilder);
        return new PageMeta(recipesDTO.size(),totalRows, rowsRemaining, totalPages,
                pagesRemaining, getUriBuilder(pageRequest, uriBuilder, rowsRemaining), nextPageOffset);
    }
    private static String getUriBuilder(PageRequest pageRequest, UriBuilder uriBuilder, int rowsRemaining) {
        LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();
        Field[] fields = PageRequest.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(pageRequest);
                if (value != null) {
                    addQueryParam(field.getName(), value, queryParams, rowsRemaining);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        return uriBuilder.build().toString();
    }
    private static void addQueryParam(String key, Object value, Map<String, Object> queryParams, int rowsRemaining) {
        if (value != null) {
            if (value instanceof Integer) {
                int intValue = (Integer) value;
                if (intValue != 0) {
                    queryParams.put(key, rowsRemaining > 0 & key.equals("page") ? (Integer) value + 1 : value);
                }
            } else if (value instanceof String) {
                String stringValue = (String) value;
                if (!stringValue.isEmpty()) {
                    queryParams.put(key, value);
                }
            }
        }
    }

}

