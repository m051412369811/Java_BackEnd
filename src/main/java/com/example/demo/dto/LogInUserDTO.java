package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInUserDTO {

    private Integer id;
    private String name;
    // 可以加上角色等需要的欄位

}
