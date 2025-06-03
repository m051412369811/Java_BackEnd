package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

// 職稱
@Entity
@Data
@Table(name = "titles")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TitleID")
    private Integer id;

    @Column(name = "TitleName", nullable = false, length = 45)
    private String name;

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY)
    private List<Employee> employees;

    // getters and setters
}
