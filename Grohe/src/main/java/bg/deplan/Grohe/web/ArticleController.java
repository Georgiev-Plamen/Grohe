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

    private static final String GROHE = "Grohe";
    private static final String VIEGA = "Viega";

    @ModelAttribute("articleData")
    public AddArticleDTO addArticleDTO() {
        return new AddArticleDTO(null,"", "", "","", "", "", "", "", 1,2025);
    }

    @GetMapping
    public String allArticles(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false, defaultValue = GROHE) String brand,
            Model model) {

        // Add user-specific data if needed
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }

        model.addAttribute("articleData", addArticleDTO());
        model.addAttribute("allArticles", articleService.getAllArticle(brand));
        model.addAttribute("brand", brand);  // Explicit attribute for view

        return "articles";
    }


    @GetMapping("/grohe")
    public String articlesGrohe(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        return allArticles(userDetails, GROHE, model);
    }

    @GetMapping("/viega")
    public String articlesViega(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        return allArticles(userDetails, VIEGA, model);
    }

    @PostMapping("/addArticle")
    public String addArticle(AddArticleDTO addArticleDTO) {
        articleService.addArticle(addArticleDTO);
        return addArticleDTO.brand().equals(VIEGA) ? "redirect:/articles/articlesViega" : "redirect:/articles/articlesGrohe";
    }

    @PutMapping("/{artNum}")
    public String editArticle(@PathVariable("artNum") String artNum, AddArticleDTO addArticleDTO) {
        articleService.editArticle(addArticleDTO);
        return addArticleDTO.brand().equals(VIEGA) ? "redirect:/articles/articlesViega" : "redirect:/articles/articlesGrohe";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") long id) {

        articleService.deleteArticle(id);

        return "redirect:/articles";
    }

}