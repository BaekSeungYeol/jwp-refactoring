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

    public static Order orderWithId(Long tableId, String orderStatus, LocalDateTime now, List<OrderLineItem> orderLineItems, Long orderId) {
        Order order = order(tableId,orderStatus,now,orderLineItems);
        order.setId(orderId);
        return order;
    }

    public static OrderTable orderTableWithId(long tableGroupId, int numberOfGuests, boolean empty, Long tableId) {
        OrderTable orderTable = orderTable(tableGroupId,numberOfGuests,empty);
        orderTable.setId(tableId);
        return orderTable;
    }

    private static OrderTable orderTable(long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderLineItem orderLineItemWithSeq(Long orderId, Long menuId, int quantity, long seq) {
        OrderLineItem orderLineItem = orderLineItem(orderId,menuId,quantity);
        orderLineItem.setSeq(seq);
        return orderLineItem;
    }

    public static MenuGroup menuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
    public static MenuGroup menuGroup(String name, Long id) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        menuGroup.setId(id);
        return menuGroup;
    }

    public static MenuProduct menuProduct(Long menuId, Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Menu menu(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        if(price != null) {
            menu.setPrice(BigDecimal.valueOf(price));
        }
        return menu;
    }
}
