package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.ExcelExportService;
import bg.deplan.Grohe.service.OrderService;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private static final String GROHE = "Grohe";
    private static final String VIEGA = "Viega";

    @ModelAttribute("preOrderData")
    public PreOrderDTO preOrderDTO() {
        return new PreOrderDTO(0l,"","",0l,"","","",LocalDate.now(), "","",false);
    }

    @GetMapping("/preOrder")
    public String preOrder(@RequestParam(required = false, defaultValue = GROHE) String brand,
                           Model model) {

        model.addAttribute("preOrderData", preOrderDTO());
        model.addAttribute("brand", brand);
        model.addAttribute("allPreOrderItems", preOrderService.getAllPreOrder(brand));
        model.addAttribute("articleList", preOrderService.getAllArticle(brand));
        model.addAttribute("newOrderName", orderService.newOrderName(brand));
        model.addAttribute("lastThreeOrdersNames", orderService.lastThreeOrderName(brand));

        return "preOrder";
    }

    @GetMapping("/preOrderGrohe")
    public String ordersNewGrohe(Model model) {
        return preOrder(GROHE, model);
    }

    @GetMapping("/preOrderViega")
    public String preOrderViega(Model model) {
        return preOrder(VIEGA, model);
    }

    @PostMapping("/preOrder")
    public String addToPreOrder(@ModelAttribute ArticleDTO articleDTO,
                                @AuthenticationPrincipal UserDetails userDetails) {

        preOrderService.addItem(articleDTO, userDetails);

        if(articleDTO.brand().equals(VIEGA)) {
            return "redirect:/orders/preOrderViega";
        }

        return "redirect:/orders/preOrderGrohe";
    }

    @PostMapping("/delete/{id}")
    public String deletePreOrderArticle (@PathVariable ("id") Long id,
                                         @RequestParam("brand") String brand) {

        preOrderService.deletePreOrderArticle(id);

        if(brand.equals(VIEGA)) {
            return "redirect:/orders/preOrderViega";
        }
        return "redirect:/orders/preOrderGrohe";
    }

    @PostMapping("/makeOrder")
    public ResponseEntity<byte[]> makeOrder(@RequestParam("name") String name,
                                            @RequestParam("brand") String brand,
                                            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        // Create the order
        boolean isOrderCreated = preOrderService.createAndExportOrder(name, brand, userDetails);

        if (!isOrderCreated) {
            // Handle the case where the order creation failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Get the last order ID
        Long lastOrderId = orderService.lastOrderId(brand);
        String orderName = orderRepository.getOrdersById(lastOrderId).getOrderName();

        if (lastOrderId == null) {
            // Handle the case where the last order ID is not available
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        byte [] excelFile;
        // Export the order to Excel
        if (brand.equals(GROHE)) {
            excelFile = excelExportService.exportOrderToExcel(lastOrderId, orderService.lastOrderNumber(brand));
        } else {
            excelFile = excelExportService.exportOrderToExcelViega(lastOrderId, "Test");
        }

        if (excelFile == null ) {
            // Handle the case where the Excel file generation failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Return the Excel file as a response
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + orderName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
//        return ResponseEntity.ok().body("Successfully create order");
    }

    @GetMapping("/importFromExcel")
    public String showUploadForm() {
        return "uploadForm"; // Return the name of the HTML template for the upload form
    }


    //TODO: need to create redirectAttributes for errors
    @PostMapping("/importFromExcel")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam ("brand") String brand,
                                   @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (!file.isEmpty()) {
            preOrderService.readPreOrderFromExcel(file.getInputStream(), brand, userDetails);
        } else {
            // Handle the case where the file is empty
            throw new IllegalArgumentException("File is empty");
        }

        if(brand.equals(VIEGA)) {
            return "redirect:/orders/preOrderViega";
        }

        return "redirect:/orders/preOrderGrohe";
    }



}
