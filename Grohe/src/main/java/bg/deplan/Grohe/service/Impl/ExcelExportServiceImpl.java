package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.OrderItem;
import bg.deplan.Grohe.service.ExcelExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    public byte[] exportOrderToExcel(Order order) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Order Export");
            CellStyle cellStyle = workbook.createCellStyle();

            Font workbookFont = workbook.createFont();
            workbookFont.setBold(true);
            workbookFont.setFontHeightInPoints((short) 12);  // Font size 12
            workbookFont.setFontName("Arial");
            workbookFont.setColor(IndexedColors.BLACK.getIndex());  // Set font color
            cellStyle.setFont(workbookFont);  // Attach font

            //Column adjust
            sheet.setColumnWidth(0, 3803);
            sheet.setColumnWidth(1, 11776);
            sheet.setColumnWidth(2, 3803);
            sheet.setColumnWidth(3, 9070);

            int rowIndex = 0;

            // Header: Order Information
            rowIndex = createOrderHeader(sheet, order, rowIndex);

            // Sub-header: Article List
            rowIndex =
                    createArticleTableHeader(sheet, rowIndex, workbook);
            int counter = 1;

            // Populate article data
            for (OrderItem article : order.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(counter);
                row.createCell(1).setCellValue(article.getArticle().getArtNum());
                row.createCell(2).setCellValue(article.getQuantity());
                row.createCell(3).setCellValue(article.getOrderReason());

                counter++;
            }

//            // Auto-size columns for better formatting
//            for (int i = 0; i < 3; i++) {
//                sheet.autoSizeColumn(i);
//            }

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Convert to byte array for download
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private int createOrderHeader(Sheet sheet, Order order, int rowIndex) {
        rowIndex++;

        Row row = sheet.createRow(rowIndex++) ;

        row.createCell(0).setCellValue("Customer's name");
        row.setHeightInPoints(30);
        row.createCell(1).setCellValue("Deplan Ltd");


        row = sheet.createRow(rowIndex++);

        row.createCell(0).setCellValue("SAP Number");
        row.createCell(1).setCellValue("12401");
        row.setHeightInPoints(27);

        row = sheet.createRow(rowIndex++);

        row.createCell(0).setCellValue("Order reason");
        row.setHeightInPoints(30);
        row.createCell(1).setCellValue("Supply warehouse,  samples, projects and other");

        row = sheet.createRow(rowIndex++);

        row.createCell(0).setCellValue("Number of the order");
        row.setHeightInPoints(33);
        row.createCell(1).setCellValue("D//25");

        row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue("data:");
        row.createCell(1).setCellValue(order.getDate().format(DateTimeFormatter.ISO_DATE));

        rowIndex++;  // Leave an empty row for spacing
        rowIndex++;  // Leave an empty row for spacing


        return rowIndex;
    }

//    private int createArticleTableHeader(Sheet sheet, int rowIndex) {
//        Row row = sheet.createRow(rowIndex++);
//        row.setHeightInPoints(43);
//        row.createCell(0).setCellValue("");
//        row.createCell(1).setCellValue("Product Number");
//        row.createCell(2).setCellValue("Q-ty");
//        row.createCell(3).setCellValue("Order reason");
//
//        return rowIndex;
//    }

    private int createArticleTableHeader(Sheet sheet, int rowIndex, Workbook workbook) {
        Row row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(32);  // Set the row height

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);  // Font size 12
        headerFont.setFontName("Arial");
        headerFont.setColor(IndexedColors.BLACK.getIndex());  // Set font color
        headerStyle.setFont(headerFont);  // Attach font

        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);  // Center alignment
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // Center alignment



        String[] headers = {"", "Product Number", "Q-ty", "Order Reason"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        return rowIndex;
    }
}
