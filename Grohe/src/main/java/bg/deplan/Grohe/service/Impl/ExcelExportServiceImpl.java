package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.OrderItem;
import bg.deplan.Grohe.service.ExcelExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    public byte[] exportOrderToExcel(OrderDTO orderDTO) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Order Export");
            int rowIndex = 0;

            // Header: Order Information
            rowIndex = createOrderHeader(sheet, orderDTO, rowIndex);

            // Sub-header: Article List
            rowIndex = createArticleTableHeader(sheet, rowIndex);

            // Populate article data
            for (OrderItem article : orderDTO.articleList()) {
                Row row = sheet.createRow(rowIndex++);
//                row.createCell(0).setCellValue(article.getArtNum());
                row.createCell(1).setCellValue(article.getQuantity());
                row.createCell(2).setCellValue(article.getOrderReason());
            }

            // Auto-size columns for better formatting
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convert to byte array for download
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private int createOrderHeader(Sheet sheet, OrderDTO orderDTO, int rowIndex) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue("Order Name:");
        row.createCell(1).setCellValue(orderDTO.orderName());

        row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue("Order Date:");
        row.createCell(1).setCellValue(orderDTO.date().format(DateTimeFormatter.ISO_DATE));

        rowIndex++;  // Leave an empty row for spacing
        return rowIndex;
    }

    private int createArticleTableHeader(Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue("Article Number");
        row.createCell(1).setCellValue("Quantity");
        row.createCell(2).setCellValue("Order Reason");
        return rowIndex;
    }
}
