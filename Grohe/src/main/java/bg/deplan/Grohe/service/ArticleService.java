package bg.deplan.Grohe.service;


import bg.deplan.Grohe.model.AddArticleDTO;

public interface ArticleService {

    // TODO:
    long addArticle(AddArticleDTO addArticleDTO);

    long addArticle (bg.deplan.Grohe.service.Impl.AddArticleDTO addArticleDTO);
}
