package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.OrderItemRepository;
import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.OrderItem;
import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ExcelExportServiceImpl excelExportService;

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
    public Order createAndExportOrder(List<PreOrderItem> preOrderItems, String name, String brand) {

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
    @Transactional
    public void exportOrder(long id) {
        try {
            byte[] excelFile = excelExportService.exportOrderToExcel(id);
            System.out.println("Exported order: " + id);
            // Optionally save or email the file
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public long lastOrderId() {
        Long id = orderRepository.findLastId();

        return id;
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

        if(!order.getOrderName().equals(orderDTO.orderName()) && orderDTO.orderName() != null) {
            order.setOrderName(orderDTO.orderName());
        }

//        if(!order.getBrand().equals(orderDTO.brand()) && orderDTO.brand() != null) {
//            order.setBrand(orderDTO.brand());
//        }

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
