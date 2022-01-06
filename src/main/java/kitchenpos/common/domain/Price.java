package kitchenpos.common.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() { /* empty */ }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public BigDecimal value() {
        return this.price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 NULL 또는 음수 일 수 없습니다.");
        }
    }
}
