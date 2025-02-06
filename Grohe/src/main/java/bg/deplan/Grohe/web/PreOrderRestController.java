package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preorder")
public class PreOrderRestController {

    @PostMapping("/receive")
    public String receivePreOrder(@RequestBody PreOrderDTO preOrderDTO) {
        System.out.println("Received PreOrder: " + preOrderDTO.artNum());
        return "PreOrder received successfully!";
    }
}
