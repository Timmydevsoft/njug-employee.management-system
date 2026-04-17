package com.timmy.employee_management_system.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExcelCellMapper {
    public static String getString(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    public static BigDecimal getBigDecimal(Cell cell) {
        if (cell == null) return null;

        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING -> new BigDecimal(cell.getStringCellValue().trim());
                default -> null;
            };
        } catch (Exception e) {
            return null; // let validation catch it
        }
    }

    public static LocalDate getLocalDate(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }

            if (cell.getCellType() == CellType.STRING) {
                return LocalDate.parse(cell.getStringCellValue().trim());
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static Boolean getBoolean(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case BOOLEAN -> cell.getBooleanCellValue();

            case NUMERIC -> cell.getNumericCellValue() == 1;

            case STRING -> {
                String value = cell.getStringCellValue().trim().toLowerCase();
                yield value.equals("true") || value.equals("yes") || value.equals("1");
            }

            default -> null;
        };
    }

}
