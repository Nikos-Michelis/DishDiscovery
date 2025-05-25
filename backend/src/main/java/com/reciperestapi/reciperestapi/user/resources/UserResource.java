package com.reciperestapi.reciperestapi.user.resources;

import com.reciperestapi.reciperestapi.security.auth.CustomSecurityContext;
import com.reciperestapi.reciperestapi.user.dto.ChangePasswordRequest;
import com.reciperestapi.reciperestapi.user.dto.ResponseDTO;
import com.reciperestapi.reciperestapi.user.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.time.ZonedDateTime;

@Path("/user")
public class UserResource {
    @Inject
    private UserService userService;
    @Context
    UriInfo uriInfo;

    @PATCH
    @Path("change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response changePassword(ChangePasswordRequest request, @Context CustomSecurityContext securityContext) {
        System.out.println("request: " + request);
        if (securityContext.getUserPrincipal() != null) {
            userService.changePassword(request, securityContext);
            return Response.accepted().entity(ResponseDTO.builder()
                            .status(Response.Status.ACCEPTED.getReasonPhrase())
                            .url(uriInfo.getBaseUriBuilder().path("/auth/login").build().toString())
                            .message("Your password has been successfully changed.")
                            .zonedDateTime(ZonedDateTime.now())
                            .build())
                    .build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("User not authorized").build();
        }
    }

}
