package com.reciperestapi.reciperestapi.recipe.repository.impl_service;

import com.reciperestapi.reciperestapi.common.paging.PageRequest;
import com.reciperestapi.reciperestapi.common.services.DatabaseService;
import com.reciperestapi.reciperestapi.recipe.dto.forms.FormRecipeDTO;
import com.reciperestapi.reciperestapi.recipe.model.Wishlist;
import com.reciperestapi.reciperestapi.recipe.repository.GenericDAO;
import com.reciperestapi.reciperestapi.recipe.repository.WishlistDAO;
import com.reciperestapi.reciperestapi.recipe.repository.mappers.DBObjectMapper;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAOImpl implements GenericDAO, WishlistDAO {
    @Inject
    private DatabaseService databaseService;

    @Override
    public Object findById(Integer id) {
        return null;
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Object save(FormRecipeDTO recipe, User user) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();

            String query = " INSERT INTO recipe_list rl " +
                    " LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id " +
                    " WHERE wl.user_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getUserId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Wishlist> wishlistItems = new ArrayList<>();
                    while (resultSet.next()) {
                        Wishlist currentRecipe = DBObjectMapper.prepareWishlistObject(resultSet);
                        wishlistItems.add(currentRecipe);
                    }
                    return wishlistItems;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }


    @Override
    public Object update(FormRecipeDTO recipe, User user) {
        return null;
    }

    @Override
    public void delete(Integer recipe_id) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();
            String query = " DELETE FROM recipe_list WHERE recipe_id = ? ";
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
    public Integer getTotalRows(PageRequest pageRequest, boolean isWishlist, User user) {
        return 0;
    }

    @Override
    public List<Wishlist> findWishlistByUserId(User user) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();

            String query = " SELECT wl.wishlist_id, wl.user_id, rl.recipe_id " +
                    " FROM recipe_list rl " +
                    " LEFT JOIN wishlist wl ON wl.wishlist_id = rl.wishlist_id " +
                    " WHERE wl.user_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getUserId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Wishlist> wishlistItems = new ArrayList<>();
                    while (resultSet.next()) {
                        Wishlist currentRecipe = DBObjectMapper.prepareWishlistObject(resultSet);
                        wishlistItems.add(currentRecipe);
                    }
                    return wishlistItems;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void issueWishlist(User user) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();

            String selectQuery = "SELECT wishlist_id FROM wishlist WHERE user_id = ?";

            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, user.getUserId());
                try (ResultSet resultSet = selectStmt.executeQuery()) {
                    if (!resultSet.next()) {
                        // User does not have a wishlist, so create one
                        String insertQuery = "INSERT INTO wishlist (user_id) VALUES (?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, user.getUserId());
                            insertStmt.executeUpdate();
                            System.out.println("Wishlist created for user ID: " + user.getUserId());
                        }
                    } else {
                        System.out.println("User already has a wishlist.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }

    @Override
    public void addToWishlist(User user, Integer recipeId) {
        Connection connection = null;
        try {
            connection = databaseService.getConnection();

            // Step 1: Check if the user has a wishlist
            String selectWishlistQuery = "SELECT wishlist_id FROM wishlist WHERE user_id = ?";
            Integer wishlistId = null;

            try (PreparedStatement selectStmt = connection.prepareStatement(selectWishlistQuery)) {
                selectStmt.setInt(1, user.getUserId());
                try (ResultSet resultSet = selectStmt.executeQuery()) {
                    if (resultSet.next()) {
                        wishlistId = resultSet.getInt("wishlist_id");
                    } else {
                        String insertWishlistQuery = "INSERT INTO wishlist (user_id) VALUES (?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertWishlistQuery, Statement.RETURN_GENERATED_KEYS)) {
                            insertStmt.setInt(1, user.getUserId());
                            insertStmt.executeUpdate();

                            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    wishlistId = generatedKeys.getInt(1);
                                }
                            }
                        }
                    }
                }
            }

            // Step 2: Add the recipe to the wishlist
            if (wishlistId != null) {
                String insertRecipeListQuery = "INSERT INTO recipe_list (wishlist_id, recipe_id) VALUES (?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertRecipeListQuery)) {
                    insertStmt.setInt(1, wishlistId);
                    insertStmt.setInt(2, recipeId);
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseService.closeConnection(connection);
        }
    }


}
