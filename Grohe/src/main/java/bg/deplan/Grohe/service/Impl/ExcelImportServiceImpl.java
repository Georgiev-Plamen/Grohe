package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;
import bg.deplan.Grohe.service.ExcelImportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelImportServiceImpl implements ExcelImportService {
    @Override
    public List<PreOrderExcelDTO> readPreOrderFromExcel(InputStream inputStream, String brand) throws IOException {
        // List to store the parsed DTOs
        List<PreOrderExcelDTO> preOrderExcelDTOList = new ArrayList<>();

        String artNumName = "Код на производител";
        String quantityName = "Количество";
        String commentName = "Забележка";
        int targetRowIndex = 8;

        // Use the provided InputStream to read the Excel file
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            // Assuming the data is in the first sheet
            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum() - 3;

            // Find index of : artNumCell / quantityCell / commentCell
            int artNumRow = findColumnIndex(sheet, targetRowIndex, artNumName);
            int quantityRow = findColumnIndex(sheet, targetRowIndex, quantityName);
            int commentRow = findColumnIndex(sheet, targetRowIndex, commentName);

            // Iterate over rows, starting from the 10th row (skip the header)
            for (int i = 9; i < lastRow; i++) {
                Row row = sheet.getRow(i);

                if (row != null) {
                    // Read article number
                    Cell artNumCell = row.getCell(artNumRow);
                    String artNum;
                    if (artNumCell.getCellType() == CellType.NUMERIC) {
                        artNum = String.valueOf((long) artNumCell.getNumericCellValue());
                    } else {
                        artNum = artNumCell.getStringCellValue();
                    }

                    // Read quantity for order
                    Cell quantityCell = row.getCell(quantityRow);
                    String quantityForOrder;
                    if (quantityCell.getCellType() == CellType.NUMERIC) {
                        quantityForOrder = String.valueOf((int) quantityCell.getNumericCellValue());
                    } else {
                        quantityForOrder = quantityCell.getStringCellValue();
                    }

                    // Read comment
                    String comment = row.getCell(commentRow).getStringCellValue();

                    // Create DTO and add it to the list
                    PreOrderExcelDTO preOrderExcelDTO = new PreOrderExcelDTO(
                            brand,
                            artNum,
                            quantityForOrder,
                            LocalDate.now(), // Use current date
                            comment
                    );

                    preOrderExcelDTOList.add(preOrderExcelDTO);
                }
            }
        }

        return preOrderExcelDTOList;
    }

    private static int findColumnIndex (Sheet sheet, int rowIndex, String searchName) {
        Row row = sheet.getRow(rowIndex); // Get the specific row
        if (row == null) {
            return -1; // Row doesn't exist
        }

        for (Cell cell : row) {
            if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(searchName)) {
                return cell.getColumnIndex(); // Return the column index (0-based)
            }
        }

        return -1; // Return -1 if the name is not found in the row
    }
}
