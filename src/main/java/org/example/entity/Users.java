package org.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Setter
    @Column(name = "user_name")
    private String userName;

    @Setter
    @Column(name = "email")
    private String email;

    @Setter
    @Column(name = "password")
    private String password;

    public Users(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}