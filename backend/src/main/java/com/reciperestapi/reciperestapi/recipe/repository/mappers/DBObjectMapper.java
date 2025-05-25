package com.reciperestapi.reciperestapi.recipe.repository.mappers;

import com.reciperestapi.reciperestapi.recipe.model.*;
import com.reciperestapi.reciperestapi.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBObjectMapper {
    public static Wishlist prepareWishlistObject(ResultSet resultSet) throws SQLException {
        return Wishlist.builder()
                .wishlistId(resultSet.getInt("wishlist_id"))
                .userId(resultSet.getInt("user_id"))
                .recipeId(resultSet.getInt("recipe_id"))
                .build();
    }


    public static Recipe prepareRecipeObject(ResultSet resultSet) throws SQLException {
        return Recipe.builder()
                .recipeId(resultSet.getInt("recipe_id"))
                .category(prepareCategoryObject(resultSet))
                .recipeName(resultSet.getString("recipe_name"))
                .difficulty(resultSet.getString("difficulty"))
                .executionTime(resultSet.getInt("execution_time"))
                .image(resultSet.getString("image"))
                .wishListId(resultSet.getInt("wishlist_id"))
                .userId(resultSet.getInt("user_id"))
                .author(prepareUserObject(resultSet))
                .build();
    }

    public static User prepareUserObject(ResultSet resultSet) throws SQLException {
        return User.builder()
                .userId(resultSet.getInt("user_id"))
                .userUUId(resultSet.getString("user_uuid"))
                .username(resultSet.getString("userName"))
                .build();
    }

    public static RecipesSteps prepareRecipeStepsObject(ResultSet resultSet) throws SQLException {
        return RecipesSteps.builder()
                .recipeId(resultSet.getInt("recipe_id"))
                .stepInstructions(resultSet.getString("step_instructions"))
                .stepNumber(resultSet.getInt("step_order"))
                .build();
    }

    public static Category prepareCategoryObject(ResultSet resultSet) throws SQLException {
        return Category.builder()
                .categoryId(resultSet.getInt("category_id"))
                .categoryName(resultSet.getString("category_name"))
                .build();
    }

    public static Ingredient prepareRecipeIngredientObject(ResultSet resultSet) throws SQLException {
        return Ingredient.builder()
                .ingredientId(resultSet.getInt("ingredient_id"))
                .recipeId(resultSet.getInt("recipe_id"))
                .ingredientName(getNullableString(resultSet, "ingredient_name"))
                .ingredientAmount(getNullableString(resultSet, "ingredient_amount"))
                .measurement(prepareMeasurementObject(resultSet))
                .ingredientCategory(prepareIngredientCategoryObject(resultSet))
                .ingredientCategoryName(resultSet.getString("ingredient_category"))
                .build();
    }

    public static Ingredient prepareIngredientObject(ResultSet resultSet) throws SQLException {
        return Ingredient.builder()
                .ingredientId(resultSet.getInt("ingredient_id"))
                .ingredientName(resultSet.getString("ingredient_name"))
                .build();
    }

    public static IngredientCategory prepareIngredientCategoryObject(ResultSet resultSet) throws SQLException {
        return IngredientCategory.builder()
                .ingredientCategoryId(resultSet.getInt("ingredient_category_id"))
                .ingredientCategory(resultSet.getString("ingredient_category"))
                .build();
    }

    public static Measurement prepareMeasurementObject(ResultSet resultSet) throws SQLException {
        return Measurement.builder()
                .unitId(resultSet.getInt("unit_id"))
                .unitName(resultSet.getString("unit_name"))
                .build();
    }

    private static String getNullableString(ResultSet resultSet, String columnLabel) throws SQLException {
        return resultSet.getString(columnLabel) != null ? resultSet.getString(columnLabel) : null;
    }
}
