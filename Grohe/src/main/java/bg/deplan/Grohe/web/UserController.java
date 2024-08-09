package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.UserLoginDTO;

import bg.deplan.Grohe.model.DTOs.UserRegisterDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {

    @ModelAttribute("registerData")
    public UserRegisterDTO userRegisterDTO(){
        return new UserRegisterDTO(null, null, null,null, null);
    }



    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("loginData", new UserLoginDTO());

        return modelAndView;
    }

    @GetMapping("/register")
    public String register() {

        return "register";
    }
}
