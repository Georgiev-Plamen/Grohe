package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.AddArticleDTO;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    private final ArticleService articleService;
    private final OrderService orderService;

    public TestController(ArticleService articleService, OrderService orderService) {
        this.articleService = articleService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index (Model model) {

        return "index";
    }

    @PostMapping("/submitForm")
    public String submitForm(
            @RequestParam("artNum") String artNum,
            @RequestParam("quantityForOrder") String quantityForOrder,
            AddArticleDTO addArticleDTO,
            Model model){

        model.addAttribute("artNum", artNum);
        model.addAttribute("quantityForOrder", quantityForOrder);

        orderService.createOrder(addArticleDTO);

        System.out.println(artNum);

        return "index";
    }
}
