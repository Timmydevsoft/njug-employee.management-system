package com.timmy.employee_management_system.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportEmployeesExcelResDto {
    private int successCount;
    private int failureCount;
    private List<String> errors;

}
