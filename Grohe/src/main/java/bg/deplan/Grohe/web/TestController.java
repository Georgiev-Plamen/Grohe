package bg.deplan.Grohe.web;

import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.Impl.AddArticleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    private final ArticleService articleService;

    public TestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index (Model model) {

        return "index";
    }

    @PostMapping("/submitForm")
    public String submitForm(
            @RequestParam("artNum") String artNum,
            @RequestParam("quantityForOrder") String quantityForOrder,
            @RequestParam("input3") String input3,
            AddArticleDTO addArticleDTO,
            Model model){

        model.addAttribute("artNum", artNum);
        model.addAttribute("quantityForOrder", quantityForOrder);
        model.addAttribute("input3", input3);

        articleService.addArticle(addArticleDTO);

        System.out.println(artNum);

        return "index";
    }
}
