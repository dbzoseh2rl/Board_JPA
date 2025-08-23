package com.example.global.converter;


import com.example.domain.dto.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
@RequiredArgsConstructor
@Deprecated
public class ResultConverter extends MappingJackson2HttpMessageConverter {

    private final HttpServletResponse response;

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ApiResponse apiResponse;

        if (object instanceof ApiResponse) {
            apiResponse = (ApiResponse) object;
        } else {
            apiResponse = ApiResponse.of(object);
        }
        
        // record의 getter 메서드 사용
        response.setStatus(apiResponse.status().value());
        super.writeInternal(apiResponse, type, outputMessage);
    }
}
