package bg.deplan.Grohe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table (name = "articles")
public class Article extends BaseEntity{

    private String name;

    @Column(name = "art_num")
    private String artNum;

    @Column(name = "image_urls")
    private String imageUrl;

    @Column(name = "barcodes")
    private String barcode;

    @Column(name = "quantity_in_pallet")
    private int quantityInPallet;

    @Column(name = "quantityForOrder")
    private int quantityForOrder;


    public Article() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtNum() {
        return artNum;
    }

    public void setArtNum(String artNum) {
        this.artNum = artNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantityInPallet() {
        return quantityInPallet;
    }

    public void setQuantityInPallet(int quantityInPallet) {
        this.quantityInPallet = quantityInPallet;
    }

    public int getQuantityForOrder() {
        return quantityForOrder;
    }

    public void setQuantityForOrder(int quantityForOrder) {
        this.quantityForOrder = quantityForOrder;
    }
}
