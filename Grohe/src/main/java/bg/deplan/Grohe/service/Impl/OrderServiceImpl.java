package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public boolean createOrder(OrderDTO orderDTO) {
        return false;
    }
}
