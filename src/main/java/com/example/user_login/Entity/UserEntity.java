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

//        (nullable = false)
        @Column
        private String password;

        @Column(nullable = true)
        private String role;

        @Column(unique = true, nullable = false)
        private String email;



    //   public  UserEntity(){}
//
//        @Column(nullable = true)
//        @Enumerated(EnumType.STRING)
//        private Role role;

}