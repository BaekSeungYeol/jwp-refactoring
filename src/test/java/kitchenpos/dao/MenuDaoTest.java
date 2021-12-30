package kitchenpos.dao;

import kitchenpos.BaseDaoTest;
import kitchenpos.domain.Fixtures;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MenuDaoTest extends BaseDaoTest {

    @Autowired
    private MenuDao menuDao;

    @Test
    @DisplayName("메뉴를 정상적으로 등록하면 ID가 생성된다.")
    void saveReturnId() {
        //given
        String name = "상품1";
        int price = 1000;
        Long menuGroupId = 1L;
        Menu menu = Fixtures.menu(name, price, menuGroupId, new ArrayList<>());

        //when
        Menu savedMenu = menuDao.save(menu);

        //then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(name);
        assertThat(savedMenu.getPrice().intValue()).isEqualTo(price);
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroupId);
    }

    @Test
    @DisplayName("메뉴 등록 시, 이름이 없으면 DataIntegrityViolationException 을 throw 해야한다.")
    void saveWithoutName() {
        //given
        Menu nullNameMenu = Fixtures.menu(null, 1000, 1L, new ArrayList<>());

        //when-then
        assertThatThrownBy(() -> menuDao.save(nullNameMenu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("메뉴 등록 시, 가격이 없으면 DataIntegrityViolationException 을 throw 해야한다.")
    void saveWithoutPrice() {
        //given
        Menu emptyPriceMenu = Fixtures.menu("메뉴1", null, 1L, new ArrayList<>());

        //when-then
        assertThatThrownBy(() -> menuDao.save(emptyPriceMenu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("메뉴 등록 시, 메뉴그룹이 없으면 DataIntegrityViolationException 을 throw 해야한다.")
    void saveWithoutMenuGroup() {
        //given
        Menu emptyMenuGroupMenu = Fixtures.menu("메뉴1", 1000, null, new ArrayList<>());

        //when-then
        assertThatThrownBy(() -> menuDao.save(emptyMenuGroupMenu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

