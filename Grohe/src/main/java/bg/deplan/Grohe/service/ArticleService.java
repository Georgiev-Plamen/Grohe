package bg.deplan.Grohe.service;



import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;

import java.util.List;

public interface ArticleService {

    void addArticle(AddArticleDTO addArticleDTO);

    List<AddArticleDTO> getAllArticle();

    AddArticleDTO getArticleData(String artNum);

    void editArticle(AddArticleDTO addArticleDTO);
}
