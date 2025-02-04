package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.model.Role;
import bg.deplan.Grohe.model.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreOrderItemRepository extends JpaRepository <PreOrderItem, Long> {

    List<PreOrderItem> findAllByArticle_Brand(String brand);

    void deleteAllByArticle_Brand(String brand);
}
