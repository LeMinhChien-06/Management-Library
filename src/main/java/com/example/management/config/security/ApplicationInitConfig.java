package com.example.management.config;

import com.example.management.entity.User;
import com.example.management.enums.Role;
import com.example.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
//                var roles = new HashSet<String>();
//                roles.add(Role.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .fullName("chien dep trai")
                        .email("minh@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(user);
                log.warn("Complete");
            }
        };
    }
}
