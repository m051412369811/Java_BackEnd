package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Title;

public interface TitleRepository extends JpaRepository<Title, Integer> {

}
