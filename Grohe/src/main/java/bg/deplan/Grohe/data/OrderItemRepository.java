package bg.deplan.Grohe.data;



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

//    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderBy = :orderBy")
    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderBy LIKE %:orderBy% ORDER BY oi.order")
    List<OrderItem> findOrderItemsByOrderBy(@Param("orderBy") String orderBy);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.article.id = :article ORDER BY oi.order")
    List<OrderItem> findOnlyArticlesInOrder(@Param("article") long article);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.article.id IN :ids ORDER BY oi.order")
    List<OrderItem> findOrderItemsByIDs(@Param("ids") List<Long> ids);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :id")
    List<OrderItem> findOrderItemsByOrderID(@Param("id") Long id);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.comment LIKE %:comment%")
    List<OrderItem> findOrderItemByComment(String comment);
}
