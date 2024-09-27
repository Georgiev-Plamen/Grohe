package bg.deplan.Grohe.service;



import bg.deplan.Grohe.model.DTOs.AddArticleDTO;

import java.util.List;

public interface ArticleService {

    // TODO:
    void addArticle(AddArticleDTO addArticleDTO);

    List<AddArticleDTO> getAllArticle();
}
