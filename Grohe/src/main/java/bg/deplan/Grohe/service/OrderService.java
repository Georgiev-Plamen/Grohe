package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.PreOrder;

public interface OrderService {

    boolean createOrder(OrderDTO orderDTO);

    void addToPreOrder(PreOrder preOrder, OrderDTO orderDTO);
}
