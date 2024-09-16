package bg.deplan.Grohe.web;

import bg.deplan.Grohe.data.PreOrderRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.PreOrder;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/orders")
public class PreOrderController {
    private PreOrderService preOrderService;
    private PreOrderRepository preOrderRepository;

    @ModelAttribute("preOrderData")
    public OrderDTO preOrderDTO() {
        return new OrderDTO("",1,"", LocalDate.now(),"","");
    }

    @GetMapping("/preOrder")
    public String preOrder(Model model) {

        model.addAttribute("preOrderData", preOrderDTO());

        return "preOrder";
    }

    @PostMapping("/preOrder")
    public String addToPreOrder(Long preOrderId, @ModelAttribute AddArticleDTO addArticleDTO) {

        PreOrder preOrder = preOrderRepository.findById(preOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PreOrder ID"));

//        preOrderService.addItemToPreOrder(preOrder, addArticleDTO);
        preOrderService.addItemToPreOrder(preOrder, addArticleDTO);

        return "preOrder";
    }
}
