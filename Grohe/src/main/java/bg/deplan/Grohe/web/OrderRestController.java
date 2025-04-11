package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleFindDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.DTOs.OrderEditArticleDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.service.ExcelExportService;
import bg.deplan.Grohe.service.OrderService;
import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderRepository orderRepository;

    private final ExcelExportService excelExportService;
    @Autowired
    private OrderService orderService;

    public OrderRestController(ExcelExportService excelExportService) {
        this.excelExportService = excelExportService;
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportOrder(@PathVariable Long id) throws IOException {
        String orderNum = orderService.lastOrderNumber();
        byte[] excelFile = excelExportService.exportOrderToExcel(id, orderNum);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_" + id + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }

    @PostMapping("/receive/bulkUpdateOrder")
    public ResponseEntity<String> bulkUpdateArticle(@RequestBody List<OrderEditArticleDTO> updates) {
        try {
            orderService.bulkUpdateArticle(updates);
            return ResponseEntity.ok().body("Update is successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing updates: " + e.getMessage());
        }
    }

}
