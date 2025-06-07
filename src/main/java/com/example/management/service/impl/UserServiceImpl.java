package com.example.management.service.impl;

import com.example.management.dto.request.UserCreatRequest;
import com.example.management.dto.request.UserUpdateRequest;
import com.example.management.dto.response.UserListResponse;
import com.example.management.dto.response.UserResponse;
import com.example.management.entity.User;
import com.example.management.exception.user.UserExceptions;
import com.example.management.mapper.UserMapper;
import com.example.management.repository.UserRepository;
import com.example.management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(UserCreatRequest userCreatRequest) {
        if (userRepository.existsByUsername(userCreatRequest.getUsername().trim())) {
            throw UserExceptions.usernameAlreadyExists();
        }

        if (userRepository.existsByEmail(userCreatRequest.getEmail().trim())) {
            throw UserExceptions.emailAlreadyExists();
        }

        if (userRepository.existsByPhone(userCreatRequest.getPhone().trim())) {
            throw UserExceptions.phoneAlreadyExists();
        }

        User user = userMapper.toUser(userCreatRequest);
        user.setPassword(passwordEncoder.encode(userCreatRequest.getPassword().trim()));

        User userSaved = userRepository.save(user);

        log.info("User {} created successfully", userSaved.getUsername());

        return userMapper.toUserResponse(userSaved);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @Override
    public UserListResponse getAllUsers() {
        return null;
    }

    @Override
    public UserResponse getUserById(Long id) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
