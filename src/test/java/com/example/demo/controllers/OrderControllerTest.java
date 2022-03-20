package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

    }


    public static User createUser() {
        User user = new User();
        user.setId(2L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(createCart());
        return user;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setDescription("Item Description ");
        item.setPrice(BigDecimal.valueOf(400.60));
        return item;
    }

    public static List<Item> createItems(){
        List<Item> items=new ArrayList<>();
        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setDescription("Item Description ");
        item.setPrice(BigDecimal.valueOf(400.60));
        items.add(item);
        return items;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        cart.setUser(null);
        cart.setId(2L);
        cart.setItems(new ArrayList<Item>());
        cart.setTotal(BigDecimal.valueOf(2.0));
        return cart;
    }

    public static ModifyCartRequest getCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("ravikr42");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        return modifyCartRequest;
    }

    @Test
    public void testSubmit() {
        User user= createUser();
        Cart cart = user.getCart();
        cart.setItems(createItems());
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("test");
        UserOrder userOrder = response.getBody();
        assertNotNull(response);
        assertNotNull(userOrder);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", userOrder.getUser().getUsername());
    }

    @Test
    public void TestSubmitNoneExistUser() {
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersUser() {
        User user= createUser();
        Cart cart = user.getCart();
        cart.setItems(createItems());
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
    }
}
