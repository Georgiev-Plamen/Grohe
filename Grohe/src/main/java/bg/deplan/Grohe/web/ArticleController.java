package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ModelAttribute("articleData")
    public AddArticleDTO addArticleDTO() {
        return new AddArticleDTO("","","","", "","", 1);
    }

    @GetMapping("/articles")
    public String allArticles(@AuthenticationPrincipal UserDetails userDetails,
                        Model model) {

        model.addAttribute("articleData", addArticleDTO());
        model.addAttribute("allArticles", articleService.getAllArticle("Grohe"));

        return "articles";
    }

    @GetMapping("/articlesViega")
    public String allArticlesViega(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {

        model.addAttribute("articleData", addArticleDTO());
        model.addAttribute("allArticles", articleService.getAllArticle("Viega"));

        return "articlesViega";
    }

    @PostMapping("/addArticle")
    public String addArticle(AddArticleDTO addArticleDTO) {

        articleService.addArticle(addArticleDTO);

        if(addArticleDTO.brand().equals("Viega")) {
            return "redirect:/articles/articlesViega";
        }

        return "redirect:/articles/articles";
    }


    @GetMapping("/{artNum}")
    public String editArticle(@PathVariable("artNum") String artNum, Model model) {

        model.addAttribute("articleData",articleService.getArticleData(artNum));

        return "editArticles";
    }

    @PutMapping("/{artNum}")
    public String editArticle(@PathVariable("artNum") String artNum, AddArticleDTO addArticleDTO) {

        articleService.editArticle(addArticleDTO);

        if(addArticleDTO.brand().equals("Viega")) {
            return "redirect:/articles/articlesViega";
        }

        return "redirect:/articles/articles";
    }
}
