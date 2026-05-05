package com.timmy.employee_management_system.service.impl;

import com.timmy.employee_management_system.service.EmailService;
import com.timmy.employee_management_system.service.ExcelService;
import com.timmy.employee_management_system.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {
    private final EmailService emailService;
    private final ExcelService excelService;
    @Override
    public String shareEmployeeRecToMail(String email) throws Exception {
        ByteArrayInputStream file = excelService.exportEmployeesStream();
        emailService.sendEmailWithAttachment(email, file);
        return "File sent successfully";
    }
}
