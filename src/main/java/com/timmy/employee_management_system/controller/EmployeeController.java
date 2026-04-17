package com.timmy.employee_management_system.controller;

import com.timmy.employee_management_system.dto.CreateEmployeeDto;
import com.timmy.employee_management_system.dto.ImportEmployeesExcelResDto;
import com.timmy.employee_management_system.entity.Employee;
import com.timmy.employee_management_system.service.EmployeeService;
import com.timmy.employee_management_system.service.ExcelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Validated
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ExcelService excelService;

    @PostMapping("/employees")
    Employee handleCreateEmployee (CreateEmployeeDto e){
        return employeeService.createEmployee(e);
    }


    @PostMapping("/employees/import")
    public ResponseEntity<?>uploadEmployees(@RequestParam("file")MultipartFile file){
        ImportEmployeesExcelResDto res = excelService.importEmployees(file);
        if(!res.getErrors().isEmpty()){
           return ResponseEntity.badRequest().body(res);
        }
        return ResponseEntity.ok(res);
    }

}

