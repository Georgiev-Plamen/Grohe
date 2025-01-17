package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.Article;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        return new PreOrderDTO("",0,"",LocalDate.now(), "","");
    }

    @GetMapping("/preOrder")
    public String preOrder(Model model) {

        model.addAttribute("preOrderData", preOrderDTO());
        model.addAttribute("allArticle", preOrderService.getAllArticle());

        return "preOrder";
    }

    @PostMapping("/preOrder")
    public String addToPreOrder(@ModelAttribute ArticleDTO articleDTO) {

        preOrderService.addItem(articleDTO);

        return "redirect:/orders/preOrder";
    }

//    @PostMapping("/orders/updateArticle/{id}")
//    public ResponseEntity<?> updateArticle(@PathVariable Long id, @RequestBody Map<String, String> updates) {
//        try {
//            String field = updates.get("field");
//            String value = updates.get("value");
//
//            Article article = articleService.findById(id);
//            if (field.equals("artNum")) {
//                preOrderService.setArtNum(value);
//            } else if (field.equals("quantityForOrder")) {
//                article.s(Integer.parseInt(value));
//            } else if (field.equals("orderBy")) {
//                article.setOrderBy(value);
//            } else if (field.equals("date")) {
//                article.setDate(LocalDate.parse(value));
//            } else if (field.equals("orderReason")) {
//                article.setOrderReason(value);
//            } else if (field.equals("comment")) {
//                article.setComment(value);
//            }
//            articleService.save(article);
//
//            return ResponseEntity.ok().body("Update successful");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update");
//        }
//    }
}
