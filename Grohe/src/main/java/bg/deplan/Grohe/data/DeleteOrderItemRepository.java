package bg.deplan.Grohe.data;

import bg.deplan.Grohe.model.DeleteOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteOrderItemRepository  extends JpaRepository <DeleteOrderItem, Long> {
}
