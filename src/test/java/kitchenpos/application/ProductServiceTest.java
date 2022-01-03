package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.Fixtures;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void create() {
        //given
        String name = "상품1";
        BigDecimal price = BigDecimal.valueOf(1000);
        Product product = Fixtures.product(name, price);
        Product savedProduct = Fixtures.productWithId(name, price, 1L);
        when(productDao.save(product)).thenReturn(savedProduct);

        //when
        Product result = productService.create(product);

        //then
        assertThat(result).isEqualTo(savedProduct);
    }

    @Test
    @DisplayName("상품 등록 시, 상품의 가격이 없으면 IllegalArgumentException을 throw 해야한다.")
    void createPriceNull() {
        //given
        Product emptyPriceProduct = Fixtures.product("상품1", null);

        //when-then
        assertThatThrownBy(() -> productService.create(emptyPriceProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 시, 상품의 가격이 0 원 미만이면 IllegalArgumentException을 throw 해야한다.")
    void createPriceLessThanZero() {
        //given
        Product negativePriceProduct = Fixtures.product("상품1", BigDecimal.valueOf(-200));

        //when-then
        assertThatThrownBy(() -> productService.create(negativePriceProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
