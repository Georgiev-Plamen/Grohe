package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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
        mappedArticle.setBrand("Grohe");
        mappedArticle.setArtNum(addArticleDTO.artNum());
        mappedArticle.setName(addArticleDTO.name());
        mappedArticle.setDescription(addArticleDTO.description());
        mappedArticle.setImageUrl(addArticleDTO.imgUrl());
        mappedArticle.setBarcode(addArticleDTO.barcode());
        mappedArticle.setQuantityInPallet(addArticleDTO.quantityInPallet());
        mappedArticle.setQuantityInPacket(1);

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

//        return modelMapper.map(articleRepository.findByArtNum(artNum).get(), AddArticleDTO.class);
        return articleRepository.findByArtNum(artNum).stream().map(ArticleServiceImpl::toAllArticle).toList().stream().findFirst().get();
    }

    @Override
    public void editArticle(AddArticleDTO addArticleDTO) {
        Article article = articleRepository.findByArtNum(addArticleDTO.artNum()).get();
        article.setArtNum(addArticleDTO.artNum());
        article.setName(addArticleDTO.name());article.setDescription(addArticleDTO.description());
        article.setImageUrl(addArticleDTO.imgUrl());
        article.setBarcode(addArticleDTO.barcode());
        article.setQuantityInPallet(addArticleDTO.quantityInPallet());

        articleRepository.save(article);
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id).get();
    }

    public Article findByArtNum(String s) {
        return articleRepository.findByArtNum(s).get();
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
