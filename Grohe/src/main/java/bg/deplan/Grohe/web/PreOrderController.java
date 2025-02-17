package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.ExcelExportService;
import bg.deplan.Grohe.service.OrderService;
import bg.deplan.Grohe.service.PreOrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/orders")
public class PreOrderController {
    @Autowired
    private PreOrderService preOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PreOrderItemRepository preOrderRepository;
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private ArticleService articleService;

    @ModelAttribute("preOrderData")
    public PreOrderDTO preOrderDTO() {
        return new PreOrderDTO(0l,"","","","",LocalDate.now(), "","");
    }

    @GetMapping("/preOrder")
    public String preOrder(Model model) {

        model.addAttribute("preOrderData", preOrderDTO());
        model.addAttribute("allArticle", preOrderService.getAllArticle("Grohe"));
        model.addAttribute("article", preOrderDTO());

        return "preOrder";
    }

    @GetMapping("/preOrderViega")
    public String preOrderViega(Model model) {

        model.addAttribute("preOrderData", preOrderDTO());
        model.addAttribute("allArticle", preOrderService.getAllArticle("Viega"));
        model.addAttribute("article", preOrderDTO());

        return "preOrderViega";
    }

    @PostMapping("/preOrder")
    public String addToPreOrder(@ModelAttribute ArticleDTO articleDTO) {

        preOrderService.addItem(articleDTO);

        if(articleDTO.brand().equals("Viega")) {
            return "redirect:/orders/preOrderViega";
        }

        return "redirect:/orders/preOrder";
    }

    @PostMapping("/updatePreOrder/{id}")
    public String updateArticle(@PathVariable ("id") Long id, @ModelAttribute PreOrderDTO preOrderDTO ) {

       preOrderService.updateItems(preOrderDTO, id);

        boolean isViega = false;
        if(preOrderService.findById(id).getArticle().getBrand().equals("Viega")) {
            isViega = true;
        }

        if(isViega) {
            return "redirect:/orders/preOrderViega";
        }

        return "redirect:/orders/preOrder";
    }

    @DeleteMapping("/delete/{id}")
    public String deletePreOrderArticle(@PathVariable ("id") Long id) {
        boolean isViega = false;

        if(preOrderService.findById(id).getArticle().getBrand().equals("Viega")) {
            isViega = true;
        }
        preOrderService.deletePreOrder(id);

        if(isViega) {
            return "redirect:/orders/preOrderViega";
        }

        return "redirect:/orders/preOrder";
    }

    @PostMapping("/makeOrder")
    public ResponseEntity<byte[]> makeOrder(@RequestParam("name") String name) throws IOException {
        String brand = "Grohe";

        // Create the order
        boolean isOrderCreated = preOrderService.createAndExportOrder(name, brand);

        if (!isOrderCreated) {
            // Handle the case where the order creation failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Get the last order ID
        Long lastOrderId = orderService.lastOrderId();

        if (lastOrderId == null) {
            // Handle the case where the last order ID is not available
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Export the order to Excel
        byte[] excelFile = excelExportService.exportOrderToExcel(lastOrderId);

        if (excelFile == null) {
            // Handle the case where the Excel file generation failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Return the Excel file as a response
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Order " + lastOrderId + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
//        return ResponseEntity.ok().body("Successfully create order");
    }

    @PostMapping("/makeOrderViega")
    public String makeOrderViega (@RequestParam ("name") String name) {
        String brand = "Viega";
        preOrderService.makeOrder(name,brand);

        return "redirect:/orders/preOrderViega";
    }

    @GetMapping("/importFromExcel")
    public String showUploadForm() {
        return "uploadForm"; // Return the name of the HTML template for the upload form
    }


    //TODO: need to create redirectAttributes for errors
    @PostMapping("/importFromExcel")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
    @RequestParam ("brand") String brand) throws IOException {
        if (!file.isEmpty()) {
            preOrderService.readPreOrderFromExcel(file.getInputStream(), brand);
        } else {
            // Handle the case where the file is empty
            throw new IllegalArgumentException("File is empty");
        }

        return "redirect:/orders/preOrder";
    }

}
