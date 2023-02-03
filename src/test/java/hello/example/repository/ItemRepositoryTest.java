package hello.example.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.example.constant.ItemSellStatus;
import hello.example.domain.Item;
import hello.example.domain.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    void findByItemNameTest() throws Exception {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("테스트 상품1");
        assertThat(itemList.get(0).getItemName()).isEqualTo("테스트 상품1");
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    void findByItemNameOrItemDetailTest() throws Exception {
        //given
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");

        for (Item item : itemList) {
            System.out.println("item = " + item);
        }

        //then
        assertThat(itemList.size()).isEqualTo(2);
        assertThat(itemList).extracting(Item::getItemName).contains("테스트 상품1");
        assertThat(itemList).extracting(Item::getItemDetail).contains("테스트 상품 상세 설명5");
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    void findByPriceLessThanTest() throws Exception {
        //given 
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);

        //then
        assertThat(itemList.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("가격 내림차순 조회")
    void findByPriceLessThanOrderByPriceDescTest() throws Exception {
        //given 
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

        //then
        assertThat(itemList).extracting(Item::getPrice)
                .containsExactly(10004, 10003, 10002, 10001);

    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    void findByItemDetailTest() throws Exception {
        //given 
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");

        //then
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    void findByItemDetailNativeTest() throws Exception {
        //given
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");

        //then
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("Querydsl 조회테스트1")
    void queryDslTest() throws Exception {
        //given 
        this.createItemList();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        //when
        List<Item> itemList = query.fetch();

        //then
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    public void createItemList(){
        for(int i=1;i<=10;i++){
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100); item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }
}