package com.example.management.entity;

import com.example.management.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, length = 50)
    String username;

    @Column(nullable = false)
    String password;

    @Column(unique = true, nullable = false, length = 100)
    String email;

    @Column(name = "full_name", nullable = false, length = 100)
    String fullName;

    @Column(length = 20)
    String phone;

    @Enumerated(EnumType.STRING)
    Role role = Role.USER;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    Boolean active = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Borrowing> borrowings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<UserLog> userLogs;


}
