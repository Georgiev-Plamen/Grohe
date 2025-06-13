package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @ModelAttribute("orderDto")
    public OrderDTO orderDTO() { return new OrderDTO(0L, "","", LocalDate.now(),null);}

    private static final String GROHE = "Grohe";
    private static final String VIEGA = "Viega";

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public String orders(Model model){

        model.addAttribute("allOrders", orderService.getAllOrders(GROHE));
        model.addAttribute("order", orderDTO());

        return "orders";
    }

    @GetMapping("/allNew")
    public String ordersNew(
            @RequestParam(required = false, defaultValue = GROHE) String brand,
            Model model
    ) {
        model.addAttribute("allOrders", orderService.getAllOrders(brand));
        model.addAttribute("brand", brand);
        model.addAttribute("order", orderDTO());
        return "ordersNew";
    }

    @GetMapping("/allNewGrohe")
    public String ordersNewGrohe(Model model) {
        return ordersNew(GROHE, model);
    }

    @GetMapping("/allNewViega")
    public String ordersNewViega(Model model) {
        return ordersNew(VIEGA, model);
    }

//    @GetMapping("/allNewViega")
//    public String ordersNewViega(Model model){
//
//        model.addAttribute("allOrders", orderService.getAllOrders(VIEGA));
//        model.addAttribute("brand", VIEGA);
//        model.addAttribute("order", orderDTO());
//
////        return "ordersNewViega";
//        return "ordersNew";
//    }

    @PostMapping("/orderDetails")
    public String getOrderDetails(@RequestParam("orderId") Long orderId, Model model) {
        // Fetch order details based on the selected orderId
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        model.addAttribute("order", orderDTO);
        return "orderDetails"; // Thymeleaf fragment or template
    }

    @PostMapping("/search/{artNum}")
    public String orderSearch(Model model, @RequestParam("artNum") String artNum){


        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum));
        model.addAttribute("allOrders", orderService.getAllOrders(GROHE));
        model.addAttribute("order", orderDTO());


        return "ordersNew";
    }

    @PostMapping("/searchOnlyArticle/{artNum}")
    public String searchOnlyArticle(Model model, @RequestParam("artNum") String artNum) {

        model.addAttribute("articlesByParam", orderService.findOnlyArticlesInOrder(artNum));
        model.addAttribute("allOrders", orderService.getAllOrders(GROHE));

        return "ordersNew";
    }

    @PostMapping("/searchOrderBy/{orderBy}")
    public String searchOrderBy(Model model, @RequestParam("orderBy") String orderBy) {

        model.addAttribute("articlesByParam", orderService.findByOrderBy(orderBy, GROHE));
        model.addAttribute("allOrders", orderService.getAllOrders(GROHE));

        return "ordersNew";
    }

    @PostMapping("/searchByComment/{comment}")
    public String searchByComment(Model model, @RequestParam("comment") String comment) {
        model.addAttribute("articlesByParam", orderService.findArticlesByComment(comment, GROHE));
        model.addAttribute("allOrders", orderService.getAllOrders(GROHE));

        return "ordersNew";
    }

    @GetMapping("/orderNewSearch/{artNum}")
    public String orderSearchView(Model model, @RequestParam("artNum") String artNum) {

        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum));

        return "ordersNew";
    }

    @PostMapping("/editOrder/{id}")
    public String editOrder(@PathVariable ("id") Long id, @ModelAttribute OrderDTO orderDTO) {

        orderService.editOrder(orderDTO, id);

        return "redirect:/orders/ordersNew";
    }

    @GetMapping("/allViega")
    public String ordersViega(Model model){

        model.addAttribute("allOrders", orderService.getAllOrders(VIEGA));

        return "ordersViega";
    }

    @PostMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable ("id") Long id,
                              @AuthenticationPrincipal UserDetails userDetails) {

        orderService.deleteOrder(id, userDetails);

        return "redirect:/orders/allNew";
    }
}
