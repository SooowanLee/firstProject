package hello.example.service;

import hello.example.domain.Cart;
import hello.example.domain.CartItem;
import hello.example.domain.Item;
import hello.example.domain.Member;
import hello.example.dto.CartDetailDto;
import hello.example.dto.CartItemDto;
import hello.example.dto.CartOrderDto;
import hello.example.dto.OrderDto;
import hello.example.repository.CartItemRepository;
import hello.example.repository.CartRepository;
import hello.example.repository.ItemRepository;
import hello.example.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email) {
        //아이템과 회원 정보를 조회
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        //장바구니를 처음 사용하는 회원이라면 장바구니 생성
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        //장바구니에 같은 아이템이 있는지 조회하기위한 정보
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        //같은 아이템이 있다면 수량을 늘려준다
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else { // 없다면 장바니구에 추가해준다
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    /**
     * 1. CartDetailDto를 담을 List만들기
     * 2. 현재 로그인한 회원 조회
     * 3. 회원아이디로 회원이 장바구니를 가지고 있는지 조회
     * 4. 회원이 장바구니에 아이템을 담았는지 확인 빈 장바구니라면 비어있는 상태로 반환
     * 5. 장바구니에 아이템이 담겨있다면 장바구니 아이디가 있을 것 장바구니 아이디로 상품들 조회하기
     * 6. 조회한 데이터 cartDetailDtoList에 담아서 cartDetailDtoList 반환
     */
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());


        if (cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList =
                cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    /**
     * 로그인한 회원의 장바구니에서 상품을 삭제하려는지 검증하는 로직
     */
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    /**
     * 장바구니 상품의 수량을 수정하는 기능
     */
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}
