package com.timmy.employee_management_system.service;
import com.timmy.employee_management_system.dto.CreateEmployeeDto;
import com.timmy.employee_management_system.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(CreateEmployeeDto e);

    String createEmployees(List<CreateEmployeeDto> dtos);

    List<Employee> findByDepartmentAndActiveDept(String department, boolean active);
}
