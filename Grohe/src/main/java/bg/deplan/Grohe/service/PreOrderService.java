package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.PreOrder;

public interface PreOrderService {
    PreOrder addItemToPreOrder(PreOrder preOrder, AddArticleDTO addArticleDTO);
}
