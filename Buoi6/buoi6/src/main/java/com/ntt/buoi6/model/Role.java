package com.ntt.buoi6.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ví dụ: "ROLE_ADMIN", "ROLE_USER"
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
}
