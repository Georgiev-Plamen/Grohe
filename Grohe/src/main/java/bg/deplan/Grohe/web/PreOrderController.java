package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new PreOrderDTO(0L,"","","",LocalDate.now(), "","");
    }

    @GetMapping("/preOrder")
    public String preOrder(Model model) {

        model.addAttribute("preOrderData", preOrderDTO());
        model.addAttribute("allArticle", preOrderService.getAllArticle());
        model.addAttribute("article", preOrderDTO());

        return "preOrder";
    }

    @PostMapping("/preOrder")
    public String addToPreOrder(@ModelAttribute ArticleDTO articleDTO) {

        preOrderService.addItem(articleDTO);

        return "redirect:/orders/preOrder";
    }

    @PostMapping("/updatePreOrder/{id}")
    public String updateArticle(@PathVariable ("id") Long id, @ModelAttribute PreOrderDTO preOrderDTO ) {

       preOrderService.updateItems(preOrderDTO, id);

        return "redirect:/orders/preOrder";
    }

    @DeleteMapping("/{id}")
    public String deletePreOrderArticle(@PathVariable ("id") Long id) {
        preOrderService.deletePreOrder(id);

        return "redirect:/orders/preOrder";
    }

    @PostMapping("/makeOrder")
    public String makeOrder (@RequestParam ("name") String name) {

        preOrderService.makeOrder(name);

        return "redirect:/orders/preOrder";
    }

    @GetMapping("/importFromExcel")
    public String showUploadForm() {
        return "uploadForm"; // Return the name of the HTML template for the upload form
    }


    //TODO: need to create redirectAttributes for errors
    @PostMapping("/importFromExcel")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            preOrderService.readPreOrderFromExcel(file.getInputStream());
        } else {
            // Handle the case where the file is empty
            throw new IllegalArgumentException("File is empty");
        }

        return "redirect:/orders/preOrder";
    }

}
