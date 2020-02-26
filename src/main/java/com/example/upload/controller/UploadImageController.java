package com.example.upload.controller;

import com.example.upload.utils.S3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/upload")
public class UploadImageController {

    @Autowired
    private S3Client s3Client;


    /**
     * Upload multiple images.
     *
     * @param images
     * @return {@link String}
     */
    @PostMapping(value = "/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam(value = "file") MultipartFile[] images){

        return new ResponseEntity<>(s3Client.uploadImageWarehouse(images), HttpStatus.OK);


    }
}
