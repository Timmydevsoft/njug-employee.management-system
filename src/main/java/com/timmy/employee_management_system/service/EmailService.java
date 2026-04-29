package com.timmy.employee_management_system.service;

import java.io.ByteArrayInputStream;

public interface EmailService {
    public void sendEmailWithAttachment(String to, ByteArrayInputStream file) throws Exception;
}
