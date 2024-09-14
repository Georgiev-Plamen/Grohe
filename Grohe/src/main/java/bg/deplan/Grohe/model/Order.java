package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
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

    @ManyToMany
    @JoinTable(
            name = "order_article",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private List<Article> article;

    @ManyToMany
    @JoinTable (
            name = "order_article",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private List<User> users;

    @Column(name = "order_name")
    private String orderName;

    public Order() {
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    @Column( name = "order_quantity")
    private int orderQuantity;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;


    public Order(List<User> users, List<Article> article) {
        this.users = new ArrayList<>();
        this.article = new ArrayList<>();
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
