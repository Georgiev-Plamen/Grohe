package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.PreOrderRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.PreOrder;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreOrderServiceImpl implements PreOrderService {

    private ArticleRepository articleRepository;

    private PreOrderRepository preOrderRepository;

    public PreOrder addItemToPreOrder(PreOrder preOrder, AddArticleDTO addArticleDTO) {
        Optional<Article> optionalArticle = articleRepository.findByArtNum(addArticleDTO.getArtNum());

        Article article;

        if(optionalArticle.isEmpty()) {
            article = new Article();
            article.setArtNum(addArticleDTO.getArtNum());
            articleRepository.save(article);
        }

        article = optionalArticle.get();

        preOrder.addItem(article, addArticleDTO.getQuantityForOrder(), addArticleDTO.getOrderReason(), addArticleDTO.getDate(),addArticleDTO.getOrderReason(),addArticleDTO.getComment());

        return preOrderRepository.save(preOrder);

    }
}
