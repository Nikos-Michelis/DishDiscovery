package com.reciperestapi.reciperestapi.recipe.repository.impl_service;

import com.reciperestapi.reciperestapi.common.caching.CacheConfig;
import com.reciperestapi.reciperestapi.recipe.repository.GenericDAO;
import com.reciperestapi.reciperestapi.recipe.repository.RecipeDAO;
import com.reciperestapi.reciperestapi.recipe.model.Ingredient;
import com.reciperestapi.reciperestapi.recipe.model.Recipe;
import com.reciperestapi.reciperestapi.recipe.model.RecipesSteps;
import com.reciperestapi.reciperestapi.recipe.dto.RecipeStepsDTO;
import com.reciperestapi.reciperestapi.recipe.dto.forms.FormIngredientDTO;
import com.reciperestapi.reciperestapi.recipe.dto.forms.FormRecipeDTO;
import com.reciperestapi.reciperestapi.common.paging.PageRequest;
import com.reciperestapi.reciperestapi.recipe.repository.mappers.DBObjectMapper;
import com.reciperestapi.reciperestapi.common.services.DatabaseService;
import com.reciperestapi.reciperestapi.recipe.services.ImagePathBuilderService;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeDAOImpl implements GenericDAO, RecipeDAO {
    @Inject
    private DatabaseService databaseService;
    @Inject
    private CacheConfig cacheConfig;
    private static final String CACHE_KEY = "weeklyRecipes";
    @Inject
    private ImagePathBuilderService imagePathBuilderService;
    @Context
    private UriInfo uriInfo;
    private static final Logger LOGGER = Logger.getLogger(RecipeDAOImpl.class.getName());


    @Override
    public Recipe findById(Integer id) {
        Recipe currentRecipe = null;
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT r.recipe_id, r.category_id, c.category_name, r.recipe_name," +
                    "                   r.difficulty, r.execution_time, r.image," +
                    "                   i.ingredient_id, i.ingredient_name, ri.ingredient_amount, m.unit_id, m.unit_name, " +
                    "                   i.ingredient_category_id ,ic.ingredient_category ," +
                    "                   rs.step_order, rs.step_instructions ," +
                    "                   wl.wishlist_id, wl.user_id, u.user_id, u.user_uuid, u.userName" +
                    " FROM recipes r" +
                    " LEFT JOIN category c ON r.category_id = c.category_id" +
                    " LEFT JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id" +
                    " LEFT JOIN measurements m ON ri.unit_id = m.unit_id" +
                    " LEFT JOIN ingredients i ON i.ingredient_id = ri.ingredient_id" +
                    " LEFT JOIN ingredients_category ic ON ic.ingredient_category_id = i.ingredient_category_id" +
                    " LEFT JOIN recipe_steps rs ON rs.recipe_id = r.recipe_id" +
                    " LEFT JOIN recipe_list rl ON rl.recipe_id = r.recipe_id" +
                    " LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id" +
                    " JOIN user u ON r.user_id = u.user_id " +
                    " WHERE r.recipe_id = ?" +
                    " ORDER BY r.recipe_id, i.ingredient_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        if (currentRecipe == null) {
                            currentRecipe = DBObjectMapper.prepareRecipeObject(resultSet);
                            currentRecipe.setIngredientsList(new ArrayList<>());
                            currentRecipe.setStepsList(new ArrayList<>());
                        }

                        Ingredient ingredient = DBObjectMapper.prepareRecipeIngredientObject(resultSet);
                        if (!currentRecipe.getIngredientsList().contains(ingredient) && ingredient.getIngredientId() > 0) {
                            currentRecipe.getIngredientsList().add(ingredient);
                        }

                        RecipesSteps recipeStep = DBObjectMapper.prepareRecipeStepsObject(resultSet);
                        if (!currentRecipe.getStepsList().contains(recipeStep)) {
                            currentRecipe.getStepsList().add(recipeStep);
                        }
                    }
                }
            }

            return currentRecipe;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving recipe with ID: " + id, e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }
    @Override
    public Recipe findByName(String recipeName) {
        Connection connection = null;
        Recipe recipe = null;
        int prevId = 0;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT r.recipe_id, r.category_id, c.category_name, r.recipe_name," +
                    "                   r.difficulty, r.execution_time, r.image," +
                    "                   i.ingredient_id, i.ingredient_name, ri.ingredient_amount, m.unit_id, m.unit_name, " +
                    "                   i.ingredient_category_id ,ic.ingredient_category ," +
                    "                   rs.step_order, rs.step_instructions " +
                    " FROM recipes r" +
                    " LEFT JOIN category c ON r.category_id = c.category_id" +
                    " LEFT JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id" +
                    " LEFT JOIN measurements m ON ri.unit_id = m.unit_id" +
                    " LEFT JOIN ingredients i ON i.ingredient_id = ri.ingredient_id" +
                    " LEFT JOIN ingredients_category ic ON ic.ingredient_category_id = i.ingredient_category_id" +
                    " LEFT JOIN recipe_steps rs ON rs.recipe_id = r.recipe_id" +
                    " WHERE r.recipe_name = ?" +
                    " ORDER BY r.recipe_id, i.ingredient_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, recipeName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        if (recipe == null || recipe.getRecipeId() != prevId) {
                            recipe = DBObjectMapper.prepareRecipeObject(resultSet);
                            recipe.setIngredientsList(new ArrayList<>());
                            recipe.setStepsList(new ArrayList<>());
                            prevId = recipe.getRecipeId();
                        }

                        Ingredient ingredient = DBObjectMapper.prepareRecipeIngredientObject(resultSet);
                        if (!recipe.getIngredientsList().contains(ingredient) && ingredient.getIngredientId() > 0) {
                            recipe.getIngredientsList().add(ingredient);
                        }

                        RecipesSteps recipeStep = DBObjectMapper.prepareRecipeStepsObject(resultSet);
                        if (!recipe.getStepsList().contains(recipeStep)) {
                            recipe.getStepsList().add(recipeStep);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
        return recipe;
    }

    @Override
    public Recipe findWishlistByUserId(User user) {
        Connection connection = null;
        Recipe recipe = null;
        try {
            connection = databaseService.getConnection();

            String query = " SELECT r.recipe_id, wl.wishlist_id, " +
                    " r.category_id, c.category_name, " +
                    " r.recipe_name, r.difficulty, r.execution_time, r.image, " +
                    " u.user_id, u.user_uuid, u.userName," +
                    " r.difficulty, r.execution_time, r.image" +
                    " FROM recipes r " +
                    " LEFT JOIN category c ON r.category_id = c.category_id" +
                    " LEFT JOIN user u ON r.user_id = u.user_id" +
                    " LEFT JOIN wishlist wl ON wl.wishlist_id = r.wishlist_id" +
                    " WHERE u.user_id = 1 AND wl.wishlist_id IS NOT NULL" +
                    " ORDER BY r.recipe_id;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getUserId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        if (recipe == null) {
                            recipe = DBObjectMapper.prepareRecipeObject(resultSet);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
        return recipe;
    }
    @Override
    public List<Recipe> findAllWeeklyRecipes() {
        List<Recipe> recipes;
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "SELECT * FROM recipes r" +
                    " LEFT JOIN category c ON r.category_id = c.category_id " +
                    " LEFT JOIN recipe_list rl ON rl.recipe_id = r.recipe_id " +
                    " LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id " +
                    " JOIN user u ON r.user_id = u.user_id " +
                    " WHERE r.created_at >= CURDATE() - INTERVAL 7 DAY";
            LOGGER.info("Weekly recipes --> " + query);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    recipes = new ArrayList<>();
                    while (resultSet.next()) {
                        Recipe currentRecipe = DBObjectMapper.prepareRecipeObject(resultSet);
                        recipes.add(currentRecipe);
                    }
                    cacheConfig.getCache().put(CACHE_KEY, recipes); // Cache the results
                    return recipes;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public List<Recipe> findAll() {
        return null;
    }

    @Override
    public Recipe save(FormRecipeDTO recipe, User user) {

        Connection connection = null;
        int recipeId = 0;
        try {
            connection = databaseService.getConnection();
            connection.setAutoCommit(false);

            try {
                String insertRecipeQuery = "INSERT INTO recipes(recipe_name, user_id, category_id, difficulty, execution_time, image) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertRecipeQuery, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, recipe.getRecipeName());
                    preparedStatement.setInt(2, user.getUserId());
                    preparedStatement.setInt(3, recipe.getCategoryId());
                    preparedStatement.setString(4, recipe.getDifficulty());
                    preparedStatement.setInt(5, recipe.getExecutionTime());
                    preparedStatement.setString(6, recipe.getImage()
                            .stream()
                            .map(imageInfo -> imagePathBuilderService.processImage(imageInfo, uriInfo))
                            .collect(Collectors.joining()));

                    preparedStatement.executeUpdate();

                    ResultSet genKeys = preparedStatement.getGeneratedKeys();
                    if (genKeys.next()) {
                         recipeId = genKeys.getInt(1);

                        for (var step : recipe.getSteps()) {
                            setStepRecipe(connection, step, recipeId);
                        }

                        for (var ingredientDTO : recipe.getIngredients()) {
                            if (ingredientDTO.getIngredientName() != null) {
                                ingredientDTO.setIngredientId(setIngredients(connection, ingredientDTO));
                            }
                        }

                        for (var recipeIngredient : recipe.getIngredients()) {
                            setRecipeIngredients(connection, recipeIngredient, recipeId);
                        }
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            return Recipe.builder()
                    .recipeId(recipeId)
                    .recipeName(recipe.getRecipeName())
                    .executionTime(recipe.getExecutionTime())
                    .difficulty(recipe.getDifficulty())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    databaseService.closeConnection(connection);
                }
                LOGGER.info("Recipe Successfully added! ");
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }
    private void setStepRecipe(Connection connection, RecipeStepsDTO recipeStepsDTO, int recipeId) throws SQLException {
        String insertStepQuery = "INSERT INTO recipe_steps(recipe_id, step_instructions, step_order) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStepQuery)) {
            preparedStatement.setInt(1, recipeId);
            preparedStatement.setString(2, recipeStepsDTO.getStepInstructions());
            preparedStatement.setInt(3, recipeStepsDTO.getStepNumber());
            preparedStatement.executeUpdate();
        }
    }

    private void setRecipeIngredients(Connection connection, FormIngredientDTO recipeIngredient, int recipeId) throws SQLException {
        String insertIngredientsQuery = "INSERT INTO recipe_ingredients(recipe_id, ingredient_id, unit_id, ingredient_amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIngredientsQuery)) {
            preparedStatement.setInt(1, recipeId);
            preparedStatement.setInt(2, recipeIngredient.getIngredientId());
            preparedStatement.setInt(3, recipeIngredient.getUnitId());
            preparedStatement.setString(4, recipeIngredient.getIngredientAmount());
            preparedStatement.executeUpdate();
        }
    }

    private int setIngredients(Connection connection, FormIngredientDTO ingredients) {
        String insertIngredientQuery = "INSERT INTO ingredients(ingredient_name, ingredient_category_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIngredientQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, ingredients.getIngredientName());
            preparedStatement.setInt(2, ingredients.getIngredientCategoryId());
            preparedStatement.executeUpdate();

            ResultSet genKeys = preparedStatement.getGeneratedKeys();
            if (genKeys.next()) {
                return genKeys.getInt(1);
            } else {
                throw new SQLException("Failed to get generated key for ingredients");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ingredient had already exists");
        }
    }
    @Override
    public Recipe update(FormRecipeDTO recipe, User user) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            connection.setAutoCommit(false);

            try {
                String updateRecipesQuery = "UPDATE recipes SET recipe_name = ?, category_id = ?, difficulty = ?, execution_time = ?, image = ? WHERE recipe_id = ? AND user_id =?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateRecipesQuery)) {
                    preparedStatement.setString(1, recipe.getRecipeName());
                    preparedStatement.setInt(2, recipe.getCategoryId());
                    preparedStatement.setString(3, recipe.getDifficulty());
                    preparedStatement.setInt(4, recipe.getExecutionTime());
                    preparedStatement.setString(5, recipe.getImage().stream()
                            .map(imageInfo -> imagePathBuilderService.processImage(imageInfo, uriInfo))
                            .collect(Collectors.joining()));
                    preparedStatement.setInt(6, recipe.getRecipeId());
                    preparedStatement.setInt(7, user.getUserId());

                    preparedStatement.executeUpdate();
                }

                // Delete existing steps for the recipe
                String deleteExistingStepsQuery = "DELETE FROM recipe_steps WHERE recipe_id = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteExistingStepsQuery)) {
                    deleteStatement.setInt(1, recipe.getRecipeId());
                    deleteStatement.executeUpdate();
                }

                String insertStepQuery = "INSERT INTO recipe_steps (step_order, step_instructions, recipe_id) VALUES (?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertStepQuery)) {
                    for (RecipeStepsDTO step : recipe.getSteps()) {
                        insertStatement.setInt(1, step.getStepNumber());
                        insertStatement.setString(2, step.getStepInstructions());
                        insertStatement.setInt(3, recipe.getRecipeId());
                        insertStatement.executeUpdate();
                    }
                }
                // Delete existing ingredients for the recipe
                String deleteExistingIngredientsQuery = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteExistingIngredientsQuery)) {
                    deleteStatement.setInt(1, recipe.getRecipeId());
                    deleteStatement.executeUpdate();
                }

                String insertIngredientsQuery = "INSERT INTO recipe_ingredients(recipe_id, ingredient_id, unit_id, ingredient_amount) VALUES (?, ?, ?, ?)";
                for (FormIngredientDTO ingredient : recipe.getIngredients()) {
                    if(ingredient.getIngredientName() != null){
                        ingredient.setIngredientId(setIngredients(connection, ingredient));
                    }
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertIngredientsQuery)) {
                        preparedStatement.setInt(1, recipe.getRecipeId());
                        preparedStatement.setInt(2, ingredient.getIngredientId());
                        preparedStatement.setInt(3, ingredient.getUnitId());
                        preparedStatement.setString(4, ingredient.getIngredientAmount());
                        preparedStatement.executeUpdate();
                    }
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            return Recipe.builder()
                    .recipeId(recipe.getRecipeId())
                    .recipeName(recipe.getRecipeName())
                    .executionTime(recipe.getExecutionTime())
                    .difficulty(recipe.getDifficulty())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    databaseService.closeConnection(connection);
                }
                LOGGER.info("Recipe Successfully updated. ");
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Integer recipe_id) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = "DELETE FROM recipes WHERE recipe_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, recipe_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public List<Recipe> findByCriteria(PageRequest pageRequest, boolean isWishlist, User user) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(
                    "SELECT r.recipe_id, r.category_id, c.category_name, r.recipe_name, r.difficulty, r.execution_time, " +
                            "r.image, wl.wishlist_id, wl.user_id, u.user_id, u.user_uuid, u.userName "
            );

            queryBuilder.append("FROM recipes r ");

            if (pageRequest.hasFilters()) {
                queryBuilder
                        .append("LEFT JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id ")
                        .append("LEFT JOIN category c ON r.category_id = c.category_id ")
                        .append("LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id ");
            } else {
                queryBuilder.append("LEFT JOIN category c ON r.category_id = c.category_id ");
            }
            if (isWishlist) {
                queryBuilder
                        .append("LEFT JOIN recipe_list rl ON rl.recipe_id = r.recipe_id ")
                        .append("LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id ")
                        .append("JOIN user u ON wl.user_id  = u.user_id ");
            }else{
                queryBuilder
                        .append("LEFT JOIN recipe_list rl ON rl.recipe_id = r.recipe_id ")
                        .append("LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id ")
                        .append("JOIN user u ON r.user_id  = u.user_id ");
            }

            queryBuilder.append("WHERE 1=1 ");
            addFilterConditions(queryBuilder, pageRequest, isWishlist);
            if (pageRequest.hasFilters()) {
                queryBuilder.append("GROUP BY r.recipe_id, r.category_id, r.recipe_name, r.difficulty, r.execution_time, r.image, wl.wishlist_id ");
            }
            queryBuilder
                    .append("ORDER BY ")
                    .append(pageRequest.getSortField())
                    .append(" ")
                    .append(pageRequest.getSortOrder());
            queryBuilder.append(" LIMIT ? OFFSET ?");
           LOGGER.info("query builder --> " + queryBuilder);
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
                int parameterIndex = setParameters(preparedStatement, 1, pageRequest, isWishlist, user);
                int offset = (pageRequest.getPage() - 1) * pageRequest.getLimit();
                preparedStatement.setInt(parameterIndex++, pageRequest.getLimit());
                preparedStatement.setInt(parameterIndex++, offset);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Recipe> recipes = new ArrayList<>();
                    while (resultSet.next()) {
                        Recipe currentRecipe = DBObjectMapper.prepareRecipeObject(resultSet);
                        recipes.add(currentRecipe);
                    }
                    return recipes;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public Integer getTotalRows(PageRequest pageRequest, boolean isWishlist, User user){
        Connection connection = null;

        try {
            connection = databaseService.getConnection();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT COUNT(DISTINCT r.recipe_id) AS total_rows ");
            if (pageRequest.hasFilters()) {
                queryBuilder
                        .append("FROM recipes r LEFT JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id ")
                        .append("LEFT JOIN category c ON r.category_id = c.category_id ")
                        .append("LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id ");

            } else {
                queryBuilder.append("FROM recipes r LEFT JOIN category c ON r.category_id = c.category_id ");
            }
            if (isWishlist) {
                queryBuilder
                        .append("LEFT JOIN recipe_list rl ON rl.recipe_id = r.recipe_id ")
                        .append("LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id ")
                        .append("JOIN user u ON wl.user_id  = u.user_id ");
            }else{
                queryBuilder
                        .append("LEFT JOIN recipe_list rl ON rl.recipe_id = r.recipe_id ")
                        .append("LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id ")
                        .append("JOIN user u ON r.user_id  = u.user_id ");
            }


            queryBuilder.append("WHERE 1=1 ");
            addFilterConditions(queryBuilder, pageRequest, isWishlist);
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
                int parameterIndex = 1;
                if (pageRequest.hasFilters() || isWishlist) {
                    setParameters(preparedStatement, parameterIndex, pageRequest, isWishlist, user);
                }
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("total_rows");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }

        return 0;
    }
    private void addFilterConditions(StringBuilder queryBuilder, PageRequest pageRequest, boolean isWishlist) {
        if (isWishlist) {
            queryBuilder.append("AND wl.user_id = ? ");
        }
        if (pageRequest.getIngredientCategoryId() > 0) {
            queryBuilder.append("AND i.ingredient_category_id = ? ");
        }
        if (pageRequest.getIngredientId() > 0) {
            queryBuilder.append("AND ri.ingredient_id = ? ");
        }
        if (pageRequest.getCategoryId() > 0) {
            queryBuilder.append("AND r.category_id = ? ");
        }
        if (pageRequest.getUnitId() > 0) {
            queryBuilder.append("AND ri.unit_id = ? ");
        }
    }

    private int setParameters(PreparedStatement preparedStatement, int parameterIndex, PageRequest pageRequest, boolean isWishlist, User user) throws SQLException {
        if(isWishlist) {
            preparedStatement.setInt(parameterIndex++, user.getUserId());
        }
        if (pageRequest.getIngredientCategoryId() > 0) {
            preparedStatement.setInt(parameterIndex++, pageRequest.getIngredientCategoryId());
        }
        if (pageRequest.getIngredientId() > 0) {
            preparedStatement.setInt(parameterIndex++, pageRequest.getIngredientId());
        }
        if (pageRequest.getCategoryId() > 0) {
            preparedStatement.setInt(parameterIndex++, pageRequest.getCategoryId());
        }
        if (pageRequest.getUnitId() > 0) {
            preparedStatement.setInt(parameterIndex++, pageRequest.getUnitId());
        }
        return parameterIndex;
    }
}
