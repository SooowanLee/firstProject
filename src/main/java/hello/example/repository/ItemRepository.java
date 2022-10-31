package hello.example.repository;

import hello.example.domain.Item;

import java.util.List;


public interface ItemRepository {

    void save(Item item);
    Item findOne(Long id);
    void delete(Item item);
    List<Item> findByItemName(String itemName);
    List<Item> findAll();
}
