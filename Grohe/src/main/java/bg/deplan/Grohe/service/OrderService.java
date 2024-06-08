package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.AddArticleDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    long createOrder(AddArticleDTO addArticleDTO);
}
