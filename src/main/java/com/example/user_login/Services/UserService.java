package com.example.user_login.Services;

import com.example.user_login.Entity.CustomUserDetails;
import com.example.user_login.Entity.UserEntity;
import com.example.user_login.Repository.UserRepository;
import ​​com.example.user_login.Entity.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService
{
    private final RestTemplate restTemplate;
    @Autowired
    public UserService (RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${external-apis.users.urls.host}")
    private String usersHost;

    @Value("${external-apis.users.urls.paths.getUsers}")
    private String getUsersPath;

    @Value("${external-apis.users.urls.paths.getUserByEmail}")
    private String getUserByEmailPath;

    @Value("${external-apis.users.urls.paths.getUserById}")
    private String getUserByIdPath;

    @Value("${external-apis.users.urls.paths.getUsersByRole}")
    private String getUsersByRolePath;

    @Value("${external-apis.users.urls.paths.addUser}")
    private String addUserPath;

    @Value("${external-apis.users.urls.paths.updateUser}")
    private String updateUserPath;

    @Value("${external-apis.users.urls.paths.deleteUser}")
    private String deleteUserPath;


    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public ResponseEntity<List<UserEntity>> getUsers() {
        String url = usersHost + getUsersPath;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<UserEntity>> entity = new HttpEntity<>(headers);
        ResponseEntity<List<UserEntity>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<UserEntity>>() {
        });
        return response;
    }

    public UserEntity getUserById(String userId) {
        String url = usersHost + getUserByIdPath + userId;
        ResponseEntity<UserEntity> response = restTemplate.getForEntity(url, UserEntity.class);
        return response.getBody();
    }

    public List<UserEntity> getUsersByRole(Role role) {
        String url = usersHost + getUsersByRolePath + "?role=" + role;
        ResponseEntity<List<UserEntity>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserEntity>>() {});
        return response.getBody();
    }

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String url = usersHost + getUserByEmailPath + "?username=" + username;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserEntity> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserEntity.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                UserEntity user = response.getBody();
                return new CustomUserDetails(user);
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }


    public UserEntity addUser(UserEntity user) {
        String url = usersHost + addUserPath;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserEntity> entity = new HttpEntity<>(user, headers);

        ResponseEntity<UserEntity> response = restTemplate.exchange(url, HttpMethod.POST, entity, UserEntity.class);
        return response.getBody();
    }


    //  public void saveUser(UserEntity user) {
    //            user.setPassword(passwordEncoder.encode(user.getPassword()));
    //             userRepository.save(user);
    //  }

    public UserEntity changeUserPermission(String email) {
        String url = usersHost + getUserByEmailPath + "?email=" + email;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<UserEntity> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserEntity.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            UserEntity user = response.getBody();
            user.setRole(Role.USER);
            return updateUser(user);
        }
        return response.getBody();
    }


    public UserEntity updateUser(UserEntity user) {
        String url = usersHost + updateUserPath;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserEntity> entity = new HttpEntity<>(user, headers);
        ResponseEntity<UserEntity> response = restTemplate.exchange(url, HttpMethod.PUT, entity, UserEntity.class);
        return response.getBody();
    }

    public void deleteUserByEmail(String email) {
        String url = usersHost + deleteUserPath + "?email=" + email;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public ResponseEntity<Void> deleteUser(String courseId) {
        String url = usersHost + deleteUserPath + courseId;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class
        );
        return response;
    }

    public UserEntity saveUser(UserEntity user) {
        String url = usersHost + addUserPath;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserEntity> entity = new HttpEntity<>(user, headers);
        ResponseEntity<UserEntity> response = restTemplate.exchange(url, HttpMethod.POST, entity, UserEntity.class);
        return response.getBody();
    }
}
