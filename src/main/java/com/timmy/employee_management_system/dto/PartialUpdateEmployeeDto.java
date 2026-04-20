package com.timmy.employee_management_system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PartialUpdateEmployeeDto {
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private BigDecimal salary;
    private String position;
    private LocalDate dateOfJoining;
    private Boolean active;
}
