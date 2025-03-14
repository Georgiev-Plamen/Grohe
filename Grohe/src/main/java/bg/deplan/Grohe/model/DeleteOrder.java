package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name ="delete_orders")
public class DeleteOrder extends BaseEntity {

    private String brand;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "deleteOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DeleteOrderItem> items;

    public DeleteOrder() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<DeleteOrderItem> getItems() {
        return items;
    }

    public void setItems(List<DeleteOrderItem> items) {
        this.items = items;
    }
}
