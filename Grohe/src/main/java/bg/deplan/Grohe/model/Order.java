package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name ="orders")
public class Order extends BaseEntity{

    @Column(name = "date_of_request")
    private Instant dataOfRequest;

    @Column(name = "date_of_order")
    private Instant dateOfOrder;

    @Column(name = "order_reason")
    private String orderReason;
    @Column( name = "comments")
    private String comment;
    @OneToMany
    private List<Article> articles;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;


    public Order(List<Article> articles) {
        this.articles = new ArrayList<>(articles);
    }

    public Instant getDataOfRequest() {
        return dataOfRequest;
    }

    public void setDataOfRequest(Instant dataOfRequest) {
        this.dataOfRequest = dataOfRequest;
    }

    public Instant getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Instant dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public String getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(String orderReason) {
        this.orderReason = orderReason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
