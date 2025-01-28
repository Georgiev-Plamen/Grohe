package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;
import bg.deplan.Grohe.model.PreOrderItem;

import java.io.IOException;
import java.util.List;

public interface PreOrderService {
//    Order finalizeOrder(PreOrder preOrder);

    void addItem(ArticleDTO articleDTO);

    List<ArticleDTO> getAllArticle();

    void makeOrder(String name);

    void updateItems(PreOrderDTO preOrderDTO, Long id);

    PreOrderItem findById(Long id);

    void deletePreOrder(Long id);

    List<PreOrderExcelDTO> readPreOrderFromExcel() throws IOException;
}
