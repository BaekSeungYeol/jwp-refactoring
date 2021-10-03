package kitchenpos.dao;


import kitchenpos.BaseDaoTest;
import kitchenpos.domain.Fixtures;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class OrderDaoTest extends BaseDaoTest {

    @Autowired
    OrderDao orderDao;

    private final static long NOT_EMPTY_TABLE_ID = 9L;
    private final static String NEW_ORDER_STATUS = OrderStatus.COOKING.name();

    @Test
    @DisplayName("주문을 정상적으로 생성하면 Id가 생성된다.")
    void createOrderSuccess() {
        Order order = Fixtures.order(NOT_EMPTY_TABLE_ID, NEW_ORDER_STATUS, LocalDateTime.now(), new ArrayList<>());
        Order savedOrder = orderDao.save(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(NEW_ORDER_STATUS);
        assertThat(savedOrder.getOrderedTime()).isNotNull();
    }

    @Test
    @DisplayName("주문 생성시 테이블 ID 가 없으면 DataIntegrityViolationException을 Throw 한다.")
    void orderWithoutTableId() {

        Order order = Fixtures.order(null, NEW_ORDER_STATUS, LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() -> orderDao.save(order))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("주문 생성시 주문 상태가 없으면 DataIntegrityViolationException을 Throw 한다.")
    void orderWithoutOrderStatus() {

        Order order = Fixtures.order(NOT_EMPTY_TABLE_ID, null, LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() -> orderDao.save(order))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("주문 생성시 주문 날짜가 없으면 DataIntegrityViolationException을 Throw 한다.")
    void orderWithoutOrderTime() {

        Order order = Fixtures.order(NOT_EMPTY_TABLE_ID, NEW_ORDER_STATUS, null, new ArrayList<>());

        assertThatThrownBy(() -> orderDao.save(order))
                .isInstanceOf(DataIntegrityViolationException.class);

    }
}
