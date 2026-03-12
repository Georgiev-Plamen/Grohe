package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.service.ExcelExportService;
import bg.deplan.Grohe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

import static org.thymeleaf.util.StringUtils.substring;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExcelExportService excelExportService;

    @ModelAttribute("orderDto")
    public OrderDTO orderDTO() { return new OrderDTO(0L, "","", LocalDate.now(),null);}

    @ModelAttribute("orderInfo")
    public OrderDTO orderInfo() { return new OrderDTO(0L, "","", LocalDate.now(),null);}

    private static final String GROHE = "Grohe";
    private static final String VIEGA = "Viega";


    @GetMapping("/all")
    public String orders(Model model){

        model.addAttribute("allOrders", orderService.getAllOrders(GROHE));
        model.addAttribute("order", orderDTO());

        return "orders";
    }

    @GetMapping("/allNew")
    public String ordersNew(
            @RequestParam(required = false, defaultValue = GROHE) String brand,
            Model model,
            @RequestParam(required = false) Integer year

    ) {
        model.addAttribute("ordersList", orderService.getOrderListByBrandAndByYear(brand, year));
        model.addAttribute("brand", brand);
        model.addAttribute("order", orderDTO());
        model.addAttribute("selectedYear", year);

        model.addAttribute("yearsWithOrder", orderService.yearWithOrder());

        return "ordersNewList";
    }

    @GetMapping("/allNewGrohe")
    public String ordersNewGrohe(Model model,
                                 @RequestParam(required = false) Integer year
    ) {
        return ordersNew(GROHE, model, year);
    }

    @GetMapping("/allNewViega")
    public String ordersNewViega(Model model,
                                 @RequestParam(required = false) Integer year
    ) {
        return ordersNew(VIEGA, model, year);
    }

    @GetMapping("/orderDetails")
    public String getOrderDetails(@RequestParam("orderId") Long orderId, Model model, Integer year) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        model.addAttribute("order", orderDTO);
        return "orderDetails";
    }

    @GetMapping("/orderInfo/{id}")
    public String orderInfo(Model model, @PathVariable("id") Long id,
                            @RequestParam("brand") String brand,
                            @RequestParam(required = false) Integer year){

        model.addAttribute("orderInfo", orderService.getOrderById(id));

        return ordersNew(brand, model, year);
    }

    @GetMapping("/search/{artNum}")
    public String orderSearch(Model model, @RequestParam(required = false) String artNum,
                              @RequestParam("brand") String brand,
                              @RequestParam(required = false) Integer year){

        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum, year));
        model.addAttribute("order", orderDTO());

        return ordersNew(brand, model, year);
    }

    @GetMapping("/searchOnlyArticle/{artNum}")
    public String searchOnlyArticle(Model model,
                                    @RequestParam(required = false) String artNum,
                                    @RequestParam("brand") String brand,
                                    @RequestParam(required = false) Integer year){

        model.addAttribute("articlesByParam", orderService.findOnlyArticlesInOrder(artNum, year));
        model.addAttribute("orderItemsByParam", orderService.findArticlesByCommentItems(orderService.findOnlyArticlesInOrder(artNum, year), year));

        return ordersNew(brand, model, year);
    }

    @GetMapping("/searchOrderBy/{orderBy}")
    public String searchOrderBy(Model model,
                                @RequestParam(required = false) String orderBy,
                                @RequestParam("brand") String brand,
                                @RequestParam(required = false) Integer year){

        model.addAttribute("articlesByParam", orderService.findByOrderBy(orderBy, brand, year));
        model.addAttribute("orderItemsByParam", orderService.findArticlesByCommentItems(orderService.findByOrderBy(orderBy, brand, year), year));


        return ordersNew(brand, model, year);
    }

    @GetMapping("/searchByComment/{comment}")
    public String searchByComment(Model model,
                                  @RequestParam(required = false) String comment,
                                  @RequestParam("brand") String brand,
                                  @RequestParam(required = false) Integer year){

        model.addAttribute("articlesByParam", orderService.findArticlesByComment(comment, brand, year));
        model.addAttribute("orderItemsByParam", orderService.findArticlesByCommentItems(orderService.findArticlesByComment(comment, brand, year), year));
//        model.addAttribute("allOrders", orderService.getAllOrders(brand));

        return ordersNew(brand, model, year);
    }

    @GetMapping("/orderNewSearch/{artNum}")
    public String orderSearchView(Model model,
                                  @RequestParam(required = false) String artNum,
                                  @RequestParam("brand") String brand,
                                  @RequestParam(required = false) Integer year){

        model.addAttribute("ordersWithArt", orderService.findOrdersContainsArt(artNum, year));

        return ordersNew(brand, model, year);
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

    @GetMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable ("id") Long id,
                              @AuthenticationPrincipal UserDetails userDetails) {

        orderService.deleteOrder(id, userDetails);

        return "redirect:/orders/allNew";
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<byte[]> orderExport(@PathVariable ("id") Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        String orderName = orderService.orderNumber(id);
        String orderNum = orderService.orderNumberById(id);
        byte [] excelFile;
        excelFile = excelExportService.exportOrderToExcel(id,orderNum);

        if (excelFile == null ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + orderName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }

    @GetMapping("/service")
    public String service(@AuthenticationPrincipal UserDetails userDetails) {

        orderService.service();

        return "redirect:/orders/allNew";
    }
}
