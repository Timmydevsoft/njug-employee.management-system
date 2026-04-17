package com.timmy.employee_management_system.exception;

public class InvalidFileFormatException extends RuntimeException{
    public InvalidFileFormatException(String message){
        super(message);
    }
}
