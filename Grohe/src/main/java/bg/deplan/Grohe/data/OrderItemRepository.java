package bg.deplan.Grohe.data;


import bg.deplan.Grohe.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
