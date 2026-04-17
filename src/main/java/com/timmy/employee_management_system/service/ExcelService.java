package com.timmy.employee_management_system.service;

import com.timmy.employee_management_system.dto.ImportEmployeesExcelResDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelService {
    ImportEmployeesExcelResDto importEmployees(MultipartFile file);

//    byte[] exportEmployees();

    void exportEmployees(String department, Boolean active, HttpServletResponse response);
}
