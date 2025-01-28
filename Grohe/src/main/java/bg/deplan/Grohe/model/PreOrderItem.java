package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name ="preOrder_item")
public class PreOrderItem extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
    private String quantityForOrder;
    private String orderBy;
    private String orderReason;
    private String comment;
    private LocalDate date;

    public PreOrderItem() {
    }

    public PreOrderItem(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getQuantityForOrder() {
        return quantityForOrder;
    }

    public void setQuantityForOrder(String quantityForOrder) {
        this.quantityForOrder = quantityForOrder;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
