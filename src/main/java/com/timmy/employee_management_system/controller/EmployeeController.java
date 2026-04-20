package com.timmy.employee_management_system.controller;

import com.timmy.employee_management_system.dto.CreateEmployeeDto;
import com.timmy.employee_management_system.dto.ImportEmployeesExcelResDto;
import com.timmy.employee_management_system.entity.Employee;
import com.timmy.employee_management_system.service.EmployeeService;
import com.timmy.employee_management_system.service.ExcelService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    Employee handleCreateEmployee ( @RequestBody @Valid CreateEmployeeDto e){
        System.out.println("emoloyee: "+ e.toString());
        return employeeService.createEmployee(e);
    }


    @PostMapping("/employees/import/excel")
    public ResponseEntity<?>uploadEmployees(@RequestParam("file")MultipartFile file){
        ImportEmployeesExcelResDto res = excelService.importEmployees(file);
        if(!res.getErrors().isEmpty()){
           return ResponseEntity.badRequest().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/employees")
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size ){
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}

