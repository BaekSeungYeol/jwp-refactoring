package kitchenpos.dao;

import kitchenpos.BaseDaoTest;
import kitchenpos.domain.Fixtures;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupDaoTest extends BaseDaoTest {

    @Autowired
    MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 정상적으로 등록하면 ID를 반환한다.")
    void createMenuGroup() {

        // given
        String name = "메뉴그룹1";
        MenuGroup menuGroup = Fixtures.menuGroup(name);

        // when
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("메뉴 그룹의 이름이 없으면 DataIntegrityViolationException을 throw 한다.")
    void createMenuGroupWithoutName() {
        // given
        MenuGroup menuGroup = Fixtures.menuGroup(null);

        // when
        assertThatThrownBy(() -> menuGroupDao.save(menuGroup))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

}