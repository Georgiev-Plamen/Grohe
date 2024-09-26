package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.AppUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @GetMapping("/all-articles")
    public String allArticles(@AuthenticationPrincipal UserDetails userDetails,
                        Model model) {

        //TODO:
        model.addAttribute("articleData", );

        return "all-articles";
    }
}
