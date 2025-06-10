package com.example.management.service.impl;

import com.example.management.annotation.TrackAction;
import com.example.management.constants.SortConstants;
import com.example.management.dto.request.users.UserCreatRequest;
import com.example.management.dto.request.users.UserUpdateRequest;
import com.example.management.dto.response.PageDTO;
import com.example.management.dto.response.users.UserListResponse;
import com.example.management.dto.response.users.UserResponse;
import com.example.management.entity.User;
import com.example.management.enums.AuditAction;
import com.example.management.enums.EntityType;
import com.example.management.exception.user.UserExceptions;
import com.example.management.mapper.UserMapper;
import com.example.management.repository.UserRepository;
import com.example.management.service.UserService;
import com.example.management.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.USER_CREATE,
            entityType = EntityType.USER,
            entityId = "#result.id",
            entityName = "#result.username"
    )
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
    @Transactional
    @TrackAction(
            action = AuditAction.USER_UPDATE,
            entityType = EntityType.USER,
            entityId = "#result.id",
            entityName = "#result.username"
    )
    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(UserExceptions::userNotFound);

        if (userUpdateRequest.getEmail() != null) {
            String newEmail = userUpdateRequest.getEmail().trim();
            if (!newEmail.equals(user.getEmail().trim()) && userRepository.existsByEmail(newEmail)) {
                throw UserExceptions.emailAlreadyExists();
            }
        }

        if (userUpdateRequest.getPhone() != null) {
            String newPhone = userUpdateRequest.getPhone().trim();
            if (!newPhone.equals(user.getPhone().trim()) && userRepository.existsByPhone(newPhone)) {
                throw UserExceptions.phoneAlreadyExists();
            }
        }

        User updatedUser = userMapper.toUser(userUpdateRequest, user);

        User userSaved = userRepository.save(updatedUser);

        log.info("User {} updated successfully", userSaved.getUsername());

        return userMapper.toUserResponse(userSaved);
    }


    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.USER_VIEW,
            entityType = EntityType.USER,
            entityId = "#arg"
    )
    public PageDTO<UserListResponse> getAllUsers(
            int page, int size, String sortBy, String sortDirection,
            String email, String phone, String fullName, Boolean isActive
    ) {

        Sort sort = SortUtils.createSort(
                sortBy,
                sortDirection,
                SortConstants.User.BASIC_FIELDS,
                SortConstants.User.CREATED_AT
        );

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.getAllUsersWithFilters(email, phone, fullName, isActive, pageRequest);

        List<UserListResponse> userList = userPage.getContent()
                .stream()
                .map(userMapper::toUserListResponse)
                .toList();

        log.info("Retrieved {} users with paging and filters", userList.size());

        return PageDTO.of(
                userList,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements()
        );
    }

    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.USER_GET_BY_ID,
            entityType = EntityType.USER,
            entityId = "#result.id",
            entityName = "#result.username"
    )
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserExceptions::userNotFound);
        log.info("User {} retrieved successfully", user.getUsername());
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    @TrackAction(
            action = AuditAction.USER_DELETE,
            entityType = EntityType.USER,
            entityId = "#result.id",
            entityName = "#result.username"
    )
    public void deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(UserExceptions::userNotFound);
        userRepository.deleteById(id);
        log.info("User {} deleted successfully", id);
    }
}
