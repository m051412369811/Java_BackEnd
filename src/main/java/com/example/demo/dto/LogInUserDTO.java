package com.example.demo.dto;

import lombok.Data;

@Data
public class LogInUserDTO {

    private Integer id;
    private String name;
    // 可以加上角色等需要的欄位

    public LogInUserDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
