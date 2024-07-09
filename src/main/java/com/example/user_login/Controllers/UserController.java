package com.example.user_login.Controllers;

import com.example.user_login.Config.JwtUtills;
import com.example.user_login.Entity.CustomUserDetails;
import com.example.user_login.Services.UserService;
import com.example.user_login.Entity.UserEntity;
import com.example.user_login.Entity.UserLoginDTO;
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

 //  @Autowired
 //  private final UserService userService;
 //  @Autowired
 //  private final JwtUtills jwtUtils;
 //  @Autowired
 //  private final  AuthenticationManager authenticationManager;


 //  @GetMapping("/")
 //  public String get() {
 //      return "hello";
 //  }

 //  @GetMapping()
 //  public List<UserEntity> getAllUsers() {
 //      return userService.getAllUsers();
 //  }

 //  @GetMapping("/{role}")
 //  public List<UserEntity> getUsersByRole(@PathVariable String role) {
 //      return userService.getUsersByRole(role);
 //  }

 //  @PostMapping("/{role}")
 //  public UserEntity addUser(@PathVariable String role, @RequestBody UserEntity user) {
 //      System.out.println("add user with role & email: " + role + user.getEmail());
 //      return userService.addUser(user.getEmail(), role);
 //  }

 //  @DeleteMapping("/{email}")
 //  public void removeUserPermission( @PathVariable String email) {
 //      System.out.println("change user permission with email:" + email );
 //      userService.removeUser(email);
 //  }


 //  @PostMapping("/login")
 //  public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
 //      System.out.println("Login attempt for user: " + userLoginDTO.getEmail());
 //       UserDetails userOpt = userService.loadUserByUsername(userLoginDTO.getEmail());

 //      if (userOpt != null) {
 //          if (userService.validatePassword(userLoginDTO.getPassword(), userOpt.getPassword())) {

 //              try {
 //                  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
 //                  final UserDetails userDetails = userService.loadUserByUsername(userLoginDTO.getEmail());
 //                  String token = jwtUtils.generateToken(userDetails);

 //                  return ResponseEntity.ok(token);
 //              } catch (Exception e) {
 //                  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
 //              }
 //          }
 //          else {
 //              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
 //          }
 //      } else {
 //          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
 //      }

 //  }



 //  @PostMapping("/register")
 //  public ResponseEntity<String> register(@RequestBody UserEntity user) {
 //      userService.saveUser(user);
 //      return ResponseEntity.ok("User registered successfully!");
 //  }

    private final UserService userService;
    private final JwtUtills jwtUtils;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String get() {
        return "hello";
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{role}")
    public List<UserEntity> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role);
    }
    @GetMapping("/users/{id}")
    public UserEntity getUsersById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users/{role}")
    public UserEntity addUser(@PathVariable String role, @RequestBody UserEntity user) {
        System.out.println("Adding user with role & email: " + role + " " + user.getEmail());
        return userService.addUser(user.getEmail(),role);
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> removeUserPermission(@PathVariable String email) {
        System.out.println("Removing user with email: " + email);
        userService.deleteUser(email);
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
