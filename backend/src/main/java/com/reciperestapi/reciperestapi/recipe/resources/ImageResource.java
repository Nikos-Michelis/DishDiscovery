package com.reciperestapi.reciperestapi.recipe.resources;

import com.reciperestapi.reciperestapi.security.service.otp.OtpResendService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Path("/images")
@RequestScoped
public class ImageResource {

    private String uploadDirectory;
    private static final Logger LOGGER = Logger.getLogger(OtpResendService.class.getName());


    @PostConstruct
    public void init() {
        String userHome = System.getProperty("user.home");
        uploadDirectory = Paths.get(userHome, "Desktop", "uploads").toString();
    }

    @GET
    @Path("/{imagePath}")
    @Produces({"image/jpeg", "image/png", "image/jpg"})
    @PermitAll
    public Response getImage(@PathParam("imagePath") String imagePath) {
        java.nio.file.Path fullImagePath = Paths.get(uploadDirectory, imagePath);
        LOGGER.info("Local Image Path -> " + fullImagePath);
        if (Files.exists(fullImagePath)) {
            byte[] imageData;
            try {
                imageData = Files.readAllBytes(fullImagePath);
            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.ok(imageData).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

