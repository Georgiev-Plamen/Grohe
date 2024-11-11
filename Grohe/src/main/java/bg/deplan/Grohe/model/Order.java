package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name ="orders")
public class Order extends BaseEntity {

    @Column(name = "order_name")
    private String orderName;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "quantity_for_order")
    private int quantityForOrder;

    @Column(name = "order_by")
    private String orderBy;
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "orderReason")
    private String orderReason;

    @Column(name = "comment")
    private String comment;

    public Order() {
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getQuantityForOrder() {
        return quantityForOrder;
    }

    public void setQuantityForOrder(int quantityForOrder) {
        this.quantityForOrder = quantityForOrder;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
}
