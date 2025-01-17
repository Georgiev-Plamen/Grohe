package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderRepository;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        preOrderItem.setDate(articleDTO.date());
        preOrderItem.setOrderReason(articleDTO.orderReason());
        preOrderItem.setComment(articleDTO.comment());

        preOrderRepository.save(preOrderItem);
    }

    @Override
    public void updateItems(ArticleDTO articleDTO, Long id) {
//        try {
//            String field = updates.get("field");
//            String value = updates.get("value");
//
//            PreOrderItem preOrderItem = preOrderService.findById(id);
//            if (field.equals("artNum")) {
//                preOrderService.updateItems(value);
//            } else if (field.equals("quantityForOrder")) {
//                article.s(Integer.parseInt(value));
//            } else if (field.equals("orderBy")) {
//                article.setOrderBy(value);
//            } else if (field.equals("date")) {
//                article.setDate(LocalDate.parse(value));
//            } else if (field.equals("orderReason")) {
//                article.setOrderReason(value);
//            } else if (field.equals("comment")) {
//                article.setComment(value);
//            }
//            articleService.save(article);
//
//            return ResponseEntity.ok().body("Update successful");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update");
//        }

        PreOrderItem preOrderItem = preOrderRepository.getReferenceById(id);

        Optional<Article> optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());

        preOrderItem.setArticle(optionalArticle.get());
        preOrderItem.setQuantityForOrder(articleDTO.quantityForOrder());
        preOrderItem.setOrderBy(articleDTO.orderBy());
        preOrderItem.setDate(articleDTO.date());
        preOrderItem.setOrderReason(articleDTO.orderReason());
        preOrderItem.setComment(articleDTO.comment());

        preOrderRepository.save(preOrderItem);
    }

    @Override
    public PreOrderItem findById(Long id) {
        return preOrderRepository.findById(id).get();
    }


    @Override
    public List<ArticleDTO> getAllArticle() {
        return preOrderRepository.findAll()
                .stream()
                .map(PreOrderServiceImpl::toAllItem)
                .toList();
    }

    @Override
    public void makeOrder(PreOrderDTO preOrderDTO) {

        preOrderRepository.deleteAll();
    }

    private static ArticleDTO toAllItem(PreOrderItem preOrderItem) {
        return new ArticleDTO(
                preOrderItem.getId(),
                preOrderItem.getArticle().getArtNum(),
                preOrderItem.getQuantityForOrder(),
                preOrderItem.getArticle().getArtUrl(),
                preOrderItem.getOrderBy(),
                preOrderItem.getDate(),
                preOrderItem.getOrderReason(),
                preOrderItem.getComment()
        );
    }
}
