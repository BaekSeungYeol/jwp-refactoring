package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.Fixtures;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static kitchenpos.domain.TestFixture.메뉴그룹_신규_NAME;
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
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(메뉴그룹_신규_NAME);

        // when
        MenuGroupResponse result = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(메뉴그룹_신규_NAME);
    }

}