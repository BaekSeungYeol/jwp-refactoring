package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;


    private static final Long NEW_ORDER_ID = 1L;
    private static final Long NOT_EMPTY_TABLE_ID = 9L;
    private static final Long MENU_ID = 1L;
    private static final Long MENU2_ID = 2L;

    private static final List<Long> MENU_IDS = Arrays.asList(MENU_ID, MENU2_ID);

    private OrderTable savedOrderTable;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private List<OrderLineItem> orderLineItems;
    private long orderLineItemsSize;
    private Order createdOrder;

    @BeforeEach
    void setUp() {
        savedOrderTable = Fixtures.orderTableWithId(1L, 2, false, NOT_EMPTY_TABLE_ID);
        orderLineItem1 = Fixtures.orderLineItem(null, MENU_ID, 2);
        orderLineItem2 = Fixtures.orderLineItem(null, MENU2_ID, 1);
        orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        orderLineItemsSize = orderLineItems.size();
        createdOrder = Fixtures.orderWithId(NOT_EMPTY_TABLE_ID, OrderStatus.COOKING.name(), LocalDateTime.now(),
                orderLineItems, NEW_ORDER_ID);
    }


    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createOrder() {

        // given
        String orderStatus = OrderStatus.COOKING.name();
        OrderLineItem savedOrderLineItem1 = Fixtures.orderLineItemWithSeq(NEW_ORDER_ID, MENU_ID, 2, 1L);
        OrderLineItem savedOrderLineItem2 = Fixtures.orderLineItemWithSeq(NEW_ORDER_ID, MENU2_ID, 1, 2L);

        when(menuDao.countByIdIn(MENU_IDS)).thenReturn(orderLineItemsSize);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(savedOrderTable));
        //save 전에 3가지 정보가 setting 되어야 함
        when(orderDao.save(argThat(allOf(
                hasProperty("orderTableId", is(NOT_EMPTY_TABLE_ID))
                , hasProperty("orderStatus", is(orderStatus))
                , hasProperty("orderedTime", notNullValue())
        )))).thenReturn(createdOrder);
        when(orderLineItemDao.save(any())).thenReturn(savedOrderLineItem1, savedOrderLineItem2);
        // when
        Order result = orderService.create(createdOrder);

        /*
          private String orderStatus;
      private LocalDateTime orderedTime;
         private List<OrderLineItem> orderLineItems;
         */
        // then
        assertThat(result.getId()).isEqualTo(NEW_ORDER_ID);
        assertThat(result.getOrderTableId()).isEqualTo(NOT_EMPTY_TABLE_ID);
        assertThat(result.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(result.getOrderedTime()).isNotNull();
        assertThat(result.getOrderLineItems()).containsExactlyInAnyOrder(savedOrderLineItem1, savedOrderLineItem2);
    }

    @Test
    @DisplayName("주문 등록 시, 주문 아이템이 Null이면 IllegalArgumentException을 throw 해야한다.")
    void createOrderItemNull() {
        //given
        Order nullItemOrder = Fixtures.order(NOT_EMPTY_TABLE_ID, null, null, null);

        //when-then
        assertThatThrownBy(() -> orderService.create(nullItemOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 시 , 주문 아이템이 비어있다면 IllegalArgumentException을 throw 한다.")
    void createOrderItemEmpty() {
        Order emptyItemOrder = Fixtures.order(NOT_EMPTY_TABLE_ID, null, null, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(emptyItemOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 등록 시, 주문 테이블이 등록되어 있지 않다면 IllegalArgumentException을 throw 해야 한다.")
    void createNotExistOrderTable() {
        Order notExistOrderTable = Fixtures.order(100L, null, null, orderLineItems);
        when(menuDao.countByIdIn(MENU_IDS)).thenReturn(orderLineItemsSize);
        when(orderTableDao.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(notExistOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }



}

