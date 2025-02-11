package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.Order;

import java.io.IOException;

public interface ExcelExportService {

    public byte[] exportOrderToExcel(Order order) throws IOException;


}
