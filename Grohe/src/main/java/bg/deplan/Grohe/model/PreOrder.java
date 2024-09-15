package bg.deplan.Grohe.model;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "pre_order")
public class PreOrder extends BaseEntity {

    @OneToMany(mappedBy = "preOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreOrderItem> items = new ArrayList<>();

    public void addItem(Article article, int quantity, String orderBy, LocalDate date, String orderReason, String comment) {
        PreOrderItem preOrderItem = new PreOrderItem();

        preOrderItem.setArticle(article);
        preOrderItem.setQuantity(quantity);
        preOrderItem.setOrderBy(orderBy);
        preOrderItem.setDate(date);
        preOrderItem.setOrderReason(orderReason);
        preOrderItem.setComment(comment);
        preOrderItem.setPreOrder(this);  // Set association back to PreOrder

        items.add(preOrderItem);
    }

    public void removeItem(PreOrderItem preOrderItem) {
        items.remove(preOrderItem);
        preOrderItem.setPreOrder(null);
    }

    public void clearItems() {
        items.clear();
    }

    public List<PreOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PreOrderItem> items) {
        this.items = items;
    }


}
