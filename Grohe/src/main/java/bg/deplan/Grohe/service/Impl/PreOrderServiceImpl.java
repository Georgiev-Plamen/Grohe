package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderRepository;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreOrderServiceImpl implements PreOrderService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private PreOrderItemRepository preOrderRepository;
    @Autowired
    private OrderRepository orderRepository;

    public PreOrderItem savePreOrder(PreOrderItem preOrder) {
        return preOrderRepository.save(preOrder);
    }


    public void addItem(AddArticleDTO addArticleDTO) {
        PreOrderItem preOrderItem = new PreOrderItem();

        Optional<Article> optionalArticle = articleRepository.findByArtNum(addArticleDTO.getArtNum());

        if(optionalArticle.isEmpty()) {
            articleService.addArticle(addArticleDTO);
            optionalArticle = articleRepository.findByArtNum(addArticleDTO.getArtNum());
        }
        preOrderItem.setArticle(optionalArticle.get());
        preOrderItem.setQuantityForOrder(addArticleDTO.getQuantityForOrder());
        preOrderItem.setOrderBy(addArticleDTO.getOrderReason());
        preOrderItem.setDate(addArticleDTO.getDate());
        preOrderItem.setOrderReason(addArticleDTO.getOrderReason());
        preOrderItem.setComment(addArticleDTO.getComment());
//        preOrderItem.setPreOrder(this);  // Set association back to PreOrder

        preOrderRepository.save(preOrderItem);

    }

    @Override
    public List<ArticleDTO> getAllArticle() {
        return preOrderRepository.findAll()
                .stream()
                .map(PreOrderServiceImpl::toAllItem)
                .toList();
    }

    private static ArticleDTO toAllItem(PreOrderItem preOrderItem) {
        return new ArticleDTO(
                preOrderItem.getArticle().getArtNum(),
                preOrderItem.getQuantityForOrder(),
                preOrderItem.getDate(),
                preOrderItem.getOrderReason(),
                preOrderItem.getComment()
        );
    }

//    public Order finalizeOrder(PreOrder preOrder) {
//        Order finalOrder = new Order();
//
//        finalOrder.setItems(preOrder.getItems());
//
//        // Save the final order
//        orderRepository.save(finalOrder);
//
//        // Clear the pre-order
//        preOrder.clearItems();
//        preOrderRepository.save(preOrder);  // Update the pre-order in DB
//
//        return finalOrder;
//    }
}
