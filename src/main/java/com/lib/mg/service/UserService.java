package com.lib.mg.service;

import com.lib.mg.Dto.UserRequestDto;
import com.lib.mg.Dto.UserResponseDto;
import com.lib.mg.entity.UserInfo;
import com.lib.mg.entity.UserRole;
import com.lib.mg.enums.Roles;
import com.lib.mg.repository.UserRepository;
import com.lib.mg.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + requestDto.getEmail());
        }

        // 1. Map incoming request data to UserInfo entity
        UserInfo user = modelMapper.map(requestDto, UserInfo.class);
        user.setId(null); // Ensure ID is null for a new entity

        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        // 2. Default to roleId = 2 (USER) if not provided
        Long roleId = (requestDto.getRoleId() != null) ? requestDto.getRoleId() : 2L;
        UserRole role = userRoleRepository.findById(roleId)
                .orElseGet(() -> UserRole.builder().roleId(roleId).build());
        user.setRole(role);

        // 3. Save the entity to the database
        UserInfo savedUser = userRepository.save(user);

        // 4. Map the saved entity to the clean Response DTO (lacks password field)
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto updatedUserDto) {
        UserInfo existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(updatedUserDto.getName());
        existingUser.setEmail(updatedUserDto.getEmail());

        if (updatedUserDto.getPassword() != null && !updatedUserDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }

        if (updatedUserDto.getRoleId() != null) {
            UserRole role = userRoleRepository.findById(updatedUserDto.getRoleId())
                    .orElseGet(() -> UserRole.builder().roleId(updatedUserDto.getRoleId()).build());
            existingUser.setRole(role);
        }

        UserInfo savedUser = userRepository.save(existingUser);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRoleId(Long roleId) {
        return userRepository.findByRoleRoleId(roleId)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRoleName(Roles roleName) {
        return userRepository.findByRoleRoleName(roleName)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }
}
