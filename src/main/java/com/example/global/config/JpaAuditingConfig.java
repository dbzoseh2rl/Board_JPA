package com.example.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<Long> {
        @Override
        public Optional<Long> getCurrentAuditor() {
            // 실제 구현에서는 현재 로그인한 사용자의 ID를 반환해야 함
            // 여기서는 임시로 1L반환
            // JWT 토큰에서 사용자 ID를 추출하거나 세션에서 가져오는 로직을 구현해야 함
            return Optional.of(1L);
        }
    }
}
