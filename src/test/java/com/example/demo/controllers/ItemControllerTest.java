package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;

import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository",itemRepository);
    }


    @Test
    public void Test_getItemByName(){
        Item item = new Item();
        long id =1;
        BigDecimal price= BigDecimal.valueOf(10.0);
        item.setId(id);
        item.setName("box");
        item.setPrice(price);
        item.setDescription("item");
        when(itemRepository.findByName("item")).thenReturn(Arrays.asList(item));

        ResponseEntity<List<Item>>response = itemController.getItemsByName("item");
        List<Item> items=Arrays.asList(item);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(items , response.getBody());
    }
    @Test
    public void Test_getItemById(){

        Item item = new Item();
        long id =1;
        BigDecimal price= BigDecimal.valueOf(10.0);
        item.setId(id);
        item.setName("box");
        item.setPrice(price);
        item.setDescription("item");

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(item,response.getBody());
    }
    @Test
    public void Test_getItems(){
        Item item = new Item();
        long id =1;
        BigDecimal price= BigDecimal.valueOf(10.0);
        item.setId(id);
        item.setName("box");
        item.setPrice(price);
        item.setDescription("item");
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

        ResponseEntity<List<Item>>response = itemController.getItems();
        List<Item> items=Arrays.asList(item);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(items,response.getBody());

    }
}
