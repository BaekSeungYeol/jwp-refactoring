package kitchenpos.dao;

import kitchenpos.BaseDaoTest;
import kitchenpos.domain.Product;
import kitchenpos.domain.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ProductDaoTest extends BaseDaoTest {

    @Autowired
    ProductDao productDao;


    @Test
    @DisplayName("상품을 정상적으로 ID를 반환한다")
    void saveReturnId() {
        // given
        String name = "상품1";
        int price = 1000;
        Product product = Fixtures.product(name,price);

        // when
        Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(name);
        assertThat(savedProduct.getPrice().intValue()).isEqualTo(price);
    }

    @Test
    @DisplayName("상품 등록 시, 이름이 없으면 DataIntegrityViolationException 을 Throw 한다.")
    void saveWithoutName() {
        Product nullNameProduct = Fixtures.product(null, 1000);

        assertThatThrownBy(() -> productDao.save(nullNameProduct))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("상품 등록 시, 가격이 없으면 DataIntegrityViolationException 을 Throw 한다.")
    void saveWithoutPrice() {
        Product nullPriceProduct = Fixtures.product("상품1", null);

        assertThatThrownBy(() -> productDao.save(nullPriceProduct))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}