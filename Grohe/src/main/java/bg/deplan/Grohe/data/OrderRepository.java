package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Order, Long> {

}

