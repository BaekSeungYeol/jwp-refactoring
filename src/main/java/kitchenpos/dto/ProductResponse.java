package kitchenpos.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

//    protected ProductResponse() { /* empty */ }


    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
