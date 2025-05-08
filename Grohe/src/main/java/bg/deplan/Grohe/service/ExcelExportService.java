package bg.deplan.Grohe.service;

import java.io.IOException;

public interface ExcelExportService {

    public byte[] exportOrderToExcel(long id, String orderNum) throws IOException;



}
