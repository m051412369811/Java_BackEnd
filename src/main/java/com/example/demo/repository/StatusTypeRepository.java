package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.StatusType;

public interface StatusTypeRepository extends JpaRepository<StatusType, Integer> {

}
