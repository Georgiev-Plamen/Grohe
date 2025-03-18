package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ExcelImportService {

    List<PreOrderExcelDTO> readPreOrderFromExcel(InputStream inputStream, String brand) throws IOException;
}
