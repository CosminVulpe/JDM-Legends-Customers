package com.jdm.legends.customers.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;


@Service
public class UtilsMock {
    private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String writeJsonAsString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong while parsing the object", e);
        }
    }

    public static <T> T readValue(String content, Class<T> responseType) {
        try {
            return jackson.readValue(content, responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Something went wrong while deserializing the object", e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return jackson.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Something went wrong while deserializing the object", e);
        }
    }

    public static <T> T readValueResponseEntity(String content, TypeReference<ResponseEntity<T>> responseEntityTypeReference) {
        try {
            return jackson.readValue(content, responseEntityTypeReference).getBody();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Something went wrong while deserializing the object", e);
        }
    }
}
