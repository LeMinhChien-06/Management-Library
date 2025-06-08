package com.example.management.config.security;

import com.example.management.entity.User;
import com.example.management.enums.Role;
import com.example.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = User.builder()
                        .username("admin")
                        .fullName("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .active(true)
                        .build();

                userRepository.save(user);
                log.warn("Complete");
            }

            if (userRepository.findByUsername("chien").isEmpty()) {

                User user = User.builder()
                        .username("chien")
                        .fullName("minh chien")
                        .email("minhchien@gmail.com")
                        .password(passwordEncoder.encode("Minhchien123"))
                        .role(Role.USER)
                        .active(true)
                        .build();

                userRepository.save(user);
                log.warn("Complete");
            }
        };
    }
}
