package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository <Article, Long> {

    @Query("SELECT a FROM Article a WHERE a.artNum LIKE %:artNum%")
    Optional<Article> findByArtNum(String artNum);

    @Query("SELECT a FROM Article a WHERE a.artNum LIKE %:artNum%")
    List<Long> findIdsByArtNum(String artNum);

}
