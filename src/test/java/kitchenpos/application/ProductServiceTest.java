package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.TestDomainConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void create() {
        //given
        String name = "상품1";
        int price = 100;
        Product product = TestDomainConstructor.product(name,price);
        Product savedProduct = TestDomainConstructor.productWithId(name,price,1L);
        when(productDao.save(product)).thenReturn(savedProduct);

        // when
        Product result = productService.create(product);

        //then
        assertThat(result).isEqualTo(savedProduct);
    }

    @Test
    @DisplayName("상품 등록 시, 상품의 가격이 없으면 IllegalArgumentException을 Throw 해야 한다.")
    void createPriceNull() {
        // given
        Product emptyPriceProduct = TestDomainConstructor.product("상품1", null);

        // when-then
        assertThatThrownBy(() -> productService.create(emptyPriceProduct))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @Test
    @DisplayName("상품 ㅁ등록 시, 가격이 0원 미만이면 IllegalArgumentException을 Throw 해야 한다.")
    void createPriceLessThanZero() {
        // given
        Product emptyPriceProduct = TestDomainConstructor.product("상품1", -200);

        // when-then
        assertThatThrownBy(() -> productService.create(emptyPriceProduct))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

}