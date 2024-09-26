package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;

import java.util.List;

public interface PreOrderService {
//    Order finalizeOrder(PreOrder preOrder);

    void addItem(AddArticleDTO addArticleDTO);

    List<ArticleDTO> getAllArticle();
}
