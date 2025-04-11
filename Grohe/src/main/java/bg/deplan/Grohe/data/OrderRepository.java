package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.DTOs.OrderDTO;
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


    Order getOrdersById(Long id);
}

