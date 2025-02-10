package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.DTOs.OrderDTO;

import java.io.IOException;

public interface ExcelExportService {

    public byte[] exportOrderToExcel(OrderDTO orderDTO) throws IOException;


}
