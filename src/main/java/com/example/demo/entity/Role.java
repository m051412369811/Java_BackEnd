package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name", nullable = false, length = 45, unique = true)
    private String roleName;

    // mappedBy 指向 Employee Entity 中用來維護關聯的屬性 "roles"
    // 這定義了 Role 到 Employee 的反向關係，通常較少直接使用
    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude // 避免在 toString() 中產生無限遞迴
    @EqualsAndHashCode.Exclude // 避免在 equals/hashCode 中產生無限遞迴
    private Set<Employee> employees;
}
