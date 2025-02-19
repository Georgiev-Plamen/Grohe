package bg.deplan.Grohe.web;

import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @ModelAttribute("orderDto")
    public OrderDTO orderDTO() { return new OrderDTO(0L, "","", LocalDate.now(),null);}

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public String orders(Model model){

        model.addAttribute("allOrders", orderService.getAllOrders("Grohe"));
        model.addAttribute("order", orderDTO());

        return "orders";
    }

    @GetMapping("/allNew")
    public String ordersNew(Model model){

        model.addAttribute("allOrders", orderService.getAllOrders("Grohe"));
        model.addAttribute("order", orderDTO());

        return "ordersNew";
    }

    @PostMapping("/orderDetails")
    public String getOrderDetails(@RequestParam("orderId") Long orderId, Model model) {
        // Fetch order details based on the selected orderId
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        model.addAttribute("order", orderDTO);
        return "orderDetails"; // Thymeleaf fragment or template
    }

    @GetMapping("/search/{artNum}")
    public String orderSearch(Model model, @RequestParam("artNum") String artNum){

        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum));

        return "redirect:/orders/allNew";
    }

//    @GetMapping("/orderNewSearch/{artNum}")
//    public String orderSearchView(Model model, @RequestParam("artNum") String artNum) {
//
//        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum));
//
//        return "orderNewSearch";
//    }

    @PostMapping("/editOrder/{id}")
    public String editOrder(@PathVariable ("id") Long id, @ModelAttribute OrderDTO orderDTO) {

        orderService.editOrder(orderDTO, id);

        return "redirect:/orders/all";
    }

    @GetMapping("/allViega")
    public String ordersViega(Model model){

        model.addAttribute("allOrders", orderService.getAllOrders("Viega"));

        return "ordersViega";
    }

    @PostMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable ("id") Long id) {

        orderService.deleteOrder(id);

        return "redirect:/orders/all";
    }
}
