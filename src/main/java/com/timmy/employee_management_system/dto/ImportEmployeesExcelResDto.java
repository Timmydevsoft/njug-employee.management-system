package com.timmy.employee_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportEmployeesExcelResDto {
    private int successCount;
    private int failureCount;
    private List<String> errors;

}
