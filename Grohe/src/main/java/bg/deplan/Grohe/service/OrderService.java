package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.*;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.PreOrderItem;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {

    Order createOrder(List<PreOrderItem> preOrderItems, String name, String brand, UserDetails userDetails) ;

    List<OrderDTO> getAllOrders(String brand);

    void editOrder(OrderDTO orderDTO, Long id);

    long lastOrderId(String brand);

    void deleteOrder(Long id, UserDetails userDetails);

    OrderDTO getOrderById(Long orderId);

    String getOrderName(Long orderId);

    List<OrderDTO> findOrdersContainsArt(String artNum);

    void bulkUpdateArticle(List<OrderEditArticleDTO> updates);

    List<ArticleFindDTO> findOnlyArticlesInOrder(String artNum);

    List<ArticleFindDTO> findByOrderBy(String orderBy);

    String lastOrderNumber(String brand);

    String newOrderName(String brand);
}
