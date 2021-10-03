package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.Fixtures;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;


    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void createMenuGroup() {

        // given
        String name = "메뉴그룹1";
        MenuGroup menuGroup = Fixtures.menuGroup(name);
        MenuGroup savedMenuGroup = Fixtures.menuGroup(name, 5L);
        when(menuGroupDao.save(menuGroup)).thenReturn(savedMenuGroup);

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result).isEqualTo(savedMenuGroup);
    }

}