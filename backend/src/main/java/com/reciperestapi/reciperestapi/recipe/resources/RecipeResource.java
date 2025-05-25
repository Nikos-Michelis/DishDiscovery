package com.reciperestapi.reciperestapi.recipe.resources;
import com.reciperestapi.reciperestapi.recipe.dto.DTOEntity;
import com.reciperestapi.reciperestapi.recipe.dto.forms.FormRecipeDTO;
import com.reciperestapi.reciperestapi.common.paging.PageRequest;
import com.reciperestapi.reciperestapi.recipe.dto.response.ResponseDTO;
import com.reciperestapi.reciperestapi.recipe.services.RecipeService;
import com.reciperestapi.reciperestapi.security.service.AuthenticationService;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;

@Path("/recipe")
@RequestScoped
public class RecipeResource {

    @Inject
    private RecipeService recipeService;
    @Inject
    private AuthenticationService authenticationService;

    @GET
    @Path("/user/recipeInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response getAllAuthUserRecipes(@BeanParam PageRequest queryParams, @Context SecurityContext securityContext) {
        User user = authenticationService.getAuthUserDetails(securityContext);
        ResponseDTO responseDTO = recipeService.getRecipesList(queryParams, false, user);
        return Response.ok().entity(responseDTO).build();
    }

    @GET
    @Path("/recipeInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAllRecipes(@BeanParam PageRequest queryParams) {
        ResponseDTO responseDTO = recipeService.getRecipesList(queryParams, false, null);
        return Response.ok().entity(responseDTO).build();
    }

    @GET
    @Path("/weekly")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAllWeeklyRecipes() throws Exception {
        ResponseDTO responseDTO = recipeService.getWeeklyRecipes();
        return Response.ok().entity(responseDTO).build();
    }


    @GET
    @Path("/wishlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response getWishlist(@BeanParam PageRequest queryParams, @Context SecurityContext securityContext) {
        User user = authenticationService.getAuthUserDetails(securityContext);
        ResponseDTO responseDTO = recipeService.getRecipesList(queryParams, true, user);
        return Response.ok().entity(responseDTO).build();
    }

    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Map<String, Object> getAllDataLists() throws Exception {
            return recipeService.getAllDataLists();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response addRecipe(@Valid @RequestBody FormRecipeDTO formRecipeDTO, @Context SecurityContext securityContext) {
        User user = authenticationService.getAuthUserDetails(securityContext);
        ResponseDTO responseDTO = recipeService.addRecipe(formRecipeDTO, user);
        return Response.created(URI.create(responseDTO.getSelf_url())).entity(responseDTO).build();
    }

    @PUT
    @Path("/edit/{recipeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response editRecipe(@Valid @RequestBody FormRecipeDTO formRecipeDTO,
                               @PathParam("recipeId") @PositiveOrZero Integer recipeId,
                               @Context SecurityContext securityContext) {
        formRecipeDTO.setRecipeId(recipeId);
        User user = authenticationService.getAuthUserDetails(securityContext);
        ResponseDTO responseDTO = recipeService.updateRecipe(formRecipeDTO, user);
        return Response.created(URI.create(responseDTO.getSelf_url())).entity(responseDTO).build();

    }

    @DELETE
    @Path("/delete/{recipeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response deleteRecipe(@PathParam("recipeId") @PositiveOrZero Integer recipeId) {
        recipeService.handleDeleteAction(recipeId);
        return Response.noContent().build();

    }
    @GET
    @Path("/{recipeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getRecipeById(@PathParam("recipeId") @PositiveOrZero(message = "recipe id must be greater that 0") Integer recipeId) throws Exception {
        DTOEntity dtoEntity = recipeService.getRecipeById(recipeId);
        return Response.ok(dtoEntity).build();

    }

    @DELETE
    @Path("/delete/wishlist/{recipeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public Response deleteFromWishlist(@Context SecurityContext securityContext, @PathParam("recipeId") @PositiveOrZero Integer recipeId) {
        User user = authenticationService.getAuthUserDetails(securityContext);
        recipeService.handleDeleteFromWishlistAction(user, recipeId);
        return Response.noContent().build();

    }
    @POST
    @Path("/wishlist/add/{recipeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response addToWishlist(@Context SecurityContext securityContext, @PathParam("recipeId") @PositiveOrZero Integer recipeId) {
        User user = authenticationService.getAuthUserDetails(securityContext);
        recipeService.handleAddToWishlistAction(user, recipeId);
        return Response.ok()
                .entity(ResponseDTO.builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .message("Recipe successfully added in your wishlist.")
                        .date(ZonedDateTime.now())).build();
    }
}