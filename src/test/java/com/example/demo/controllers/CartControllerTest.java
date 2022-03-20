package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.demo.controllers.OrderControllerTest.getCartRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        when(userRepository.findByUsername("test")).thenReturn(createUser(2));
        when(itemRepository.findById((long) 2)).thenReturn(Optional.of(createItem(2)));
    }


    public static User createUser(long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("test"+id);
        user.setPassword("testPassword"+id);
        user.setCart(createCart(user,id));
        return user;
    }

    public static Item createItem(long id){
        Item item = new Item();
        item.setId(id);
        item.setName("Test" + item.getId());
        item.setDescription("Item Description "+id);
        item.setPrice(BigDecimal.valueOf(400*id));
        return item;
    }

    public static Cart createCart(User user,long id) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(createItem(id));
        return cart;
    }


    @Test
    public void Test_addToCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(5);
        request.setItemId(2);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(response.getBody().getUser().getUsername(),"test2");
    }

    @Test
    public void TestAddToCortNoItem() {
        ResponseEntity<Cart> response = cartController.addTocart(getCartRequest());
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void Test_removeFromCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(5);
        request.setItemId(2);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(response.getBody().getUser().getUsername(),"test2");
    }
}
