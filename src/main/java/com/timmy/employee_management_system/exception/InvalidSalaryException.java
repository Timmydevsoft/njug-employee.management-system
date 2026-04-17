package com.timmy.employee_management_system.exception;

public class InvalidSalaryException extends RuntimeException{
    public InvalidSalaryException(String message){
        super(message);
    }
}
