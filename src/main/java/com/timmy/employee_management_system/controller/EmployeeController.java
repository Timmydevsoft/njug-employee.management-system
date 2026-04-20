package com.timmy.employee_management_system.controller;

import com.timmy.employee_management_system.dto.CreateEmployeeDto;
import com.timmy.employee_management_system.dto.ImportEmployeesExcelResDto;
import com.timmy.employee_management_system.dto.PartialUpdateEmployeeDto;
import com.timmy.employee_management_system.entity.Employee;
import com.timmy.employee_management_system.service.EmployeeService;
import com.timmy.employee_management_system.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> fullEmployeeUpdate(@PathVariable Long id, @RequestBody @Valid CreateEmployeeDto dto){
       return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @PatchMapping("/employees/{id}")
    public ResponseEntity<Employee> partialUpdateEmployee(@PathVariable Long id, @RequestBody PartialUpdateEmployeeDto dto){
        return ResponseEntity.ok(employeeService.partialUpdateEmployee(id, dto));
    }

    @DeleteMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDeleteEmployee(@PathVariable Long id){
        employeeService.softDeleteEmployee(id);
    }

    @DeleteMapping("/employees/{id}/hard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void hardDeleteEmployee(@PathVariable Long id){
        employeeService.hardDeleteEmployee(id);
    }

    @GetMapping("/employees/export/excel")
    public void exportEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean active,
            HttpServletResponse response
    ){
        excelService.exportEmployees(department, active, response);
    }

    @GetMapping("/employees/salary-range")
    public ResponseEntity<List<Employee>> getEmployeeBySalaryRange(@PathVariable BigDecimal min, @PathVariable BigDecimal max){
        return ResponseEntity.ok(employeeService.getEmployeesBySalaryRange(min, max));
    }
}

