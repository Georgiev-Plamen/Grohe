package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentOrderRepository extends JpaRepository <Article, Long> {

}
