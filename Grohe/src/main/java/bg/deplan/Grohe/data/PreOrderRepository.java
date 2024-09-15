package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.PreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreOrderRepository extends JpaRepository <PreOrder, Long> {

}
