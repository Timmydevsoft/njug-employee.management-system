package com.timmy.employee_management_system.service.impl;
import com.timmy.employee_management_system.dto.CreateEmployeeDto;
import com.timmy.employee_management_system.dto.ImportEmployeesExcelResDto;
import com.timmy.employee_management_system.entity.Employee;
import com.timmy.employee_management_system.exception.ExcelProcessingException;
import com.timmy.employee_management_system.exception.InvalidFileFormatException;
import com.timmy.employee_management_system.service.EmployeeService;
import com.timmy.employee_management_system.service.ExcelService;
import com.timmy.employee_management_system.utils.ExcelCellMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    private final EmployeeService employeeService;
    private final Validator validator;


    private CreateEmployeeDto mapRowToDto(Row row) {

        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setFirstName(ExcelCellMapper.getString(row.getCell(0)));
        dto.setLastName(ExcelCellMapper.getString(row.getCell(1)));
        dto.setEmail(ExcelCellMapper.getString(row.getCell(2)));
        dto.setDepartment(ExcelCellMapper.getString(row.getCell(3)));
        dto.setSalary(ExcelCellMapper.getBigDecimal(row.getCell(4)));
        dto.setPosition(ExcelCellMapper.getString(row.getCell(5)));
        dto.setDateOfJoining(ExcelCellMapper.getLocalDate(row.getCell(6)));
        dto.setActive(ExcelCellMapper.getBoolean(row.getCell(7)));
        return dto;
    }



    @Override
    @Transactional
    public ImportEmployeesExcelResDto importEmployees(MultipartFile file) {

        List<String> errors = new ArrayList<>();
        List<CreateEmployeeDto> validEmpDtos = new ArrayList<>();

        if (file == null || file.getOriginalFilename() == null ||
                !file.getOriginalFilename().endsWith(".xlsx")) {
            throw new InvalidFileFormatException(
                    "Invalid file type, Only Excel (.xlsx) files are allowed"
            );
        }

        try (
                InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)
        ) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) continue;

                CreateEmployeeDto dto = mapRowToDto(row);

                Set<ConstraintViolation<CreateEmployeeDto>> violations =
                        validator.validate(dto);

                if (!violations.isEmpty()) {
                    for (ConstraintViolation<CreateEmployeeDto> v : violations) {
                        errors.add(String.format(
                                "Row %d: %s - %s",
                                i + 1,
                                v.getPropertyPath(),
                                v.getMessage()
                        ));
                    }
                    continue;
                }



                if (dto.getPosition() == null || dto.getPosition().isBlank()) {
                    errors.add("Row " + (i + 1) + ": position is required");
                    continue;
                }

                validEmpDtos.add(dto);
            }

        } catch (Exception ex) {
            throw new ExcelProcessingException("Error processing Excel file", ex);
        }
        int successCount = validEmpDtos.size();
        int failureCount = errors.size();
        ImportEmployeesExcelResDto result = new ImportEmployeesExcelResDto(successCount, failureCount, errors);
        //Stop everything if any error exist
        if (!errors.isEmpty()) {
            return result;
        }

        //  only valid data reaches service
        employeeService.createEmployees(validEmpDtos);

        return result;
    }

    @Override
    public void exportEmployees(String department, Boolean active, HttpServletResponse response){
        List<Employee> employees = employeeService.findByDepartmentAndActiveDept(department, active);
    }

}
