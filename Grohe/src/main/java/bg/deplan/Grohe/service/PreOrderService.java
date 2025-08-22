package bg.deplan.Grohe.service;

import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;
import bg.deplan.Grohe.model.PreOrderItem;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface PreOrderService {
//    Order finalizeOrder(PreOrder preOrder);

    void addItem(ArticleDTO articleDTO, UserDetails userDetails);

    List<ArticleDTO> getAllArticle(String brand);

    void updateItems(PreOrderDTO preOrderDTO, Long id);

    PreOrderItem findById(Long id);

    void deletePreOrderArticle(Long id, String brand);

    List<PreOrderExcelDTO> readPreOrderFromExcel(InputStream inputStream, String brand, UserDetails userDetails) throws IOException;

    List<PreOrderDTO> getAllPreOrder(String brand);

    boolean createAndExportOrder(String name, String brand, UserDetails userDetails) throws IOException;
}
