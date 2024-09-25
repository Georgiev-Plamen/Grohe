package bg.deplan.Grohe.web;

import bg.deplan.Grohe.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private ArticleService articleService;


//    @ModelAttribute("preOrder")
//    public PreOrder preOrder() {
//        return new PreOrder();
//    }

    @GetMapping("/all")
    public String orders(){
        return "orders";
    }

}
