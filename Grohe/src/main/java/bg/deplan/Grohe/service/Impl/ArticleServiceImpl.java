package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.service.ArticleService;
import jdk.dynalink.Operation;
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
        mappedArticle.setName(addArticleDTO.name());
        mappedArticle.setDescription(addArticleDTO.description());
        mappedArticle.setImageUrl(addArticleDTO.imgUrl());
        mappedArticle.setBarcode(addArticleDTO.barcode());
        mappedArticle.setQuantityInPallet(addArticleDTO.quantityInPallet());

        articleRepository.save(mappedArticle);
    }

    @Override
    public List<AddArticleDTO> getAllArticle() {
        return articleRepository.findAll()
                .stream()
                .map(ArticleServiceImpl::toAllArticle)
                .toList();
    }

    @Override
    public AddArticleDTO getArticleData(String artNum) {

        return modelMapper.map(articleRepository.findByArtNum(artNum).get(), AddArticleDTO.class);
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
