package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AttendanceRecord;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {

}
