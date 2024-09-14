package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository <Article, Long> {

    Optional<Article> findByArtNum(String artNum);
}
