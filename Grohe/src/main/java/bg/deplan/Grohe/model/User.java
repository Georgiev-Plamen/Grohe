package bg.deplan.Grohe.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    @ManyToOne
    private Order orders;

}
