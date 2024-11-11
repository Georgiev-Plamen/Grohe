package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.OrderDTO;

import java.util.List;

public interface OrderService {

    boolean createOrder(OrderDTO orderDTO);

    List<OrderDTO> getAllOrders();

}
