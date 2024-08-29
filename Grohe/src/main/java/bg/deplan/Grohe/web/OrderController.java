package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.UserLoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/orders")
    public String orders(){
        return "orders";
    }
}
