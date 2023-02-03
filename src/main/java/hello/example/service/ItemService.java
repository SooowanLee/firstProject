package hello.example.service;

import hello.example.domain.Item;
import hello.example.domain.ItemImg;
import hello.example.dto.ItemFormDto;
import hello.example.dto.ItemImgDto;
import hello.example.dto.ItemSearchDto;
import hello.example.dto.MainItemDto;
import hello.example.repository.ItemImgRepository;
import hello.example.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    //상품 저장 기능
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        Item item = itemFormDto.createItem(); //ItemFormDto -> Item Entity로 변환
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {

            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0) {
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");
            }

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }


    /**
     * 1. itemId로 이미지들 가져오기
     * 2. itemImgList를 순회하면서 itemImgDto로 변환
     * 3. itemImgDtoList에 저장하기
     * 4. itemId로 Item찾기
     * 5. itemFormDto로 변환
     * 6. itemFormDot.itemImgDtoList에 itemImgDtoList저장
     * 7. itemFormDto 반환
     */
    //상품 조회 기능
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();


        for (ItemImg itemImg : itemImgList) {

            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    //상품 수정 기능
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgList) throws Exception {

        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        // 이미지 등록
        for (int i = 0; i < itemImgList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgList.get(i));
        }

        return item.getId();
    }

    //admin페이지에서 조회 조건별 상품조회
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    //main페이지에서 조회 조건별 상품조회
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}