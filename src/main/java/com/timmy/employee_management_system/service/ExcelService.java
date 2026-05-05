package com.timmy.employee_management_system.service;

import com.timmy.employee_management_system.dto.employee.ImportEmployeesExcelResDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public interface ExcelService {
    ImportEmployeesExcelResDto importEmployees(MultipartFile file);

    ByteArrayInputStream exportEmployeesStream();

    void exportEmployees(String department, Boolean active, HttpServletResponse response);
}
