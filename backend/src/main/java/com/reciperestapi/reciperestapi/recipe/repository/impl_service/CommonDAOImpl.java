package com.reciperestapi.reciperestapi.recipe.repository.impl_service;

import com.reciperestapi.reciperestapi.recipe.repository.CommonDAO;
import com.reciperestapi.reciperestapi.recipe.model.Category;
import com.reciperestapi.reciperestapi.recipe.model.Ingredient;
import com.reciperestapi.reciperestapi.recipe.model.IngredientCategory;
import com.reciperestapi.reciperestapi.recipe.model.Measurement;
import com.reciperestapi.reciperestapi.common.services.DatabaseService;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.reciperestapi.reciperestapi.recipe.repository.mappers.DBObjectMapper.*;

public class CommonDAOImpl implements CommonDAO {
    @Inject
    private DatabaseService databaseService;
    @Override
    public List<Category> findAllRecipeCategory() {
        Connection connection = null;

        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM category ORDER BY category_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Category> categoryList = new ArrayList<>();

            while (resultSet.next()) {
                Category category = prepareCategoryObject(resultSet);
                categoryList.add(category);
            }
            resultSet.close();
            statement.close();
            databaseService.closeConnection(connection);
            return categoryList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<IngredientCategory> findAllIngredientCategory() {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM ingredients_category ORDER BY ingredient_category_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<IngredientCategory> ingredientCategoryList = new ArrayList<>();

            while (resultSet.next()) {
                IngredientCategory ingredientCategory = prepareIngredientCategoryObject(resultSet);
                ingredientCategoryList.add(ingredientCategory);
            }
            resultSet.close();
            statement.close();
            databaseService.closeConnection(connection);
            return ingredientCategoryList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Ingredient> findAllIngredients() {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM ingredients ORDER BY ingredient_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Ingredient> ingredientList = new ArrayList<>();

            while (resultSet.next()) {
                Ingredient ingredient = prepareIngredientObject(resultSet);
                ingredientList.add(ingredient);
            }
            resultSet.close();
            statement.close();
            databaseService.closeConnection(connection);
            return ingredientList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Measurement> findAllMeasurements() {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM measurements ORDER BY unit_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Measurement> measurementList = new ArrayList<>();

            while (resultSet.next()) {
                Measurement measurement = prepareMeasurementObject(resultSet);
                measurementList.add(measurement);
            }
            resultSet.close();
            statement.close();
            databaseService.closeConnection(connection);
            return measurementList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
