package com.order_management.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.order_management.demo.exception.UserNotFoundException;
import com.order_management.demo.service.JwtService;
import com.order_management.demo.service.RefreshTokenService;
import com.order_management.demo.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService service;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtService jwtservice;
    @Autowired
    CurrentUserDetails currentUserDetails;

    // public UserController(UserService service) {
    //     this.service = service;
    // }

    @PostMapping("/auth/refresh")
    public RefreshTokenResponseDTO generateRefreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        //TODO: process POST request
      RefreshToken refreshToken= refreshTokenService.validateRefreshToken(refreshTokenRequestDTO.getRefreshToken());

        String accessToken= jwtservice.generateToken(refreshToken.getUser());

        return new RefreshTokenResponseDTO(accessToken);
        
    }
    
    
    @PostMapping("/auth/register")
    public String registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        //TODO: process POST request

        UserResponseDTO userResponseDTO = service.createuser(userRequestDTO);
        
        return "User created successfully with id: " + userResponseDTO.getId();
    }

    @PostMapping("/auth/login")
    public LoginResponseDTO loginUser(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        //TODO: process POST request
        
        return service.loginUser(userLoginRequestDTO);
    }

    @PostMapping("/auth/logout")
    public String logout(@RequestBody RefreshTokenRequestDTO refreshToken) {
        //TODO: process POST request
        refreshTokenService.revokeRefreshToken(refreshToken.getRefreshToken());
        return "User logout sucessfully!";

    }
    
    

    @GetMapping("/getusers") //working for roles
    public List<UserResponseDTO> getAllUsers() {

        List<UserResponseDTO> userResponseDTOs = service.getAllUsers();
        if(userResponseDTOs.isEmpty()){
            throw new UserNotFoundException("No users are found!");
        }
    
        return userResponseDTOs;
    }

    @GetMapping("/getusers/paginated") //working for roles
    public Page<UserResponseDTO> getMethodName(@RequestParam String page, @RequestParam String size) {
        return service.getAllUsers(Integer.parseInt(page), Integer.parseInt(size));
    }
    

    @GetMapping("/getuser/{id}")  //as of now get method in user controller is only accessed for admins
    public UserResponseDTO getMethodName(@PathVariable Long id) {
        return service.getUserById(id);
    }
    


    @PutMapping("/updateuser/{id}")  
    public String postMethodName(@PathVariable Long id, @Valid @RequestBody UserRequestDTO entity) {
        //TODO: process POST request
        String result= service.updateUser(id, entity); 
        return result;
    }
    
    

    @DeleteMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable Long id) {
        // TODO Auto-generated method stub
        String result = service.deleteUser(id);
        return result;
    }

    @PatchMapping("/updateuserdetails/{id}")
    public UserResponseDTO updateUserDetails(@PathVariable Long id, @Valid @RequestBody UserPatchRequestDTO userRequestDTO) {
        return service.updateUserDetails(id, userRequestDTO);
    }


    
    
}
