package bg.deplan.Grohe.data;


import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.ArticleFindDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrderId(long id);

    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId")
    void deleteAllByOrderId(@Param("orderId") long id);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderBy = :orderBy")
    List<OrderItem> findOrderItemsByOrderBy(@Param("orderBy") String orderBy);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.articleId = :articleId")
    List<OrderItem> findOnlyArticlesInOrder(@Param("articleID") long articleId);
}
