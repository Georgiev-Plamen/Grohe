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

    @ModelAttribute("orderInfo")
    public OrderDTO orderInfo() { return new OrderDTO(0L, "","", LocalDate.now(),null);}

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
        model.addAttribute("ordersList", orderService.getOrderList(brand));
        model.addAttribute("allOrders", orderService.getAllOrders(brand));
        model.addAttribute("brand", brand);
        model.addAttribute("order", orderDTO());
        return "ordersNewList";
    }

//    @GetMapping("/allNew")
//    public String ordersNew(
//            @RequestParam(required = false, defaultValue = GROHE) String brand,
//            Model model
//    ) {
//        model.addAttribute("allOrders", orderService.getAllOrders(brand));
//        model.addAttribute("brand", brand);
//        model.addAttribute("order", orderDTO());
//        return "ordersNew";
//    }

    @GetMapping("/allNewGrohe")
    public String ordersNewGrohe(Model model) {
        return ordersNew(GROHE, model);
    }

    @GetMapping("/allNewViega")
    public String ordersNewViega(Model model) {
        return ordersNew(VIEGA, model);
    }

    @PostMapping("/orderDetails")
    public String getOrderDetails(@RequestParam("orderId") Long orderId, Model model) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        model.addAttribute("order", orderDTO);
        return "orderDetails"; // Thymeleaf fragment or template
    }

    @PostMapping("/orderInfo/{id}")
    public String orderInfo(Model model, @PathVariable("id") Long id,
                            @RequestParam("brand") String brand){

        model.addAttribute("orderInfo", orderService.getOrderById(id));

        return ordersNew(brand, model);
    }

    @PostMapping("/search/{artNum}")
    public String orderSearch(Model model, @RequestParam("artNum") String artNum,
                              @RequestParam("brand") String brand) {

        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum));
        model.addAttribute("allOrders", orderService.getAllOrders(brand));
        model.addAttribute("order", orderDTO());

        return ordersNew(brand, model);
    }

    @PostMapping("/searchOnlyArticle/{artNum}")
    public String searchOnlyArticle(Model model, @RequestParam("artNum") String artNum,
                                    @RequestParam("brand") String brand) {

        model.addAttribute("articlesByParam", orderService.findOnlyArticlesInOrder(artNum));
        model.addAttribute("allOrders", orderService.getAllOrders(brand));

        return ordersNew(brand, model);
    }

    @PostMapping("/searchOrderBy/{orderBy}")
    public String searchOrderBy(Model model, @RequestParam("orderBy") String orderBy,
                                @RequestParam("brand") String brand) {

        model.addAttribute("articlesByParam", orderService.findByOrderBy(orderBy, brand));
        model.addAttribute("allOrders", orderService.getAllOrders(brand));

        return ordersNew(brand, model);
    }

    @PostMapping("/searchByComment/{comment}")
    public String searchByComment(Model model, @RequestParam("comment") String comment,
        @RequestParam("brand") String brand) {

        model.addAttribute("articlesByParam", orderService.findArticlesByComment(comment, brand));
        model.addAttribute("allOrders", orderService.getAllOrders(brand));

        return ordersNew(brand, model);
    }

    @GetMapping("/orderNewSearch/{artNum}")
    public String orderSearchView(Model model, @RequestParam("artNum") String artNum,
                                  @RequestParam("brand") String brand) {

        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum));

        return ordersNew(brand, model);
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

    @GetMapping("/service")
    public String service(@AuthenticationPrincipal UserDetails userDetails) {

        orderService.service();

        return "redirect:/orders/allNew";
    }
}
