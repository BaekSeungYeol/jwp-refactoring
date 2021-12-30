package kitchenpos.dao;

import kitchenpos.BaseDaoTest;
import kitchenpos.domain.Fixtures;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupDaoTest extends BaseDaoTest {
    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("단체를 정상적으로 등록하면 ID가 생성된다.")
    void saveReturnId() {
        //given
        TableGroup tableGroup = Fixtures.tableGroup(new ArrayList<>(), LocalDateTime.now());

        //when
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        //then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("단체 지정 시, 생성일자가 없으면 DataIntegrityViolationException 을 throw 해야한다.")
    void saveWithoutPrice() {
        //given
        TableGroup tableGroup = Fixtures.tableGroup(new ArrayList<>(), null);

        //when-then
        assertThatThrownBy(() -> tableGroupDao.save(tableGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
