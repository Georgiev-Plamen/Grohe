package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.PreOrderItem;

import java.util.List;

public interface OrderService {

    void createOrder(List<PreOrderItem> preOrderItems);

    List<OrderDTO> getAllOrders();

}
