package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.PreOrderItem;

import java.util.List;

public interface OrderService {

    Order createOrder(List<PreOrderItem> preOrderItems, String name, String brand) ;

    Order createAndExportOrder(List<PreOrderItem> preOrderItems, String name, String brand);

    List<OrderDTO> getAllOrders(String brand);

    void editOrder(OrderDTO orderDTO, Long id);

    void exportOrder(long id);

    long lastOrderId();
}
