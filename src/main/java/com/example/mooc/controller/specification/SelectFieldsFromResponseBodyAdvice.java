package com.example.mooc.controller.specification;

import com.example.mooc.exception.SelectFieldNameNotExists;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
public class SelectFieldsFromResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Method, Boolean> supportCache = new ConcurrentHashMap<>();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return supportCache.computeIfAbsent(returnType.getMethod(),
                _ -> returnType.hasMethodAnnotation(SelectFieldsSupport.class) || returnTypeClassHasSelectDataAnnotation(returnType));
    }

    private boolean returnTypeClassHasSelectDataAnnotation(MethodParameter returnType) {
        var type = ResolvableType.forMethodParameter(returnType).resolveGeneric(0);
        if (type == null) {
            type = returnType.getParameterType();
        }
        return type.isAnnotationPresent(SelectFieldsSupport.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        var selectParameter = httpServletRequest.getParameter("select");
        if (selectParameter == null) {
            return body;
        }

        var listOfFiled = List.of(selectParameter.split(","));

        if (listOfFiled.isEmpty()) {
            return body;
        }

        if (Collection.class.isAssignableFrom(body.getClass())) {
            List<Map<String, Object>> bodyAsMap = objectMapper.convertValue(body, new TypeReference<>() {});
            return bodyAsMap.stream().map(it -> selectFields(it, listOfFiled)).toList();
        } else {
            Map<String, Object> bodyAsMap = objectMapper.convertValue(body, new TypeReference<>() {});
            return selectFields(bodyAsMap, listOfFiled);
        }

    }

    private Map<String, Object> selectFields(Map<String, Object> body, List<String> listOfFiled) {
        var selectedResult = new HashMap<String, Object>();
        listOfFiled.forEach(it -> {
            if (!body.containsKey(it)) {
                throw new SelectFieldNameNotExists();
            }
            selectedResult.put(it, body.get(it));
        });
        return selectedResult;
    }
}
