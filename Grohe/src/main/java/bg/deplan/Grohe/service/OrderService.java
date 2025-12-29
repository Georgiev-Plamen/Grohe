package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.*;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.OrderItem;
import bg.deplan.Grohe.model.PreOrderItem;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {

    Order createOrder(List<PreOrderItem> preOrderItems, String name, String brand, UserDetails userDetails) ;

    List<OrderDTO> getAllOrders(String brand);

    List<OrderDTO> getAllOrdersByBrandAndYear(String brand, Integer year);

    void editOrder(OrderDTO orderDTO, Long id);

    long lastOrderId(String brand);

    String lastThreeOrderName(String brand);

    void deleteOrder(Long id, UserDetails userDetails);

    OrderDTO getOrderById(Long orderId);

    String getOrderName(Long orderId);

    List<OrderDTO> findOrdersContainsArt(String artNum, Integer year);

    void bulkUpdateArticle(List<OrderEditArticleDTO> updates);

    List<ArticleFindDTO> findOnlyArticlesInOrder(String artNum, Integer year);

    List<ArticleFindDTO> findByOrderBy(String orderBy, String brand, Integer year);

    String lastOrderNumber(String brand);

    String newOrderName(String brand);

    List<ArticleFindDTO> findArticlesByComment(String comment, String brand, Integer year);

    List<OrderItem> findArticlesByCommentItems(List<ArticleFindDTO> articleDTOS, Integer year);

    List<OrderTitleDTO> getOrderList(String brand);

    List<OrderTitleDTO> getOrderListByBrandAndByYear(String brand, Integer year);

    void service();

    List<Integer> yearWithOrder();

//    int findArticleIndex(Long orderId,Long ArticleId);
}
