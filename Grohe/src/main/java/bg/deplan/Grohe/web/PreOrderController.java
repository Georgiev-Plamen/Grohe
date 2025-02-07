package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class PreOrderController {
    @Autowired
    private PreOrderService preOrderService;
    @Autowired
    private PreOrderItemRepository preOrderRepository;

    @Autowired
    private ArticleService articleService;

    @ModelAttribute("preOrderData")
    public PreOrderDTO preOrderDTO() {
        return new PreOrderDTO(0L,"","","","",LocalDate.now(), "","");
    }

    @GetMapping("/preOrder")
    public String preOrder(Model model) {

        model.addAttribute("preOrderData", preOrderDTO());
        model.addAttribute("allArticle", preOrderService.getAllArticle("Grohe"));
        model.addAttribute("article", preOrderDTO());

        return "preOrder";
    }

   @GetMapping("/preOrderTest")
   public String getPreOrderPage() {
           // The HTML file should be placed in src/main/resources/templates/preorder.html
            return "preOrderTest";
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
    public String makeOrder (@RequestParam ("name") String name) {
        String brand = "Grohe";
        preOrderService.makeOrder(name,brand);

        return "redirect:/orders/preOrder";
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
