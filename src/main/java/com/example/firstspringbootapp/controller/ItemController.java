package com.example.firstspringbootapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.firstspringbootapp.entity.ItemEntity;
import com.example.firstspringbootapp.service.ItemService;

@RestController
@RequestMapping({"/api", "/item/api"})
public class ItemController {

    private final ItemService itemService;

    @Value("${app.author:Leah Barbaso}")
    private String author;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/printAuthor")
    public String printAuthor() {
        return author;
    }

    @GetMapping({"/items", "/getAllItems"})
    public List<ItemEntity> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemEntity> getItemById(@PathVariable int itemId) {
        return itemService.getItemById(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"/items", "/postNewItem"})
    public ResponseEntity<ItemEntity> saveItem(@RequestBody ItemEntity item) {
        ItemEntity savedItem = itemService.saveItem(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @PutMapping({"/items/{itemId}", "/putItem/{itemId}", "/putItem"})
    public ResponseEntity<ItemEntity> updateItem(
            @PathVariable(required = false) Integer itemId,
            @RequestBody ItemEntity itemDetails) {
        int id = itemId != null ? itemId : itemDetails.getItemId();
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return itemService.getItemById(id)
                .map(item -> {
                    item.setItemName(itemDetails.getItemName());
                    item.setItemUnit(itemDetails.getItemUnit());
                    item.setItemPrice(itemDetails.getItemPrice());
                    return ResponseEntity.ok(itemService.saveItem(item));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/items/{itemId}", "/deleteItem/{itemId}"})
    public ResponseEntity<String> deleteItem(@PathVariable int itemId) {
        return itemService.getItemById(itemId)
                .map(item -> {
                    itemService.deleteItem(itemId);
                    return ResponseEntity.ok("Item deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
