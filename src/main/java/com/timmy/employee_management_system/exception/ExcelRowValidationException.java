package com.timmy.employee_management_system.exception;

public class ExcelRowValidationException extends RuntimeException{
    public  ExcelRowValidationException(String message){
        super(message);
    }
}
