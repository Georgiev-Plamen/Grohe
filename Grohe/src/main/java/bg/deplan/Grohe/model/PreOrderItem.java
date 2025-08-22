package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name ="pre_order_item")
public class PreOrderItem extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
    private String quantityForOrder;
    private String orderBy;
    private String orderReason;
    private String comment;
    private LocalDate date;
    private boolean isHold;
    private int position;

    @ManyToOne
    private User user;

    public PreOrderItem() {
    }

    public PreOrderItem(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return this.article;
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

    public boolean isHold() {
        return isHold;
    }

    public void setHold(boolean hold) {
        isHold = hold;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
