package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderRepository;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.Article;
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


    public void addItem(ArticleDTO articleDTO) {
        PreOrderItem preOrderItem = new PreOrderItem();

        Optional<Article> optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());

        if(optionalArticle.isEmpty()) {
            Article article = new Article();
            article.setArtNum(articleDTO.artNum());
            articleRepository.save(article);
            optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());
        }
        preOrderItem.setArticle(optionalArticle.get());
        preOrderItem.setQuantityForOrder(articleDTO.quantityForOrder());
        preOrderItem.setOrderBy(articleDTO.orderBy());
        preOrderItem.setOrderBy(articleDTO.orderReason());
        preOrderItem.setDate(articleDTO.date());
        preOrderItem.setOrderReason(articleDTO.orderReason());
        preOrderItem.setComment(articleDTO.comment());
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
                preOrderItem.getOrderBy(),
                preOrderItem.getDate(),
                preOrderItem.getOrderReason(),
                preOrderItem.getComment()
        );
    }
}
