package bg.deplan.Grohe.service;



import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    void addArticle(AddArticleDTO addArticleDTO);

    List<AddArticleDTO> getAllArticle(String brand);

    AddArticleDTO getArticleData(String artNum);

    void editArticle(AddArticleDTO addArticleDTO);

    Optional<Article> findById(Long id);

    Optional<Article> findByArtNum(String artNum);

    List<Long> findArticleIds(String artNum);

    void bulkUpdateArticle(List<AddArticleDTO> updates);

    void deleteArticle(Long id);

    void createArticleByArtName(String artName, String brand);
}
