package com.timmy.employee_management_system.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PdfService {
    void exportEmployeesPdf(HttpServletResponse response) throws IOException;
}
