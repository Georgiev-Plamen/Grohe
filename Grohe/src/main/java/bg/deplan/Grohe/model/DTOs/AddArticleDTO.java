package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public class AddArticleDTO {

    private String artNum;

    private int quantityForOrder;

    private LocalDate date;

    private String orderReason;

    private String comment;

    public AddArticleDTO() {}

    public String getArtNum() {
        return artNum;
    }

    public void setArtNum(String artNum) {
        this.artNum = artNum;
    }

    public int getQuantityForOrder() {
        return quantityForOrder;
    }

    public void setQuantityForOrder(int quantityForOrder) {
        this.quantityForOrder = quantityForOrder;
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
