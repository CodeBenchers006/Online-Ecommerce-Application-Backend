package codebenchers006.ecommerce.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int sales_id;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    private int totalSales;


}
