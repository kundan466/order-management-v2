package com.order_management.demo.service;

import java.security.InvalidAlgorithmParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.order_management.demo.Utils.CurrentUserDetails;
import com.order_management.demo.dto.LoginResponseDTO;
import com.order_management.demo.dto.RefreshTokenRequestDTO;
import com.order_management.demo.dto.RefreshTokenResponseDTO;
import com.order_management.demo.dto.UserLoginRequestDTO;
import com.order_management.demo.dto.UserPatchRequestDTO;
import com.order_management.demo.dto.UserRequestDTO;
import com.order_management.demo.dto.UserResponseDTO;
import com.order_management.demo.entity.RefreshToken;
import com.order_management.demo.entity.User;
import com.order_management.demo.enums.UserRole;
import com.order_management.demo.enums.UserStatus;
import com.order_management.demo.mapper.UserMapper;
import com.order_management.demo.repository.UserRepository;

import jakarta.validation.Valid;

import com.order_management.demo.exception.InvalidCredentialsException;
import com.order_management.demo.exception.UserInactiveException;
import com.order_management.demo.exception.UserNotFoundException;



import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    CurrentUserDetails currentUser;
    @Autowired
    RefreshTokenService refreshTokenService;

    public UserResponseDTO createuser(UserRequestDTO userRequestDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'createuser'");
        if(userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            log.warn("Attempted to register with existing email: {}", userRequestDTO.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }

        User user = UserMapper.toEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.CUSTOMER);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        log.info("User created with id: {}", savedUser.getId());
        user.setCreatedBy(savedUser.getId());
        user.setUpdatedBy(savedUser.getId());
        User savedUserFinal = userRepository.save(user);

        return UserMapper.toDTO(savedUserFinal);
    }

    public List<UserResponseDTO> getAllUsers() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
        List<User> users= userRepository.findAll();

        log.info("Retrieving all users. Total count: {}", users.size());
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
        
    }

    public String deleteUser(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
        User currUser=currentUser.getUserDetails();
        if(currUser.getId()!=id && currUser.getRole()==UserRole.CUSTOMER){
            throw new UserNotFoundException("User has to delete the details of them");
        }
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            log.info("Deleting user with id: {}", id);
            return "User deleted successfully - " + id;
        } else {
            log.warn("Attempted to delete non-existent user with id: {}", id);
            return "User not found";
        }
        
       
    }

    public String updateUser(Long id, UserRequestDTO entity) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateUser'");

        User existingUser=userRepository.findById(id).orElseThrow(() -> {
            log.warn("Attempted to update non-existent user with id: {}", id);
            return new UserNotFoundException("User not found");
        });
        User currUser=currentUser.getUserDetails();
        if(currUser.getId()!=id && currUser.getRole()==UserRole.CUSTOMER){
            throw new UserNotFoundException("User has to update the details of them");
        }
        existingUser.setId(id); // Ensure the ID is set to the existing user's ID
        existingUser.setName(entity.getName());
        existingUser.setEmail(entity.getEmail());
        existingUser.setPhone_number(entity.getPhoneNumber());
        existingUser.setAddress(entity.getAddress());
        existingUser.setStatus(entity.getStatus());
        existingUser.setUpdatedAt(LocalDateTime.now());
        existingUser.setUpdatedBy(id);
        userRepository.save(existingUser);
        log.info("Updating user with id: {}", id);
        return "User updated successfully - " + id;
        
    }

    public UserResponseDTO getUserById(long long1) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getUserById'");

        User user = userRepository.findById(long1).orElseThrow(() -> {
            log.warn("Attempted to retrieve non-existent user with id: {}", long1);
            return new UserNotFoundException("User not found");
        });
        return UserMapper.toDTO(user);
    }

    public UserResponseDTO updateUserDetails(Long id, UserPatchRequestDTO userRequestDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateUserDetails'");
        User existingUser = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Attempted to update details of non-existent user with id: {}", id);
            return new UserNotFoundException("User not found");
        });

        User currUser=currentUser.getUserDetails();
        if(currUser.getId()!=id && currUser.getRole()==UserRole.CUSTOMER){
            throw new UserNotFoundException("User has to update the details of them");
        }

        if(userRequestDTO.getName() != null) {
            existingUser.setName(userRequestDTO.getName());
        }
        if(userRequestDTO.getEmail() != null) {
            existingUser.setEmail(userRequestDTO.getEmail());
        }
        if(userRequestDTO.getPhoneNumber() != null) {
            existingUser.setPhone_number(userRequestDTO.getPhoneNumber());
        }
        if(userRequestDTO.getAddress() != null) {
            existingUser.setAddress(userRequestDTO.getAddress());
        }
        if(userRequestDTO.getStatus() != null) {
            existingUser.setStatus(userRequestDTO.getStatus());
        }
        
        existingUser.setUpdatedAt(LocalDateTime.now());
        existingUser.setUpdatedBy(currentUser.getUserDetails().getId());
        userRepository.save(existingUser);

        log.info("Updating user details with id: {}", id);
        return UserMapper.toDTO(existingUser);
    }

    public Page<UserResponseDTO> getAllUsers(int int1, int int2) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
        Page<User> usersPage = userRepository.findAll(PageRequest.of(int1, int2));
        log.info("Retrieved users page: {} with size: {}", int1, int2);
        return usersPage.map(UserMapper::toDTO);
    }

    public LoginResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'loginUser'");
        User user = userRepository.findByEmail(userLoginRequestDTO.getEmail()).orElseThrow(() -> {
            log.warn("Attempted login with non-existent email: {}", userLoginRequestDTO.getEmail());
            return new UserNotFoundException("User not found");
        });
        if(user.getStatus() != UserStatus.ACTIVE) {
            throw new UserInactiveException( "User account is inactive");
        }
        if(passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
            log.info("User logged in successfully with email: {}", userLoginRequestDTO.getEmail());
            LoginResponseDTO response = new LoginResponseDTO();
            RefreshToken refreshToken =refreshTokenService.createRefreshToken(user);
            response.setAccesstoken(jwtService.generateToken(user));
            response.setRefreshToken(refreshToken.getToken());
            response.setRole(user.getRole().toString());
            return response;
        } else {
            log.warn("Failed login attempt with email: {}", userLoginRequestDTO.getEmail());
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    


}
