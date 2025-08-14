package com.example.global.common.config;

import com.example.global.interceptor.AccessInterceptor;
import com.example.global.interceptor.RefreshInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AccessInterceptor accessInterceptor;
    private final RefreshInterceptor refreshInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor)
                .excludePathPatterns(
                        "/auths/signin", "/auths/login", "/auths/refresh",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/error"
                );
        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/auth/refresh");
    }

//    private final JwtUtil jwtUtil;
//
//    @Bean
//    public FilterRegistrationBean<OncePerRequestFilter> authFilter() {
//        return new FilterRegistrationBean<>(new AuthFilter(jwtUtil));
//    }
}
