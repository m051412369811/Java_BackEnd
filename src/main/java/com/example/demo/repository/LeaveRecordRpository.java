package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.LeaveRecord;

public interface LeaveRecordRpository extends JpaRepository<LeaveRecord, Integer> {

}
