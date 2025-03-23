package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.PreOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreOrderItemRepository extends JpaRepository <PreOrderItem, Long> {

    List<PreOrderItem> findAllByArticle_Brand(String brand);

    void deleteAllByArticle_BrandAndIsHoldIsFalse(String brand);

    @Modifying
    @Query("DELETE FROM PreOrderItem p WHERE p.id = :id")
    void deleteByArticleId(@Param("id") Long id);

}
