package com.example.user_login.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column
        private String password;

        @Column(nullable = true)
        private Role role;

        @Column(unique = true, nullable = false)
        private String email;
