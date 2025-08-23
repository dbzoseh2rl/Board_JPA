package com.example.global.interceptor;

import com.example.global.exception.RefreshTokenRequiredException;
import com.example.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class RefreshInterceptor extends AuthInterceptor {

    private static final String REFRESH_TOKEN_TYPE = "REFRESH";
    private static final String REFRESH_TOKEN_HEADER_NAME = "R-Authorization";

    public RefreshInterceptor(JwtUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        this.setToken(this.getTokenFromHeader(request, REFRESH_TOKEN_HEADER_NAME));

        return super.preHandle(request, response, handler);
    }

    @Override
    protected void checkTokenExist() {
        if (isEmpty(this.token) || !this.isValidType(REFRESH_TOKEN_TYPE)) {
            throw new RefreshTokenRequiredException(this.uri);
        }
    }

}
