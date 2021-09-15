package kitchenpos.ui;

import kitchenpos.BaseControllerTest;
import kitchenpos.domain.Fixtures;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 Controller 테스트")
public class OrderRestControllerTest extends BaseControllerTest {

    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private List<OrderLineItem> orderLineItems;
    private Order order;
    private int orderItemSize;
    private static final Long NEW_ORDER_ID = 1L;
    private static final Long NOT_EMPTY_ORDER_TABLE_ID = 9L;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderLineItem1 = Fixtures.orderLineItem(null,1L,2);
        orderLineItem2 = Fixtures.orderLineItem(null,2L,1);
        orderLineItems = Arrays.asList(orderLineItem1,orderLineItem2);
        orderItemSize = orderLineItems.size();
        order = Fixtures.order(NOT_EMPTY_ORDER_TABLE_ID, null, null, orderLineItems);

    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void getOrderLists() throws Exception {
        createOrder();

        mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$..id").isNotEmpty())
                .andExpect(jsonPath("$..orderStatus").isNotEmpty())
                .andExpect(jsonPath("$..orderedTime").isNotEmpty())
                .andExpect(jsonPath("$..orderLineItems").isNotEmpty())
                .andExpect(jsonPath("$..seq", hasSize(orderItemSize)))
                .andExpect(jsonPath("$..orderId", hasSize(orderItemSize)))
                .andExpect(jsonPath("$..menuId", hasSize(orderItemSize)))
                .andExpect(jsonPath("$..quantity", hasSize(orderItemSize)))
                ;

    }

    @Test
    @DisplayName("주문 등록 후 등록된 주문의 아이디를 포함한 정보를 반환한다.")
    void createOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
                .andExpect(jsonPath("$.orderedTime").isNotEmpty())
                .andExpect(jsonPath("$..seq").isNotEmpty())
                .andExpect(jsonPath("$..orderId").isNotEmpty());

    }

    @Test
    @DisplayName("주문 조리 상태를 수정할 수 있다.")
    void updateOrder() throws Exception {
        createOrder();
        Order savedOrder  = Fixtures.orderWithId(NOT_EMPTY_ORDER_TABLE_ID, OrderStatus.MEAL.name(),
                LocalDateTime.now(), orderLineItems, NEW_ORDER_ID);

        /*
        "/api/orders/{orderId}/order-status")
         */
        mockMvc.perform(put(String.format("/api/orders/%d/order-status", NEW_ORDER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedOrder)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(NEW_ORDER_ID))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.MEAL.name()))
                .andExpect(jsonPath("$.orderedTime").isNotEmpty())
                .andExpect(jsonPath("$..seq", hasSize(orderItemSize)))
                .andExpect(jsonPath("$..orderId", hasSize(orderItemSize)));
    }

}
