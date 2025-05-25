package com.reciperestapi.reciperestapi.recipe.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.reciperestapi.reciperestapi.common.caching.CacheConfig;
import com.reciperestapi.reciperestapi.common.caching.CacheService;
import com.reciperestapi.reciperestapi.recipe.dto.*;
import com.reciperestapi.reciperestapi.recipe.model.*;
import com.reciperestapi.reciperestapi.recipe.dto.forms.FormRecipeDTO;
import com.reciperestapi.reciperestapi.common.paging.PageRequest;
import com.reciperestapi.reciperestapi.recipe.repository.impl_service.CommonDAOImpl;
import com.reciperestapi.reciperestapi.recipe.repository.impl_service.RecipeDAOImpl;
import com.reciperestapi.reciperestapi.common.services.ValidationService;
import com.reciperestapi.reciperestapi.recipe.dto.response.ResponseDTO;
import com.reciperestapi.reciperestapi.common.settings.exceptions.ResourceNotFoundException;
import com.reciperestapi.reciperestapi.common.utils.dto.DtoUtils;
import com.reciperestapi.reciperestapi.common.utils.paging_meta.MetaDataUtils;
import com.reciperestapi.reciperestapi.recipe.repository.impl_service.WishlistDAOImpl;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.*;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeService {
    @Context
    private UriInfo uriInfo;
    @Inject
    private ValidationService validationService;
    @Inject
    private RecipeDAOImpl recipeDAO;
    @Inject
    private CommonDAOImpl commonDataDAO;
    @Inject
    private WishlistDAOImpl wishlistDAO;
    @Inject
    private CacheConfig cacheConfig;
    @Inject
    private CacheService cacheService;
    private static final String FILTERS_CACHE_KEY = "filters-cache";
    private static final String WEEKLY_RECIPES_CACHE_KEY = "weekly_recipes";
    private static final String RECIPE_CACHE_KEY_PREFIX = "recipe_";



    public List<Recipe> setWishlistedRecipes(List<Recipe> recipes, User user) {
        return recipes.stream()
                .peek(recipe -> recipe.setWishlisted(recipe.getWishListId()!=0 && recipe.getUserId() == user.getUserId()))
                .collect(Collectors.toList());
    }

    public ResponseDTO getRecipesList(PageRequest pageRequest, Boolean isWishlist, User user) {
        List<Recipe> recipes = recipeDAO.findByCriteria(pageRequest, isWishlist, user);
        if (user != null) {
            recipes = setWishlistedRecipes(recipes, user);
        }
        List<DTOEntity> recipesDTO = recipes.stream()
                .map(recipe -> new DtoUtils().convertToDto(recipe, new RecipeDTO()))
                .collect(Collectors.toList());
        return ResponseDTO.builder()
                .status(Response.Status.OK.getReasonPhrase())
                .date(ZonedDateTime.now())
                .data(recipesDTO)
                .meta(MetaDataUtils.getMetaData(pageRequest, isWishlist, user, recipesDTO, uriInfo, recipeDAO))
                .build();
    }

    public ResponseDTO getWeeklyRecipes() throws Exception {
        Cache<String, Object> cache = cacheConfig.getCache();
        List<Recipe> recipes = (List<Recipe>) cacheService.getCache(cache, WEEKLY_RECIPES_CACHE_KEY, () -> recipeDAO.findAllWeeklyRecipes());
        List<DTOEntity> recipesDTO = recipes.stream()
                .map(recipe -> new DtoUtils().convertToDto(recipe, new RecipeDTO()))
                .collect(Collectors.toList());
        return ResponseDTO.builder()
                .status(Response.Status.OK.getReasonPhrase())
                .data(recipesDTO)
                .date(ZonedDateTime.now())
                .build();
    }

    public Map<String, Object> getAllDataLists() throws Exception {
        Cache<String, Object> cache = cacheConfig.getCache();
        return (Map<String, Object>) cacheService.getCache(cache, FILTERS_CACHE_KEY, this::loadAllDataLists);
    }

    private Map<String, Object> loadAllDataLists() {
        Map<String, List<Object>> data = new HashMap<>();
        data.put("categories", fetchAndTransform(commonDataDAO.findAllRecipeCategory(), new CategoryDTO()));
        data.put("ingredients", fetchAndTransform(commonDataDAO.findAllIngredientCategory(), new IngredientDTO()));
        data.put("ingredient_categories", fetchAndTransform(commonDataDAO.findAllIngredientCategory(), new IngredientCategoryDTO()));
        data.put("measurements", fetchAndTransform(commonDataDAO.findAllMeasurements(), new MeasurementsDTO()));

        Map<String, Object> allCommonData = new HashMap<>();
        allCommonData.put("data", data);

        return allCommonData;
    }
    private <T> List<Object> fetchAndTransform(List<T> entities, DTOEntity dtoEntity) {
        return entities.stream()
                .map(object -> new DtoUtils().convertToDto(object, dtoEntity))
                .collect(Collectors.toList());
    }

    private ResponseDTO ResponseBuilder(RecipeDTO recipeDTO, String message){
        return ResponseDTO.builder()
                .date(ZonedDateTime.now())
                .status(Response.Status.CREATED.getReasonPhrase())
                .message(message)
                .self_url(uriInfo.getBaseUriBuilder().path("/recipe/" + recipeDTO.getRecipeId()).build().toString())
                .data(recipeDTO)
                .build();
    }

    public ResponseDTO addRecipe(FormRecipeDTO formRecipeDTO, User user){
        validationService.handleViolations(formRecipeDTO);
        Recipe savedRecipe = recipeDAO.save(formRecipeDTO, user);
        return ResponseBuilder((RecipeDTO) new DtoUtils().convertToDto(savedRecipe, new RecipeDTO()), "Recipe Successfully Created.");
    }

    public ResponseDTO updateRecipe(FormRecipeDTO formRecipeDTO, User user){
        validationService.handleViolations(formRecipeDTO);
        Recipe savedRecipe = recipeDAO.update(formRecipeDTO, user);
        return ResponseBuilder((RecipeDTO) new DtoUtils().convertToDto(savedRecipe, new RecipeDTO()), "Recipe Successfully Updated.");
    }

    public void handleDeleteAction(Integer recipeId) {
        Recipe recipe = recipeDAO.findById(recipeId);
        if (recipe == null) {
            throw new ResourceNotFoundException("Recipe not found.");
        }
        recipeDAO.delete(recipeId);
    }

    public void handleDeleteFromWishlistAction(User user, Integer recipeId) {
        List<Wishlist> recipesList = wishlistDAO.findWishlistByUserId(user)
                .stream()
                .filter(recipe -> recipe.getRecipeId() == recipeId)
                .toList();
        if (recipesList.isEmpty()) {
            throw new ResourceNotFoundException("Recipe not found.");
        }
        wishlistDAO.delete(recipeId);
    }

    public void handleAddToWishlistAction(User user, Integer recipeId) {
        wishlistDAO.issueWishlist(user);
        List<Wishlist> recipesList = wishlistDAO.findWishlistByUserId(user)
                .stream()
                .filter(recipe -> recipe.getRecipeId() == recipeId)
                .toList();
        if (recipesList.isEmpty()) {
            wishlistDAO.addToWishlist(user, recipeId);
        }else{
            throw new ResourceNotFoundException("Recipe has already added.");
        }

    }

    public ResponseDTO getRecipeById(Integer recipeId) throws Exception {
        Cache<String, Object> cache = cacheConfig.getCache();
        String cacheKey = RECIPE_CACHE_KEY_PREFIX + recipeId;

        Recipe recipe = (Recipe) cacheService.getCache(cache, cacheKey, () -> {
            Recipe fetchedRecipe = recipeDAO.findById(recipeId);
            if (fetchedRecipe == null) {
                throw new ResourceNotFoundException("Recipe not found.");
            }
            return fetchedRecipe;
        });

        return ResponseDTO.builder()
                .data(new DtoUtils().convertToDto(recipe, new RecipeDTO()))
                .build();
    }
}