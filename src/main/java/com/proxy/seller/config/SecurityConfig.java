package com.proxy.seller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс конфигурации для настройки шифрования паролей в Spring Security.
 */
@Configuration
public class SecurityConfig {

    /**
     * Создает бин PasswordEncoder для шифрования паролей.
     *
     * @return Бин PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
