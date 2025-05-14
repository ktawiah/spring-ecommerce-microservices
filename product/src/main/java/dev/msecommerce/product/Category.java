package dev.msecommerce.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private Integer id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
