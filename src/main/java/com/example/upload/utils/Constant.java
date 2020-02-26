package com.example.upload.utils;

import java.util.regex.Pattern;

public class Constant {

    public static final Pattern IMAGE_EXTENSION_VALIDATION_REGEX = Pattern.compile("([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.jpg|.jpeg|.png|.JPG|.JPEG|.PNG)$");

    public enum Code {
        SUCCESS("00"),
        GENERAL_ERROR("01"),
        NO_MEDIA_SELECTED("77"),
        MAX_SIZE_FILE_EXCEEDED("66"),
        FILE_FORMAT_INVALID("22");

        private String statusCode;

        Code(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusCode() {
            return statusCode;
        }
    }
}
