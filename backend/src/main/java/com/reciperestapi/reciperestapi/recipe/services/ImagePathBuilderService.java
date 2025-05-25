package com.reciperestapi.reciperestapi.recipe.services;

import com.reciperestapi.reciperestapi.recipe.dto.ImageInfoDTO;
import jakarta.ws.rs.core.UriInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Logger;

public class ImagePathBuilderService {
    private final String uploadDirectory = Paths.get(System.getProperty("user.home"), "Desktop", "uploads").toString();
    private static final Logger LOGGER = Logger.getLogger(ImagePathBuilderService.class.getName());

    public String processImage(ImageInfoDTO imageInfo, UriInfo uriInfo) {
        byte[] decodedImage = Base64.getDecoder().decode(imageInfo.getImageData().getBytes(StandardCharsets.UTF_8));
        String fileName = System.currentTimeMillis() + "_" + imageInfo.getImagePath();
        String savePathToDb = uriInfo.getBaseUri() + "images/" + fileName;
        String filePath = uploadDirectory + File.separator + fileName;
        LOGGER.info("filePath -> " + filePath);
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(decodedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return savePathToDb;
    }
}
