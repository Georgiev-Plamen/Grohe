package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.OrderItemRepository;
import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.DTOs.OrderEditArticleDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.OrderItem;
import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ExcelExportServiceImpl excelExportService;
    @Autowired
    private ArticleService articleService;

    @Override
    public Order createOrder(List<PreOrderItem> preOrderItems, String name, String brand) {

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setOrderName(name);
        order.setBrand(brand);
        orderRepository.save(order);

        for (PreOrderItem preOrderItem : preOrderItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setArticle(preOrderItem.getArticle());
            orderItem.setQuantity(preOrderItem.getQuantityForOrder());
            orderItem.setOrderBy(preOrderItem.getOrderBy());
            orderItem.setDateOfOrder(preOrderItem.getDate());
            orderItem.setOrderReason(preOrderItem.getOrderReason());
            orderItem.setComment(preOrderItem.getComment());

            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }


        return order;
    }

    @Override
    public long lastOrderId() {
        Long id = orderRepository.findLastId();

        return id;
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderItemRepository.deleteAllByOrderId(id);
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.getReferenceById(orderId);
        OrderDTO orderDTO = toAllOrders(order);

        return orderDTO;
    }

    @Override
    @Transactional
    public List<OrderDTO> findOrdersContainsArt(String artNum) {
        return orderRepository.findAll()
                .stream()
                .map(OrderServiceImpl::toAllOrders)
                .filter(o -> o.articleList()
                        .stream()
                        .anyMatch(a -> a.getArticle().getArtNum().contains(artNum)))
                .toList();

    }

    //TODO:
    @Override
    public void bulkUpdateArticle(List<OrderEditArticleDTO> updates) {

       for(OrderEditArticleDTO orderEditArticleDTO : updates) {

            Order order = orderRepository.findById(orderEditArticleDTO.orderId()).get();

            if(orderEditArticleDTO.orderName() != null) {
                order.setOrderName(orderEditArticleDTO.orderName());
            }

            OrderItem orderItem = order.getItems().get(orderEditArticleDTO.index());

            if(orderEditArticleDTO.artNum() != null) {
                Optional<Article> optionalArticle = articleService.findByArtNum(orderEditArticleDTO.artNum());
                if(optionalArticle.isEmpty()) {
                    articleService.createArticleByArtName(orderEditArticleDTO.artNum(), orderEditArticleDTO.brand());
                    optionalArticle = articleService.findByArtNum(orderEditArticleDTO.artNum());
                }

                orderItem.setArticle(optionalArticle.get());
            }

            // Update the orderItem with the new data

           if(orderEditArticleDTO.quantity() != null) {
               orderItem.setQuantity(orderEditArticleDTO.quantity());
           }

           if(orderEditArticleDTO.orderBy() != null) {
               orderItem.setOrderBy(orderEditArticleDTO.orderBy());
           }

           if(orderEditArticleDTO.dateOfOrder() != null) {
               orderItem.setDateOfOrder(orderEditArticleDTO.dateOfOrder());
           }

           if(orderEditArticleDTO.comment() != null) {
               orderItem.setOrderReason(orderEditArticleDTO.orderReason());
           }

           if(orderEditArticleDTO.dateOfDelivery() != null) {
               orderItem.setDateOfDelivery(orderEditArticleDTO.dateOfDelivery());
           }

           if(orderEditArticleDTO.orderReason() != null) {
               orderItem.setOrderReason(orderEditArticleDTO.orderReason());
           }

           if(orderEditArticleDTO.comment() != null) {
               orderItem.setComment(orderEditArticleDTO.comment());
           }


            orderItemRepository.save(orderItem);
        }
    }

    @Override
    public List<OrderDTO> getAllOrders(String brand) {
        return orderRepository.findAll()
                .stream()
                .map(OrderServiceImpl::toAllOrders)
                .filter(o -> o.brand().equals(brand))
                .toList();
    }

    @Override
    public void editOrder(OrderDTO orderDTO, Long id) {
        Order order = orderRepository.findById(id).get();

        if(!order.getOrderName().equals(orderDTO.orderName()) && orderDTO.orderName() != null && !orderDTO.orderName().isEmpty()) {
            order.setOrderName(orderDTO.orderName());
        }

        if(!order.getDate().equals(orderDTO.date()) && orderDTO.date() != null) {
            order.setDate(orderDTO.date());
        }

        orderRepository.save(order);
    }

    private static OrderDTO toAllOrders(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getBrand(),
                order.getOrderName(),
                order.getDate(),
                order.getItems()
                );
    }


}
