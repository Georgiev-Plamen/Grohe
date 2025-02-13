package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {

    @Query("SELECT coalesce(max(id), 0) FROM Order")
    Long findLastId();

}

