package com.example.global.filter;

import com.example.domain.dto.common.ResultType;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        AddHeaderWrapper wrapper = new AddHeaderWrapper(request);
        String path = request.getRequestURI();
        if (path.startsWith("/v1/auth/")) {
            chain.doFilter(request, response);
        }

        // Request Header 에서 token 문자열 받아오기
        String token = request.getHeader("Authorization");

        // Request Header 에 Authorization 이 존재하지 않을 때
        if (token == null || "".equals(token)) {
            setHttpServletResponse(response, ResultType.ACCESS_TOKEN_REQUIRED);
        } else {
            Long userSeq = null;
            try {
                userSeq = jwtUtil.getUserSeqFromToken(token);
            } catch (ExpiredJwtException ex) {
                logger.error("ExpiredJwtException :: ex", ex);
                setHttpServletResponse(response, ResultType.EXPIRED_TOKEN);
            } catch (MalformedJwtException ex) {
                logger.error("MalformedJwtException :: ex", ex);
                setHttpServletResponse(response, ResultType.INVALID_TOKEN);
            } catch (Exception ex) {
                logger.error("Exception :: ex", ex);
            }

            wrapper.addHeader("userSeq", String.valueOf(userSeq));
            chain.doFilter(wrapper, response);
        }
    }

    private void setHttpServletResponse(HttpServletResponse response, ResultType resultType) throws IOException {
        ApiResponse apiResponse = ApiResponse.of(resultType);
        
        // record의 getter 메서드 사용: getStatus() → status()
        response.setStatus(apiResponse.status().value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String content = new ObjectMapper().writeValueAsString(apiResponse);
        response.setContentLength(content.length());
        response.getWriter()
                .write(content);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
