package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.service.Impl.PreOrderServiceImpl;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preorder")
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
                preOrderService.updateItems(preOrderDTO, preOrderDTO.id());
            }
            return ResponseEntity.ok("Bulk updates processed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing updates: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deletePreOrderItem(@PathVariable Long id,
                                                                  @RequestParam String brand) {
        try {
            preOrderService.deletePreOrderArticle(id, brand);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item deleted successfully");
            response.put("deletedId", id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error deleting item: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
