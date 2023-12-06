package com.example.skillnest.helpers;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class AssignmentHelper {

    private static final String PUBLIC_KEY = "public_a4sQ25uxpjBSrjqHBRgVlyzs4U0=";
    private static final String PRIVATE_KEY = "private_6mR3rnEbg2F/3SyUDtsT9oSFNaQ=";
    private static final String URL_ENDPOINT = "https://ik.imagekit.io/cczhtkq8r";

    private final ImageKit imageKit;


    public AssignmentHelper() {
        this.imageKit = ImageKit.getInstance();
        Configuration config = new Configuration(PUBLIC_KEY, PRIVATE_KEY, URL_ENDPOINT);
        imageKit.setConfig(config);
    }

    public String uploadAssignment(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("tempAssignment", ".docx");
        multipartFile.transferTo(tempFile);

        String base64 = Utils.fileToBase64(tempFile);
        FileCreateRequest fileCreateRequest = new FileCreateRequest(base64, tempFile.getName());
        fileCreateRequest.setFolder("assignments");

        try {
            Result result = imageKit.upload(fileCreateRequest);
            return result.getUrl();
        } catch (InternalServerException | BadRequestException | UnknownException | ForbiddenException |
                 TooManyRequestsException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }
    }

}
