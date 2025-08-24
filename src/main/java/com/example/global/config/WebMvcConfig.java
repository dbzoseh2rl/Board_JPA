package com.example.global.config;

import com.example.global.interceptor.AccessInterceptor;
import com.example.global.interceptor.RefreshInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AccessInterceptor accessInterceptor;
    private final RefreshInterceptor refreshInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // /** 대s신 /** 사용
                .allowedOriginPatterns("http://localhost:*")  // 모든 localhost 포트 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("*")  // 클라이언트가 접근할 수 있는 헤더 추가
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // AccessInterceptor는 인증이 필요한 경로에만 적용
        registry.addInterceptor(accessInterceptor)
                .addPathPatterns("/**")  // 모든 경로에 적용하고
                .excludePathPatterns(     // 인증이 필요없는 경로 제외
                        "/**",  // 혹시 모를 다른 auth 경로
                        "/auths/**",  // 혹시 모를 다른 auth 경로
                        "/swagger-ui/**",
                        "/api-docs/**",
                        "/swagger-resources/**",
                        "/error"
                );

        // RefreshInterceptor는 refresh 경로에만 적용
        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/auths/refresh");
    }

//    private final JwtUtil jwtUtil;
//
//    @Bean
//    public FilterRegistrationBean<OncePerRequestFilter> authFilter() {
//        return new FilterRegistrationBean<>(new AuthFilter(jwtUtil));
//    }
}
