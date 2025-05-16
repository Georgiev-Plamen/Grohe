package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.OrderItemRepository;
import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.OrderItem;
import bg.deplan.Grohe.service.ExcelExportService;
import bg.deplan.Grohe.service.OrderService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    public ExcelExportServiceImpl(OrderRepository orderRepository,
                                  OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public byte[] exportOrderToExcel(long id, String orderNum) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Order Export");
            sheet.setHorizontallyCenter(true);
            sheet.setVerticallyCenter(true);
            CellStyle cellStyle = workbook.createCellStyle();


            Font workbookFont = workbook.createFont();
            workbookFont.setBold(true);
            workbookFont.setFontHeightInPoints((short) 11);  // Font size 12
            workbookFont.setFontName("Arial");
            workbookFont.setColor(IndexedColors.BLACK.getIndex());  // Set font color
            cellStyle.setFont(workbookFont);  // Attach font
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setWrapText(true);

//            cellStyle.setBorderTop(BorderStyle.MEDIUM);
//            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
//            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
//            cellStyle.setBorderRight(BorderStyle.MEDIUM);

            //Column adjust
            sheet.setColumnWidth(0, 3820);
            sheet.setColumnWidth(1, 11776);
            sheet.setColumnWidth(2, 3803);
            sheet.setColumnWidth(3, 9070);

            int rowIndex = 0;

            Order order = orderRepository.getReferenceById(id);
            order.setItems(orderItemRepository.findAllByOrderId(id));

            // Header: Order Information
            rowIndex = createOrderHeader(sheet, order, rowIndex, cellStyle, orderNum);

            // Sub-header: Article List
            rowIndex = createArticleTableHeader(sheet, rowIndex, workbook);
            int counter = 1;

            List <OrderItem> articles = order.getItems();

            // Populate article data
            for (OrderItem article : order.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                Cell cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(counter);

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(article.getArticle().getArtNum());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(article.getQuantity());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(article.getOrderReason());

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

            CellRangeAddress headerRange = new CellRangeAddress(1, 5, 0, 1);
            CellRangeAddress articleRange = new CellRangeAddress(9, rowIndex-1, 0, 3);
            CellRangeAddress articleHeaderRange = new CellRangeAddress(8,8,0,3);

            setInnerBorder(headerRange, sheet);
            setInnerBorder(articleRange, sheet);
            setInnerBorder(articleHeaderRange,sheet);

            setOutsideBorderToRange(headerRange, sheet);
            setOutsideBorderToRange(articleRange, sheet);
            setOutsideBorderToRange(articleHeaderRange, sheet);

            // Convert to byte array for download
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static void setOutsideBorderToRange(CellRangeAddress headerRange, Sheet sheet) {
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, headerRange, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, headerRange, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, headerRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, headerRange, sheet);
    }

    private static void setInnerBorder(CellRangeAddress header, Sheet sheet) {
        for (int row = header.getFirstRow(); row <= header.getLastRow(); row++) {
            for (int col = header.getFirstColumn(); col <= header.getLastColumn(); col++) {
                Cell cell = sheet.getRow(row).getCell(col);
                if (cell == null) {
                    cell = sheet.getRow(row).createCell(col);
                }

                // Set thin borders for all sides (will be overridden by outer borders)
                CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_TOP, BorderStyle.THIN);
                CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_BOTTOM, BorderStyle.THIN);
                CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_LEFT, BorderStyle.THIN);
                CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_RIGHT, BorderStyle.THIN);
            }
        }
    }

    private int createOrderHeader(Sheet sheet, Order order, int rowIndex, CellStyle cellStyle, String orderNum) {
        rowIndex++;

        Row row = sheet.createRow(rowIndex++) ;

        Cell cell = row.createCell(0);
        row.setHeightInPoints(30);
        cell.setCellValue("Customer's name");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Deplan Ltd");
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(rowIndex++);

        cell = row.createCell(0);
        cell.setCellValue("SAP Number");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        row.setHeightInPoints(27);
        cell.setCellValue("12401");
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(rowIndex++);

        cell = row.createCell(0);
        row.setHeightInPoints(30);
        cell.setCellValue("Order reason");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Supply warehouse,  samples, projects and other");
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(rowIndex++);

        cell = row.createCell(0);
        row.setHeightInPoints(33);
        cell.setCellValue("Number of the order");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("D/" + orderNum + "/25");
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(rowIndex++);

        cell = row.createCell(0);
        cell.setCellValue("data");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue(order.getDate().format(DateTimeFormatter.ISO_DATE));
        cell.setCellStyle(cellStyle);

        rowIndex++;
        rowIndex++;

        return rowIndex;
    }

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

//        headerStyle.setBorderTop(BorderStyle.MEDIUM);
//        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
//        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
//        headerStyle.setBorderRight(BorderStyle.MEDIUM);
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
