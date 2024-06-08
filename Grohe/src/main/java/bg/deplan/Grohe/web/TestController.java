package bg.deplan.Grohe.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class TestController {

    @GetMapping("/")
    public String index (Model model) {

        return "index";
    }

    @PostMapping("/submitForm")
    public String submitForm(
            @RequestParam("artNum") String artNum,
            @RequestParam("quantityForOrder") String quantityForOrder,
            @RequestParam("input3") String input3,
            Model model){

        model.addAttribute("artNum", artNum);
        model.addAttribute("quantityForOrder", quantityForOrder);
        model.addAttribute("input3", input3);

        System.out.println(input3);

        return "index";
    }
}
