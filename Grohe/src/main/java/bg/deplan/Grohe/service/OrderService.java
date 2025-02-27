package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.DTOs.OrderEditArticleDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.PreOrderItem;

import java.util.List;

public interface OrderService {

    Order createOrder(List<PreOrderItem> preOrderItems, String name, String brand) ;

    List<OrderDTO> getAllOrders(String brand);

    void editOrder(OrderDTO orderDTO, Long id);

    long lastOrderId();

    void deleteOrder(Long id);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> findOrdersContainsArt(String artNum);

    void bulkUpdateArticle(List<OrderEditArticleDTO> updates);
}
