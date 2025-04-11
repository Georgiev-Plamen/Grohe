package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.*;
import bg.deplan.Grohe.model.*;
import bg.deplan.Grohe.model.DTOs.*;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteOrderRepository deleteOrderRepository;

    @Autowired
    private DeleteOrderItemRepository deleteOrderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ExcelExportServiceImpl excelExportService;
    @Autowired
    private ArticleService articleService;

    @Override
    public Order createOrder(List<PreOrderItem> preOrderItems, String name, String brand, UserDetails userDetails) {

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setOrderName(name);
        order.setBrand(brand);
        order.setUser(userRepository.findByUsername(userDetails.getUsername()).get());
        orderRepository.save(order);

        for (PreOrderItem preOrderItem : preOrderItems) {
            if(!preOrderItem.isHold()) {

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
    public void deleteOrder(Long id, UserDetails userDetails) {
        Order order = orderRepository.getOrdersById(id);
        List <OrderItem> orderItems = order.getItems();

        DeleteOrder deleteOrder = new DeleteOrder() ;
        deleteOrder.setBrand(order.getBrand());
        deleteOrder.setOrderName(order.getOrderName());
        deleteOrder.setDate(order.getDate());
        deleteOrderRepository.save(deleteOrder);
        deleteOrder.setUser(userRepository.findByUsername(userDetails.getUsername()).get());
        deleteOrder.setDateOfDelete(LocalDateTime.now());

        List<DeleteOrderItem> deleteOrderItems = new ArrayList<>();

        for(OrderItem orderItem :orderItems) {
            DeleteOrderItem deleteOrderItem = new DeleteOrderItem();
            deleteOrderItem.setId(null);
            modelMapper.map(orderItem, deleteOrderItem);
            deleteOrderItem.setDeleteOrder(deleteOrder);
            deleteOrderItems.add(deleteOrderItem);
            deleteOrderItem.setDateOfDelete(LocalDateTime.now());
            deleteOrderItem.setUser(userRepository.findByUsername(userDetails.getUsername()).get());

            deleteOrderItem.setDeleteOrder(deleteOrder);
            deleteOrderItemRepository.save(deleteOrderItem);
        }

        orderItemRepository.deleteAll(orderItems);
        orderRepository.delete(order);

    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.getReferenceById(orderId);
        OrderDTO orderDTO = toAllOrders(order);

        return orderDTO;
    }

    @Override
    public String getOrderName(Long orderId) {
        return orderRepository.getReferenceById(orderId).getOrderName();
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
    @Transactional
    public List<ArticleFindDTO> findOnlyArticlesInOrder(String artNum) {

        List<Long> articleIds = articleService.findArticleIds(artNum);

        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByIDs(articleIds);

        return orderItems.stream()
                    .map(this::mapToArticleFindDTO)
                    .toList();
    }

    @Override
    public List<ArticleFindDTO> findByOrderBy(String orderBy) {

         return orderItemRepository.findOrderItemsByOrderBy(orderBy).stream()
                 .map(this::mapToArticleFindDTO)
                 .toList();
    }

    @Override
    public String lastOrderNumber() {
        Order order = orderRepository.getReferenceById(lastOrderId());
        String orderName = order.getOrderName();

        String orderNum = orderName.split(" ")[1];

        try {
            int num = Integer.parseInt(orderNum);
            return String.valueOf(num);
        } catch (NumberFormatException nfe) {
            return "";
        }
    }

    private ArticleFindDTO mapToArticleFindDTO(OrderItem orderItem) {
        return new ArticleFindDTO(
                orderItem.getArticle().getBrand(),
                orderItem.getDateOfDelivery(),
                orderItem.getDateOfOrder(),
                orderItem.getOrderBy(),
                orderItem.getOrderReason(),
                orderItem.getQuantity(),
                orderItem.getComment(),
                articleService.findById(orderItem.articleId()).get().getArtNum(),
                orderItem.getOrder().getOrderName()
//                getOrderName(orderItem.getOrder().getId())
        );
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
