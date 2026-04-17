package com.timmy.employee_management_system.dto;


import com.timmy.employee_management_system.enums.Position;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class CreateEmployeeDto {
    @NotBlank(message = "firstName is required")
    @Size(max = 50, message = "firstName must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(max = 50, message = "firstName must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "department is required")
    @Size(max = 100, message = "department must not exceed 100 characters")
    private String department;

    @DecimalMin("0.00")
    private BigDecimal salary;

    @NotBlank
    private String position;

    @NotNull
    @PastOrPresent
    private LocalDate dateOfJoining;

    @NotNull
    private Boolean active;

}
