package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderRepository orderRepository;

    private final ExcelExportService excelExportService;

    public OrderRestController(ExcelExportService excelExportService) {
        this.excelExportService = excelExportService;
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportOrder(@PathVariable Long id) throws IOException {
        // Simulate fetching an orderDTO from the database
        Order order = orderRepository.getReferenceById(id); // Replace with real service call

        byte[] excelFile = excelExportService.exportOrderToExcel(order);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_" + id + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
}
