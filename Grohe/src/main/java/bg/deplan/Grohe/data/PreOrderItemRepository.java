package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.PreOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreOrderItemRepository extends JpaRepository <PreOrderItem, Long> {

}
