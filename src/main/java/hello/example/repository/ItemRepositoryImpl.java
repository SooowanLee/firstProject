package hello.example.repository;

import hello.example.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final EntityManager em;

    @Override
    public void save(Item item) {
        em.persist(item);
    }

    @Override
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    @Override
    public void delete(Item item) {
        em.remove(item);
    }

    @Override
    public List<Item> findByItemName(String itemName) {
        return em.createQuery("select i from Item i where i.itemName = :itemName", Item.class)
                .setParameter("itemName", itemName)
                .getResultList();
    }

    @Override
    public List<Item> findByPriceLessThan(Integer price) {
        return em.createQuery("select i from Item i where i.price < :price", Item.class)
                .setParameter("price", price)
                .getResultList();
    }

    @Override
    public List<Item> findByItemDetail(String itemDetail) {
        return em.createQuery("select I from Item i where i.itemDetail like %:itemDetail% order by i.price desc", Item.class)
                .setParameter("itemDetail", itemDetail)
                .getResultList();
    }

    @Override
    public List<Item> findByPriceLessThanOrderByPriceDesc(Integer price) {
        return em.createQuery("select i from Item i where i.price < :price order by i.price desc", Item.class)
                .setParameter("price", price)
                .getResultList();
    }

    @Override
    public List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail) {
        return em.createQuery("select i from Item i where i.itemName =:itemName or i.itemDetail =:itemDetail", Item.class)
                .setParameter("itemName", itemName)
                .setParameter("itemDetail", itemDetail)
                .getResultList();
    }

    @Override
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
