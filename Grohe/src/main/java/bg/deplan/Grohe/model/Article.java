package bg.deplan.Grohe.model;

import jakarta.persistence.*;

@Entity
@Table (name = "articles")
public class Article extends BaseEntity{

    @Column
    private String brand;
    @Column
    private String name;

    @Column(name = "art_num")
    private String artNum;

    @Column(name = "code_deplan")
    private String codeDeplan;

    @Column
    private String description;

    @Column(name = "image_urls")
    private String imageUrl;

    @Column(name = "art_urls")
    private String artUrl;

    @Column(name = "barcodes")
    private String barcode;

    @Column(name = "quantity_in_pallet")
    private int quantityInPallet;

    @Column(name = "quantity_in_packet")
    private int quantityInPacket;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtUrl() {
        return artUrl;
    }

    public void setArtUrl(String artUrl) {
        this.artUrl = artUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantityInPacket() {
        return quantityInPacket;
    }

    public void setQuantityInPacket(int quantityInPacket) {
        this.quantityInPacket = quantityInPacket;
    }

    public String getCodeDeplan() {
        return codeDeplan;
    }

    public void setCodeDeplan(String codeDeplan) {
        this.codeDeplan = codeDeplan;
    }
}
