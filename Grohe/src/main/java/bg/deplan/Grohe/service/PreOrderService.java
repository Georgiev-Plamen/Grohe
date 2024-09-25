package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.DTOs.AddArticleDTO;

public interface PreOrderService {
//    Order finalizeOrder(PreOrder preOrder);

    void addItem(AddArticleDTO addArticleDTO);
}
