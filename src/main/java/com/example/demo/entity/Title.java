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
    private Integer id;

    @Column(nullable = false, length = 45)
    private String titleName; // 對應到 title_name

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY)
    private List<Employee> employees;

    // getters and setters
}
