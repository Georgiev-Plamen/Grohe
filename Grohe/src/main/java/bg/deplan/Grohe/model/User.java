package bg.deplan.Grohe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    @OneToMany
    private List<Order> orders;

}
