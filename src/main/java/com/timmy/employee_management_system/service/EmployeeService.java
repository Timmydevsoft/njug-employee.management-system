package com.timmy.employee_management_system.service;
import com.timmy.employee_management_system.dto.CreateEmployeeDto;
import com.timmy.employee_management_system.dto.PartialUpdateEmployeeDto;
import com.timmy.employee_management_system.entity.Employee;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(CreateEmployeeDto e);

    String createEmployees(List<CreateEmployeeDto> dtos);

    List<Employee> findByDepartmentAndActiveDept(String department, boolean active);

    Page<Employee> getAllEmployees(int page, int size);

    Employee getEmployeeById(Long id);

    Employee updateEmployee(Long id, CreateEmployeeDto dto);

    Employee partialUpdateEmployee(Long id, PartialUpdateEmployeeDto dto);

    void softDeleteEmployee(Long id);

    void hardDeleteEmployee(Long id);

    List<Employee> getEmployeesBySalaryRange(BigDecimal min, BigDecimal max);
}
