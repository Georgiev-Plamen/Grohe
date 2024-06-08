package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.time.Instant;
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
    @OneToMany
    private List<Article> articles;

    @ManyToOne
    private User user;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

}
