package com.example.user_login.Controllers;

import com.example.user_login.Config.JwtUtills;
import com.example.user_login.Entity.CustomUserDetails;
import com.example.user_login.Services.UserService;
import com.example.user_login.Entity.UserEntity;
import com.example.user_login.Entity.UserLoginDTO;
import com.example.user_login.Entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final JwtUtills jwtUtils;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String get() {
        return "hello !!!";
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{role}")
    public List<UserEntity> getUsersByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }
    @GetMapping("/users/{id}")
    public UserEntity getUsersById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users/addUser")
    public UserEntity addUser(@RequestBody UserEntity user) {
        System.out.println("Create user with role & email: " + user.getRole() + " " + user.getEmail());
        return userService.addUser(user);
    }

    @PostMapping("/users/changeUserPermission")
    public UserEntity changeUserPermission(@RequestParam String email) {
        System.out.println("Change user permission with email: " + email +" to role " +Role.USER);
        return userService.changeUserPermission(email);
    }

    @PutMapping("/users/updateUser")
    public UserEntity updateUser(@RequestBody UserEntity user) {
        System.out.println("Update user with email: " + user.getEmail());
        return userService.updateUser(user);
    }

    @DeleteMapping("users/deleteUser")
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam String email) {
        System.out.println("Delete user with email: " + email);
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{courseId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String courseId) {
        System.out.println("Delete user from " + courseId);
        userService.deleteUser(courseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        System.out.println("Login attempt for user: " + userLoginDTO.getEmail());
        UserDetails userDetails = userService.loadUserByUsername(userLoginDTO.getEmail());

        if (userDetails != null) {
            if (userService.validatePassword(userLoginDTO.getPassword(), userDetails.getPassword())) {
                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
                    final UserDetails authenticatedUser = userService.loadUserByUsername(userLoginDTO.getEmail());
                    String token = jwtUtils.generateToken(authenticatedUser);
                    return ResponseEntity.ok(token);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }
}
