package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.service.Impl.PreOrderServiceImpl;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders/api/preorder")
public class PreOrderRestController {

    private PreOrderService preOrderService;

    public PreOrderRestController(PreOrderService preOrderService) {
        this.preOrderService = preOrderService;
        }

    @PostMapping("/receive/bulkUpdatePreOrder")
    public ResponseEntity<String> bulkUpdatePreOrder(@RequestBody List<PreOrderDTO> updates,
                                                     @RequestParam(required = false) String _method) {
        try {
            for (PreOrderDTO preOrderDTO : updates) {
                preOrderService.updateItems(preOrderDTO, preOrderDTO.id()); // Assuming an update method exists
            }
            return ResponseEntity.ok("Bulk updates processed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing updates: " + e.getMessage());
        }
    }
}
