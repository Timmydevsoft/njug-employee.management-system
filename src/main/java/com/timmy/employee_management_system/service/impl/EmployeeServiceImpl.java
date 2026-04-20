package com.timmy.employee_management_system.service.impl;

import com.timmy.employee_management_system.Repository.EmployeeRepository;
import com.timmy.employee_management_system.dto.CreateEmployeeDto;
import com.timmy.employee_management_system.dto.PartialUpdateEmployeeDto;
import com.timmy.employee_management_system.entity.Employee;
import com.timmy.employee_management_system.enums.Position;
import com.timmy.employee_management_system.exception.DuplicateEmailException;
import com.timmy.employee_management_system.exception.EmployeeNotFoundException;
import com.timmy.employee_management_system.exception.InvalidSalaryException;
import com.timmy.employee_management_system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(CreateEmployeeDto e){
        Optional<Employee> isAnEmployee = employeeRepository.findByEmail(e.getEmail());
        if(isAnEmployee.isPresent()){
            throw new DuplicateEmailException("Email has been taken");
        }
        Position position = Position.valueOf(e.getPosition());
        boolean isIntern = position ==Position.INTERN;
        boolean salaryIsLessThan30k = e.getSalary().compareTo(new BigDecimal(30000))<0;
        boolean salaryIsLessThan15k = e.getSalary().compareTo(new BigDecimal("15000"))<0;

        if(!isIntern && salaryIsLessThan30k){
            throw new InvalidSalaryException("Salary must not be less than 30000 an employee that is not an INTERN");
        }

        if(isIntern && salaryIsLessThan15k){
            throw new InvalidSalaryException("Salary must not be less than 15000 for an INTERN");
        }


         Employee newEmployee =  Employee.builder()
                 .firstName(e.getFirstName())
                 .lastName(e.getLastName())
                 .email(e.getEmail())
                 .salary(e.getSalary())
                 .department(e.getDepartment())
                 .position(position)
                 .dateOfJoining(e.getDateOfJoining().atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli())
                 .active(e.getActive())
                 .build();
         return employeeRepository.save(newEmployee);
    }

    @Override
    @Transactional
    public String createEmployees(List<CreateEmployeeDto> dtos) {

        List<Employee> employees = new ArrayList<>();

        for (CreateEmployeeDto e : dtos) {

            if (employeeRepository.findByEmail(e.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists: " + e.getEmail());
            }

            Position position = Position.valueOf(e.getPosition().toUpperCase());

            Employee employee = Employee.builder()
                    .firstName(e.getFirstName())
                    .lastName(e.getLastName())
                    .email(e.getEmail())
                    .salary(e.getSalary())
                    .department(e.getDepartment())
                    .position(position)
                    .dateOfJoining(
                            e.getDateOfJoining()
                                    .atStartOfDay(ZoneId.of("UTC"))
                                    .toInstant()
                                    .toEpochMilli()
                    )
                    .active(e.getActive())
                    .build();

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);
        return "Records saved successfully";
    }

    @Override
    public List<Employee> findByDepartmentAndActiveDept(String department, boolean active){
        return employeeRepository.findByDepartmentAndActive(department, active);
    }

    @Override
    public Page<Employee> getAllEmployees(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee getEmployeeById(Long id){
        Optional<Employee> emp = employeeRepository.findById(id);
        return emp.orElseThrow(()-> new EmployeeNotFoundException("Employee with id "+id+" not found"));
    }

    @Override
    public  Employee updateEmployee(Long id, CreateEmployeeDto dto){
        Employee emp = employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("Employee with id "+id+" not found"));
       if(!emp.getEmail().equals(dto.getEmail())){
           employeeRepository.findByEmail(dto.getEmail()).ifPresent((e)->{
               throw new DuplicateEmailException("Email is already taken");
           });
       }

        Position position = Position.valueOf(dto.getPosition());

       emp.setFirstName(dto.getFirstName());
       emp.setLastName(dto.getLastName());
        emp.setEmail(dto.getEmail());
        emp.setSalary(dto.getSalary());
        emp.setDepartment(dto.getDepartment());
        emp.setPosition(position);
        emp.setDateOfJoining(dto.getDateOfJoining().atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli());
        emp.setActive(dto.getActive());
        return employeeRepository.save(emp);

    }

    @Override
    public Employee partialUpdateEmployee(Long id, PartialUpdateEmployeeDto dto){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->
                new EmployeeNotFoundException("Employee with id: "+id+" not found"));

        if (dto.getFirstName() != null) {
            employee.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            employee.setLastName(dto.getLastName());
        }

        if (dto.getEmail() != null) {
            if(!employee.getEmail().equals(dto.getEmail())){
                employeeRepository.findByEmail(dto.getEmail()).ifPresent((e)->{
                    throw new DuplicateEmailException("Email is already taken");
                });
            }
            employee.setEmail(dto.getEmail());
        }

        if (dto.getDepartment() != null) {
            employee.setDepartment(dto.getDepartment());
        }

        if (dto.getSalary() != null) {
            employee.setSalary(dto.getSalary());
        }

        if (dto.getActive() != null) {
            employee.setActive(dto.getActive());
        }

        if (dto.getPosition() != null) {
            Position position = Position.valueOf(dto.getPosition());
            employee.setPosition(position);
        }

        if (dto.getDateOfJoining() != null) {
            employee.setDateOfJoining(
                    dto.getDateOfJoining()
                            .atStartOfDay(ZoneId.of("UTC"))
                            .toInstant()
                            .toEpochMilli()
            );
        }

        return employeeRepository.save(employee);
    }

    @Override
    public void softDeleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->
                new EmployeeNotFoundException("Employee with id "+id + " not found")
                );

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    @Override
    public void hardDeleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->
                new EmployeeNotFoundException("Employee with id "+id + " not found")
        );

        if(employee.getActive()){
            employee.setActive(false);
            employeeRepository.save(employee);
        }
        else{
            employeeRepository.delete(employee);
        }
    }
}
