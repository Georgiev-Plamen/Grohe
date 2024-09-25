package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.OrderDTO;

public interface OrderService {

    boolean createOrder(OrderDTO orderDTO);

}
