package com.pl.arkadiusz.diet_pro.utils;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class testUtil {
        public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),
                StandardCharsets.UTF_8
        );
    }

