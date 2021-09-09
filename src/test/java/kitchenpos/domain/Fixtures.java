package kitchenpos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Fixtures {

    public static Product product(String name, Integer price) {
        Product product = new Product();
        product.setName(name);

        if(price != null) {
            product.setPrice(BigDecimal.valueOf(price));
        }
        return product;
    }

    public static Product productWithId(String name, int price, Long id) {
        Product product = product(name,price);
        product.setId(id);
        return product;
    }

    public static OrderLineItem orderLineItem(Long orderId, Long menuId , int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static Order order(Long tableId, String orderStatus, LocalDateTime localDateTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(localDateTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
