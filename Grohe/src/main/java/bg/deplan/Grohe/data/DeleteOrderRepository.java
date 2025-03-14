package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.DeleteOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteOrderRepository extends JpaRepository<DeleteOrder, Long> {
}
