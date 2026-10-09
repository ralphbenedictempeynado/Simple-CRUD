package com.example.firstspringbootapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.firstspringbootapp.entity.ItemEntity;
import com.example.firstspringbootapp.repository.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemEntity> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<ItemEntity> getItemById(int itemId) {
        return itemRepository.findById(itemId);
    }

    public ItemEntity saveItem(ItemEntity item) {
        return itemRepository.save(item);
    }

    public ItemEntity updateItem(int itemId, ItemEntity itemDetails) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        item.setItemName(itemDetails.getItemName());
        item.setItemUnit(itemDetails.getItemUnit());
        item.setItemPrice(itemDetails.getItemPrice());

        return itemRepository.save(item);
    }

    public void deleteItem(int itemId) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
        itemRepository.delete(item);
    }
}
