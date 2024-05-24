package com.partypilot.api.service;

import com.partypilot.api.dto.CredentialsDto;
import com.partypilot.api.dto.SignUpDto;
import com.partypilot.api.dto.UserDto;
import com.partypilot.api.exception.AppException;
import com.partypilot.api.mapper.UserMapper;
import com.partypilot.api.model.User;
import com.partypilot.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.email())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> oUser = userRepository.findByEmail(signUpDto.email());

        if (oUser.isPresent()) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto);
    }

    public Optional<UserDto> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(userFromDb -> {
                    userFromDb.setFirstName(user.getFirstName());
                    userFromDb.setLastName(user.getLastName());
                    userFromDb.setPhoneNumber(user.getPhoneNumber());
                    userFromDb.setProfilePhotoPath(user.getProfilePhotoPath());
                    return userMapper.toUserDto(userRepository.save(userFromDb));
                });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
