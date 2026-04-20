package com.timmy.employee_management_system.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.timmy.employee_management_system.Repository.EmployeeRepository;
import com.timmy.employee_management_system.entity.Employee;
import com.timmy.employee_management_system.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {
    private final EmployeeRepository employeeRepository;

    @Override
    public void exportEmployeesPdf(HttpServletResponse response) throws IOException {
        List<Employee> employees = employeeRepository.findAll();

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // =========================
        // TITLE PAGE HEADER
        // =========================
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font metaFont = new Font(Font.HELVETICA, 12);

        Paragraph title = new Paragraph("EMPLOYEE REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Company: Timmy Tech Solutions", metaFont));
        document.add(new Paragraph("Generated: " + LocalDateTime.now(), metaFont));
        document.add(new Paragraph("Total Employees: " + employees.size(), metaFont));

        document.add(Chunk.NEWLINE);

        // =========================
        // TABLE SETUP
        // =========================
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        float[] widths = {1f, 3f, 4f, 3f, 2f, 3f, 2f};
        table.setWidths(widths);

        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Color headerBg = new Color(0, 102, 204);

        String[] headers = {
                "ID", "Full Name", "Email", "Department",
                "Salary", "Date of Joining", "Status"
        };

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerBg);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // =========================
        // ROWS
        // =========================
        Font normalFont = new Font(Font.HELVETICA, 10);
        Font inactiveFont = new Font(Font.HELVETICA, 10, Font.STRIKETHRU);

        boolean alternate = false;

        for (Employee e : employees) {
            Color rowColor = alternate ? new Color(245, 245, 245) : Color.WHITE;
            alternate = !alternate;

            Font rowFont = e.getActive() ? normalFont : inactiveFont;

            addCell(table, String.valueOf(e.getId()), rowFont, rowColor);
            addCell(table, e.getFirstName() + " " + e.getLastName(), rowFont, rowColor);
            addCell(table, e.getEmail(), rowFont, rowColor);
            addCell(table, e.getDepartment(), rowFont, rowColor);

            PdfPCell salaryCell = new PdfPCell(new Phrase("₦" + e.getSalary(), rowFont));
            salaryCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            salaryCell.setBackgroundColor(rowColor);
            table.addCell(salaryCell);

            addCell(table, String.valueOf(e.getDateOfJoining()), rowFont, rowColor);
            addCell(table, e.getActive() ? "ACTIVE" : "INACTIVE", rowFont, rowColor);
        }

        document.add(table);

        // =========================
        // FOOTER PAGE NUMBERS
        // =========================
        int totalPages = writer.getPageNumber();
        for (int i = 1; i <= totalPages; i++) {

            ColumnText.showTextAligned(
                    writer.getDirectContent(),
                    Element.ALIGN_CENTER,
                    new Phrase("Page " + i + " of " + totalPages),
                    300, 30, 0
            );
        }

        document.close();

    }

    private void addCell(PdfPTable table, String text, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
