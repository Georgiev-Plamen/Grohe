package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name ="order_items")
public class OrderItem extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "comment")
    private String comment;

    @Column(name = "order_by")
    private String orderBy;

    @Column(name = "order_reason")
    private String orderReason;

    @Column(name = "date_of_order")
    private LocalDate dateOfOrder;

    @Column(name = "date_of_delivery")
    private LocalDate dateOfDelivery;

    @ManyToOne
    private User user;

    public OrderItem() {}

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Article getArticle() {
        return article;
    }

    public long articleId() {
        return article.getId();
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(String orderReason) {
        this.orderReason = orderReason;
    }

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public LocalDate getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(LocalDate dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
