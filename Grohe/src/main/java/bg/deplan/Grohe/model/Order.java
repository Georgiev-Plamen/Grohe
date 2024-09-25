package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name ="orders")
public class Order extends BaseEntity{

    @Column(name = "date_of_request")
    private LocalDate dataOfRequest;

    @Column(name = "date_of_order")
    private LocalDate dateOfOrder;

    @Column(name = "order_name")
    private String orderName;

    public Order() {
    }

    public LocalDate getDataOfRequest() {
        return dataOfRequest;
    }

    public void setDataOfRequest(LocalDate dataOfRequest) {
        this.dataOfRequest = dataOfRequest;
    }

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

//    public List<PreOrderItem> getItems() {
//        return items;
//    }
//
//    public void setItems(List<PreOrderItem> items) {
//        this.items = items;
//    }
}
