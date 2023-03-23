package codebenchers006.ecommerce.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    private int dtoId;
    private @NotNull String name;
    private @NotNull String imageUrl;
    private @NotNull double price;
    private @NotNull String description;
    private @NotNull int categoryId;

    private int totalItems;
}
