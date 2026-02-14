package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {

    @Query("SELECT coalesce(max(id), 0) FROM Order WHERE brand = %:brand%")
    Long findLastId(@Param("brand") String brand);

    @Query("SELECT o FROM Order o WHERE o.brand = :brand ORDER BY o.id DESC LIMIT 5")
    List<Order> findLastFiveOrdersByBrand(@Param("brand") String brand);

    Order getOrdersById(Long id);

    @Query("SELECT o FROM Order o WHERE o.brand = :brand AND YEAR(o.date) = :year")
    List<Order> findByBrandAndDateYear(@Param("brand") String brand, @Param("year") Integer year);

    @Query("SELECT o FROM Order o WHERE o.brand = :brand")
    List<Order> findByBrand(@Param("brand") String brand);
}