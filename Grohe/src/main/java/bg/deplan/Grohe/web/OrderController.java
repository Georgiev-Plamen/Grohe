package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @ModelAttribute("preOrderData")
    public OrderDTO preOrderDTO() {
        return new OrderDTO("",0,"", LocalDate.now(),"","");
    }

    @GetMapping("/orders")
    public String orders(){
        return "orders";
    }

    @GetMapping("/preOrder")
    public String preOrder(Model model) {

        model.addAttribute("preOrderData", preOrderDTO());

        return "preOrder";
    }

    @PostMapping("/preOrder")
    public String preOrder(OrderDTO orderDTO) {

        orderService.createOrder(orderDTO);

        return "preOrder";
    }
}
