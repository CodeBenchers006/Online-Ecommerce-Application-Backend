package codebenchers006.ecommerce.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int address_id;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private @NotNull String country;
    private @NotNull String firstName;
    private @NotNull String lastName;
    private @NotNull String contact;
    private @NotNull String address;
    private String apartment;
    private @NotNull String city;
    private @NotNull String state;
    private @NotNull String pin;

}
