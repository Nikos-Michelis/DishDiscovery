package com.reciperestapi.reciperestapi.security.resources;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/protected")
@DeclareRoles({"ADMIN", "USER"})
public class ProtectedResource {
    /***
     * Access ONLY ADMIN
     * ***/
    @GET
    @Path("secured")
    @RolesAllowed({"ADMIN"})
    public Response getProtectedResource() {
        return Response.ok("This is a protected resource ").build();
    }

}
