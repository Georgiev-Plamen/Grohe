package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.AddArticleDTO;

public interface OrderService {

    long createOrder(AddArticleDTO addArticleDTO);

}
