package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void addArticle(AddArticleDTO addArticleDTO) {
        Article mappedArticle = new Article();
        mappedArticle.setArtNum(addArticleDTO.artNum());

        articleRepository.save(mappedArticle);
    }

    @Override
    public List<AddArticleDTO> getAllArticle() {
        return articleRepository.findAll()
                .stream()
                .map(ArticleServiceImpl::toAllArticle)
                .toList();
    }

    private static AddArticleDTO toAllArticle(Article article) {
        return new AddArticleDTO(
                article.getArtNum(),
                article.getName(),
                article.getDescription(),
                article.getImageUrl(),
                article.getBarcode(),
                article.getQuantityInPallet()
        );
    }
}
