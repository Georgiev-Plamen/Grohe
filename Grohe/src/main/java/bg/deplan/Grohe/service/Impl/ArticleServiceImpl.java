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
        mappedArticle.setBrand(addArticleDTO.brand());
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
    public List<AddArticleDTO> getAllArticle(String brand) {
        return articleRepository.findAll()
                .stream()
                .map(ArticleServiceImpl::toAllArticle)
                .filter(a -> a.brand().equals(brand))
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
        article.setName(addArticleDTO.name());
        article.setDescription(addArticleDTO.description());
        article.setImageUrl(addArticleDTO.imgUrl());
        article.setBarcode(addArticleDTO.barcode());
        article.setQuantityInPallet(addArticleDTO.quantityInPallet());

        articleRepository.save(article);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public void bulkUpdateArticle(List<AddArticleDTO> updates) {
        for (AddArticleDTO articleDTO : updates) {
                Long id = articleDTO.id();

                Article article = articleRepository.findById(id).get();

                if (articleDTO.artNum().isEmpty()) {
                    return;
                }
                article.setArtNum(articleDTO.artNum());
                article.setCodeDeplan(articleDTO.codeDeplan());
                article.setBarcode(articleDTO.barcode());
                article.setName(articleDTO.name());
                article.setDescription(articleDTO.description());
                article.setImageUrl(articleDTO.imgUrl());
                article.setBarcode(articleDTO.barcode());
                article.setQuantityInPallet(articleDTO.quantityInPallet());

                articleRepository.save(article);
        }
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public void createArticleByArtName(String artName, String brand) {
        Article article = new Article();
        article.setArtNum(artName);
        article.setBrand(brand);

        articleRepository.save(article);
    }

    @Override
    public Optional<Article> findByArtNum(String s) {
        return articleRepository.findByArtNum(s);
    }

    private static AddArticleDTO toAllArticle(Article article) {
        return new AddArticleDTO(
                article.getId(),
                article.getBrand(),
                article.getCodeDeplan(),
                article.getArtNum(),
                article.getName(),
                article.getDescription(),
                article.getImageUrl(),
                article.getBarcode(),
                article.getQuantityInPallet()
        );
    }
}
