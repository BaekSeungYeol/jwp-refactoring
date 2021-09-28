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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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

    @Test
    @DisplayName("주문 등록 시, 메뉴 등록이 되어 있지 않으면 IllegalArgumentException을 throw 해야 한다.")
    void createNotExistMenus() {

        Order notExistMenus = Fixtures.order(NOT_EMPTY_TABLE_ID, null, null, orderLineItems);
        when(menuDao.countByIdIn(MENU_IDS)).thenReturn(1L);

        assertThatThrownBy(() -> orderService.create(notExistMenus))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 목록을 주문의 상품목록과 함께 조회할 수 있다.")
    void list() {

        OrderLineItem orderLineItem = mock(OrderLineItem.class);

        //given
        Order ordersWithTwoLines = Fixtures.orderWithId(NOT_EMPTY_TABLE_ID,null,null,
                Arrays.asList(orderLineItem, orderLineItem),1L);
        Order ordersWithThreeLines = Fixtures.orderWithId(NOT_EMPTY_TABLE_ID,null,null,
                Arrays.asList(orderLineItem, orderLineItem, orderLineItem),2L);

        when(orderDao.findAll()).thenReturn(Arrays.asList(ordersWithTwoLines, ordersWithThreeLines));
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(ordersWithTwoLines.getOrderLineItems(), ordersWithThreeLines.getOrderLineItems());


        // when
        List<Order> results = orderService.list();

        // then
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0).getOrderLineItems().size()).isEqualTo(2);
        assertThat(results.get(1).getOrderLineItems().size()).isEqualTo(3);
    }


    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        String changedStatus = OrderStatus.MEAL.name();
        Order fixedOrder = Fixtures.order(NOT_EMPTY_TABLE_ID, changedStatus, null, orderLineItems);
        when(orderDao.findById(NEW_ORDER_ID)).thenReturn(Optional.of(fixedOrder));
        when(orderLineItemDao.findAllByOrderId(NEW_ORDER_ID)).thenReturn(orderLineItems);

        // when
        Order result = orderService.changeOrderStatus(NEW_ORDER_ID, fixedOrder);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(changedStatus);
        assertThat(result.getOrderLineItems().size()).isEqualTo(orderLineItemsSize);
    }

    @Test
    @DisplayName("주문이 존재하지 않아 상태를 변경할 수 없다.")
    void notExistedOrderChange() {
        // given
        Long notExistOrderId = 200L;
        Order changeStatusOrder = mock(Order.class);
        when(orderDao.findById(notExistOrderId)).thenReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, changeStatusOrder))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문상태가 완료인 경우 주문 상태를 변경시 IllegalArgumetException 발생")
    void changeCompletedOrderStatus() {

        // given
        Order savedOrder = Fixtures.order(NOT_EMPTY_TABLE_ID, OrderStatus.COMPLETION.name(), null, orderLineItems);
        when(orderDao.findById(NEW_ORDER_ID)).thenReturn(Optional.of(savedOrder));

        // when - then
        assertThatThrownBy(() -> orderService.changeOrderStatus(NEW_ORDER_ID,savedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }


}

