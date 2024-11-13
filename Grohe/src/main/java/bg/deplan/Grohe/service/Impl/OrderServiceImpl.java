package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean createOrder(OrderDTO orderDTO) {
        return false;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderServiceImpl::toAllOrders)
                .toList();
    }

    private static OrderDTO toAllOrders(Order order) {
        return new OrderDTO(
                order.getOrderName(),
                order.getDate(),
                order.getItems()
                );
    }


}
