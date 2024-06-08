package bg.deplan.Grohe.model;

public class ArticleDTO {

    private String artNum;

    private int quantityForOrder;

    public ArticleDTO() {}

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
}
